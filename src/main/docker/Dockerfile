FROM frolvlad/alpine-oraclejdk12:slim
VOLUME /tmp
ADD docker-study.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]