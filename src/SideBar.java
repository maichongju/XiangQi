import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SideBar extends JPanel implements MouseListener{
    private Board board;

    private final JLabel labelCurrentPlayer = new JLabel("Current");
    private final JButton btnNewGame = new JButton("New Game");
    private final JButton btnSaveGame = new JButton("Save");
    private final JButton btnLoad = new JButton("Load");
    private final JScrollPane scrollPaneStepLog = new JScrollPane();
    private final StepLogList listStepLog = new StepLogList();
    private final JButton btnAudio = new JButton();

    private final Dimension buttonDimension = new Dimension(100,25);
    private final Dimension dimension;
    private final Cell infoCell = new Cell(-1,-1);
    private boolean isMuted = false;

    public SideBar(Dimension dimension){
        this.dimension = dimension;
        addMouseListener(this);
        setSize(dimension);
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        componentConfig();
        componentInit();


    }

    private void componentConfig(){
        infoCell.setStone(new Shuai(Stone.Color.RED));
        labelCurrentPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelCurrentPlayer.setFont(new Font(labelCurrentPlayer.getFont().getName(),Font.BOLD,18));
        btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNewGame.setMinimumSize(buttonDimension);
        btnNewGame.setMaximumSize(buttonDimension);
        btnNewGame.setPreferredSize(buttonDimension);
        btnNewGame.addActionListener(e -> {
            board.reset();
            reset();
        });

        btnSaveGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSaveGame.setMinimumSize(buttonDimension);
        btnSaveGame.setMaximumSize(buttonDimension);
        btnSaveGame.setPreferredSize(buttonDimension);
        btnSaveGame.setEnabled(false);

        btnLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLoad.setMinimumSize(buttonDimension);
        btnLoad.setMaximumSize(buttonDimension);
        btnLoad.setPreferredSize(buttonDimension);
        btnLoad.setEnabled(false);

        Dimension scrollPanelDimension = new Dimension(100,200);
        scrollPaneStepLog.setViewportView(listStepLog);
        scrollPaneStepLog.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPaneStepLog.setMinimumSize(scrollPanelDimension);
        scrollPaneStepLog.setMaximumSize(scrollPanelDimension);
        scrollPaneStepLog.setPreferredSize(scrollPanelDimension);

        listStepLog.setLayoutOrientation(JList.VERTICAL);

        Dimension audioButtonDimension = new Dimension(32,32);
        btnAudio.setIcon(new ImageIcon(getClass().getResource("/icons/icons8-speaker-high-volume-32.png")));
        btnAudio.setMinimumSize(audioButtonDimension);
        btnAudio.setMaximumSize(audioButtonDimension);
        btnAudio.setPreferredSize(audioButtonDimension);
        btnAudio.setOpaque(false);
        btnAudio.setContentAreaFilled(false);
        btnAudio.setBorderPainted(false);
        btnAudio.addActionListener(e -> {
            isMuted = !isMuted;
            btnAudio.setIcon(new ImageIcon(getClass().getResource(
                    isMuted ? "/icons/icons8-muted-speaker-32.png":
                            "/icons/icons8-speaker-high-volume-32.png"
            )));
        });


    }

    private void componentInit(){
        add(Box.createRigidArea(new Dimension(0,50)));
        add(labelCurrentPlayer);
        add(infoCell);
        add(Box.createRigidArea(new Dimension(0,375)));
        add(btnNewGame);
        add(Box.createRigidArea(new Dimension(0,5)));
        add(btnSaveGame);
        add(Box.createRigidArea(new Dimension(0,5)));
        add(btnLoad);
        add(Box.createRigidArea(new Dimension(0,25)));
        add(scrollPaneStepLog);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(btnAudio);
    }

    public void addStep(Coordinate f, Coordinate t){
        listStepLog.addStep(f,t);
    }

    public void setBoard(Board b){
        board = b;
    }

    /**
     * Change player turn icon
     */
    public void changePlayerTurn(){
        infoCell.getStone().flipColor();
        infoCell.repaint();
    }

    /**
     * Reset Side bar
     */
    public void reset(){
        listStepLog.clear();
        infoCell.getStone().color = Stone.Color.RED;
        infoCell.repaint();
    }

    /**
     * Get if it is in mute status
     * @return
     */
    public boolean isMuted(){
        return isMuted;
    }

    @Override
    public Dimension getPreferredSize() {
        return dimension;
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
    public void mouseClicked(MouseEvent e) {
        listStepLog.clearSelection();
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

class StepLogList extends JList<Step> implements MouseListener{
    private final DefaultListModel<Step> listModel = new DefaultListModel<>();

    public StepLogList(){
        setModel(listModel);
        setVisibleRowCount(-1);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addMouseListener(this);
        setCellRenderer(new StepLogCellRenderer());
    }

    public void addStep(Coordinate from, Coordinate to){
        listModel.addElement(new Step(from,to));
        ensureIndexIsVisible(listModel.size()-1);
    }

    public void saveToFile(){

    }

    /**
     * Clear all the steps
     */
    public void clear(){
        listModel.clear();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            clearSelection();
        }
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

class StepLogCellRenderer implements ListCellRenderer<Step>{

    @Override
    public Component getListCellRendererComponent(JList<? extends Step> list, Step value, int index, boolean isSelected, boolean cellHasFocus) {
        DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
        JLabel render = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
        render.setFont(new Font(render.getFont().getName(),Font.PLAIN,14));
        return render;
    }
}
class Step{
    public Coordinate from;
    public Coordinate to;
    public Step(Coordinate f, Coordinate t){
        from = f;
        to = t;
    }

    @Override
    public String toString() {
        return from + " - " + to;
    }
}
