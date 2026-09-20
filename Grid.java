
//Generic 2D Grid class for storing and managing grid based data.
//Provides type safe operations with bounds checking and element management.
  
 //<T>: The type of elements stored in the grid
 
public class Grid<T> {
    private T[][] data;
    private int rows;
    private int cols;

    
     //Constructs a Grid with the specified dimensions.
     //All cells are initially null/empty. 
     //rows: The number of rows in the grid
     //cols: The number of columns in the grid
    @SuppressWarnings("unchecked")
    public Grid(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException(
                    "Grid dimensions must be positive. Received: rows=" + rows + ", cols=" + cols);
        }
        this.rows = rows;
        this.cols = cols;
        this.data = (T[][]) new Object[rows][cols];
    }

    // Gets the element at the specified position.
    
    // row: The row index
    // col: The column index

    // returns the element at the position or null if empty
    // throws ArrayIndexOutOfBoundsException if the position is out of bounds
    
    public T get(int row, int col) throws ArrayIndexOutOfBoundsException {
        checkBounds(row, col);
        return data[row][col];
    }

    // Sets the element at the specified position.
    // value: The element to store (can be null to clear the cell)
    // Throws ArrayIndexOutOfBoundsException if the position is out of bounds
    
    public void set(int row, int col, T value) throws ArrayIndexOutOfBoundsException {
        checkBounds(row, col);
        data[row][col] = value;
    }

    // Checks if a cell is empty (contains null).
    // Returns false if position is out of bounds
    
    public boolean isEmpty(int row, int col) {
        if (!isInBounds(row, col)) {
            return false; // Out of bounds is treated as "not empty"
        }
        return data[row][col] == null;
    }

    // Moves an element from one position to another.
    // Performs validation to ensure both positions are valid and the move is legal.

    // fromRow: The source row
    // fromCol: The source column
    // toRow: The destination row
    // toCol: The destination column

    // Throws ArrayIndexOutOfBoundsException if either position is out of bounds
    // Throws IllegalStateException if the move is invalid due to:
    // Source cell is empty or Destination cell is not empty
    
    public void move(int fromRow, int fromCol, int toRow, int toCol) 
            throws ArrayIndexOutOfBoundsException, IllegalStateException {
        checkBounds(fromRow, fromCol);
        checkBounds(toRow, toCol);

        if (data[fromRow][fromCol] == null) {
            throw new IllegalStateException("Cannot move an empty cell.");
        }

        if (data[toRow][toCol] != null) {
            throw new IllegalStateException("Cannot move element into an occupied cell.");
        }

        data[toRow][toCol] = data[fromRow][fromCol];
        data[fromRow][fromCol] = null;
    }

    // Clears all elements from the grid (sets all cells to null).
    public void clear() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                data[row][col] = null;
            }
        }
    }

    // Gets the number of rows in the grid.
    public int getRows() {
        return rows;
    }

    // Gets the number of columns in the grid.
    public int getCols() {
        return cols;
    }

    // Checks if a position is within grid bounds.
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // Validates that a position is within grid bounds.
    // row: The row index
    // col: The column index
    // throws ArrayIndexOutOfBoundsException if the position is out of bounds
    
    private void checkBounds(int row, int col) throws ArrayIndexOutOfBoundsException {
        if (!isInBounds(row, col)) {
            throw new ArrayIndexOutOfBoundsException(
                    "Invalid grid position: [" + row + "][" + col + "]. " +
                    "Valid range: [0-" + (rows - 1) + "][0-" + (cols - 1) + "]");
        }
    }
}
