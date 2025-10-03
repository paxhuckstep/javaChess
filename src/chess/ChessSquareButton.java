package chess;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;



class ChessSquareButton extends JButton {
    private boolean isLegal = false;

    public void setIsLegal(boolean isLegal) {
        this.isLegal = isLegal;
        repaint();
    }

    public boolean getIsLegal() {
        return this.isLegal;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isLegal) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(165, 42, 42, 128)); // red with 50% transparency
            int diameter = Math.min(getWidth(), getHeight()) / 3;
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
            g2d.fillOval(x, y, diameter, diameter);
            g2d.dispose();
        }
    }

}
