import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
// import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {
    private static final int R = 256; 

    private static class Node {
        public int index;
        public Node next;
        public Node last;

        public Node(int i) {
            index = i;
            next = null;
            last = null;
        }
    }

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
        // StdOut.println("Mode: Burrows-Wheeler transform");
        for (int i = 0; !BinaryStdIn.isEmpty(); i++) {
            String st = BinaryStdIn.readString();
            // StdOut.println(st);

            CircularSuffixArray cs = new CircularSuffixArray(st);

            int indexLast = st.length()-1;
            char[] tArray = new char[st.length()];
            int first = -1;
            for (int j = 0; j < cs.length(); j++) {
                int indexT, indexI = cs.index(j);
                if (indexI == 0) {
                    indexT = indexLast;
                } else {
                    indexT = indexI-1;
                }
                if (indexI == 0) {
                    first = j;
                }
                tArray[j] = st.charAt(indexT);
            }

            // StdOut.println(first);
            BinaryStdOut.write(first);
            for (int j = 0; j < tArray.length; j++) {
                // StdOut.print(tArray[j]);
                BinaryStdOut.write(tArray[j]);
            }
            BinaryStdOut.flush();
            // StdOut.println();
        }
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        // StdOut.println("Mode: Burrows-Wheeler inverse transform");
        int first = -1;
        String st = "";
        if (!BinaryStdIn.isEmpty()) {
            first = BinaryStdIn.readInt();
            // StdOut.println(first);
        }
        if (!BinaryStdIn.isEmpty()) {
            st = BinaryStdIn.readString();
            // StdOut.println(st);
        }
        int n = st.length();
        Node[] nodes = new Node[R];
        int[] next = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            Node nodeCur = nodes[st.charAt(i)];
            if (nodeCur == null) {
                nodeCur = new Node(i);
                nodes[st.charAt(i)] = nodeCur;
            } else {
                if (nodeCur.last != null) {
                    nodeCur.last.next = new Node(i);
                    nodeCur.last = nodeCur.last.next;
                } else {
                    nodeCur.next = new Node(i);
                    nodeCur.last = nodeCur.next;
                }
            }
        }

        for (int i = 0; i < nodes.length; i++) {
            Node nodeCur = nodes[i];
            if (nodeCur != null) {
                // StdOut.print(nodeCur.index+" ");
                next[count++] = nodeCur.index;
                while (nodeCur.next != null) {
                    nodeCur = nodeCur.next;
                    // StdOut.print(nodeCur.index+" ");
                    next[count++] = nodeCur.index;
                }
                // StdOut.println();
            }
        }

        /* for (int i = 0; i < n; i++) {
            StdOut.println("i="+i+", next="+next[i]);
        } */
        int index = next[first];
        count = 1;
        // StdOut.println(n);
        while (count < n) {
            // StdOut.print(st.charAt(index));
            BinaryStdOut.write(st.charAt(index));
            index = next[index];
            count++;
        }
        // StdOut.println(st.charAt(first));
        BinaryStdOut.write(st.charAt(first));
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        int mode = -1;
        if (args.length == 1) {
            if ("-".equals(args[0])) {
                mode = 0;
            } else if ("+".equals(args[0])) {
                mode = 1;
            }
        }

        if (mode == 0) {
            transform();
        } else if (mode == 1) {
            inverseTransform();
        }
    }
}