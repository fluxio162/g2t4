FROM maven:3.6.3-jdk-11

WORKDIR /src
ADD pom.xml /src
RUN mvn verify clean --fail-never

COPY . /src
ADD /docker_resources/application.properties /src/src/main/resources
ADD /docker_resources/log4j.properties /src/src/main/resources
RUN mvn -v
RUN mvn javadoc:javadoc
RUN mvn clean install
EXPOSE 8080
ENTRYPOINT ["mvn","spring-boot:run"]