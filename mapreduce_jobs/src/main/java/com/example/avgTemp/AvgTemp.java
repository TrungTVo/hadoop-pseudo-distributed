package com.example.avgTemp;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
// import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvgTemp {
    private static Logger logger = LoggerFactory.getLogger(AvgTemp.class);

    public static class YearMapper
            extends Mapper<Object, Text, Text, Text> {

        private Logger logger = LoggerFactory.getLogger(YearMapper.class);
        private Text year = new Text();
        private Text temperature_count = new Text();

        @Override
        public void map(Object inkey, Text invalue, Context context) throws IOException, InterruptedException {
            logger.info("Map Value: " + invalue.toString());
            String[] temp_record = invalue.toString().split(",");
            String year = temp_record[0];
            String temperature = temp_record[2] + ",1";
            this.year.set(year);
            this.temperature_count.set(temperature);
            context.write(this.year, this.temperature_count);
        }
    }

    
    public static class AvgTempReducer
            extends Reducer<Text, Text, Text, FloatWritable> {

        private Logger logger = LoggerFactory.getLogger(AvgTempReducer.class);
        private FloatWritable temp_avg = new FloatWritable();

        @Override
        public void reduce(Text year, Iterable<Text> temperature_counts,
                Context context) throws IOException, InterruptedException {
            float temp_sum = 0.0f;
            int count = 0;
            for (Text temp_count : temperature_counts) {
                String[] temp_record = temp_count.toString().split(",");
                temp_sum += Float.parseFloat(temp_record[0]);
                count += Integer.parseInt(temp_record[1]);
            }
            float temp_avg = (float) temp_sum / count;
            this.temp_avg.set(temp_avg);
            logger.info(String.format("%s -> Average Temperature: %.2f", year.toString(), temp_avg));
            context.write(year, this.temp_avg);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        // conf.set("mapreduce.framework.name","local");
        // conf.set("fs.defaultFS","file:///");

        Job job = Job.getInstance(conf, "avg_temperature");

        // logger.info("mapreduce.framework.name: " + conf.get("mapreduce.framework.name"));
        // logger.info("dfs.namenode.name.dir: " + conf.get("dfs.namenode.name.dir"));
        // logger.info("fs.defaultFS: " + conf.get("fs.defaultFS"));

        job.setJarByClass(AvgTemp.class);
        job.setMapperClass(YearMapper.class);
        // job.setNumReduceTasks(0);
        // job.setOutputFormatClass(NullOutputFormat.class);
        // job.setCombinerClass(AvgTempReducer.class);                  // CANNOT USE COMBINER FOR AVERAGE CALCULATION
        job.setReducerClass(AvgTempReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        List<InputSplit> inputSplits = job.getInputFormatClass().getDeclaredConstructor().newInstance().getSplits(job);
        logger.info("Number of input splits: " + inputSplits.size());
        logger.info("Input Splits Details:");
        for (InputSplit split : inputSplits) {
            logger.info(split.toString());
        }

        boolean jobSuccess = job.waitForCompletion(true);
        if (jobSuccess) {
            logger.info("Job completed successfully.");
            System.exit(0);
        } else {
            logger.error("Job failed.");
            System.exit(1);
        }
    }
}
