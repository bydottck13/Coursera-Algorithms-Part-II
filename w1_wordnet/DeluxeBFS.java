/*
 * Modify by BreadthFirstDirectedPaths.java
 * Optimal version for shortest common ancestor
 * @author Bo-Rong Chen
 */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;

public class DeluxeBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;
    private int[] distTo;
    private int foundSoFar;

    private int ancestor = -1;
    private int length = -1;
    private boolean found = false;

    public DeluxeBFS(Digraph G, int s, int path) {
        foundSoFar = path;
        StdOut.println("s:"+s+", foundSoFar "+foundSoFar);
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        validateVertex(s);
        bfs(G, s);
    }

    public DeluxeBFS(Digraph G, Iterable<Integer> sources, int path) {
        foundSoFar = path;
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
        validateVertices(sources);
        bfs(G, sources);
    }

    // BFS from single source
    private void bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        marked[s] = true;
        distTo[s] = 0;
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    // BFS from multiple sources
    private void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }


    public boolean hasPathTo(int v) {
        validateVertex(v);
        return marked[v];
    }

    public int distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    public int acquireAncestor() {
        return ancestor;
    }

    public int acquireLength() {
        return length;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = marked.length;
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        // StdOut.println(G);

        ArrayList<Integer> s = new ArrayList<Integer>();
        s.add(1);
        s.add(6);
        // s.add(3);
        // s.add(11);
        Stopwatch sp0 = new Stopwatch();
        DeluxeBFS bfs = new DeluxeBFS(G, s, INFINITY);
        StdOut.println("length "+bfs.acquireLength());
        StdOut.println("ancestor "+bfs.acquireAncestor());
        double time0 = sp0.elapsedTime();
        StdOut.println("Elapsed time is "+time0+"s");
    }
}