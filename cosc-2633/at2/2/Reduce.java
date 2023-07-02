package s3455453;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Reduce extends Reducer<Text, Stripe, Text, Stripe> {
    // Implementing the superclass setup method
    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    // Implementing a custom reduce methods to combine stripes and emit text/ stripe key, value pairs
    @Override
    public void reduce(Text key, Iterable<Stripe> values, Context context) throws IOException, InterruptedException{
        Stripe stripe = new Stripe();
        float sum = 0;

        for (Stripe val : values) {
            stripe.add(val);
        }

        for (Writable stripeKey : stripe.keySet()) {
            sum += ((FloatWritable)stripe.get(stripeKey)).get();
        }

        for (Writable stripeKey : stripe.keySet()) {
            stripe.put(stripeKey, new FloatWritable(((FloatWritable)stripe.get(stripeKey)).get() / sum));
        }
        context.write(key, stripe);
    }

    // Implementing the superclass cleanup method
    @Override
    public void cleanup(Context context) throws IOException, InterruptedException{
        super.cleanup(context);
    }
}
