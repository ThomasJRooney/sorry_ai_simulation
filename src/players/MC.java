package players;

import simulation.*;
import Tree.*;

import java.util.ArrayList;
import java.util.Random;
import Main.AITester;

public class MC extends Player{
    double c;
    int d;
    int s;
    int curCard = 0;

    public MC(double c, int d, int s) {
        this.c = c;
        this.d = d;
        this.s = s;
    }


    @Override
    public Move play(State state, int card) {
        ArrayList<Move> moves = Game.getMoves(state, card);
        if (moves.isEmpty()) {
            return null;
        }
        Random random = new Random();
        double[] moveWinRates = new double[moves.size()];
        for (int i = 0; i < d; i++) {
            arrayAdd(moveWinRates, determinizedPlay(state.clone(), card, random.nextLong()));
        }
        int maxI = 0;
        for (int i = 0; i < moveWinRates.length; i++) {
            if (moveWinRates[i] > moveWinRates[maxI]) {
                maxI = i;
            }
        }
        return moves.get(maxI);
    }

    private double[] determinizedPlay(State state, int card, long seed) {
        Random random = new Random(seed);
        MCTSTreeValue root = new MCTSTreeValue(state.clone());
        GenericTree<MCTSTreeValue> tree = new GenericTree<>(root);
        for (int i = 0; i < s; i++) {
            random.setSeed(seed);
            curCard = card;
            GenericTree<MCTSTreeValue> selected = selection(tree, random);
            int winner = Game.getWinner(selected.getValue().state);
            if (winner != -1) {
                backpropagate(selected, winner);
                continue;
            }
            GenericTree<MCTSTreeValue> expanded = expansion(selected, random);
            winner = playout(expanded, random);
            backpropagate(expanded, winner);
        }
        MCTSTreeValue[] children = tree.getChildrenVals(MCTSTreeValue.class);
        double[] winRates = new double[children.length];
        for (int i = 0; i < children.length; i++) {
            winRates[i] = children[i].wins/children[i].visits;
        }
        return winRates;
    }

    private int playout(GenericTree<MCTSTreeValue> expanded, Random random) {
        RandomAI randomAI = new RandomAI();
        State state = expanded.getValue().state.clone();
        int winner = -1;
        while (winner == -1) {
            Game.applyMove(state, randomAI.play(state, curCard));
            curCard = random.nextInt(45) + 1;
            winner = Game.getWinner(state);
        }
        return winner;
    }

    private void backpropagate(GenericTree<MCTSTreeValue> expanded, int winner) {
        GenericTree<MCTSTreeValue> node = expanded;
        while (node.getParent() != null) {
            node.getValue().visits++;
            if (node.getParent().getValue().state.currP == winner) {
                node.getValue().wins += 1;
            }
            node = node.getParent();
        }
    }

    private GenericTree<MCTSTreeValue> expansion(GenericTree<MCTSTreeValue> selected, Random random) {
        State newState = selected.getValue().state.clone();
        if (selected.getValue().moves.isEmpty()) {
            if(selected.getParent() != null) {
                curCard = AITester.drawCard(newState, curCard);
            }
            selected.getValue().moves = Game.getMoves(newState, curCard);
        }
        MCTSTreeValue[] children = selected.getChildrenVals(MCTSTreeValue.class);
        Move move;
        while (selected.getValue().moves.isEmpty()) {
            move = null;
            Game.applyMove(newState, move);
            MCTSTreeValue child = new MCTSTreeValue(newState);
            selected.addChild(child);
            selected = selected.getChildren()[0];
            curCard = random.nextInt(45) + 1;
            curCard = AITester.drawCard(newState, curCard);
            selected.getValue().moves = Game.getMoves(newState, curCard);
        }
        move = selected.getValue().moves.get(children.length);
        Game.applyMove(newState, move);
        MCTSTreeValue child = new MCTSTreeValue(newState);
        selected.addChild(child);
        curCard = random.nextInt(45) + 1;
        return selected.getChildren()[selected.getChildren().length - 1];
    }

    private GenericTree<MCTSTreeValue> selection(GenericTree<MCTSTreeValue> tree, Random random) {
        MCTSTreeValue[] children = tree.getChildrenVals(MCTSTreeValue.class);
        //State state = tree.getValue().state.clone();
        while (children.length != 0 && children.length >= tree.getValue().moves.size()) {
            curCard = random.nextInt(45) + 1;
            double maxUCB = 0;
            int maxUCBI = 0;
            double UCB;
            for (int i = 0; i < children.length; i++) {
                UCB = children[i].wins/children[i].visits + c*Math.sqrt((Math.log(tree.getValue().visits)/children[i].visits));
                if (UCB > maxUCB) {
                    maxUCB = UCB;
                    maxUCBI = i;
                }
            }
            tree = tree.getChildren()[maxUCBI];
            children = tree.getChildrenVals(MCTSTreeValue.class);
        }
        return tree;
    }


    private void arrayAdd(double[] base, double[] addition)
    {
        for (int i = 0; i < base.length; i++)
        {
            base[i] += addition[i];
        }
    }
}
