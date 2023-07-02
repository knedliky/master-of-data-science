package s3455453;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class KarumbiPartitioner extends Partitioner <Text, IntWritable> {
    private static final Logger LOG = Logger.getLogger(KarumbiPartitioner.class);

    @Override
    public int getPartition(Text key, IntWritable value, int numReduceTasks) {
        // Set debug logging
        LOG.setLevel(Level.DEBUG);

        if (numReduceTasks == 0) {
            LOG.debug("Only one Reducer, no partitioning");
            return 0;
        }

        if (value.toString().equalsIgnoreCase("Small")) {
            LOG.debug(key + "word sent to Partition: 0");
            return 0;

        } else if (value.toString().equalsIgnoreCase("Medium")) {
            LOG.debug(key + "word sent to Partition: 0");
            return 0;

        } else if (value.toString().equalsIgnoreCase("Long")) {
            LOG.debug(key + "word sent to Partition: " + Integer.toString(1 % numReduceTasks));
            return 1 % numReduceTasks;

        } else {
            LOG.debug(key + "word sent to Partition: " + Integer.toString(2 % numReduceTasks));
            return 2 % numReduceTasks;
        }
    }
}
