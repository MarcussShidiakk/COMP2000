public class Sandbox {

    static int WIDTH = 800;
    static int HEIGHT = 600;
    static int CELL_SIZE = 4;

    static int COLS = WIDTH / CELL_SIZE;
    static int ROWS = HEIGHT / CELL_SIZE;
//
    Grid<Elements> grid;
//
    Tester<Elements> tester = new Tester<>();

    public Sandbox() {
        tester.set(new Sand());//
        grid = new Grid<>(ROWS, COLS);
        try {
            grid.set(20, 40, tester.get());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error initializing grid: " + e.getMessage());
        }
    }//

    int frameCounter = 0;

    public boolean isEmpty(int row, int col) { //checks if the square is on the grid//
        return grid.isEmpty(row, col);
    }//

    public void moveElement(int row, int col, int newRow, int newCol) 
            throws ArrayIndexOutOfBoundsException, IllegalStateException {
        // Grid.move() handles all validation including bounds checking and emptiness checks
        grid.move(row, col, newRow, newCol);
    }//

    public void step() {
        frameCounter++;//
        try {
            // Remove fire that has burnt out, otherwise it stays in the grid and piles up
            for (int row = 0; row < grid.getRows(); row++) {
                for (int col = 0; col < grid.getCols(); col++) {
                    Elements element = grid.get(row, col);
                    if (element instanceof Fire && ((Fire) element).isDead()) {
                        grid.set(row, col, null);
                    }
                }
            }
//
            for (int row = grid.getRows() - 2; row >= 0; row--) {
                for (int col = 0; col < grid.getCols(); col++) {
                    Elements e = grid.get(row, col);
                    if (e != null && e.fallsDown()) {
                        e.step(this, row, col);
                    }
                }
            }
//
            if (frameCounter % 2 == 0) {
                for (int row = 1; row < grid.getRows(); row++) {
                    for (int col = 0; col < grid.getCols(); col++) {
                        Elements e = grid.get(row, col);
                        if (e != null && e.floatsUp()) {
                            e.step(this, row, col);
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Step error: Invalid grid position - " + e.getMessage());
        }
    }

    public void side_gravity(int row, int col) {//
        try {
            if (Math.random() < 0.5) {
                // Try down-left
                if (col > 0 && grid.isEmpty(row + 1, col - 1)) {
                    grid.move(row, col, row + 1, col - 1);
                }
                // If left is blocked, try down-right
                else if (col < grid.getCols() - 1 && grid.isEmpty(row + 1, col + 1)) {
                    grid.move(row, col, row + 1, col + 1);
                }
            } else {
                // Try down-right
                if (col < grid.getCols() - 1 && grid.isEmpty(row + 1, col + 1)) {
                    grid.move(row, col, row + 1, col + 1);
                }
                // If right is blocked, try down-left
                else if (col > 0 && grid.isEmpty(row + 1, col - 1)) {
                    grid.move(row, col, row + 1, col - 1);
                }
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalStateException e) {
            System.out.println("Side gravity error: " + e.getMessage());
        }
    }

    // Reverse gravity:
    // 50% chance left, 50% chance right
    public void reverse_side_gravity(int row, int col) {//
        try {
            if (Math.random() < 0.5) {
                if (col > 0 && grid.isEmpty(row - 1, col - 1)) {
                    grid.move(row, col, row - 1, col - 1);
                } else if (col < grid.getCols() - 1 && grid.isEmpty(row - 1, col + 1)) {
                    grid.move(row, col, row - 1, col + 1);
                }
            } else {
                if (col < grid.getCols() - 1 && grid.isEmpty(row - 1, col + 1)) {
                    grid.move(row, col, row - 1, col + 1);
                } else if (col > 0 && grid.isEmpty(row - 1, col - 1)) {
                    grid.move(row, col, row - 1, col - 1);
                }
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalStateException e) {
            System.out.println("Reverse side gravity error: " + e.getMessage());
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
