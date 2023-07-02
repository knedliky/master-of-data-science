package s3455453;

import de.jungblut.math.DoubleVector;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KMedioidsReduce extends Reducer<Centroid, DataPoint, Centroid, DataPoint> {
    // A flag indicates if the clustering converges.
    public static enum Counter {
        CONVERGED
    }

    private final List<Centroid> centers = new ArrayList<>();
    private int iteration = 0;

    //This method is called once for each key. For each cluster, calculate the new center
    protected void reduce(Centroid centroid, Iterable<DataPoint> dataPoints, Context context) throws IOException, InterruptedException {

        List<DataPoint> vectorList = new ArrayList<>();
        DoubleVector newCenter = null;

        for (DataPoint value : dataPoints) {
            vectorList.add(new DataPoint(value));
            if (newCenter == null)
                newCenter = value.getVector().deepCopy();
            else
                newCenter = newCenter.add(value.getVector());
        }

        // Step 3 Start: Update
        // On each dimension, calculate the average of all points in the same cluster for a new centre
        // and then find the nearest existing data point to that centroid and assign it as the new centroid
        newCenter = newCenter.divide(vectorList.size());
        Centroid newCentroid = null;
        DistanceMeasurer distance = new ManhattanDistance();
        double nearestAverageDistance = Double.MAX_VALUE;

        for (DataPoint value : vectorList) {
            double dist = distance.measureDistance(value.getVector(), newCenter);
            if (dist < nearestAverageDistance || newCentroid == null) {
                nearestAverageDistance = dist;
                newCentroid = new Centroid(value);
            }
        }
        centers.add(newCentroid);

        // write new key-value pairs to HDFS depth_i, which will be fed into next round mapReduce job.
        for (DataPoint vector : vectorList) {
            context.write(newCentroid, vector);
        }

        // check if all centroids are converged.
        // If all of them are converged, the counter would be zero.
        // If one or more of them are not, the counter would be greater than zero.
        // Step 3 End

        // Step 4 Start/ End
        if (newCentroid.update(centroid))
            context.getCounter(Counter.CONVERGED).increment(1);

    }

    //Write the recomputed centroids to disk, i.e., centroid.seq
    //Called once at the end of the task
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
        Configuration conf = context.getConfiguration();
        Path outPath = new Path(conf.get("centroid.path"));
        FileSystem fs = FileSystem.get(conf);
        fs.delete(outPath, true);

        try (SequenceFile.Writer out = SequenceFile.createWriter(fs, context.getConfiguration(), outPath, Centroid.class, IntWritable.class)) {
            final IntWritable value = new IntWritable(iteration);
            System.out.println("Iteration Print: " + iteration );

            for (Centroid center : centers) {
                try {
                    out.append(center, value);
                    System.out.println("CenterPrint: "+center.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
