FROM daocloud.io/rockytan/docker-base-maven-tomcat:latest

ADD / /tmp/build/
RUN cd /tmp/build && mvn clean compile -Dmaven.test.skip=true -P test

        #构建应用
RUN cd /tmp/build/javaweb && mvn package -Dmaven.test.skip=true -P production \
        #拷贝编译结果到指定目录
	&& rm -rf $CATALINA_HOME/webapps/* \
        && mv javaweb/target/*.war $CATALINA_HOME/webapps/ROOT.war \
        #清理编译痕迹
        && cd / && rm -rf /tmp/build