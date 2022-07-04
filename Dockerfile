FROM tomcat:9.0

COPY /target/cake-pake-latest.war /usr/local/tomcat/webapps/cake-pake-latest.war

ENV JPDA_ADDRESS="5555"
ENV JPDA_TRANSPORT="dt_socket"

EXPOSE 8080 5555
ENTRYPOINT ["catalina.sh", "jpda", "run"]