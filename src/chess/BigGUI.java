package chess;

import javax.swing.*;
import java.awt.*;

public class BigGUI extends JFrame {
    private final ChessGUI chessBoard;
    private final IntroGUI introScreen;

    public static JFrame bigGuiReference;

    public BigGUI() {
        bigGuiReference = this;

        setTitle("Chess Application");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        introScreen = new IntroGUI(this::handleFreePlayClicked);
        add(introScreen);

        // Create chessboard
        chessBoard = new ChessGUI(Color.WHITE, Color.lightGray, true);

        }

   public void handleFreePlayClicked() {
        remove(introScreen);

        //Create Database
        chess.Database.createMovesTable();

        // Create buttons
        JButton resetButton = new JButton("New Game");
        JButton flipButton = new JButton("Flip Board");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(resetButton);
        buttonPanel.add(flipButton);

        // Add to frame
        add(chessBoard, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        resetButton.addActionListener(e -> {
            chessBoard.resetGame();
        });
        flipButton.addActionListener(e -> {
            chessBoard.flipBoard();
        });
        revalidate();
        repaint();
    }
}
