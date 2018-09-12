#!/usr/bin/env bash
kill -9 $(sudo lsof -ti tcp:5555)
kill -9 $(sudo lsof -ti tcp:5556)
kill -9 $(ps aux|grep "/bin/sh -c connect_client*"|awk '{print $2}')
kill -9 $(ps aux|grep "sh *.sh"|awk '{print $2}')
kill -9 $(ps aux|grep "(sh)"|awk '{print $2}')
kill -9 $(ps aux|grep "sh start_parallel-clients.sh"|awk '{print $2}')
