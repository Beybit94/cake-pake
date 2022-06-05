FROM tomcat:9.0

COPY /target/cake-pake-latest.war /usr/local/tomcat/webapps/cake-pake-latest.war

EXPOSE 8080
CMD ["catalina.sh", "run"]