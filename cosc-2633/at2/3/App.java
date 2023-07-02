package s3455453;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    private static final Log LOG = LogFactory.getLog(App.class);

    public static void main (String[]args) throws IOException, InterruptedException, ClassNotFoundException {

        // Initialisation the configuration of the job and starting the iterations
        int iteration = 1;
        Configuration conf = new Configuration();
        conf.set("num.iteration", iteration + "");

        // Setting the paths for the data and centroid information
        String homePath = new String(args[2]);
        Path PointDataPath = new Path(homePath + "clustering/data.seq");
        Path centroidDataPath = new Path(homePath + "clustering/centroid.seq");
        conf.set("centroid.path", centroidDataPath.toString());
        conf.set("output.path", new Path(args[1]).toString());
        Path outputDir = new Path(homePath + "clustering/depth_1");

        // Creating the MapReduce job
        Job job = Job.getInstance(conf);
        job.setJobName("Assessment Task 2.3");

        // Setting the Mapper and Reducer classes
        job.setMapperClass(KMedioidsMap.class);
        job.setReducerClass(KMedioidsReduce.class);
        job.setJarByClass(App.class);

        // Setting the file input path and number of centroids to calculate
        FileInputFormat.addInputPath(job, PointDataPath);
        FileSystem fs = FileSystem.get(conf);
        int k = Integer.parseInt(args[2]);

        // Deleting file paths if they exist
        if (fs.exists(outputDir)) {
            fs.delete(outputDir, true);
        }

        if (fs.exists(centroidDataPath)) {
            fs.delete(centroidDataPath, true);
        }

        if (fs.exists(PointDataPath)) {
            fs.delete(PointDataPath, true);
        }

        // Generate centroid and datapoint seq files in hdfs
        // Step 1 Start: Initialising centroids at lines k*100 (see method for specifics)
        // generateCentroid(conf, centroidDataPath, fs, k);
        generateCentroid(conf, new Path(args[0]), centroidDataPath, fs, k);
        // Step 1 End
        // generateDataPoints(conf, PointDataPath, fs, k);
        generateDataPoints(conf, new Path(args[0]), PointDataPath, fs, k);

        // Setting the number of reduce tasks, and the output directory
        job.setNumReduceTasks(1);
        FileOutputFormat.setOutputPath(job, outputDir);

        // Setting the input and output format class, as well as the output key value formats
        //job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setOutputKeyClass(Centroid.class);
        job.setOutputValueClass(DataPoint.class);

        job.waitForCompletion(true);

        long counter = job.getCounters().findCounter(KMedioidsReduce.Counter.CONVERGED).getValue();
        iteration++;

        // Starting the first iteration of the clustering job
        // Step 4 Start/ End
        while (counter > 0) {
            System.out.println("Starting the inner loop of the mapreduce task where counter = " + counter);
            conf = new Configuration();
            conf.set("centroid.path", centroidDataPath.toString());
            conf.set("num.iteration", iteration + "");
            job = Job.getInstance(conf);
            job.setJobName("K Medioids Clustering " + iteration);

            // Step 2 Start: Assignment Step (see class)
            job.setMapperClass(KMedioidsMap.class);
            // Step 2 End: Assignment Step
            // Step 3 Start: Update Step (see class)
            job.setReducerClass(KMedioidsReduce.class);
            // Step 3 End: Update Step
            job.setJarByClass(App.class);

            PointDataPath = new Path("clustering/depth_" + (iteration - 1) + "/");
            outputDir = new Path("clustering/depth_" + iteration);

            FileInputFormat.addInputPath(job, PointDataPath);
            if (fs.exists(outputDir))
                fs.delete(outputDir, true);

            FileOutputFormat.setOutputPath(job, outputDir);
            job.setInputFormatClass(SequenceFileInputFormat.class);
            job.setOutputFormatClass(SequenceFileOutputFormat.class);
            job.setOutputKeyClass(Centroid.class);
            job.setOutputValueClass(DataPoint.class);
            job.setNumReduceTasks(1);

            job.waitForCompletion(true);
            iteration++;
            counter = job.getCounters().findCounter(KMedioidsReduce.Counter.CONVERGED).getValue();
        }

        Path result = new Path("clustering/depth_" + (iteration - 1) + "/");

        FileStatus[] stati = fs.listStatus(result);
        for (FileStatus status : stati) {
            if (!status.isDir()) {
                Path path = status.getPath();
                if (!path.getName().equals("_SUCCESS")) {
                    LOG.info("FOUND " + path.toString());
                    try (SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf)) {
                        Centroid key = new Centroid();
                        DataPoint v = new DataPoint();
                        while (reader.next(key, v)) {
                            LOG.info(key + " / " + v);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void generateDataPoints (Configuration conf, Path in, Path out, FileSystem fs, int k) throws IOException {
        try (SequenceFile.Writer dataWriter = SequenceFile.createWriter(fs, conf, out, Centroid.class,
                DataPoint.class)) {

            // Setting up a buffered reader to input lat lon points from text file
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(in)));
            try {
                String line;
                line = br.readLine();
                while (line != null){
                    // Splitting the lines on spaces and reading lat and lon points for each
                    float lat = Float.parseFloat(line.split(" ")[0]);
                    float lon = Float.parseFloat(line.split(" ")[1]);
                    // Assigning a (0,0) centroid to initialise
                    dataWriter.append(new Centroid(new DataPoint(0,0)), new DataPoint(lat, lon));
                    line = br.readLine();
                }
            } finally {
                br.close();
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void generateCentroid (Configuration conf, Path in, Path center, FileSystem fs, int k) throws IOException {
        try (SequenceFile.Writer centerWriter = SequenceFile.createWriter(fs, conf, center, Centroid.class,
                IntWritable.class)) {

            // Initialising rows to equal 1
            int rows = 1;
            final IntWritable value = new IntWritable(0);

            // Setting up a buffered reader to input data from the text file to the seq file
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(in)));
            try {
                String line;
                line = br.readLine();
                int counter = 0;
                while (line != null && counter < k){
                    if ((rows % 100) == 0) {
                        // Splitting the lines on spaces, getting lat and lon points
                        float lat = Float.parseFloat(line.split(" ")[0]);
                        float lon = Float.parseFloat(line.split(" ")[1]);
                        Centroid newCentroid = new Centroid(new DataPoint(lat, lon));
                        centerWriter.append(newCentroid, value);
                        counter++;
                    }
                    // Reading the next line and increasing the iterator
                    line = br.readLine();
                    rows++;
                }
            } finally {
                br.close();
            }
        }
    }
}
