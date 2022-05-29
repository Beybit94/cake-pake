FROM tomcat:latest

COPY /target/cake-pake-latest /usr/local/tomcat/webapps/cake-pake/
COPY /target/cake-pake-latest.war /usr/local/tomcat/webapps/cake-pake-latest.war

EXPOSE 8080
CMD ["catalina.sh", "run"]