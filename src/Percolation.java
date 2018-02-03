import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int sideLength;
    private int numberOfCells;
    private int topVirtualsite;
    private int bottomVirtualSite;
    boolean[] openOrClosed; // automatically initializes all elements to false
    int[][] cellArray;

    WeightedQuickUnionUF unionObj;  // add two to account for the top and bottom virtual sites. This needs to be one greater than the total number of cells to work

    public Percolation(int sideLength) {
        this.sideLength = sideLength;
        this.numberOfCells = sideLength * sideLength;
        this.topVirtualsite = numberOfCells + 1;
        this.bottomVirtualSite = numberOfCells + 2;
        this.openOrClosed = new boolean[numberOfCells];
        this.cellArray = new int[sideLength][sideLength];
        this.unionObj = new WeightedQuickUnionUF(numberOfCells + 3);

        // load the two dimensional array
        int increment = 0;
        for (int outerCounter = 0; outerCounter < sideLength; outerCounter++) {
            for (int innerCounter = 0; innerCounter < sideLength; innerCounter++) {
                cellArray[outerCounter][innerCounter] = increment;
                increment++;
            }
        }

        // join to the top and bottom virtual sites
        for (int counter = 0; counter < sideLength; counter++) {
            unionObj.union(cellArray[0][counter], topVirtualsite);
            unionObj.union(cellArray[sideLength - 1][counter], bottomVirtualSite);
        }
    }

    public void open(int y, int x) {
        int cellNumber = xyTo1Dimension(y, x);
        System.out.println(cellNumber);
        openOrClosed[cellNumber] = true;

        //one to the right
        if (joinable(y, (x + 1)) != null) {
            if (isOpen(y, x + 1)) {
                unionObj.union(cellNumber, cellNumber + 1);
                System.out.println(cellNumber);
            }
        }
        //one to the left
        if (joinable(y, x - 1) != null) {
            if (isOpen(y, x - 1)) {
                unionObj.union(cellNumber, cellNumber - 1);
            }
        }
        //one up
        if (joinable(y - 1, x) != null) {
            if (isOpen(y - 1, x)) {
                unionObj.union(cellNumber, cellNumber - sideLength);
            }
        }
        //one down
        if (joinable(y + 1, x) != null) {
            if (isOpen(y + 1, x)) {
                unionObj.union(cellNumber, cellNumber + sideLength);
            }
        }

    }

    public boolean isOpen(int y, int x) {
        validateIndices(x);
        validateIndices(y);
        return openOrClosed[xyTo1Dimension(y, x)]; // set openorclosed to true at the corresponding index of the cell number
    }

    // to be full a cell must be open and connected to the top virtual site
    public boolean isFull(int y, int x) {
        int cellNumber = xyTo1Dimension(y, x);
        boolean connected = false;
        if (isOpen(y, x) == true) {
            connected = unionObj.connected(cellNumber, topVirtualsite);
        }
        return connected;
    }

    public boolean percolates() {
        return unionObj.connected(topVirtualsite, bottomVirtualSite);

    }

    private int xyTo1Dimension(int y, int x) {
        validateIndices(x);
        validateIndices(y);
        int singleDimension = 0;
        if (y > 0) {
            singleDimension = (sideLength * y) + x;
        } else if (y == 0) {
            singleDimension = x;
        }

        return singleDimension;
    }

    private void validateIndices(int index) {
        if (index < 0 || index > sideLength) {
            throw new IndexOutOfBoundsException("row index " + index + " must be between 0 and " + sideLength);
        }
    }

    // required for the visualizers to work
    protected int numberOfOpenSites() {
        int count = 0;
        for (boolean element : openOrClosed) {
            if (element == true) {
                count++;
            }
        }
        return count;
    }

    // Takes in two numbers and returns the cellNumber if it is available or null if not
    private java.lang.Integer joinable(int x, int y) {
        try {
            return cellArray[y][x];
        } catch (Exception e) {
            return null;
        }
    }
}
