# -*- coding: utf-8 -*-
"""
mybatis generator的分表支持脚本

mybatis generator不支持分表，本脚本对生成的 XXXmapper.xml和 XXXmapper.java文件进行处理，
使得支持在调用mapper方法时，能够灵活传入tableName

* 对于mapper.xml, 把delete from, select from, update等sql语句中的表名替换成${tableName}
* 对于mapper.java, 给所有的方法，加入参数 @Param("tableName") String tableName

使用方式
     * 安装python
     * 执行 python script/mybatis_add_tablename.py path_to_mapper_java
     * 执行 python script/mybatis_add_tablename.py path_to_mapper_xml
"""
import sys
import re


def parse_xml(file_path):
    """
    把delete from, select from, update等sql语句中的表名替换成${tableName}
    """
    with open(file_path, "r+") as file:
        result = ""
        for line in file.readlines():

            # delete from, select from
            match = re.match(r'(.*? from )(\w+)(.*?\n)$', line)
            if match:
                line = match.groups()[0] + "${tableName}" + match.groups()[2]
                print('replace "%s" to "%s"' % (match.groups()[1], "${tableName}"))

            # insert into
            match = re.match(r'(.*? insert into )(\w+)(.*?\n)', line)
            if match:
                line = match.groups()[0] + "${tableName}" + match.groups()[2]
                print('replace "%s" to "%s"' % (match.groups()[1], "${tableName}"))

            # update
            match = re.match(r'(.*? update )(\w+)(.*?\n)', line)
            if match:
                line = match.groups()[0] + "${tableName}" + match.groups()[2]
                print('replace "%s" to "%s"' % (match.groups()[1], "${tableName}"))

            result += line

    with open(file_path, "w+") as file:
        file.write(result)


def parse_java(file_path):
    with open(file_path, "r+") as file:
        result = ""
        # TODO: 又忘记了可变个数的group写法
        for line in file.readlines():
            # 是否含有括号
            method_match = re.match(r'(.*?\()(.*)(\);.*?)$', line)
            # 是函数那一行
            if method_match:
                argument_str = method_match.groups()[1]
                origin_argument_str = argument_str
                if '@Param' in argument_str:
                    argument_str += ', @Param("tableName") String tableName'
                else:
                    arg_name = argument_str.split(' ')[-1]
                    argument_str = '@Param("%s") ' % arg_name + argument_str + ', @Param("tableName") String tableName'
                print('replace "%s" to "%s"' % (origin_argument_str, argument_str))
                line = method_match.groups()[0] + argument_str + method_match.groups()[2] + "\n"

            result += line

    with open(file_path, "w+") as file:
        file.write(result)


if __name__ == "__main__":

    file_path = sys.argv[1]
    print("parse %s" % file_path)

    extension = file_path.split(".")[-1]

    if extension == "xml":
        parse_xml(file_path)
    elif extension == "java":
        parse_java(file_path)
    else:
        raise Exception("invalid file, must be .xml or .java")

    print("done...")
