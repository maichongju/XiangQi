import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Stone {

    protected Color color;
    protected String name;
    protected Cell cell = null;
    protected int[][] moveLocation = null;
    protected Font stoneFont = null;
    public final Dimension stoneSize = new Dimension(85, 85);
    protected final java.awt.Color redColor = new java.awt.Color(176, 11, 0);
    protected final java.awt.Color background = new java.awt.Color(245, 234, 152);

    public List<Cell> getMovableCells(final Board board) {
        List<Cell> result = new ArrayList<>();
        if (moveLocation != null) {
            for (int[] val : moveLocation) {
                Coordinate newLocation = new Coordinate(cell.getCoord().getX() + val[0], cell.getCoord().getY() + val[1]);
                if (val.length > 2) {
                    Coordinate blockLocation = new Coordinate(cell.getCoord().getX() + val[2], cell.getCoord().getY() + val[3]);
                    if (validCell(newLocation) && !board.getCell(blockLocation).hasStone()) {
                        if (board.getCell(newLocation).hasStone()) {
                            if (board.getCell(newLocation).getStone().getColor() != color) {
                                result.add(board.getCell(newLocation));
                            }
                        } else {
                            result.add(board.getCell(newLocation));
                        }
                    }
                } else {
                    if (validCell(newLocation)) {
                        if (board.getCell(newLocation).hasStone()) {
                            if (board.getCell(newLocation).getStone().getColor() != color) {
                                result.add(board.getCell(newLocation));
                            }
                        } else {
                            result.add(board.getCell(newLocation));
                        }
                    }
                }
            }
        }
        return result;
    }

    public enum Color {RED, BLACK}

    public Stone(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    public Cell getCell() {
        return cell;
    }

    public Color getColor() {
        return color;
    }

    public void flipColor(){
        color = color == Color.RED ? Color.BLACK : Color.RED;
    }

    /**
     * Paint the base of Stone.
     *
     * @param g Graphics object for function
     */
    protected void paintStoneBase(Graphics g) {
        Graphics2D antiGraph = (Graphics2D) g;
        int borderThickness = 3;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        antiGraph.setRenderingHints(rh);
        antiGraph.setStroke(new BasicStroke(borderThickness));
        antiGraph.setColor(background);
        antiGraph.fillOval(5, 5,
                stoneSize.width - 10, stoneSize.height - 10);
        antiGraph.setColor(java.awt.Color.black);
        antiGraph.drawOval(5, 5,
                stoneSize.width - 10, stoneSize.height - 10);
        if (color == Color.RED) {
            antiGraph.setColor(redColor);
        }
        antiGraph.drawOval(10, 10,
                stoneSize.width - 20, stoneSize.height - 20);
    }

    /**
     * Paint the word of the stone in the middle of the stone.
     *
     * @param g     Graphics object from function
     * @param s     String that need to print
     * @param color Color of the string
     */
    protected void paintStoneWord(Graphics g, String s, java.awt.Color color) {
        Graphics2D antiGraph = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        antiGraph.setRenderingHints(rh);
        if (stoneFont == null) {
            stoneFont = new Font(g.getFont().getFontName(), Font.BOLD, 40);
        }
        FontMetrics fm = g.getFontMetrics(stoneFont);
        int x = (stoneSize.width - fm.stringWidth(s)) / 2;
        int y = (stoneSize.height - fm.getHeight()) / 2 + fm.getAscent();
        antiGraph.setFont(stoneFont);
        antiGraph.setColor(color);
        antiGraph.drawString(s, x, y);

    }

    /**
     * Return if the current stone is between two king.
     *
     * @param board board
     * @return true if is between false otherwise
     */
    protected boolean isBetweenKing(Board board) {
        boolean upperKing = false, lowerKing = false;

        Coordinate location = cell.getCoord();
        for (int y = location.getY() - 1; y >= 0; y--) {
            if (board.getCell(location.getX(), y).hasStone()) {
                if (board.getCell(location.getX(), y).getStone().isKing()) {
                    upperKing = true;
                }
                break;
            }
        }
        for (int y = location.getY() + 1; y <= 9; y++) {
            if (board.getCell(location.getX(), y).hasStone()) {
                if (board.getCell(location.getX(), y).getStone().isKing()) {
                    lowerKing = true;
                }
                break;
            }
        }

        return upperKing && lowerKing;
    }

    public boolean isKing() {
        return false;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    protected boolean validCell(Coordinate coordinate) {
        return validCell(coordinate.getX(), coordinate.getY());
    }

    protected boolean validCell(int x, int y) {
        return
                x >= 0 && x <= 8 &&
                        y >= 0 && y <= 9;
    }

    public void setFont(Font font) {
        stoneFont = font;
    }

    public void paint(Graphics g) {

        paintStoneBase(g);
        paintStoneWord(g, this.toString(),
                color == Color.RED ? redColor : java.awt.Color.black);
    }

    @Override
    public String toString() {
        return "Stone{" +
                "_color=" + color.name() +
                ", _name='" + name + '\'' +
                '}';
    }
}

class Che extends Stone {
    private final static Logger LOGGER = Logger.getLogger(Stone.class.getName());

    @Override
    public List<Cell> getMovableCells(final Board board) {
        if (cell == null) {
            LOGGER.log(Level.WARNING, "Stone is not in any cell");
            throw new XiangQiException("Stone is not in any cell");
        }
        List<Cell> result = new ArrayList<>();
        Coordinate location = cell.getCoord();
        if (!isBetweenKing(board)) {
            for (int x = location.getX() - 1; x >= 0; x--) {
                result.add(board.getCell(x, location.getY()));
                if (board.getCell(x, location.getY()).hasStone()) {
                    if (board.getCell(x, location.getY()).getStone().color == color) {
                        result.remove(result.size() - 1);
                    }
                    break;
                }
            }
            for (int x = location.getX() + 1; x <= 8; x++) {
                result.add(board.getCell(x, location.getY()));
                if (board.getCell(x, location.getY()).hasStone()) {
                    if (board.getCell(x, location.getY()).getStone().color == color) {
                        result.remove(result.size() - 1);
                    }
                    break;
                }
            }
        }

        for (int y = location.getY() - 1; y >= 0; y--) {
            result.add(board.getCell(location.getX(), y));
            if (board.getCell(location.getX(), y).hasStone()) {
                if (board.getCell(location.getX(), y).getStone().color == color) {
                    result.remove(result.size() - 1);
                }
                break;
            }
        }
        for (int y = location.getY() + 1; y <= 9; y++) {
            result.add(board.getCell(location.getX(), y));
            if (board.getCell(location.getX(), y).hasStone()) {
                if (board.getCell(location.getX(), y).getStone().color == color) {
                    result.remove(result.size() - 1);
                }
                break;
            }
        }
        return result;
    }

    public Che(Color color) {
        super(color, "Che");

    }

    @Override
    public String toString() {
        return color == Color.RED ?
                "俥" :
                "車";
    }
}

class Ma extends Stone {
    private final static Logger LOGGER = Logger.getLogger(Stone.class.getName());

    @Override
    public List<Cell> getMovableCells(final Board board) {
        if (isBetweenKing(board)) {
            return new ArrayList<>();
        }
        super.moveLocation = new int[][]{ // {x,y,x1,y1} relevant location, x1,y1 look out location
                {1, 2, 0, 1},
                {2, 1, 1, 0},
                {2, -1, 1, 0},
                {1, -2, 0, -1},
                {-1, -2, 0, -1},
                {-2, -1, -1, 0},
                {-2, 1, -1, 0},
                {-1, 2, 0, 1}
        };
        return super.getMovableCells(board);
    }

    public Ma(Color color) {
        super(color, "Ma");
    }


    @Override
    public String toString() {
        return color == Color.RED ?
                "傌" :
                "馬";
    }
}

class Xiang extends Stone {
    private final static Logger LOGGER = Logger.getLogger(Stone.class.getName());

    @Override
    public List<Cell> getMovableCells(final Board board) {
        if (isBetweenKing(board)) {
            return new ArrayList<>();
        }
        super.moveLocation = new int[][]{
                {2, 2, 1, 1},
                {2, -2, 1, -1},
                {-2, -2, -1, -1},
                {-2, 2, -1, 1}
        };
        return super.getMovableCells(board);
    }

    @Override
    protected boolean validCell(Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() <= 8 &&
                color == Color.RED ?
                coordinate.getY() >= 0 && coordinate.getY() <= 4 :
                coordinate.getY() >= 5 && coordinate.getY() <= 9;
    }

    public Xiang(Color color) {
        super(color, "Xiang");
    }

    public String toString() {
        return color == Color.RED ?
                "相" :
                "象";
    }
}

class Shi extends Stone {
    private final static Logger LOGGER = Logger.getLogger(Stone.class.getName());

    @Override
    public List<Cell> getMovableCells(final Board board) {
        if (isBetweenKing(board)) {
            return new ArrayList<>();
        }
        super.moveLocation = new int[][]{
                {1, 1},
                {1, -1},
                {-1, -1},
                {-1, 1}
        };
        return super.getMovableCells(board);
    }

    @Override
    protected boolean validCell(Coordinate coordinate) {
        return coordinate.getX() >= 3 && coordinate.getX() <= 5 &&
                color == Color.RED ?
                coordinate.getY() >= 0 && coordinate.getY() <= 2 :
                coordinate.getY() >= 7 && coordinate.getY() <= 9;
    }

    public Shi(Color color) {
        super(color, "Shi");
    }

    public String toString() {
        return color == Color.RED ?
                "仕" :
                "士";
    }
}

class Shuai extends Stone {
    private final static Logger LOGGER = Logger.getLogger(Stone.class.getName());

    @Override
    public boolean isKing() {
        return true;
    }

    public List<Cell> getMovableCells(final Board board) {
        super.moveLocation = new int[][]{
                {0, 1},
                {1, 0},
                {0, -1},
                {-1, 0}
        };
        List<Cell> result = super.getMovableCells(board);
        for (Cell cell : result) {
            Coordinate location = cell.getCoord();
            boolean valid = true;
            if (color == Color.RED) {
                if (cell.getCoord().getY() > this.cell.getCoord().getY()) {
                    for (int y = location.getY() + 1; y <= 9; y++) {
                        if (board.getCell(location.getX(), y).hasStone()) {
                            if (board.getCell(location.getX(), y).getStone().isKing()) {
                                valid = false;
                            }
                            break;
                        }
                    }
                }

            } else {
                if (cell.getCoord().getY() < this.cell.getCoord().getY()) {
                    for (int y = location.getY() - 1; y >= 0; y--) {
                        if (board.getCell(location.getX(), y).hasStone()) {
                            if (board.getCell(location.getX(), y).getStone().isKing()) {
                                valid = false;
                            }
                            break;
                        }
                    }
                }
            }
            if (!valid) {
                result.remove(cell);
                break;
            }
        }
        return result;
    }

    @Override
    protected boolean validCell(Coordinate coordinate) {
        return coordinate.getX() >= 3 && coordinate.getX() <= 5 &&
                color == Color.RED ?
                coordinate.getY() >= 0 && coordinate.getY() <= 2 :
                coordinate.getY() >= 7 && coordinate.getY() <= 9;
    }

    public Shuai(Color color) {
        super(color, "Shuai");
    }

    public String toString() {
        return color == Color.RED ?
                "帥" :
                "将";
    }
}

class Pao extends Stone {
    private final static Logger LOGGER = Logger.getLogger(Stone.class.getName());

    @Override
    public List<Cell> getMovableCells(final Board board) {

        List<Cell> result = new ArrayList<>();
        Coordinate location = cell.getCoord();
        boolean skipStone = false;
        if (!isBetweenKing(board)) {
            for (int x = location.getX() - 1; x >= 0; x--) {
                if (board.getCell(x, location.getY()).hasStone()) {
                    if (!skipStone) {
                        skipStone = true;
                    } else {
                        if (board.getCell(x, location.getY()).getStone().getColor() != color) {
                            result.add(board.getCell(x, location.getY()));
                        }

                        break;
                    }
                } else if (!skipStone) {
                    result.add(board.getCell(x, location.getY()));
                }

            }
            skipStone = false;
            for (int x = location.getX() + 1; x <= 8; x++) {
                if (board.getCell(x, location.getY()).hasStone()) {
                    if (!skipStone) {
                        skipStone = true;
                    } else {
                        if (board.getCell(x, location.getY()).getStone().getColor() != color) {
                            result.add(board.getCell(x, location.getY()));
                        }
                        break;
                    }
                } else if (!skipStone) {
                    result.add(board.getCell(x, location.getY()));
                }
            }
        }

        skipStone = false;
        for (int y = location.getY() - 1; y >= 0; y--) {
            if (board.getCell(location.getX(), y).hasStone()) {
                if (!skipStone) {
                    skipStone = true;
                } else {
                    if (board.getCell(location.getX(), y).getStone().getColor() != color) {
                        result.add(board.getCell(location.getX(), y));
                    }
                    break;
                }
            } else if (!skipStone) {
                result.add(board.getCell(location.getX(), y));
            }
        }
        skipStone = false;
        for (int y = location.getY() + 1; y <= 9; y++) {
            if (board.getCell(location.getX(), y).hasStone()) {
                if (!skipStone) {
                    skipStone = true;
                } else {
                    if (board.getCell(location.getX(), y).getStone().getColor() != color) {
                        result.add(board.getCell(location.getX(), y));
                    }
                    break;
                }
            } else if (!skipStone) {
                result.add(board.getCell(location.getX(), y));
            }
        }
        return result;
    }

    public Pao(Color color) {
        super(color, "Pao");
    }

    public String toString() {
        return color == Color.RED ?
                "砲" :
                "炮";
    }
}

class Bing extends Stone {
    private final static Logger LOGGER = Logger.getLogger(Stone.class.getName());

    @Override
    public List<Cell> getMovableCells(final Board board) {
        List<Cell> result = new ArrayList<>();
        Coordinate location = cell.getCoord();
        if (color == Color.RED && cell.getCoord().getY() <= 4) {
            if (board.getCell(location.getX(), location.getY() + 1).hasStone()) {
                if (board.getCell(location.getX(), location.getY() + 1).getStone().color != color) {
                    result.add(board.getCell(location.getX(), location.getY() + 1));
                }
            } else {
                result.add(board.getCell(location.getX(), location.getY() + 1));
            }

        } else if (color == Color.BLACK && cell.getCoord().getY() >= 5) {
            if (board.getCell(location.getX(), location.getY() - 1).hasStone()) {
                if (board.getCell(location.getX(), location.getY() - 1).getStone().color != color) {
                    result.add(board.getCell(location.getX(), location.getY() - 1));
                }
            } else {
                result.add(board.getCell(location.getX(), location.getY() - 1));
            }
        } else {
            if (color == Color.RED) {
                if (isBetweenKing(board)) {
                    super.moveLocation = new int[][]{
                            {0, 1}
                    };
                } else {
                    super.moveLocation = new int[][]{
                            {-1, 0},
                            {0, 1},
                            {1, 0}
                    };
                }

            } else {
                if (isBetweenKing(board)) {
                    super.moveLocation = new int[][]{
                            {0, -1}
                    };
                } else {
                    super.moveLocation = new int[][]{
                            {-1, 0},
                            {1, 0},
                            {0, -1}
                    };
                }
            }
            result = super.getMovableCells(board);
        }
        return result;
    }

    public Bing(Color color) {
        super(color, "Bing");
    }

    public String toString() {
        return color == Color.RED ?
                "兵" :
                "卒";
    }
}
