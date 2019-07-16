import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;

public class Outcast {
    private WordNet wordnetCopy;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException();
        }
        wordnetCopy = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException();
        }
        int n = nouns.length;
        for (int i = 0; i < n; i++) {
            if (nouns[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        // cache distance
        int[][] distAll = new int[n][n];
        int distanceMax = -1;
        int idxMax = -1;
        for (int i = 0; i < n; i++) {
            // StdOut.println("New round");
            int distance = 0;
            for (int j = 0; j < i; j++) {
                distance = distance+distAll[j][i];
            }
            for (int j = i+1; j < n; j++) {
                distAll[i][j] = wordnetCopy.distance(nouns[i], nouns[j]);
                distance = distance+distAll[i][j];
            }
            if (distance >= distanceMax) {
                distanceMax = distance;
                idxMax = i;
            }
        }
        // for debug
        /* for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                StdOut.print(distAll[i][j]+" ");
            }
            StdOut.println();
        } */
        String outcastString = nouns[idxMax];
        // StdOut.println("distanceMax: "+distanceMax);
        return outcastString;
    }

    // see test client below
    public static void main(String[] args) {
        Stopwatch sp0 = new Stopwatch();
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        // String[] ss = {"water", "coffee"};
        // StdOut.println("water-coffee: " + outcast.outcast(ss));

        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
        double time0 = sp0.elapsedTime();
        StdOut.println("Elapsed time is "+time0+"s");
    }
}