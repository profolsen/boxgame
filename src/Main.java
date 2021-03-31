public class Main {
    public static void main(String[] args) {
        Board b = new Board(9);
        for(int i = 0; i < 4; i++) {
            new Player(b);
        }
        b.startGame();
        System.out.println(b);
    }
}
