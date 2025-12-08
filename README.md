# Single Node (Pseudo Distributed) Cluster

## Build Image
```
docker build -t hadoop-pseudo:latest .
```

## Start a Single Node Cluster
```
docker run -it --name hadoop-single-cluster -h localhost \
    -p 9870:9870 \
    -p 9868:9868 \
    -p 9864:9864 \
    -p 8088:8088 \
    -p 8042:8042 \
    -p 19888:19888 \
    -v ./etc/hadoop/core-site.xml:/opt/hadoop-3.4.2/etc/hadoop/core-site.xml \
    -v ./etc/hadoop/hadoop-env.sh:/opt/hadoop-3.4.2/etc/hadoop/hadoop-env.sh \
    -v ./etc/hadoop/hdfs-site.xml:/opt/hadoop-3.4.2/etc/hadoop/hdfs-site.xml \
    -v ./etc/hadoop/mapred-site.xml:/opt/hadoop-3.4.2/etc/hadoop/mapred-site.xml \
    -v ./etc/hadoop/yarn-site.xml:/opt/hadoop-3.4.2/etc/hadoop/yarn-site.xml \
    hadoop-pseudo:latest
```

## Exec into Cluster
```
docker exec -it hadoop-single-cluster /bin/bash
```

In Pseudo Distributed mode, all `Namenodes`, `Datanodes`, `Secondary Namenode`, `Resourcemanager`, `Nodemanager` are run in the same machine (node, aka container), but they are run as separate JVM processes.

Check running processes:
```
jps -l
```

## Some Common Default ports

| Configuration | Port |
|---|---|
| `dfs.namenode.http-address` | 9870 |
| `dfs.namenode.https-address` | 9871 |
| `dfs.namenode.secondary.http-address` | 9868 |
| `dfs.datanode.http.address` | 9864 |
| `dfs.datanode.https.address` | 9865 |
| `dfs.datanode.address` | 9866 |
| `dfs.datanode.ipc.address` | 9867 |
| `yarn.resourcemanager.webapp.address` | 8088 |
| `yarn.resourcemanager.webapp.https.address` | 8090 |
| `yarn.nodemanager.webapp.address` | 8042 |
| `yarn.nodemanager.webapp.https.address` | 8044 |
| `mapreduce.jobhistory.webapp.address` | 19888 |
| `mapreduce.jobhistory.webapp.https.address` | 19890 |