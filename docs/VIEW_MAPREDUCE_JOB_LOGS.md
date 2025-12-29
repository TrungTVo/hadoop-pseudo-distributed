# Inspect logs of mapreduce job

Enable logs aggregation config in `yarn-site.xml`
```
yarn.log-aggregation-enable = true

// OPTIONAL
yarn.nodemanager.remote-app-log-dir
```

We can also config directory where to store these logs via `yarn.nodemanager.remote-app-log-dir`.
Default is `/tmp/logs`.

Another way is to view logs in Resource Manager UI or Job History UI via tracking URL.

Sample command to view logs
```
hdfs dfs -cat /tmp/logs/root/bucket-logs-tfile/0001/application_1766885263418_0001/hadoop-single-cluster_46173
```

OR

```
yarn logs -applicationId application_1766885263418_0001
```
