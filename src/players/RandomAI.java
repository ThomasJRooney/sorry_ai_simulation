package players;

import simulation.*;
import java.util.Random;
import java.util.ArrayList;

public class RandomAI extends Player{


    @Override
    public Move play(State state, int card) {
        ArrayList<Move> moves = Game.getMoves(state, card);
        if (moves.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return moves.get(random.nextInt(moves.size()));
    }
}
