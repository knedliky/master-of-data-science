package s3455453;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Karumbi extends Configured implements Tool {
    public static final Logger LOG = Logger.getLogger(Karumbi.class);

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Karumbi(), args));
    }

    public int run(String[] args) throws Exception {
        System.out.println("Started running word count job");

        Configuration conf = getConf();
        Job job = new Job(conf, "Assessment Task 1");
        job.setJarByClass(Karumbi.class);

        if (args.length != 3) {
            System.err.println("Please enter an @param input path, @param output path and @param task id");
            System.exit(1);

        } else {
            try {
                FileInputFormat.setInputPaths(job, new Path(args[0]));
                FileOutputFormat.setOutputPath(job,new Path(args[1]));
                int taskNumber = Integer.parseInt(args[2]);

                if(taskNumber == 1) {
                    job.setMapperClass(KarumbiMapper.class);
                    //set partitioner statement for task 1
                    job.setPartitionerClass(KarumbiPartitioner.class);

                } else if (taskNumber == 2) {
                    // set mapper statement for task 2
                    job.setMapperClass(KarumbiMapper2.class);

                } else if (taskNumber == 3) {
                    // set mapper statement for task 3
                    job.setMapperClass(KarumbiMapper3.class);

                } else {
                    System.out.println("Please enter a valid task number: 1, 2 or 3");
                    System.exit(1);
                }

            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[2] + " must be an integer represent either task number 1, 2 or 3");
                System.exit(1);
            }
        }

        // Set log-level to information and log all the arguments passed to the application
        LOG.setLevel(Level.INFO);
        LOG.info("Input path: " + args[0]);
        LOG.info("Output path: " + args[1]);
        LOG.info("Task number: " + args[2]);

        // Set job parameters for Mapper and Reducer
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setCombinerClass(KarumbiReducer.class);
        job.setReducerClass(KarumbiReducer.class);
        job.setNumReduceTasks(3);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        System.exit(job.waitForCompletion(true)? 0 : 1);
        return 0;
    }

}
