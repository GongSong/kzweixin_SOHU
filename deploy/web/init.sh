#!/bin/bash


source /root/.bashrc
domeize -template /jetty-tpl.ini:/opt/kuaizhan/jetty/start.ini
supervisorctl start jetty