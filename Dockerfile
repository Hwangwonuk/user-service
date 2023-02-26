FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/user-service-1.0.jar UserService.jar
ENTRYPOINT ["java","-jar","UserService.jar"]



# docker network 생성하기
# docker network create --gateway 172.19.0.1 --subnet 172.19.0.0/16 ecommerce-network

# docker run with network
# docker run -d --name rabbitmq --network ecommerce-network
# -p 15672:15672 -p 5672:5672 -p 15671:15671 -p 5671:5671 -p 4370:4370
# -e RABBITMQ_DEFAULT_USER=guest -e  RABBITMQ_DEFAULT_PASS=guest
# rabbitmq:management

# ---------------------------------------
# docker image 생성하기
# docker build -t hwk0173/user-service:1.0 .

# image push 하기
# docker push hwk0173/user-service:1.0

# docker container 생성 및 실행하기
# docker run -d --network ecommerce-network --name user-service
# -e "spring.cloud.config.uri=http://config-service:8888" -e "spring.rabbitmq.host=rabbitmq"
# -e "spring.zipkin.base-url=http://zipkin:9411"
# -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/"
# -e "logging.file=/api-logs/users-ws.log" hwk0173/user-service:1.0



# ---------------------------------------

# Mariadb 컨테이너 생성하기
# FROM mariadb
# ENV MYSQL_ROOT_PASSWORD test1357
# ENV MYSQL_DATABASE mydb
# COPY ./mysql /var/lib/mysql
# EXPOSE 3306
# ENTRYPOINT ["mysqld", "--user=root"]

# 이미지 생성하기
# docker build -t hwk0173/my_mariadb:1.0 .

# Mariadb 컨테이너 실행하기
# docker run -d -p 3306:3306 --network ecommerce-network --name mariadb hwk0173/my_mariadb:1.0

# docker 내부의 DB권한 변경
# docker exec -it mariadb /bin/bash
# mysql -hlocalhost -uroot -p
# 비밀번호 입력 test1357
# 아래 권한변경 명령 실행
# grant all privileges on *.* to 'root'@'%' identified by 'test1357';
# flush privileges;
# exit
# mysql -h127.0.0.1 -uroot -p
# 비밀번호 입력
# 로그인 되는것 확인!

