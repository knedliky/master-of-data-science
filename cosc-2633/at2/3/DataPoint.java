package s3455453;

import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleVector;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class DataPoint implements WritableComparable<DataPoint> {

    private DoubleVector vector;

    public DataPoint() {
        super();
    }

    public DataPoint(DataPoint v) {
        this.vector = v.getVector();
    }

    public DataPoint(DenseDoubleVector v) {
        this.vector = v;
    }

    public DataPoint(double x) {
        this.vector = new DenseDoubleVector(new double[] { x });
    }

    public DataPoint(double x, double y) {
        this.vector = new DenseDoubleVector(new double[] { x, y });
    }

    public DataPoint(double[] arr) {
        this.vector = new DenseDoubleVector(arr);
    }

    @Override
    public final void write(DataOutput out) throws IOException {
        writeVector(this.vector, out);
    }

    @Override
    public final void readFields(DataInput in) throws IOException {
        this.vector = readVector(in);
    }

    @Override
    public final int compareTo(DataPoint o) {
        return compareVector(this, o);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vector == null) ? 0 : vector.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataPoint other = (DataPoint) obj;
        if (vector == null) {
            return other.vector == null;
        } else return vector.equals(other.vector);
    }

    public DoubleVector getVector() {
        return vector;
    }

    @Override
    public String toString() {
        return vector.toString();
    }

    public static void writeVector(DoubleVector vector, DataOutput out) throws IOException {
        out.writeInt(vector.getLength());
        for (int i = 0; i < vector.getDimension(); i++) {
            out.writeDouble(vector.get(i));
        }
    }

    public static DoubleVector readVector(DataInput in) throws IOException {
        final int length = in.readInt();
        DoubleVector vector = new DenseDoubleVector(length);
        for (int i = 0; i < length; i++) {
            vector.set(i, in.readDouble());
        }
        return vector;
    }

    public static int compareVector(DataPoint a, DataPoint o) {
        return compareVector(a.getVector(), o.getVector());
    }

    public static int compareVector(DoubleVector a, DoubleVector o) {
        DoubleVector subtract = a.subtract(o);
        return (int) subtract.sum();
    }

    public static DataPoint wrap(DenseDoubleVector a) {
        return new DataPoint(a);
    }

}
