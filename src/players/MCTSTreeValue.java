package players;

import simulation.*;

import java.util.ArrayList;

public class MCTSTreeValue
{
    public State state;
    public double visits;
    public double wins;
    ArrayList<Move> moves = new ArrayList<Move>();


    public MCTSTreeValue(State state, double visits, double wins) {
        this.state = state;
        this.visits = visits;
        this.wins = wins;
    }

    public MCTSTreeValue(State state) {
        this(state, 0, 0);
    }
}
