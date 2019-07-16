import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
// import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
    private static final int R = 256;
    
    private static class Node {
        public char item;
        public Node next;

        public Node(char item, Node next) {
            this.item = item;
            this.next = next;
        }
    }

    private static Node initializeAsciiNodes() {
        char lastC = (char) (R-1 & 0xff);
        // StdOut.println(lastC);
        Node last = new Node(lastC, null);
        for (int i = R-2; i >= 0; i--) {
            // StdOut.println((char) i);
            char curC = (char) (i & 0xff); 
            Node cur = new Node(curC, last);
            last = cur;
        }
        /* int i = 0;
        while (last.next != null) {
            StdOut.println("i="+i+" "+last.item);
            last = last.next;
            i++;
        }
        StdOut.println("i="+i+" "+last.item); */
        return last;
    }

    private static int find(Node first, char item) {
        Node cur = first;
        int index = 0;
        while (cur != null) {
            if (item == cur.item) {
                return index;
            }
            cur = cur.next;
            index++;
        }
        return -1;
    }

    private static char get(Node first, int indexT) {
        Node cur = first;
        int index = 0;
        char c = (char) 0x00;
        while (cur != null) {
            if (index == indexT) {
                c = cur.item;
                return c;
            }
            cur = cur.next;
            index++;
        }
        return c;
    }

    private static Node delete(Node first, char item) {
        Node cur = first;
        Node last = null;
        while (cur != null) {
            if (item == cur.item) {
                if (last != null) {
                    last.next = cur.next;
                    first = new Node(item, first);
                    return first;
                } else {
                    return first;
                }
            }
            last = cur;
            cur = cur.next;
        }
        return first;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        Node first = initializeAsciiNodes();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int out = find(first, c);
            // StdOut.println("out="+out+", c="+(int) c);
            first = delete(first, c);
            BinaryStdOut.write((char) (out & 0xff)); 
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        Node first = initializeAsciiNodes();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char outC = get(first, c);
            first = delete(first, outC);
            // StdOut.println(outC);
            BinaryStdOut.write(outC);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
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
            encode();
        } else if (mode == 1) {
            decode();
        }
    }

}