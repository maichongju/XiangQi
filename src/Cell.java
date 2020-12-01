import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.logging.Logger;

public class Cell extends JComponent {
    private final static Logger LOGGER = Logger.getLogger(Stone.class.getName());
    public final Dimension dimension = new Dimension(85, 85);
    private Stone stone = null;
    private boolean hint = false;
    private boolean selected = false;
    private Coordinate coord = null;

    public Cell(int x, int y) {
        coord = new Coordinate(x, y);
        setBackground(new Color(0, 0, 0, 0));
    }

    public Coordinate getCoord() {
        return coord;
    }

    public void setHint(boolean hint) {
        this.hint = hint;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setStone(Stone stone) {
        this.stone = stone;
    }

    public Stone getStone() {
        return stone;
    }


    public boolean hasStone() {
        return stone != null;
    }

    @Override
    public Dimension getMaximumSize() {
        return dimension;
    }

    @Override
    public Dimension getMinimumSize() {
        return dimension;
    }

    @Override
    public Dimension getPreferredSize() {
        return dimension;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stone != null) {
            stone.paint(g);
        }
        if (selected || hint) {
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setRenderingHints(rh);
            int borderThickness = 3;
            g2d.setStroke(new BasicStroke(borderThickness));
            if (selected) {
                g2d.setColor(new Color(0, 0, 0));
            } else {
                g2d.setColor(new Color(194, 0, 0));
            }
            int len = 15;
            int offset = 3;
            g2d.drawLine(offset, offset, len + offset, offset);
            g2d.drawLine(offset, offset, offset, len + offset);
            g2d.drawLine(dimension.width - len - offset, offset, dimension.width - offset, offset);
            g2d.drawLine(dimension.width  - offset, offset, dimension.width  - offset, len + offset);
            g2d.drawLine(offset, dimension.height - len - offset, offset, dimension.height - offset);
            g2d.drawLine(offset, dimension.height  - offset, len + offset, dimension.height  - offset);
            g2d.drawLine(dimension.width - len - offset, dimension.height  - offset, dimension.width - offset, dimension.height  - offset);
            g2d.drawLine(dimension.width  - offset, dimension.height - len - offset, dimension.width  - offset, dimension.height - offset);
        }


    }

    /**
     * Reset Cell to default. Remove any stone in the cell if there is any
     */
    public void reset() {
        stone = null;
        selected = false;
        hint = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return Objects.equals(coord, cell.coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord);
    }

    @Override
    public String toString() {
        return "Cell{" +
                coord +
                ", stone=" + stone +
                '}';
    }
}

class Coordinate {
    private int X = 0, Y = 0;

    public Coordinate(int x, int y) {
        X = x;
        Y = y;
    }

    public Coordinate(char x, int y) {
        X = x - 'A';
        Y = y;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int[] toArray() {
        return new int[]{X, Y};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return X == that.X &&
                Y == that.Y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(X, Y);
    }

    @Override
    public String toString() {
        return (char) (X + 'A')
                + Integer.toString(Y);
    }
}
