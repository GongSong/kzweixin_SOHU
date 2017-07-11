# -*- coding: utf-8 -*-
from datetime import datetime
import requests
import hashlib
import redis
import sys

redis_client = redis.StrictRedis(host="10.23.72.97", password="crs-2krquz24:kuaizhanisgood!")


def is_uploaded(url):
    r = requests.get(url, stream=True)

    md5 = hashlib.md5()
    for chunk in r.iter_content(1024):
        md5.update(chunk)

    if md5.hexdigest() == "fee9458c29cdccf10af7ec01155dc7f0":
        return False
    else:
        return True


def check(date):
    key = "plf-kzweixin:image_uploaded:" + date
    result = redis_client.zrange(key, 0, -1, withscores=True)
    result = [(url.decode(), score) for url, score in result]

    print("总共上传%s张图片" % len(result))

    for url, score in result:
        sys.stdout.write('.')
        sys.stdout.flush()
        if not is_uploaded(url):
            print("\n上传失败, 时间: %s, 图片: %s" % (datetime.fromtimestamp(score), url))

if __name__ == "__main__":
    date_str = sys.argv[1]
    check(date_str)
