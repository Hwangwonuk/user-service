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

# Mariadb 컨테이너 생성하기
# FROM mariadb
# ENV MYSQL_ROOT_PASSWORD test1357
# ENV MYSQL_DATABASE mydb
# COPY ./mysql /var/lib/mysql
# EXPOSE 3306
# ENTRYPOINT ["mysqld", "--user=root"]

# Mariadb 컨테이너 실행하기
# docker run -d -p 3306:3306  --network ecommerce-network --name mariadb edowon0623/my-mariadb:1.0

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

