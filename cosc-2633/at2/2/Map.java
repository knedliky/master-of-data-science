package s3455453;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class Map extends Mapper<Object, Text, Text, Stripe> {

    private final static FloatWritable one = new FloatWritable(1);

    // Implementing the super setup method
    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    // Overriding the map method and performing a custom, stripes implementations
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException{
        StringTokenizer itr = new StringTokenizer(value.toString());

        Stripe stripe = new Stripe();
        Text word = new Text();
        String[] window = new String[itr.countTokens()];

        for (int i = 0; i < window.length; i++) {
            window[i] = itr.nextToken();
        }

        for (int i = 0; i < window.length; i++) {

            word.set(window[i]);
            stripe.clear();

            int lower = (i - 3 < 0) ? 0 : i -3;
            int upper = (i + 3 < window.length) ? i + 3 : window.length - 1;

            for (int j = lower; j <= upper; j++) {
                if( i == j ) continue;

                Text neighbour = new Text(window[j]);

                if (stripe.containsKey(neighbour)) {
                    stripe.put(neighbour, new FloatWritable(((FloatWritable)stripe.get(neighbour)).get() + one.get()));
                } else {
                    stripe.put(neighbour, one);
                }
            }
            context.write(word, stripe);
        }
    }

    // Implementing the super cleanup method
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }
}
