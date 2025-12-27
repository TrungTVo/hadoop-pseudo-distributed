# Run Custom MapReduce Job

Custom Mapreduce Jobs (including source code, packaged jar, resources, etc...) are stored under `mapreduce_jobs` folder.
Here, we already write a simple `WordCount.java` program to count the number of unique words in provided input files.

All of our custom Mapreduce Jobs code are mounted from `localhost` to Hadoop single node cluster container under `/opt/hadoop-3.4.2/mapreduce_jobs/` directory.
This makes it easier to write code and develop in local host machine and have it mounted over the Hadoop Cluster.

In Hadoop Cluster, we first need to compile the Mapreduce Java code and create executable `jar` file for Hadoop to run.

In Hadoop Cluster container, navigate to `/opt/hadoop-3.4.2/mapreduce_jobs/` directory.
```
cd /opt/hadoop-3.4.2/mapreduce_jobs
```

Use `gradlew` command to compile and create `jar` file for our Mapreduce `WordCount` Job
```
./gradlew clean
./gradlew compileJava
./gradlew jar
```

`jar` file will be created under `/opt/hadoop-3.4.2/mapreduce_jobs/build/libs/` folder.

Now we're ready to run our Mapreduce Job.

First, let's make the HDFS directories required to execute MapReduce jobs. Create `wordcount/input` folder to store some input files.
```
hdfs dfs -mkdir -p /user/root/wordcount/input
```

Copy sample input files from local cluster (Hadoop container) into the distributed filesystem (HDFS).
Here, we already created 2 sample input files: `wc_input01.txt` and `wc_input02.txt` under `/opt/hadoop-3.4.2/mapreduce_jobs/build/resources/main/` directory.
```
hdfs dfs -put /opt/hadoop-3.4.2/mapreduce_jobs/build/resources/main/wc_input* wordcount/input
```

Before running our custom Mapreduce `WordCount` Job, we can check the classpath where its `main()` method is run by inspecting its `jar` file
```
jar tf /opt/hadoop-3.4.2/mapreduce_jobs/build/libs/demo-1.0-SNAPSHOT.jar
```

Output classpath:
```
META-INF/
META-INF/MANIFEST.MF
com/
com/example/
com/example/wordcount/
com/example/wordcount/WordCount$IntSumReducer.class
com/example/wordcount/WordCount$TokenizerMapper.class
com/example/wordcount/WordCount.class
wc_input01.txt
wc_input02.txt
```

Run our custom Mapreduce `WordCount` Job:
```
hadoop jar /opt/hadoop-3.4.2/mapreduce_jobs/build/libs/demo-1.0-SNAPSHOT.jar \
    com/example/wordcount/WordCount \
    /user/root/wordcount/input \
    /user/root/wordcount/output
```

Examine the output files: Copy the output files from HDFS to the container filesystem (cluster node) and examine them
```
hdfs dfs -get wordcount/output output
cat output/*
```

Result:
```
Aiko	1
Bye	2
Chu	1
Goodbye	2
Hadoop	2
Hello	4
Trung	1
Vo	1
World	2
```
