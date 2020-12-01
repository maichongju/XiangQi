import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
        ^
        |
       2|
       1|
       0|
        --------->
        ABCDEFGHI
 */

public class Board extends JPanel implements MouseListener {
    private final static Logger LOGGER = Logger.getLogger(Stone.class.getName());
    private final List<List<Cell>> grid = new ArrayList<>();
    private final int startX;
    private final int startY;
    private final int cellSize = 85;
    private Cell selectedCell = null;
    private boolean gameOver = false;
    private Stone.Color playerTurn = Stone.Color.RED;
    private final List<Cell> hintList = new ArrayList<>();
    private final SideBar sideBar;
    private Dimension dimension;

    public Board(Dimension dimension, SideBar sideBar) {
        this.dimension = dimension;
        this.sideBar = sideBar;
        startX = (dimension.width - (cellSize * 8)) / 2;
        startY = (dimension.height - (cellSize * 9)) / 2; // Title bar need

        // Board Init
        for (int y = 0; y < 10; y++) {
            List<Cell> row = new ArrayList<>();
            for (int x = 0; x < 9; x++) {
                Cell c = new Cell(x, y);
                row.add(c);
                add(c);
                c.setBounds(startX + x * cellSize - cellSize / 2, startY + y * cellSize - cellSize / 2,
                        c.dimension.width, c.dimension.height);
                Board board = this;
                c.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!gameOver){
                            // Other cell selected, clean old cell
                            if (selectedCell != null) {
                                Cell oldCell = selectedCell;
                                selectedCell.setSelected(false);
                                selectedCell.repaint();
                                // Valid move
                                if (hintList.contains(c)) {
                                    cleanHints();
                                    playSound("/audio/go.wav");
                                    if (c.hasStone() && c.getStone().isKing()) {
                                        gameOver = true;
                                        JOptionPane.showMessageDialog(null,
                                                c.getStone().getColor() == Stone.Color.RED ?
                                                        "Black Win":
                                                        "Red Win"
                                        );
                                    } else {
                                        moveStone(selectedCell.getStone(), c);
                                        sideBar.changePlayerTurn();
                                        if (isChecked(c.getStone())){
                                            playSound("/audio/danger.wav");
                                        }
                                    }
                                    selectedCell = null;
                                    playerTurn = playerTurn == Stone.Color.RED ?
                                            Stone.Color.BLACK : Stone.Color.RED;


                                    // SUCCESS CASE
                                    // Step Printing
                                    System.out.println(oldCell.getCoord() + " - " + c.getCoord());
                                    sideBar.addStep(oldCell.getCoord(),c.getCoord());
                                } else {
                                    cleanHints();
                                    if (c.hasStone() && c.getStone().getColor() == playerTurn) {
                                        selectedCell = c;
                                        c.setSelected(true);
                                        c.repaint();
                                        addHints(c.getStone().getMovableCells(board));
                                    }
                                }
                            } else {
                                cleanHints();
                                if (c.hasStone() && c.getStone().getColor() == playerTurn) {
                                    selectedCell = c;
                                    c.setSelected(true);
                                    c.repaint();
                                    playSound("/audio/select.wav");
                                    addHints(c.getStone().getMovableCells(board));
                                }

                            }
                        }


                        System.out.println(c.getCoord() + " Clicked");
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
            }
            grid.add(row);
        }
        addMouseListener(this);
        setLayout(null);

        setSize(dimension);

        setBackground(new Color(206, 93, 0));
        setDefaultStone();
    }

    /**
     * Set up default Stones for the board.
     */
    private void setDefaultStone() {
        // Red 俥 A0 I0
        addStone('A', 0, new Che(Stone.Color.RED));
        addStone('I', 0, new Che(Stone.Color.RED));
        // Red 傌 B0 H0
        addStone('B', 0, new Ma(Stone.Color.RED));
        addStone('H', 0, new Ma(Stone.Color.RED));
        // Red 相 C0 G0
        addStone('C', 0, new Xiang(Stone.Color.RED));
        addStone('G', 0, new Xiang(Stone.Color.RED));
        // Red 仕 D0 F0
        addStone('D', 0, new Shi(Stone.Color.RED));
        addStone('F', 0, new Shi(Stone.Color.RED));
        // Red 帥 E0
        addStone('E', 0, new Shuai(Stone.Color.RED));
        // Red 砲 B2 H2
        addStone('B', 2, new Pao(Stone.Color.RED));
        addStone('H', 2, new Pao(Stone.Color.RED));
        // Red 兵 A3 C3 E3 H3 I3
        addStone('A', 3, new Bing(Stone.Color.RED));
        addStone('C', 3, new Bing(Stone.Color.RED));
        addStone('E', 3, new Bing(Stone.Color.RED));
        addStone('G', 3, new Bing(Stone.Color.RED));
        addStone('I', 3, new Bing(Stone.Color.RED));

        // BLACK 車 A9 I9
        addStone('A', 9, new Che(Stone.Color.BLACK));
        addStone('I', 9, new Che(Stone.Color.BLACK));
        // BLACK 傌 B9 H9
        addStone('B', 9, new Ma(Stone.Color.BLACK));
        addStone('H', 9, new Ma(Stone.Color.BLACK));
        // BLACK 相 C9 G9
        addStone('C', 9, new Xiang(Stone.Color.BLACK));
        addStone('G', 9, new Xiang(Stone.Color.BLACK));
        // BLACK 仕 D9 F9
        addStone('D', 9, new Shi(Stone.Color.BLACK));
        addStone('F', 9, new Shi(Stone.Color.BLACK));
        // BLACK 帥 E9
        addStone('E', 9, new Shuai(Stone.Color.BLACK));
        // BLACK 砲 B7 H7
        addStone('B', 7, new Pao(Stone.Color.BLACK));
        addStone('H', 7, new Pao(Stone.Color.BLACK));
        // BLACK 兵 A6 C6 E6 H6 I6
        addStone('A', 6, new Bing(Stone.Color.BLACK));
        addStone('C', 6, new Bing(Stone.Color.BLACK));
        addStone('E', 6, new Bing(Stone.Color.BLACK));
        addStone('G', 6, new Bing(Stone.Color.BLACK));
        addStone('I', 6, new Bing(Stone.Color.BLACK));
    }

    /**
     * add stone the the given coordinate
     *
     * @param x     X coordinate
     * @param y     Y coordinate
     * @param stone stone need to be add
     */
    private void addStone(char x, int y, Stone stone) {
        int X = x - 'A';
        if (X < 0 || X > 9 || y < 0 || y > 10) {
            LOGGER.log(Level.WARNING, stone.toString() + " Fail to add");
            throw new XiangQiException();
        } else if (stone == null) {
            LOGGER.log(Level.WARNING, "Stone is null, Fail to add");
            throw new XiangQiException("Stone is null");
        }
        Cell currentCell = grid.get(y).get(X);
        stone.setCell(currentCell);
        currentCell.setStone(stone);
        currentCell.repaint();

    }

    private void playSound(String name){
        if (!sideBar.isMuted()){
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(name));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D antiGraph = (Graphics2D) g;
        int borderThickness = 2;
        int edgeThickness = 5;
        antiGraph.setStroke(new BasicStroke(edgeThickness));

        RenderingHints rhAntiOFF = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        antiGraph.setRenderingHints(rhAntiOFF);
        antiGraph.setColor(new Color(81, 59, 1));
        antiGraph.drawRect(2,2,dimension.width-5,dimension.height-5);

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        antiGraph.setRenderingHints(rh);
        antiGraph.setStroke(new BasicStroke(borderThickness));
        antiGraph.setColor(new Color(252, 176, 62));
        antiGraph.fillRect(startX, startY, startX + cellSize * 7 + 25, startY + cellSize * 8 + 40);

        antiGraph.setColor(Color.black);

        for (int i = 0; i < 10; i++) {
            int y = startY + i * cellSize;
            antiGraph.drawLine(startX, y, startX + cellSize * 8, y);
        }
        for (int i = 0; i < 9; i++) {
            int x = startX + i * cellSize;
            if (i == 0 || i == 8) {
                antiGraph.drawLine(x, startY, x,
                        startY + cellSize * 9);
            } else {
                antiGraph.drawLine(x, startY, x,
                        startY + cellSize * 4);
                antiGraph.drawLine(x, startY + cellSize * 5,
                        x, startY + cellSize * 9);
            }
        }
        // X part
        antiGraph.drawLine(startX + cellSize * 3, startY,
                startX + cellSize * 5, startY + cellSize * 2);
        antiGraph.drawLine(startX + cellSize * 5, startY,
                startX + cellSize * 3, startY + cellSize * 2);

        antiGraph.drawLine(startX + cellSize * 3, startY + cellSize * 7,
                startX + cellSize * 5, startY + cellSize * 9);
        antiGraph.drawLine(startX + cellSize * 5, startY + cellSize * 7,
                startX + cellSize * 3, startY + cellSize * 9);

    }

    /**
     * Move the stone to the destination Cell. Function will call repaint for old and new cell. If
     * Stone need to be remove from Cell and destroy, desc should be null
     *
     * @param s    Stone need to move
     * @param desc Destination cell
     */
    private void moveStone(Stone s, Cell desc) {
        Cell oldCell = s.getCell();
        oldCell.setStone(null);
        oldCell.repaint();
        if (desc != null) {
            if (desc.hasStone()) {
                desc.getStone().setCell(null);
                playSound("/audio/eat.wav");
            }
            desc.setStone(s);
            s.setCell(desc);
            desc.repaint();
        }
    }

    public boolean isChecked(Stone s){
        List<Cell> possible = s.getMovableCells(this);
        for (Cell c: possible){
            if (c.hasStone() && c.getStone().isKing() && c.getStone().getColor() != s.getColor()){
                return true;
            }
        }
        return false;
    }

    /**
     * Reset the board to new game status
     */
    public void reset(){
        for (List<Cell> row: grid){
            for (Cell cell: row){
                cell.reset();
                cell.repaint();
            }
        }
        setDefaultStone();
        playerTurn = Stone.Color.RED;
    }

    /**
     * Clean all the cell from hint state
     */
    private void cleanHints() {
        for (Cell cell : hintList) {
            cell.setHint(false);
            cell.repaint();
        }
        hintList.clear();
    }

    /**
     * Add the given cells to the hint and update the cell
     *
     * @param cells
     */
    private void addHints(final List<Cell> cells) {
        for (Cell cell : cells) {
            cell.setHint(true);
            cell.repaint();
            hintList.add(cell);
        }
    }

    public Cell getCell(int x, int y) {
        return grid.get(y).get(x);
    }

    public Cell getCell(Coordinate coordinate) {
        return getCell(coordinate.getX(), coordinate.getY());
    }

    @Override
    public String toString() {
        return "Board{}";
    }

    @Override
    public Dimension getPreferredSize() {
        return dimension;
    }

    @Override
    public Dimension getMinimumSize() {
        return dimension;
    }

    @Override
    public Dimension getMaximumSize() {
        return dimension;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (selectedCell != null) {
            selectedCell.setSelected(false);
            selectedCell.repaint();
            selectedCell = null;
        }
        cleanHints();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}



