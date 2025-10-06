package chess;

import javax.swing.*;
import java.awt.*;

public class PromotionGUI extends JDialog {
    private String selectedPiece = null;

    public PromotionGUI(JFrame parentFrame, boolean isWhite) {
        super(parentFrame, "Promote", true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // can't close with X
        setUndecorated(true);                          // removes title bar (no move)
        setResizable(false);
        setLayout(new GridLayout(4, 1));
        setSize(120, 400);
        setLocationRelativeTo(parentFrame);

        // Buttons for promotion choices
        addPromotionButton("queen", isWhite );
        addPromotionButton("rook", isWhite );
        addPromotionButton("bishop", isWhite);
        addPromotionButton("knight", isWhite);
    }

private void addPromotionButton(String pieceType, boolean isWhite) {
    String color = isWhite ? "white" : "black";
    ImageIcon rawIcon = new ImageIcon(getClass().getResource("/chess/images/" + color + "_" + pieceType + ".png"));
    Image scaled = rawIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
    JButton button = new JButton(new ImageIcon(scaled));
    button.setBackground(Color.WHITE);
    button.addActionListener(e -> {
        selectedPiece = pieceType;
        dispose();
    });
    add(button);
}


    public String getSelectedPiece() {
        return selectedPiece;
    }

    public static String openPromotionPopup(JFrame parent, boolean isWhite) {
        PromotionGUI dialog = new PromotionGUI(parent, isWhite);
        dialog.setVisible(true);
        return dialog.getSelectedPiece();
    }
}
