import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private static final double BROADER = 1000.0;
    private int width;
    private int height;
    private double[][] dualGradientEnergy;
    private int[][] imagePixel;
    private boolean vertical = true; 

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        Picture pictureCopy = new Picture(picture);
        width  = pictureCopy.width();
        height = pictureCopy.height();
        
        // 1. compute energy function
        dualGradientEnergy = new double[height][width];
        imagePixel = new int[height][width];
        // StdOut.println("width: "+width+", height: "+height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                imagePixel[i][j] = pictureCopy.getRGB(j, i);
            }
        }
        /* for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++)
                StdOut.printf("%d ", imagePixel[row][col]);
            StdOut.println();
        } */

        int widthLast = width-1;
        int heightLast = height-1;
        for (int col = 0; col < width; col++) {
            dualGradientEnergy[0][col] = BROADER;
            dualGradientEnergy[heightLast][col] = BROADER;
        }
        for (int row = 1; row < height-1; row++) {
            dualGradientEnergy[row][0] = BROADER;
            dualGradientEnergy[row][widthLast] = BROADER;
        }
        for (int col = 1; col < width-1; col++) {
            for (int row = 1; row < height-1; row++) {
                dualGradientEnergy[row][col] = calculateEnergy(imagePixel, col, row);
                // StdOut.println(col+"/"+row+": ("+energyOfPixel+")");
            }
        }
    }

    private double calculateEnergy(int[][] image, int col, int row) {
        int rgbU = image[row][col-1];
        int rgbD = image[row][col+1];
        int rgbL = image[row-1][col];
        int rgbR = image[row+1][col];
        int rU = (rgbU >> 16) & 0xFF;
        int gU = (rgbU >>  8) & 0xFF;
        int bU = (rgbU >>  0) & 0xFF;
        int rD = (rgbD >> 16) & 0xFF;
        int gD = (rgbD >>  8) & 0xFF;
        int bD = (rgbD >>  0) & 0xFF;
        int rL = (rgbL >> 16) & 0xFF;
        int gL = (rgbL >>  8) & 0xFF;
        int bL = (rgbL >>  0) & 0xFF;
        int rR = (rgbR >> 16) & 0xFF;
        int gR = (rgbR >>  8) & 0xFF;
        int bR = (rgbR >>  0) & 0xFF;

        int rX = rL-rR;
        int gX = gL-gR;
        int bX = bL-bR;
        int deltaX = rX*rX+gX*gX+bX*bX;
        int rY = rD-rU;
        int gY = gD-gU;
        int bY = bD-bU;
        int deltaY = rY*rY+gY*gY+bY*bY;
        double energyOfPixel = Math.sqrt(deltaX+deltaY);
        return energyOfPixel;
    }

    // current picture
    public Picture picture() {
        Picture pictureReturn = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (vertical) {
                    pictureReturn.setRGB(i, j, imagePixel[j][i]);
                } else {
                    pictureReturn.setRGB(i, j, imagePixel[i][j]);
                }
            }
        }
        return pictureReturn;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
            throw new IllegalArgumentException();
        }
        double pixel;
        if (vertical) {
            pixel = dualGradientEnergy[y][x];
        } else {
            pixel = dualGradientEnergy[x][y];
        }
        return pixel;
    }

    private int[] scanIdx(int idx, int last) {
        int[] idxAll = new int[3];
        if (idx == 0) {
            idxAll[0] = last;
        } else {
            idxAll[0] = idx-1;
        }
        idxAll[1] = idx;
        if (idx == last) {
            idxAll[2] = 0;
        } else {
            idxAll[2] = idx+1;
        }
        return idxAll;
    }

    private void transposeH() {
        if (vertical) {
            double[][] temp = new double[width][height];
            int[][] imagePixelTemp = new int[width][height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    temp[j][i] = dualGradientEnergy[i][j];
                    imagePixelTemp[j][i] = imagePixel[i][j];
                }
            }
            dualGradientEnergy = temp;
            imagePixel = imagePixelTemp;
            vertical = false;
        }
    }

    private void transposeV() {
        if (!vertical) {
            double[][] temp = new double[height][width];
            int[][] imagePixelTemp = new int[height][width];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    temp[j][i] = dualGradientEnergy[i][j];
                    imagePixelTemp[j][i] = imagePixel[i][j];
                }
            }
            dualGradientEnergy = temp;
            imagePixel = imagePixelTemp;
            vertical = true;
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposeH();
        int[] seam = seekSeam(dualGradientEnergy, width, height);
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        transposeV();
        int[] seam = seekSeam(dualGradientEnergy, height, width);
        return seam;
    }

    private int[] seekSeam(double[][] gradientEnergy, int h, int w) {
        if (h > 2) {
            double[][] distTo = new double[h-2][w];
            int[][] edgeTo = new int[h-2][w];
            for (int i = 0; i < w; i++) {
                distTo[0][i] = gradientEnergy[1][i];
                if (i == 0) {
                    edgeTo[0][i] = w-1;
                } else {
                    edgeTo[0][i] = i-1;
                }
            }
            for (int i = 1; i < h-2; i++) {
               int lastIdx = i-1;
               for (int j = 0; j < w; j++) {
                    int[] idxAll = scanIdx(j, w-1);
                    double min = distTo[lastIdx][idxAll[0]];
                    int minIdx = idxAll[0];
                    for (int k = 1; k < 3; k++) {
                        if (distTo[lastIdx][idxAll[k]] < min) {
                            min = distTo[lastIdx][idxAll[k]];
                            minIdx = idxAll[k];
                        }
                    }
                    distTo[i][j] = gradientEnergy[i+1][j] + distTo[lastIdx][minIdx];
                    edgeTo[i][j] = minIdx;
                } 
            }
            int minIdxAll = 0;
            int lastArrayIdx = h-3;
            double minAll = distTo[lastArrayIdx][minIdxAll];
            for (int i = 1; i < w; i++) {
                if (distTo[lastArrayIdx][i] < minAll) {
                    minAll = distTo[lastArrayIdx][i];
                    minIdxAll = i;
                }
            }
            
            int[] seam = new int[h];
            if (minIdxAll == 0) {
                seam[h-1] = 0;
            } else {
                seam[h-1] = minIdxAll-1;
            }
            seam[h-2] = minIdxAll;
            for (int i = lastArrayIdx; i >= 0; i--) {
                minIdxAll = edgeTo[i][minIdxAll];
                seam[i] = minIdxAll;
            }
            return seam;
        } else {
            int[] seam = new int[h];
            for (int i = 0; i < h; i++) {
                seam[i] = 0;
            }
            return seam;
        }
        
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != width) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < width; i++) {
            if ((seam[i] < 0) || (seam[i] >= height)) {
                throw new IllegalArgumentException();
            }
        }
        int heightLength = height-1;
        for (int i = 0; i < width-1; i++) {
            int diff = Math.abs(seam[i]-seam[i+1]);
            if ((diff > 1) && ((diff != heightLength) || (height <= 3))) {
                throw new IllegalArgumentException("height/width: "+height+"/"+width+", seam["+i+"]: "+seam[i]+"/seam["+i+"+1]: "+seam[i+1]);
            }
        }
        transposeH();

        height = heightLength;
        int[][] imagePixelNew = new int[width][height];
        for (int i = 0; i < width; i++) {
            int lengthFirst = seam[i];
            int lengthSecond = height-seam[i];
            System.arraycopy(imagePixel[i], 0, imagePixelNew[i], 0, lengthFirst);
            System.arraycopy(imagePixel[i], seam[i]+1, imagePixelNew[i], seam[i], lengthSecond);
        }
        
        double[][] dualGradientEnergyNew = new double[width][height];
        System.arraycopy(dualGradientEnergy[0], 0, dualGradientEnergyNew[0], 0, height);
        int lastHeight = height-1;
        for (int i = 1; i < width-1; i++) {
            int lastIdx = seam[i]-1;
            if (lastIdx > 0) {
                System.arraycopy(dualGradientEnergy[i], 0, dualGradientEnergyNew[i], 0, lastIdx);
            } else {
                dualGradientEnergyNew[i][0] = BROADER;
            }
            int lengthSec = lastHeight-seam[i];
            if (lengthSec > 0) {
                System.arraycopy(dualGradientEnergy[i], seam[i]+2, dualGradientEnergyNew[i], seam[i]+1, lengthSec);
            }
            if (lastIdx == 0) {
                dualGradientEnergyNew[i][lastIdx] = BROADER;
            } else if (lastIdx == lastHeight) {
                dualGradientEnergyNew[i][lastIdx] = BROADER;
            } else if (lastIdx >= 1) {
                dualGradientEnergyNew[i][lastIdx] = calculateEnergy(imagePixelNew, lastIdx, i);
            }
            if (seam[i] == lastHeight) {
                dualGradientEnergyNew[i][seam[i]] = BROADER;
            } else if ((seam[i] >= 1) && (seam[i] < height)) {
                dualGradientEnergyNew[i][seam[i]] = calculateEnergy(imagePixelNew, seam[i], i);
            }
        }
        System.arraycopy(dualGradientEnergy[width-1], 0, dualGradientEnergyNew[width-1], 0, height);
        imagePixel = imagePixelNew;
        dualGradientEnergy = dualGradientEnergyNew;
        
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != height) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < height; i++) {
            if ((seam[i] < 0) || (seam[i] >= width)) {
                throw new IllegalArgumentException();
            }
        }
        int widthLength = width-1;
        for (int i = 0; i < height-1; i++) {
            int diff = Math.abs(seam[i]-seam[i+1]);
            // StdOut.println(width+" "+diff+" "+widthLength);
            // StdOut.println("height/width: "+height+"/"+width+", seam["+i+"]: "+seam[i]+"/seam["+i+"+1]: "+seam[i+1]);
            if ((diff > 1) && ((diff != widthLength) || (width <= 3))) {
                throw new IllegalArgumentException("height/width: "+height+"/"+width+", seam["+i+"]: "+seam[i]+"/seam["+i+"+1]: "+seam[i+1]);
            }
        }
        transposeV();

        width = widthLength;
        int[][] imagePixelNew = new int[height][width];
        for (int i = 0; i < height; i++) {
            int lengthFirst = seam[i];
            int lengthSecond = width-seam[i];
            System.arraycopy(imagePixel[i], 0, imagePixelNew[i], 0, lengthFirst);
            System.arraycopy(imagePixel[i], seam[i]+1, imagePixelNew[i], seam[i], lengthSecond);
        }
        double[][] dualGradientEnergyNew = new double[height][width];
        System.arraycopy(dualGradientEnergy[0], 0, dualGradientEnergyNew[0], 0, width);
        int lastWidth = width-1;
        for (int i = 1; i < height-1; i++) {
            int lastIdx = seam[i]-1;
            if (lastIdx > 0) {
                System.arraycopy(dualGradientEnergy[i], 0, dualGradientEnergyNew[i], 0, lastIdx);
            } else {
                dualGradientEnergyNew[i][0] = BROADER;
            }
            // StdOut.println("lastWidth: "+lastWidth+"/seam[i]:"+seam[i]);
            int lengthSec = lastWidth-seam[i];
            if (lengthSec > 0) {
                System.arraycopy(dualGradientEnergy[i], seam[i]+2, dualGradientEnergyNew[i], seam[i]+1, lengthSec);
            }
            if (lastIdx == 0) {
                dualGradientEnergyNew[i][lastIdx] = BROADER;
            } else if (lastIdx == lastWidth) {
                dualGradientEnergyNew[i][lastIdx] = BROADER;
            } else if (lastIdx >= 1) {
                dualGradientEnergyNew[i][lastIdx] = calculateEnergy(imagePixelNew, lastIdx, i);
            }
            if (seam[i] == lastWidth) {
                dualGradientEnergyNew[i][seam[i]] = BROADER;
            } else if ((seam[i] >= 1) && (seam[i] < width)) {
                dualGradientEnergyNew[i][seam[i]] = calculateEnergy(imagePixelNew, seam[i], i);
            }
        }
        System.arraycopy(dualGradientEnergy[height-1], 0, dualGradientEnergyNew[height-1], 0, width);
        imagePixel = imagePixelNew;
        dualGradientEnergy = dualGradientEnergyNew;
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        int width  = picture.width();
        int height = picture.height();
        StdOut.println("width*height: "+width+"*"+height);
        SeamCarver sc = new SeamCarver(picture);
        int[] verticalSeam = sc.findVerticalSeam();
        for (int i = 0; i < verticalSeam.length; i++) {
            StdOut.printf("%d ", verticalSeam[i]);
        }
        StdOut.println();
        int[] randomSeam = { 2, 1, 1, 0, 2, 2, 1 }; /* new int[sc.height()];
        for (int i = 0; i < sc.height(); i++) {
            randomSeam[i] = sc.width()-1;
            StdOut.printf("%d ", randomSeam[i]);
            // randomSeam[i] = 0;
        } */
        StdOut.println();
        sc.removeVerticalSeam(randomSeam);
        StdOut.println("width*height: "+sc.width()+"*"+sc.height());

        /* int[] randomSeam2 = new int[sc.width()];
        for (int i = 0; i < sc.width(); i++) {
            randomSeam2[i] = sc.height()-1;
            StdOut.printf("%d ", randomSeam2[i]);
            // randomSeam2[i] = 0;
        }
        StdOut.println();
        sc.removeHorizontalSeam(randomSeam2);
        StdOut.println("width*height: "+sc.width()+"*"+sc.height()); */

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.2f ", sc.energy(col, row));
            StdOut.println();
        }

    }
}