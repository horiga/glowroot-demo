FROM centos:centos7

MAINTAINER h.horiga@gmail.com

RUN yum -y upgrade
RUN yum -y install wget

RUN wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u102-b14/jdk-8u102-linux-x64.rpm" -O /tmp/jdk-8-linux-x64.rpm

RUN yum -y install /tmp/jdk-8-linux-x64.rpm

RUN alternatives --install /usr/bin/java jar /usr/java/latest/bin/java 200000
RUN alternatives --install /usr/bin/javaws javaws /usr/java/latest/bin/javaws 200000
RUN alternatives --install /usr/bin/javac javac /usr/java/latest/bin/javac 200000

ENV APP_VERSION 0.0.1
ADD ./target/glowroot-demo-${APP_VERSION}.jar glowroot-demo-${APP_VERSION}.jar
EXPOSE 8080
EXPOSE 20080

CMD /usr/java/latest/bin/java -jar glowroot-demo-${APP_VERSION}.jar



