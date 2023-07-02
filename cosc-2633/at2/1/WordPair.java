package s3455453;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class WordPair implements WritableComparable<WordPair> {

    private String firstWord;
    private String secondWord;

    public WordPair(){
    }

    public WordPair(String firstWord, String secondWord){
        set(firstWord, secondWord);
    }

    public void set(String firstWord, String secondWord) {
        this.firstWord = firstWord;
        this.secondWord = secondWord;
    }

    @Override
    public int compareTo(WordPair wordPair) {
        if (firstWord.compareTo(wordPair.firstWord) == 0) {
            return secondWord.compareTo(wordPair.secondWord);
        } else {
            return firstWord.compareTo(wordPair.firstWord);
        }
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        String[] words = new String[] {firstWord, secondWord};
        WritableUtils.writeStringArray(dataOutput, words);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        String[] words = WritableUtils.readStringArray(dataInput);
        firstWord = words[0];
        secondWord = words[1];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("WordPair");
        sb.append("[first word:'").append(firstWord).append('\'');
        sb.append(", second word:'").append(secondWord).append('\'');
        sb.append(']');
        return sb.toString();
    }
}
