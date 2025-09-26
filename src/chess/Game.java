package chess;

public class Game {
    private Board board;

    public Game() {
        board = new Board();
    }

    public void start() {
        board.setup();
        System.out.println("Chess game started!");
        // Later: add game loop here
    }
}
