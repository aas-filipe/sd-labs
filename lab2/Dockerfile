# base ubuntu official image 
FROM ubuntu

# run a command (install a package)
RUN apt-get update && apt-get install iproute2 -y

# Copy openjdk 21 from another image
ENV JAVA_HOME=/opt/java/openjdk
COPY --from=eclipse-temurin:21 $JAVA_HOME $JAVA_HOME
ENV PATH=$PATH:$JAVA_HOME/bin

# working directory inside docker image
WORKDIR /home/sd

# copy storage directory with the default avatar image
COPY  avatarFiles avatarFiles

# copy an example image for experiments
COPY example.png exanple.png

# copy the jar created by assembly to the docker image
COPY target/*jar-with-dependencies.jar sd2425.jar


# run Discovery when starting the docker image
CMD ["java", "-cp", "sd2425.jar", "lab2.server.UsersServer"]
