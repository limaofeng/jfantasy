FROM index.docker.io/stackoverflower/java7-tomcat7-maven3:1.0

ADD / /tmp/build/
RUN cd /tmp/build && mvn clean compile -Dmaven.test.skip=true -P test

#构建应用
RUN cd /tmp/build && mvn package -Dmaven.test.skip=true -P production \
        #拷贝编译结果到指定目录
	&& rm -rf $CATALINA_HOME/webapps/* \
        && mv javaweb/target/*.war $CATALINA_HOME/webapps/ROOT.war \
        #清理编译痕迹
        && cd / && rm -rf /tmp/build

#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/shanghai" > /etc/timezone

ENV LANG="zh_CN.UTF-8"
ENV JAVA_OPTS="-server -Dfile.encoding=UTF-8 -Xms1024m -Xmx1024m -XX:PermSize=256M -XX:MaxPermSize=600m -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=50983"

VOLUME ["/mnt/haolue/hbao","/tomcat/logs"]

EXPOSE 8080
EXPOSE 50983