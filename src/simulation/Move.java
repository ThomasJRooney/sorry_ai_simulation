package simulation;

public class Move {
    public int player;
    public int token;
    public int whereTo;
    public int swapToPlayer;
    public int swapToToken;
    public int card;
    public int token2 = -1;
    public int whereTo2 = -69;

    public Move(int player, int token, int whereTo, int swapToPlayer, int swapToToken, int card) {
        this.player = player;
        this.token = token;
        this.whereTo = whereTo;
        this.swapToPlayer = swapToPlayer;
        this.swapToToken = swapToToken;
        this.card = card;
    }

    public Move(int player, int token, int whereTo, int swapToPlayer, int swapToToken, int card, int token2, int whereTo2) {
        this(player, token, whereTo, swapToPlayer, swapToToken, card);
        this.token2 = token2;
        this.whereTo2 = whereTo2;
    }
}
