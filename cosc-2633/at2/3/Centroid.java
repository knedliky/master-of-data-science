package s3455453;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import de.jungblut.math.DoubleVector;
import org.apache.hadoop.io.WritableComparable;

public class Centroid implements WritableComparable<Centroid> {
    private DoubleVector center;
    private int kTimesIncremented = 1;
    private int clusterIndex;

    public Centroid() {
        super();
    }

    public Centroid(DoubleVector center) {
        super();
        this.center = center.deepCopy();
    }

    public Centroid(Centroid center) {
        super();
        this.center = center.center.deepCopy();
        this.kTimesIncremented = center.kTimesIncremented;
    }

    public Centroid(DataPoint center) {
        super();
        this.center = center.getVector().deepCopy();
    }

    public final void plus(DataPoint c) {
        plus(c.getVector());
    }

    public final void plus(DoubleVector c) {
        center = center.add(c);
        kTimesIncremented++;
    }

    public final void plus(Centroid c) {
        kTimesIncremented += c.kTimesIncremented;
        center = center.add(c.getCenterVector());
    }

    public final void divideByK() {
        center = center.divide(kTimesIncremented);
    }

    public final boolean update(Centroid c) {
        return calculateError(c.getCenterVector()) > 0;
    }

    public final double calculateError(DoubleVector v) {
        return Math.sqrt(center.subtract(v).abs().sum());
    }

    @Override
    public final void write(DataOutput out) throws IOException {
        DataPoint.writeVector(center, out);
        out.writeInt(kTimesIncremented);
        out.writeInt(clusterIndex);
    }

    @Override
    public final void readFields(DataInput in) throws IOException {
        this.center = DataPoint.readVector(in);
        kTimesIncremented = in.readInt();
        clusterIndex = in.readInt();
    }

    @Override
    public final int compareTo(Centroid o) {
        return Integer.compare(clusterIndex, o.clusterIndex);
    }

    // return the center
    public final DoubleVector getCenterVector() {
        return center;
    }

    // returns the index of the cluster
    public int getClusterIndex() {
        return clusterIndex;
    }

    public void setClusterIndex(int clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((center == null) ? 0 : center.hashCode());
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Centroid other = (Centroid) obj;
        if (center == null) {
            if (other.center != null)
                return false;
        } else if (!center.equals(other.center))
            return false;
        return true;
    }

    @Override
    public final String toString() {
        return "Centroid [center=" + center + "]";
    }
}
