import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class XiangQi extends JPanel {
    private final Dimension boardDimension = new Dimension(800,900);
    private final Dimension dimension = new Dimension(950,900);

    public XiangQi(){
        setSize(dimension);
        SideBar sideBar = new SideBar(new Dimension(150,900));
        Board board = new Board(boardDimension,sideBar);
        sideBar.setBoard(board);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(board);
        add(sideBar);
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

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Xiang Qi");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new XiangQi());
                frame.setResizable(false);
                try {
                    frame.setIconImage(ImageIO.read(getClass().getResource("/icons/icon.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                frame.pack();
                frame.setVisible(true);

            }
        });

    }
}
