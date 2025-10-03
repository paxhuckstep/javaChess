package chess;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;



class ChessSquareButton extends JButton {
    private boolean showCircle = false;

    public void setShowCircle(boolean show) {
        this.showCircle = show;
        repaint();
    }

    public boolean getIsLegal() {
        return this.showCircle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (showCircle) {
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
