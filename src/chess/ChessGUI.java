package chess;

import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {
    private JButton[][] boardSquares = new JButton[8][8];

    public ChessGUI() {
        setTitle("Chess");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                JButton square = new JButton();
                square.setOpaque(true);
                square.setBorderPainted(false);

                if ((row + column) % 2 == 0) {
                    square.setBackground(Color.WHITE);
                } else {
                    square.setBackground(Color.GRAY);
                }

                boardSquares[row][column] = square;
                add(square);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGUI gui = new ChessGUI();
            gui.setVisible(true);
        });
    }
}
