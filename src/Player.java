import java.util.Random;

public class Player extends Thread{
    boolean[] visited = null;
    Random random = null;
    Board board = null;
    int currentSquare = -1;
    boolean gameOver = false;

    public Player(Board b) {
        visited = new boolean[b.squares()];
        board = b;
        b.addPlayer(this);
        random = new Random(Thread.currentThread().getName().hashCode() ^ System.currentTimeMillis());
        gameOver = false;
    }

    public void run() {
        while(! (won() || gameOver)) {
            int nextSquare = random.nextInt(visited.length);
            while(visited[nextSquare]) nextSquare = random.nextInt(board.squares());
            //System.out.println(Thread.currentThread().getName() + " from " + currentSquare + " to " + nextSquare);
            board.move(currentSquare, nextSquare);
            currentSquare = nextSquare;
            visited[currentSquare] = true;
        }
        board.move(currentSquare, -1);
        board.endGame();
        if(won()) System.out.println(Thread.currentThread().getName() + " won.");
    }

    public boolean visited(int square) {
        return visited[square];
    }

    public boolean won() {
        for(boolean b : visited) if(!b) return false;
        return true;
    }

    public void endPlay() {
        gameOver = true;
    }
}
