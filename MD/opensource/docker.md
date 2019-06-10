Job for docker.service failed because the control process exited with error
vim /etc/docker/daemon.json
	"storage-driver": "overlay"
	
docker run -t -i ubuntu:12.04 /bin/bash

#### Docker 常⽤用命令
镜像相关
```
docker pull <image>
docker search <image>
```
容器器相关
```
docker run
docker start/stop <容器器名>
docker ps <容器器名>
docker logs <容器器名>
```

run
```
-d 后台
-e 设置环境变量
--expose / -p 宿主端口:容器端口
--name 容器名称
-v 宿主目录:容器目录，挂载磁盘卷
```

##### 镜像配置
```
官⽅方 Docker Hub
	https://hub.docker.com 
官方镜像
	镜像 https://www.docker-cn.com/registry-mirror
	下载 https://www.docker-cn.com/get-docker 
阿⾥里云镜像
	https://dev.aliyun.com 
```

运行 MongoDB 镜像 
```
docker run --name mongo -p 27017:27017 -v ~/dockerdata/mongo:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=admin -d mongo

登录到 MongoDB 容器器中
	docker exec -it mongo bash 
通过 Shell 连接 MongoDB
	mongo -u admin -p admin 
```

启动 Redis
```
docker run --name redis -d -p 6379:6379 redis
```