# Coursera: Algorithms, Part II
* [Coursera Website](https://www.coursera.org/learn/algorithms-part2)

# Assignments
* [Programming Assignment 1: WordNet](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php)
* [Programming Assignment 2: Seam Carving](https://coursera.cs.princeton.edu/algs4/assignments/seam/specification.php)
* [Programming Assignment 3: Baseball Elimination](https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php)
* [Programming Assignment 4: Boggle](https://coursera.cs.princeton.edu/algs4/assignments/boggle/specification.php)
* [Programming Assignment 5: Burrows–Wheeler](https://coursera.cs.princeton.edu/algs4/assignments/burrows/specification.php)

# Compile
```
$ javac-algs4 -Werror SAP.java DeluxeBFS.java WordNet.java Outcast.java
$ javac-algs4 -Werror PrintEnergy.java SeamCarver.java PrintSeams.java ShowSeams.java ShowEnergy.java ResizeDemo.java SCUtility.java
$ javac-algs4 -Werror BaseballElimination.java
$ javac-algs4 -Werror BoggleSolver.java BoggleBoard.java MyTST.java
$ javac-algs4 -Werror CircularSuffixArray.java BurrowsWheeler.java MoveToFront.java
```

# Instructions
* MP1:
Details refer to [FAQ](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/faq.php).
```
$ zip wordnet.zip DeluxeBFS.java WordNet.java SAP.java Outcast.java
$ java-algs4 SAP digraph1.txt
$ java-algs4 WordNet synsets.txt hypernyms.txt
$ java-algs4 Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
```

* MP2:
Details refer to [FAQ](https://coursera.cs.princeton.edu/algs4/assignments/seam/faq.php) and [Fall16](https://www.cs.princeton.edu/courses/archive/fall16/cos226/checklist/seamCarving.html).
```
$ zip seam.zip SeamCarver.java
$ java-algs4 PrintSeams 10x12.png
$ java-algs4 ResizeDemo 10x12.png 1 1 
```

* MP3:
Details refer to [FAQ](https://coursera.cs.princeton.edu/algs4/assignments/baseball/faq.php).
```
$ zip baseball.zip BaseballElimination.java
$ java-algs4 BaseballElimination teams4.txt
```

* MP4:
Details refer to [FAQ](https://coursera.cs.princeton.edu/algs4/assignments/boggle/faq.php).
```
$ zip boggle.zip BoggleSolver.java MyTST.java
$ java-algs4 BoggleSolver dictionary-yawl.txt board-points0.txt
```

* MP5:
Details refer to [FAQ](https://coursera.cs.princeton.edu/algs4/assignments/burrows/faq.php).
```
$ zip burrows.zip MoveToFront.java BurrowsWheeler.java CircularSuffixArray.java
$ java-algs4 CircularSuffixArray
$ java-algs4 BurrowsWheeler - < abra.txt | java-algs4 edu.princeton.cs.algs4.HexDump 16
$ java-algs4 MoveToFront - < abra.txt | java-algs4 edu.princeton.cs.algs4.HexDump 16
$ java-algs4 MoveToFront - < abra.txt | java-algs4 MoveToFront +
$ time java-algs4 BurrowsWheeler - < mobydick.txt | java-algs4 MoveToFront - | java-algs4 edu.princeton.cs.algs4.Huffman - > mobyDickOutputFileName
$ time java-algs4 edu.princeton.cs.algs4.Huffman + < mobyDickOutputFileName | java-algs4 MoveToFront + | java-algs4 BurrowsWheeler + > moby-copy.txt
```
