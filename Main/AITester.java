package Main;

import simulation.*;
import players.*;
import java.util.Random;


public class AITester {
    Player[] players;
    boolean[] playersBool;
    int numPlayers;


    public AITester(Player p0, Player p1) {
        this(p0, null, p1, null);
        numPlayers = 2;
    }

    public AITester(Player p0, Player p1, Player p2) {
        this(p0, p1, p2, null);
        numPlayers = 3;
    }

    public AITester(Player p0, Player p1, Player p2, Player p3) {
        players = new Player[4];
        players[0] = p0;
        players[1] = p1;
        players[2] = p2;
        players[3] = p3;
        numPlayers = 4;
        playersBool = new boolean[4];
        for(int i = 0; i < 4; i++) {
            playersBool[i] = players[i] == null ? false : true;
        }
    }

    public int[] run(int games) {
        State state;
        int winner;
        Random random = new Random();
        int[] wins = new int[]{0, 0, 0, 0};
        for (int i = 0; i < games; i++) {
            state = new State(playersBool);
            winner = -1;
            while (winner == -1) {
                Player player = players[state.currP];
                int card = random.nextInt(state.counter) + 1;
                card = drawCard(state, card);
                Move move = player.play(state.clone(), card);
                Game.applyMove(state, move);
                winner = Game.getWinner(state);
            }
            wins[winner]++;
        }
        return wins;
    }

    public static int drawCard(State state, int card) {
        for (int j = 0; j < 12; j++) {
            if (card - state.deck[j] <= 0) {
                card = j;
                state.deck[j]--;
                break;
            } else {
                card -= state.deck[j];
            }
        }
        return card;
    }
}
