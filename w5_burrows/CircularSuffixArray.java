import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

public class CircularSuffixArray {
    private int n = 0;
    private int[] indices;

    private static class CircularSuffix implements Comparable<CircularSuffix> {
        public String s;
        public int index;
        private int n;

        public CircularSuffix(String s, int index) {
            this.s = s;
            this.index = index;
            n = s.length();
        }

        public int charAt(int i) {
            int indexR = i+index;
            if (indexR >= n) {
                indexR = indexR - n;
            }
            int value = (int) this.s.charAt(indexR);
            return value;
        }

        public int compareTo(CircularSuffix that) {
            for (int i = 0; i < n; i++) {
                int cmp = Integer.compare(this.charAt(i), that.charAt(i));
                if (cmp > 0) {
                    return 1;
                } else if (cmp < 0) {
                    return -1;
                }
            }
            return 0;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        n = s.length();
        indices = new int[n];
        CircularSuffix[] cs = new CircularSuffix[n];
        for (int i = 0; i < n; i++) {
            cs[i] = new CircularSuffix(s, i);
        }

        Arrays.sort(cs);

        for (int i = 0; i < n; i++) {
            // StdOut.println("i="+i+", index="+cs[i].index);
            indices[i] = cs[i].index;
        }

    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if ((i < 0) || (i >= n)) {
            throw new IllegalArgumentException();
        }
        int value = indices[i];
        return value;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String test = args[0]; // "ABRACADABRA!";
        Stopwatch sp0 = new Stopwatch();
        CircularSuffixArray cs = new CircularSuffixArray(test);
        double time0 = sp0.elapsedTime();
        StdOut.println("N is "+cs.length());
        int indexLast = test.length()-1;
        char[] tArray = new char[test.length()];
        int first = -1;
        for (int i = 0; i < cs.length(); i++) {
            int indexT, indexI = cs.index(i);
            if (indexI == 0) {
                indexT = indexLast;
            } else {
                indexT = indexI-1;
            }
            if (indexI == 0) {
                first = i;
            }
            tArray[i] = test.charAt(indexT);
            StdOut.println("i="+i+", index:"+indexI+", t["+i+"]="+tArray[i]);
        }
        StdOut.println("First="+first);
        StdOut.println("Elapsed time of building CircularSuffixArray is "+time0+"s");
    }
}