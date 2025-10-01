package chess;

import javax.swing.*;
import java.awt.*;



public class BigGUI extends JFrame {
    private ChessGUI chessBoard;

    public BigGUI() {
        setTitle("Chess Application");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create chessboard
        chessBoard = new ChessGUI(Color.WHITE, Color.lightGray, true);

        // Create buttons
        JButton resetButton = new JButton("Reset");
        JButton flipButton = new JButton("Flip Board");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(resetButton);
        buttonPanel.add(flipButton);

        // Add to frame
        add(chessBoard, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Example button action (you’ll hook these up later)
        resetButton.addActionListener(e -> {
            System.out.println("Reset clicked!");
        });
        flipButton.addActionListener(e -> {
            System.out.println("Flip clicked!");
        });
    }
}
