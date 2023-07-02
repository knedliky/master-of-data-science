package s3455453;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Map extends Mapper<Object, Text, WordPair, IntWritable> {

    private HashMap<WordPair,Integer> count;
    // private static final IntWritable one = new IntWritable(1);

    // Overriding the setup method to instantiate associative array
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        count = new HashMap<WordPair, Integer>();
    }

    // Overriding the map method with custom implementation
    @Override
    protected void map(Object key, Text value,  Context context) throws IOException, InterruptedException {
        StringTokenizer itr = new StringTokenizer(value.toString());

        String[] window = new String[itr.countTokens()];

        for (int i = 0; i < window.length; i++) {
            window[i] = itr.nextToken();
        }

        for (int i = 0; i < window.length; i++) {
            String firstWord = (window[i]);

            int lower = (i - 3 < 0) ? 0 : i -3;
            int upper = (i + 3 < window.length) ? i + 3 : window.length - 1;

            for (int j = lower; j <= upper; j++) {
                if (i == j) continue;

                String secondWord = (window[j]);
                if (firstWord.compareTo(secondWord) == 0) continue;

                WordPair wordPair = new WordPair(firstWord, secondWord);

                if (count.containsKey(wordPair)) {
                    count.put(wordPair, count.get(wordPair) + 1);
                } else {
                    count.put(wordPair, 1);
                }
            }
        }
    }

    // Overriding the cleanup method to emit the associative array at the end of the task
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (WordPair wordPair : count.keySet()) {
            context.write(wordPair, new IntWritable(count.get(wordPair)));
        }
    }
}
