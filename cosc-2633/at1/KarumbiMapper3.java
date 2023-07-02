package s3455453;

import org.apache.commons.math3.util.Pair;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class KarumbiMapper3 extends Mapper<Object, Text, Text, IntWritable> {
    // Create logger for class
    private final Logger LOG = Logger.getLogger(KarumbiMapper3.class);

    private Text word = new Text();
    private String wordLength = new String();
    private Map count;

    // Override the hadoop setup method to instantiate an associative array
    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        // Instantiating the global HashMap to be used in the MapReduce task
        count = new HashMap<String, Integer>();
    }

    // Override the hadoop map method with custom implementation
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Set logging to DEBUG and log the Mapper task
        LOG.info("The Map Task 2 of Simon Karumbi, s3455453");
        LOG.setLevel(Level.DEBUG);

        try {
            // Log every line
            LOG.debug(value);

            // Map word to word length key
            StringTokenizer itr = new StringTokenizer(value.toString());

            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                int length = word.toString().length();

                if (length > 0 && length <= 4) {
                    wordLength = "Short";

                } else if (length > 4 && length <= 7) {
                    wordLength = "Medium";

                } else if (length > 7 && length <= 10) {
                    wordLength = "Long";

                } else if (length > 10) {
                    wordLength = "Extra Long";
                }

                // Adding the values to the in map combiner if they exist, adding the key and values
                if(count.containsKey(wordLength)) {
                    int sum = (int)count.get(wordLength) + 1;
                    count.put(wordLength, sum);

                } else {
                    count.put(wordLength, 1);

                }
            }
        }
        catch (Exception ex) {
            LOG.error("Caught Exception", ex);

        }
    }

    // Override the cleanup method to emit the entire HashMap and release resources at end of MapReduce task
    @Override
    public void cleanup(Context context) throws IOException, InterruptedException {
        LOG.info("Running the cleanup method of the Mapper task");

        Iterator<Map.Entry<String, Integer>> temp = count.entrySet().iterator();

        while(temp.hasNext()) {
            Map.Entry<String, Integer> entry = temp.next();
            String keyVal = entry.getKey();
            int countVal = entry.getValue().intValue();

            context.write(new Text(keyVal), new IntWritable(countVal));
        }
    }
}

