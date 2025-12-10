#!/bin/bash

# Format only on first run
if [ ! -d "${HADOOP_HOME}/hadoop-trungtvo-data/name/current" ]; then
    echo "Formatting HDFS namenode..."
    hdfs namenode -format
else
    echo "NameNode already formatted → skipping format"
fi

# OpenSSH server daemon (the program that listens on port 22 and accepts SSH connections)
/usr/sbin/sshd

echo "Starting Hadoop services..."

# Start HDFS: Namenode, Secondary Namenode and Datanode
start-dfs.sh

# Start YARN: ResourceManager and NodeManager
start-yarn.sh

# Start MapReduce JobHistory Server
mapred --daemon start historyserver

# Keep container running by tailing logs (or just wait)
echo "Hadoop is running. Logs:"
tail -F $HADOOP_HOME/logs/*.log
