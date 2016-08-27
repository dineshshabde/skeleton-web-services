FROM openjdk
MAINTAINER dinesh 
COPY ./target/skeleton-web-services-1.0-SNAPSHOT.jar /home/skeleton-web-services-1.0-SNAPSHOT.jar
CMD ["java","-jar","/home/skeleton-web-services-1.0-SNAPSHOT.jar"]