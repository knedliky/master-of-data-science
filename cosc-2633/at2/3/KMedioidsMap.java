package s3455453;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KMedioidsMap extends Mapper<Centroid, DataPoint, Centroid, DataPoint> {

    private final List<Centroid> centers = new ArrayList<>();
    private DistanceMeasurer distanceMeasurer;

    //read in the centroids
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);

        //Mapper implementations can access the Configuration for the job via the JobContext.getConfiguration().
        Configuration conf = context.getConfiguration();

        // We get the URI to the centroid file on hadoop file system (not local fs!).
        // The url is set beforehand in #main.
        Path centroids = new Path(conf.get("centroid.path"));
        FileSystem fs = FileSystem.get(conf);

        // After having the location of the file containing all centroids data,
        // we read them using SequenceFile.Reader, which is another API provided by hadoop for reading binary file
        // The data is modeled in Centroid.class and stored in global variable centers, which will be used in map()
        try (SequenceFile.Reader reader = new SequenceFile.Reader(fs, centroids, conf)) {
            Centroid key = new Centroid();
            IntWritable value = new IntWritable();
            int index = 0;

            while (reader.next(key, value)) {
                Centroid centroid = new Centroid(key);
                centroid.setClusterIndex(index++);
                centers.add(centroid);
            }
        }
        // This is for calculating the distance between a point and another (centroid is essentially a point).
        distanceMeasurer = new ManhattanDistance();
    }

    //map(KEYIN key, VALUEIN value, org.apache.hadoop.mapreduce.Mapper.Context context)
    //Called once for each key/value pair in the input split
    //Process each line from depth_i
    protected void map(Centroid centroid, DataPoint dataPoint, Context context) throws IOException, InterruptedException {
        Centroid nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Centroid c : centers) {
            // Find the nearest centroid for the current dataPoint, pass the pair to reducer
            // Step 2 Start: Assignment
            double distance = distanceMeasurer.measureDistance(c.getCenterVector(), dataPoint.getVector());

            if (distance < nearestDistance || nearest == null) {
                nearestDistance = distance;
                nearest = c;
            }
            // Step 2 End: Assignment
        }
        context.write(nearest, dataPoint);
    }
}
