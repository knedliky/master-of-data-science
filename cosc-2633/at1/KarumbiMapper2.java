package s3455453;

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

public class KarumbiMapper2 extends Mapper<Object, Text, Text, IntWritable> {
    // Create logger for class
    private final static Logger LOG = Logger.getLogger(KarumbiMapper2.class);

    private Text word = new Text();
    private Text wordLength = new Text();
    private Map count;

    // Override the hadoop map method with custom implementation
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Set logging to DEBUG
        LOG.info("The Map Task 2 of Simon Karumbi, s3455453");
        LOG.setLevel(Level.DEBUG);

        // Instantiating the local associative array
        count = new HashMap<String, Integer>();

        try {
            // Log every line
            LOG.debug(value);

            // Map word to word length key
            StringTokenizer itr = new StringTokenizer(value.toString());

            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                int length = word.toString().length();

                if (length > 0 && length <= 4) {
                    wordLength.set("Short");

                } else if (length > 4 && length <= 7) {
                    wordLength.set("Medium");

                } else if (length > 7 && length <= 10) {
                    wordLength.set("Long");

                } else if (length > 10) {
                    wordLength.set("Extra Long");
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

        Iterator<Map.Entry<Text, Integer>> temp = count.entrySet().iterator();

        while(temp.hasNext()) {
            Map.Entry<Text, Integer> entry = temp.next();
            String keyVal = entry.getKey().toString();
            int countVal = entry.getValue().intValue();

            context.write(new Text(keyVal), new IntWritable(countVal));
        }

    }

}

