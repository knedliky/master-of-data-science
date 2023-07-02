package s3455453;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;

public class Stripe extends MapWritable {
    public void add(Stripe stripe) {
        // Adding existing key/value counts to the stripe, or adding the key/value pair to the pair
        for (Writable key : stripe.keySet()) {
            if (containsKey(key)) {
                put(key, new FloatWritable(((FloatWritable) get(key)).get() + ((FloatWritable) stripe.get(key)).get()));
            } else {
                put(key, stripe.get(key));
            }
        }
    }
}
