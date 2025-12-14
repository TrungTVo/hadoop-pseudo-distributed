# Run Sample MapReduce Job

Make the HDFS directories required to execute MapReduce jobs
```
hdfs dfs -mkdir -p /user/root
```

Create `input` folder to store some input files
```
hdfs dfs -mkdir input
```

Copy sample input files from local cluster (Hadoop container) into the distributed filesystem (HDFS)
```
hdfs dfs -put /opt/hadoop-3.4.2/etc/hadoop/*.xml input
```

Run some of the examples provided by Hadoop
```
hadoop jar /opt/hadoop-3.4.2/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.4.2.jar grep input output 'dfs[a-z.]+'
```

Examine the output files: Copy the output files from HDFS to the container filesystem (cluster node) and examine them
```
hdfs dfs -get output output
cat output/*
```
