package s3455453;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class KarumbiReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private static final Logger LOG = Logger.getLogger(KarumbiReducer.class);
    private IntWritable result = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        // Set logging to DEBUG
        LOG.info("The Reduce Task of Simon Karumbi, s3455453");
        LOG.setLevel(Level.DEBUG);

        // Sum count values of keys (short, medium, long and extra long)
        int sum = 0;

        for (IntWritable val: values) {
            sum += val.get();
        }

        result.set(sum);
        context.write(key, result);
    }
}
