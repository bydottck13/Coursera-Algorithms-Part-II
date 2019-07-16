import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.Topological;

import java.util.NoSuchElementException;
// import java.util.Arrays;

public class WordNet {
    private LinearProbingHashST<Integer, SET<String>> stIdString = new LinearProbingHashST<>();
    private LinearProbingHashST<String, SET<Integer>> stStringId = new LinearProbingHashST<>();
    private Digraph graph;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if ((synsets == null) || (hypernyms == null)) {
            throw new IllegalArgumentException();
        }
        // handle with synsets
        try {
            In synsetsIn = new In(synsets);
            while (!synsetsIn.isEmpty()) {
                String s = synsetsIn.readLine();
                String[] parts = s.split(",");
                int id = Integer.parseInt(parts[0]);
                SET<String> set = new SET<>();
                String[] words = parts[1].split(" ");
                for (int i = 0; i < words.length; i++) {
                    set.add(words[i]);

                    SET<Integer> ids;
                    if (!stStringId.contains(words[i])) {
                        ids = new SET<>();
                    } else {
                        ids = stStringId.get(words[i]);
                    }
                    ids.add(id);
                    stStringId.put(words[i], ids);
                }
                stIdString.put(id, set);
                // StdOut.println(id+": "+set);
            }   
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in WordNet constructor", e);
        }
        // StdOut.println("The number of nouns in synsets.txt is "+stStringId.size()+" vs. 119,188");

        int vertices = stIdString.size();
        graph = new Digraph(vertices);
        // StdOut.println("synsets: "+vertices);
        // handle with hypernyms
        try {
            In hypernymsIn = new In(hypernyms);
            while (!hypernymsIn.isEmpty()) {
                String s = hypernymsIn.readLine();
                String[] parts = s.split(",");
                int v = Integer.parseInt(parts[0]);
                for (int i = 1; i < parts.length; i++) {
                    int w = Integer.parseInt(parts[i]);
                    graph.addEdge(v, w);
                    // StdOut.print(id+" ");
                }
                // StdOut.println();
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in WordNet constructor", e);
        }
        // StdOut.println("V: "+graph.V()+" vs. 82,192 vertices");
        // StdOut.println("E: "+graph.E()+" vs. 84,505 edges");

        Topological topological = new Topological(graph);
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException("Digraph has cycle");
        }
        int roots = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0) {
                roots++;
                if (roots >= 2) {
                    throw new IllegalArgumentException("Digraph has "+roots+" roots");
                }
            }
        }
        // StdOut.println("Digraph has "+roots+" roots");

        // create SAP
        sap = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        SET<String> nounAll = new SET<>();
        for (String s : stStringId.keys()) {
            nounAll.add(s);
        }
        return nounAll;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return stStringId.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        SET<Integer> idAs = stStringId.get(nounA);
        SET<Integer> idBs = stStringId.get(nounB);
        int dist = sap.length(idAs, idBs);
        StdOut.println("nounA: "+nounA+", ids: "+idAs);
        StdOut.println("nounB: "+nounB+", ids: "+idBs);
        return dist;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        SET<Integer> idAs = stStringId.get(nounA);
        SET<Integer> idBs = stStringId.get(nounB);
        int ancestor = sap.ancestor(idAs, idBs);
        SET<String> set = stIdString.get(ancestor);
        /* String[] setSorted = new String[set.size()];
        int i = 0;
        for (String word : set) {
            setSorted[i] = word;
            i++;
        } */
        // Arrays.sort(setSorted);
        StringBuilder s = new StringBuilder();
        for (String word : set) {
            s.append(word+" ");
        }
        return s.toString();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String[] nounAs = {"white_marlin", "Black_Plague", "American_water_spaniel", "Brown_Swiss"};
        String[] nounBs = {"mileage", "black_marlin", "histology", "barrel_roll"};
        int[] distTarget = {23, 33, 27, 29};

        Stopwatch sp0 = new Stopwatch();
        WordNet wordnet = new WordNet(args[0], args[1]);
        double time0 = sp0.elapsedTime();
        StdOut.println("Elapsed time of building SAP is "+time0+"s");

        /* for (int i = 0; i < 4; i++) {
            String nounA = nounAs[i];
            String nounB = nounBs[i];

            Stopwatch sp1 = new Stopwatch();
            int dist = wordnet.distance(nounA, nounB);
            String ancestor = wordnet.sap(nounA, nounB);
            double time1 = sp1.elapsedTime();

            StdOut.println("The distance between "+nounA+" and "+nounB+" is "+dist+" vs. "+distTarget[i]);
            StdOut.println("The ancestor of "+nounA+" and "+nounB+" is "+ancestor);
            StdOut.println("Elapsed time of each query is "+time1+"s");
        } */

        StdOut.println("The distance between water and coffee is "+wordnet.distance("water", "coffee"));
        StdOut.println("The ancestor of water and coffee is "+wordnet.sap("water", "coffee"));
    }
}