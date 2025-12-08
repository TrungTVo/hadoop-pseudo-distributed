FROM ubuntu:latest

# Install necessary packages
RUN apt-get update && apt-get install -y \
    sudo ssh pdsh rsync openjdk-21-jdk wget vim iputils-ping && \
    rm -rf /var/lib/apt/lists/*

# Misc configuration files
COPY ./misc/.vimrc /root/.vimrc
COPY ./misc/.ssh/config /root/.ssh/config

ENV HADOOP_VERSION=3.4.2

# Download and install Hadoop Distribution Package
RUN wget https://dlcdn.apache.org/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz \
    && tar -xzf hadoop-${HADOOP_VERSION}.tar.gz -C /opt \
    && rm hadoop-${HADOOP_VERSION}.tar.gz

# Configure environment variables
ENV HADOOP_HOME=/opt/hadoop-${HADOOP_VERSION}
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-arm64
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

# SSH Configuration for passwordless (communication within Hadoop cluster)
RUN mkdir -p ~/.ssh/hadoop && \
    mkdir -p /run/sshd && \
    ssh-keygen -t rsa -P '' -f ~/.ssh/hadoop/id_rsa && \
    cat ~/.ssh/hadoop/id_rsa.pub >> ~/.ssh/authorized_keys && \
    chmod 600 ~/.ssh/authorized_keys

# Start script
COPY start.sh /start.sh
RUN chmod +x /start.sh
ENTRYPOINT ["/start.sh"]
