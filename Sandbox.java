public class Sandbox {

    static int WIDTH = 800;
    static int HEIGHT = 600;
    static int CELL_SIZE = 4;

    static int COLS = WIDTH / CELL_SIZE;
    static int ROWS = HEIGHT / CELL_SIZE;

    Elements[][] grid;

    Tester<Elements> tester = new Tester<>();

    public Sandbox() {
        tester.set(new Sand());
        grid = new Elements[ROWS][COLS];
        grid[20][40] = tester.get();
    }


      public void clearSandbox() {
        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++) {
                grid[row][col] = null;
            }
        }

    }

    int frameCounter = 0;

    // Checks that a position is inside the grid
    public void checkPosition(int row, int col) throws Exception {
        if (row < 0 || row >= ROWS ||
                col < 0 || col >= COLS) {
            throw new Exception(
                    "Invalid grid position: [" + row + "][" + col + "]");
        }
    }

    public void moveElement(int row, int col, int newRow, int newCol) throws Exception {
        // Check both positions
        checkPosition(row, col);
        checkPosition(newRow, newCol);
        // Check that there is actually an element to move
        if (grid[row][col] == null) {
            throw new Exception("Cannot move an empty cell.");
        }
        // Check that the destination is empty
        if (grid[newRow][newCol] != null) {
            throw new Exception("Cannot move element into an occupied cell.");
        }
        // Move element
        grid[newRow][newCol] = grid[row][col];
        grid[row][col] = null;
    }

    public void step() {
        frameCounter++;
                // Remove fire that has burnt out, otherwise it stays in the grid and piles up
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col] instanceof Fire && ((Fire) grid[row][col]).isDead()) {
                    grid[row][col] = null;
                }
            }
        }

        // Normal gravity
        for (int row = ROWS - 2; row >= 0; row--) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col] != null &&
                        !grid[row][col].reverseGravity) {

                    // Fall straight down
                    if (grid[row + 1][col] == null) {
                        try {
                            moveElement(row, col, row + 1, col);
                        } catch (Exception e) {
                            System.out.println("Gravity error: " + e.getMessage());
                        }
                    }
                    // If blocked, move diagonally
                    else {
                        side_gravity(row, col);
                    }
                }
            }
        }

        // Reverse gravity
        if (frameCounter % 2 == 0) {
            for (int row = 1; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (grid[row][col] != null && grid[row][col].reverseGravity) {
                        // 60% chance to move diagonally
                        if (Math.random() < 0.6) {
                            try {
                                reverse_side_gravity(row, col);
                            } catch (Exception e) {
                                System.out.println("Reverse side gravity error: " + e.getMessage());
                            }
                        }
                        // Otherwise move straight up
                        else if (grid[row - 1][col] == null) {
                            try {
                                moveElement(row, col, row - 1, col);
                            } catch (Exception e) {
                                System.out.println("Reverse gravity error: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    public void side_gravity(int row, int col) {
        if (Math.random() < 0.5) {
            // Try down-left
            if (col > 0 && grid[row + 1][col - 1] == null) {
                grid[row + 1][col - 1] = grid[row][col];
                grid[row][col] = null;
            }
            // If left is blocked, try down-right
            else if (col < COLS - 1 && grid[row + 1][col + 1] == null) {
                grid[row + 1][col + 1] = grid[row][col];
                grid[row][col] = null;
            }
        } else {
            // Try down-right
            if (col < COLS - 1 && grid[row + 1][col + 1] == null) {
                grid[row + 1][col + 1] = grid[row][col];
                grid[row][col] = null;
            }
            // If right is blocked, try down-left
            else if (col > 0 && grid[row + 1][col - 1] == null) {

                grid[row + 1][col - 1] = grid[row][col];
                grid[row][col] = null;
            }
        }
    }

    // Reverse gravity:
    // 50% chance left, 50% chance right
    public void reverse_side_gravity(int row, int col) {
        if (Math.random() < 0.5) {
            if (col > 0 && grid[row - 1][col - 1] == null) {
                grid[row - 1][col - 1] = grid[row][col];
                grid[row][col] = null;
            } else if (col < COLS - 1 && grid[row - 1][col + 1] == null) {
                grid[row - 1][col + 1] = grid[row][col];
                grid[row][col] = null;
            }
        } else {
            if (col < COLS - 1 && grid[row - 1][col + 1] == null) {
                grid[row - 1][col + 1] = grid[row][col];
                grid[row][col] = null;
            } else if (col > 0 && grid[row - 1][col - 1] == null) {
                grid[row - 1][col - 1] = grid[row][col];
                grid[row][col] = null;
            }
        }
    }

    // Generic tester class
    class Tester<T> {

        T value;

        void set(T value) {
            this.value = value;
        }

        T get() {
            return value;
        }
    }
}
