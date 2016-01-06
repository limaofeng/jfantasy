FROM daocloud.io/rockytan/docker-base-maven-tomcat:latest

ADD / /tmp/build/
RUN cd /tmp/build && mvn -q dependency:resolve

        #构建应用
RUN cd /tmp/build && mvn -q -DskipTests=true package \
        #拷贝编译结果到指定目录
	&& rm -rf $CATALINA_HOME/webapps/* \
        && mv target/*.war $CATALINA_HOME/webapps/ROOT.war \
        #清理编译痕迹
        && cd / && rm -rf /tmp/build