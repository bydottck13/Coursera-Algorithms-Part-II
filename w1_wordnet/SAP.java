import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;

public class SAP {
    private Digraph graph;
    private LinearProbingHashST<Integer, LinearProbingHashST<Integer, Integer>> st = new LinearProbingHashST<>();
    private LinearProbingHashST<Integer, LinearProbingHashST<Integer, Integer>> stLength = new LinearProbingHashST<>();
    private LinearProbingHashST<Integer, LinearProbingHashST<Integer, Integer>> stAncestor = new LinearProbingHashST<>();

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        graph = new Digraph(G);
    }

    private LinearProbingHashST<Integer, Integer> acquireAncestors(int v, int path) {
        LinearProbingHashST<Integer, Integer> ancestors = new LinearProbingHashST<>();
        if (st.contains(v)) {
            ancestors = st.get(v);
        } else {
            DeluxeBFS bfs = new DeluxeBFS(graph, v, path);
            for (int i = 0; i < graph.V(); i++) {
                if (bfs.hasPathTo(i)) {
                    ancestors.put(i, bfs.distTo(i));
                }
            }
            st.put(v, ancestors);
        }
        return ancestors;
    }

    private int seekCommonAncestors(int v, int w, int path, boolean length) {
        // slow version by using BreadthFirstDirectedPaths
        int pathLength = Integer.MAX_VALUE;
        int ancestorShort = -1;
        LinearProbingHashST<Integer, Integer> ancestorsV = acquireAncestors(v, path);
        LinearProbingHashST<Integer, Integer> ancestorsW = acquireAncestors(w, path);
        SET<Integer> ansV = new SET<>();
        SET<Integer> ansW = new SET<>();
        for (int i : ancestorsV.keys()) {
            ansV.add(i);
        }
        for (int i : ancestorsW.keys()) {
            ansW.add(i);
        }   
        SET<Integer> ancestorCommon = ansV.intersects(ansW);
        if (ancestorCommon.isEmpty())
            return -1;
        for (int i : ancestorCommon) {
            int cur = ancestorsV.get(i) + ancestorsW.get(i);
            if (cur < pathLength) {
                pathLength = cur;
                ancestorShort = i;
            }
        }
        LinearProbingHashST<Integer, Integer> lengthV = new LinearProbingHashST<>();
        LinearProbingHashST<Integer, Integer> ancestorV = new LinearProbingHashST<>();
        lengthV.put(w, pathLength);
        stLength.put(v, lengthV);
        ancestorV.put(w, ancestorShort);
        stAncestor.put(v, ancestorV);
        if (length) {
            return pathLength;
        } else {
            return ancestorShort;
        }

        // quick version by using DeluxeBFS
        /* ArrayList<Integer> s = new ArrayList<Integer>();
        s.add(v);
        s.add(w);
        DeluxeBFS bfs = new DeluxeBFS(graph, s);
        int pathLength = bfs.acquireLength();
        int ancestorShort = bfs.acquireAncestor();
        stLength.put(key, pathLength);
        stAncestor.put(key, ancestorShort);
        if (length) {
            return pathLength;
        } else {
            return ancestorShort;
        } */
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w, int path) {
        int big, small;
        if (v > w) {
            big = v;
            small = w;
        } else {
            big = w;
            small = v;
        }
        if (stLength.contains(big)) {
            LinearProbingHashST<Integer, Integer> lengthCache1 = stLength.get(big);
            if (lengthCache1.contains(small)) {
                int lengthCache2 = lengthCache1.get(small);
                // StdOut.println("Get by cache (stLength)");
                return lengthCache2;
            }
        }
        int pathLength = seekCommonAncestors(big, small, path, true);
        // StdOut.println("v: "+v+", w: "+w+", length: "+pathLength);
        return pathLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w, int path) {
        int big, small;
        if (v > w) {
            big = v;
            small = w;
        } else {
            big = w;
            small = v;
        }
        if (stAncestor.contains(big)) {
            LinearProbingHashST<Integer, Integer> ancestorCache1 = stAncestor.get(big);
            if (ancestorCache1.contains(small)) {
                int ancestorCache2 = ancestorCache1.get(small);
                // StdOut.println("Get by cache (stAncestor)");
                return ancestorCache2;
            }
        }
        int ancestorShort = seekCommonAncestors(big, small, path, false);
        // StdOut.println("v: "+v+", w: "+w+", ancestor: "+ancestorShort);
        return ancestorShort;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if ((v == null) || (w == null)) {
            throw new IllegalArgumentException();
        }
        for (Integer iv : v) {
            if (iv == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer iw : w) {
            if (iw == null) {
                throw new IllegalArgumentException();
            }
        }
        int pathLength = Integer.MAX_VALUE;
        for (int iv : v) {
            for (int iw : w) {
                int cur = length(iv, iw, pathLength);
                if ((cur < pathLength) && (cur != -1)) {
                    pathLength = cur;
                }
            }
        }
        if (pathLength == Integer.MAX_VALUE)
            return -1;
        return pathLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if ((v == null) || (w == null)) {
            throw new IllegalArgumentException();
        }
        for (Integer iv : v) {
            if (iv == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer iw : w) {
            if (iw == null) {
                throw new IllegalArgumentException();
            }
        }
        int pathLength = Integer.MAX_VALUE;
        int ancestorShort = 0;
        for (int iv : v) {
            for (int iw : w) {
                int cur = length(iv, iw, pathLength);
                int i = ancestor(iv, iw, pathLength);
                if ((cur < pathLength) && (cur != -1)) {
                    pathLength = cur;
                    ancestorShort = i;
                }
            }
        }
        if (pathLength == Integer.MAX_VALUE)
            return -1;
        return ancestorShort;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        /*// ArrayList<Integer> vs = null;
        ArrayList<Integer> vs = new ArrayList<Integer>();
        vs.add(13);
        vs.add(23);
        vs.add(24);
        // vs.add(null);
        ArrayList<Integer> ws = new ArrayList<Integer>();
        ws.add(6);
        ws.add(16);
        ws.add(17);
        Stopwatch sp1 = new Stopwatch();
        int lengthAll = sap.length(vs, ws);
        int ancestorAll = sap.ancestor(vs, ws);
        double time1 = sp1.elapsedTime();
        StdOut.printf("length = %d, ancestor = %d, elapsed time is %f s\n", lengthAll, ancestorAll, time1); */

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            Stopwatch sp0 = new Stopwatch();
            int length   = sap.length(v, w, Integer.MAX_VALUE);
            int ancestor = sap.ancestor(v, w, Integer.MAX_VALUE);
            double time0 = sp0.elapsedTime();
            StdOut.printf("length = %d, ancestor = %d, elapsed time is %f s\n", length, ancestor, time0);
        }
    }
}