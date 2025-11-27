package chess;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class BigGUI extends JFrame {
    private  ChessGUI chessBoard;
    private final IntroGUI introScreen;

    public static JFrame bigGuiReference;

    public BigGUI() {
        bigGuiReference = this;

        setTitle("Chess Application");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        introScreen = new IntroGUI(this::handleFreePlayClicked, this::handleAddOpeningClicked, this::handleAddLineClicked);
        add(introScreen);

        // Create chessboard
//        chessBoard = new ChessGUI(Color.WHITE, Color.lightGray, true);
        //Create Database
        chess.Database.createMovesTable();

        }


   public void handleAddOpeningClicked(String openingName, boolean isWhiteOpening) {
        Database.createOpeningTable(openingName, isWhiteOpening);
   }

   public void handleFreePlayClicked() {
        remove(introScreen);

       // Create chessboard
        chessBoard = new ChessGUI(Color.WHITE, Color.lightGray, true, "freePlay", "freePlay");

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

    public void handleAddLineClicked(String openingName) {
        remove(introScreen);

        // Create chessboard
        chessBoard = new ChessGUI(Color.WHITE, Color.lightGray, true, "addLine", openingName);

        // Create buttons
        JButton saveLineButton = new JButton("Save Line");
//        JButton resetButton = new JButton("New Game");
        JButton flipButton = new JButton("Flip Board");

        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(resetButton);
        buttonPanel.add(saveLineButton);
        buttonPanel.add(flipButton);

        // Add to frame
        add(chessBoard, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveLineButton.addActionListener(e -> {
            // add line code
            System.out.println("Add Line Clicked");
        });
        flipButton.addActionListener(e -> {
            chessBoard.flipBoard();
        });
        revalidate();
        repaint();
    }
}
