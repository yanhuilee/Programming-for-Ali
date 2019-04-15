Job for docker.service failed because the control process exited with error
vim /etc/docker/daemon.json
	"storage-driver": "overlay"
	
docker run -t -i ubuntu:12.04 /bin/bash