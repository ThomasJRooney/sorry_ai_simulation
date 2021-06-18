package simulation;
import java.util.Arrays;

public class State {
    public static final int[] STARTS = {1, 16, 31, 46};
    public static final int[] HOMES = {59, 14, 29, 44};
    public static final int[] DECKORIGINAL = {4, 5, 4, 4, 4, 4, 0, 4, 4, 0, 4, 4, 4};
    public int[] deck = {4, 5, 4, 4, 4, 4, 0, 4, 4, 0, 4, 4, 4};
    public int counter;

    public boolean[] players;

    public int[][] tokens;
    public int currP;

    public State() {
        currP = 0;
        tokens = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
        players = new boolean[]{false, false, false, false};
        counter = 45;
    }

    public State(int numPlayers) {
        this();
        switch (numPlayers) {
            case 1: players = new boolean[]{true, false, false, false}; break;
            case 2: players = new boolean[]{true, false, true, false}; break;
            case 3: players = new boolean[]{true, true, true, false}; break;
            case 4: players = new boolean[]{true, true, true, true}; break;
        }
    }

    public State(boolean[] players) {
        this();
        this.players = players;
        for(int i = 0; i < 4; i++) {
            if(players[i] == true) {
                currP = i;
                break;
            }
        }
    }

    public State(boolean[] players, int[][] tokens, int currP) {
        this.players = players;
        this.tokens = tokens;
        this.currP = currP;
    }

    public State clone() {
        boolean[] players = this.players.clone();
        int[][] tokens = Arrays.stream(this.tokens).map(a -> Arrays.copyOf(a, a.length)).toArray(int[][]::new);
        State newState = new State(players, tokens, currP);
        return newState;
    }



}
