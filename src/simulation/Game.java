package simulation;

import java.util.ArrayList;

public class Game {

    public static ArrayList<Move> getMoves(State state, int card) {
        ArrayList<Move> moves = new ArrayList<>();
        int[] currPTokens = state.tokens[state.currP];
        for(int i = 0; i < 4; i++) {
            if (currPTokens[i] == -6) {
                continue;
            }
            if (currPTokens[i] == 0) {
                if (card == 0) {
                    for (int j = 0; j < 4; j++) {
                        if (j == state.currP) {
                            continue;
                        }
                        for (int k = 0; k < 4; k++) {
                            if (state.tokens[j][k] > 0) {
                                moves.add(new Move(state.currP, i, -69, j, k, card));
                            }
                        }
                    }
                } else if (card == 1 || card == 2) {
                    if (openSpace(currPTokens, State.STARTS[state.currP])) {
                        moves.add(new Move(state.currP, i, state.STARTS[state.currP], -1, -1, card));
                    }
                }
                continue;
            }
            if (card == 0) {
                continue;
            }
            if (card == 4) {
                int goToSpace = currPTokens[i];
                if (goToSpace < 0) {
                    if (goToSpace == -5) {
                        goToSpace = -1;
                    } else {
                        goToSpace = State.HOMES[state.currP] - (4 + goToSpace);
                    }
                } else if (goToSpace <= 4) {
                    goToSpace = 60 + goToSpace - 4;
                } else {
                    goToSpace -= 4;
                }
                if (openSpace(currPTokens, goToSpace)) {
                    moves.add(new Move(state.currP, i, goToSpace, -1, -1, card));
                }
                continue;
            }
            int goTo = currPTokens[i];
            if (goTo <= State.HOMES[state.currP] && goTo + card > State.HOMES[state.currP]) {
                goTo = State.HOMES[state.currP] - (goTo + card);
            } else if (goTo < 0) {
                goTo -= card;
            } else {
                goTo += card;
                if (goTo > 60) {
                    goTo -= 60;
                }
            }
            if ((goTo >= -6 && openSpace(currPTokens, goTo)) || goTo == -6) {
                moves.add(new Move(state.currP, i, goTo, -1, -1, card));
            }
            if (card == 10) {
                goTo = currPTokens[i];
                if (goTo == -1) {
                    goTo = State.HOMES[state.currP];
                } else if (goTo == 1) {
                    goTo = 60;
                } else if (goTo < -1) {
                    goTo++;
                } else {
                    goTo--;
                }
                if (openSpace(currPTokens, goTo)) {
                    moves.add(new Move(state.currP, i, goTo, -1, -1, card));
                }
                continue;
            }
            if (card == 11) {
                if (currPTokens[i] < 0) {
                    continue;
                }
                for (int j = 0; j < 4; j++) {
                    if (j == state.currP) {
                        continue;
                    }
                    for (int k = 0; k < 4; k++) {
                        if (state.tokens[j][k] > 0) {
                            moves.add(new Move(state.currP, i, -69, j, k, card));
                        }
                    }
                }
                continue;
            }
            if (card == 7) {
                for (int j = 0; j < 4; j++) {
                    if (j == i || currPTokens[j] == -6) {
                        continue;
                    }
                    for (int k = 6; k > 0; k--) {
                        int goTo1 = currPTokens[i];
                        int goTo2 = currPTokens[j];
                        if (goTo2 == 0) {
                            continue;
                        }
                        if (goTo1 <= State.HOMES[state.currP] && goTo1 + k > State.HOMES[state.currP]) {
                            goTo1 = State.HOMES[state.currP] - (goTo1 + k);
                        } else if (goTo1 < 0) {
                            goTo1 -= k;
                        } else {
                            goTo1 += k;
                            if (goTo1 > 60) {
                                goTo1 -= 60;
                            }
                        }
                        if (goTo2 <= State.HOMES[state.currP] && goTo2 + 7 - k > State.HOMES[state.currP]) {
                            goTo2 = State.HOMES[state.currP] - (goTo2 + 7 - k);
                        } else if (goTo2 < 0) {
                            goTo2 -= 7 - k;
                        } else {
                            goTo2 += 7 - k;
                            if (goTo2 > 60) {
                                goTo2 -= 60;
                            }
                        }
                        if (((goTo1 >= -6 && openSpace(currPTokens, goTo1)) || goTo1 == -6) && ((goTo2 >= -6 && openSpace(currPTokens, goTo2)) || goTo2 == -6) && goTo1 != goTo2) {
                            moves.add(new Move(state.currP, i, goTo1, -1, -1, card, j, goTo2));
                        }
                    }
                }
            }
        }
        return moves;
    }

    public static boolean openSpace(int[] currPTokens, int space) {
        for (int j = 0; j < 4; j++) {
            if (currPTokens[j] == space) {
                return false;
            }
        }
        return true;
    }

    public static void applyMove(State state, Move move) {
        state.counter--;
        if (state.counter == 0) {
            state.counter = 45;
            for (int i = 0; i < state.deck.length; i++) {
                state.deck[i] = state.DECKORIGINAL[i];
            }
        }
        if (move == null) {
            passTurn(state);
            return;
        }
        if (move.swapToToken != -1) {
            int tokenLoc = state.tokens[move.player][move.token];
            state.tokens[move.player][move.token] = state.tokens[move.swapToPlayer][move.swapToToken];
            state.tokens[move.swapToPlayer][move.swapToToken] = tokenLoc;
            passTurn(state);
            return;
        }
        state.tokens[move.player][move.token] = move.whereTo;
        if (move.token2 != -1) {
            state.tokens[move.player][move.token2] = move.whereTo2;
        }
        for (int i = 0; i < 4; i++) {
            if (move.player == i) {
                continue;
            }
            for (int j = 0; j < 4; j++) {
                if (state.tokens[i][j] == move.whereTo || state.tokens[i][j] == move.whereTo2) {
                    state.tokens[i][j] = 0;
                }
            }
        }
        if (!(move.card == 2)) {
            passTurn(state);
        }
    }

    public static void passTurn(State state) {
        int player;
        for(int i = 1; i < 4; i++) {
            player = (i+state.currP)%4;
            if(state.players[player]) {
                state.currP = player;
                return;
            }
        }
    }

    public static int getWinner(State state) {
        for (int i = 0; i < 4; i++) {
            if(state.players[i]) {
                boolean won = true;
                for (int token : state.tokens[i]) {
                    if (token != -6) {
                        won = false;
                        break;
                    }
                }
                if (won) {
                    return i;
                }
            }
        }
        return -1;
    }
}