import java.util.ArrayList;
import java.util.Iterator;

public class Board {
    private BinarySemaphore[] locks;
    private BinarySemaphore synchronizationBarrier;
    private ArrayList<Player> players;
    private Thread terminator;

    public Board(int squares) {
        locks = new BinarySemaphore[squares];
        for(int i = 0; i < squares; i++) {
            locks[i] = new BinarySemaphore();
        }
        synchronizationBarrier = new BinarySemaphore(true);
        players = new ArrayList<Player>();
        terminator = new Thread() {
            public void run() {
                try {
                    synchronizationBarrier.lock();
                } catch(InterruptedException ie) {
                    System.err.println("terminator interrupted.");
                }
                for(Player p : players) {
                    p.endPlay();
                }
                System.out.println("Players informed of ended game...");
                for(Player p : players) {
                    try {
                        p.join();
                        //System.out.println(p.getName() + " terminated...");
                    } catch(InterruptedException ie) {
                        System.err.println("Terminator interrupted.");
                    }
                }
            }
        };
    }

    public void startGame() {
        terminator.start();
        for(Player p : players) p.start();
        try {
            terminator.join();
        } catch(InterruptedException ie) {
            //System.err.println("Game interrupted");
        }
    }

    public int squares()  {  return locks.length;  }

    public void move(int from, int to) {
        if(from > -1) locks[from].unlock();
        if(to == -1) return;
        try {
            locks[to].lock();
        } catch(InterruptedException ie) {
            System.err.println("got interrupted during move");
        }
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void endGame() {
        synchronizationBarrier.unlock();
        //System.out.println("Game end initiated...");
    }

    public String toString() {
        String ans = "";
        for(int row = 0; row < Math.sqrt(squares()); row++) {
            for(int col = 0; col < Math.sqrt(squares()); col++) {
                for(int i = 0; i < players.size(); i++) {
                    ans += players.get(i).visited(row*3+col) ? i : "";
                }
                ans += " ";
            }
            ans += "\n";
        }
        return ans;
    }
}
