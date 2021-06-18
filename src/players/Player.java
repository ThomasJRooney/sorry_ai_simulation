package players;

import simulation.*;

public abstract class Player {
    public abstract Move play(State state, int card);
}
