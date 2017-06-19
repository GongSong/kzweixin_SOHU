# -*- coding: utf-8 -*-

import logging
from MySQLdb import Connection

# dev
# weixin_db = dict(
#     charset='utf8',
#     host='10.11.172.135',
#     db='kuaizhan_weixin_1',
#     user='kuaizhandev',
#     passwd='kkkzzz',
# )

# site_db = dict(
#     charset='utf8',
#     host='10.11.172.135',
#     db='kuaizhan_site_1',
#     user='kuaizhandev',
#     passwd='kkkzzz',
# )

# test
# weixin_db = dict(
#     charset='utf8',
#     host='192.168.110.78',
#     db='kuaizhan_weixin_1',
#     user='kuaizhant1',
#     passwd='kkkzzz',
# )
#
# site_db = dict(
#     charset='utf8',
#     host='192.168.110.78',
#     db='kuaizhan_site_1',
#     user='kuaizhant1',
#     passwd='kkkzzz',
# )

# prod
weixin_db = dict(
    charset='utf8',
    host='10.11.164.221',
    db='kuaizhan_site_1',
    user='kuaizhan_docker_rw',
    passwd='Ilc92mDceo',
)

site_db = dict(
    charset='utf8',
    host='10.16.41.50',
    db='kuaizhan_site_1',
    user='kuaizhan_site_1',
    passwd='3u9w92vD4ggrlsv',
)

logger = logging.getLogger(__name__)


def fix(num):
    """每次执行多少数目"""
    wx_conn = Connection(**weixin_db)
    site_conn = Connection(**site_db)

    with wx_conn.cursor() as cur:
        sql = "SELECT weixin_appid, site_id FROM site_weixin limit %s;" % num
        cur.execute(sql)

        results = [dict(weixin_appid=row[0], site_id=row[1]) for row in cur.fetchall()]

    for item in results:
        with site_conn.cursor() as cur:
            sql = "SELECT user_id FROM site_main WHERE site_id=%s;" % item['site_id']
            cur.execute(sql)
            try:
                user_id = cur.fetchall()[0][0]
                print(user_id)
            except IndexError as e:
                print("get user_id failed, item:%s" % item)
                raise e

            with wx_conn.cursor() as cur:
                sql = "UPDATE site_weixin set user_id = %s WHERE weixin_appid=%s;" % (user_id, item["weixin_appid"])
                cur.execute(sql)
                wx_conn.commit()

    wx_conn.close()
    site_conn.close()


if __name__ == "__main__":

    count = 0
    while True:
        fix(500)
        count += 500
        print("finish %s" % count)
