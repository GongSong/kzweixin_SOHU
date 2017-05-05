# -*- coding: utf-8 -*-
"""
获取从某时间开始，新增粉丝数前100的站点，以及相关信息
"""

import logging
from MySQLdb import Connection
from datetime import datetime
from collections import defaultdict

weixin_db = dict(
    charset='utf8',
    host='10.11.164.221',
    db='kuaizhan_site_1',
    user='kuaizhan_docker_rw',
    passwd='Ilc92mDceo',
)

user_db = dict(
    charset='utf8',
    host='192.168.61.95',
    db='kuaizhan_user',
    user='kuaizhan_user',
    passwd='IGsbEhH20fLfJLk',
)

site_db = dict(
    charset='utf8',
    host='10.16.41.50',
    db='kuaizhan_site_1',
    user='kuaizhan_site_1',
    passwd='3u9w92vD4ggrlsv',
)

logger = logging.getLogger(__name__)


the_time = datetime(2017, 4, 28, 0, 0, 0).timestamp()
print(int(the_time))


def get_fans_rank():
    fans_rank = defaultdict(lambda: dict(fans_new=0, table_num=None))

    conn = Connection(**weixin_db)
    for table_num in range(128):
        with conn.cursor() as cur:
            sql = "SELECT app_id FROM WEIXIN_FANS_%s WHERE subscribe_time > %s;" % (table_num, the_time)
            print("start query weixin_fans" + str(table_num))
            cur.execute(sql)
            print("end query weixin_fans" + str(table_num))
            for row in cur.fetchall():
                weixin_appid = row[0]
                fans_rank[weixin_appid]["fans_new"] += 1
                if fans_rank[weixin_appid]["table_num"] == None:
                    fans_rank[weixin_appid]["table_num"] = table_num
                elif fans_rank[weixin_appid]["table_num"] != table_num:
                    raise Exception("user's fan in different table")
    conn.close()

    fans_rank = [dict(app_id=key, **val) for key, val in fans_rank.items()]
    fans_rank = sorted(fans_rank, key=lambda item: item["fans_new"], reverse=True)
    return fans_rank[:100]


def get_weixin_info(fans_rank):
    print("start weixin_info")
    conn = Connection(**weixin_db)
    for item in fans_rank:
        # site_id
        with conn.cursor() as cur:
            sql = "select site_id, nick_name, weixin_appid from site_weixin WHERE app_id='%s';" % item["app_id"]
            cur.execute(sql)
            item["site_id"], item["weixin_name"], item["weixin_appid"] = cur.fetchall()[0]
        # 微积分
        with conn.cursor() as cur:
            sql = "select count(*) from weixin_attract_fans_config WHERE weixin_appid=%s and status=1" % item["weixin_appid"]
            cur.execute(sql)
            row = cur.fetchall()[0]
            item["attract_fans"] = bool(row[0])
        # 扫码投票
        with conn.cursor() as cur:
            sql = "select count(*) from weixin_vote WHERE weixin_appid=%s and status=1" % item["weixin_appid"]
            cur.execute(sql)
            row = cur.fetchall()[0]
            item["vote"] = bool(row[0])


def get_fans_detail(fans_rank):
    print("开始计算粉丝总数和净增数")
    conn = Connection(**weixin_db)
    for item in fans_rank:
        # 总数
        with conn.cursor() as cur:
            sql = "select count(*) from WEIXIN_FANS_%s WHERE app_id='%s' and status=1" % (item["table_num"], item["app_id"])
            cur.execute(sql)
            item["fans_count"] = cur.fetchall()[0][0]
        # 净增数
        with conn.cursor() as cur:
            sql = "select count(*) from WEIXIN_FANS_%s WHERE app_id='%s' and status=2 AND update_time > %s" % (item["table_num"], item["app_id"], the_time)
            cur.execute(sql)
            item["fans_net_new"] = item["fans_new"] - cur.fetchall()[0][0]



def get_site_name(fans_rank):
    print("get site name start")
    conn = Connection(**site_db)
    for item in fans_rank:
        with conn.cursor() as cur:
            sql = "select user_id, name from site_main WHERE site_id=%s" % item["site_id"]
            cur.execute(sql)
            item["user_id"], item["site_name"] = cur.fetchall()[0]
    print(fans_rank)


def get_user_info(fans_rank):
    print("get user info")
    conn = Connection(**user_db)
    for item in fans_rank:
        with conn.cursor() as cur:
            sql = "select phone from user_main WHERE user_id=%s" % item["user_id"]
            cur.execute(sql)
            item["phone"] = cur.fetchall()[0][0]


def write_csv(fans_rank):
    print("开始写文件")
    with open("粉丝前100.csv", "a", encoding="utf8") as file:
        file.write("站点id,站名,联系电话,公众号名称,公众号粉丝数,一周粉丝新增数,一周粉丝净增数,微积粉,扫码投票 \n")
        for item in fans_rank:
            value_list = [item["site_id"], item["site_name"], item["phone"], item["weixin_name"],
                          item["fans_count"], item["fans_new"], item["fans_net_new"],
                          "开通" if item["attract_fans"] else "未开通", "开通" if item["vote"] else "未开通"]
            file.write(",".join([str(value) for value in value_list]) + "\n")

fans_rank = get_fans_rank()

get_weixin_info(fans_rank)
get_fans_detail(fans_rank)
get_site_name(fans_rank)
get_user_info(fans_rank)
write_csv(fans_rank)
