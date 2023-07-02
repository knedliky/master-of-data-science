package s3455453;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.StringTokenizer;

public class KarumbiMapper extends Mapper<Object, Text, Text, IntWritable> {
    // Create logger for class
    private final static Logger LOG = Logger.getLogger(KarumbiMapper.class);

    // Create counter for word
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    // Override the hadoop map method with custom implementation
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Log task, set debug logging to all lines
        LOG.info("The Map Task 1 of Simon Karumbi, s3455453");
        LOG.setLevel(Level.DEBUG);
        LOG.debug(value);

        // map word to value of one to send to partitioner
        StringTokenizer itr = new StringTokenizer(value.toString());

        while (itr.hasMoreTokens()) {
            word.set(itr.nextToken());
            int wordLength = word.toString().length();

            if (wordLength > 0 && wordLength <= 4) {
                context.write(new Text("Short"), one);

            } else if (wordLength > 4 && wordLength <= 7) {
                context.write(new Text("Medium"), one);

            } else if (wordLength > 7 && wordLength <= 10) {
                context.write(new Text("Long"), one);

            } else {
                context.write(new Text("Extra Long"), one);

            }
        }
    }
}

