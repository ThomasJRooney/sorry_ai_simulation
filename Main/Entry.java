package Main;

import Main.AITester;
import players.*;

public class Entry {

    public static void main(String args[]) {
        Player randomPlayer = new RandomAI();
        Player MCPlayer = new MC(1.4, 25, 100);
        int[] wins = new int[4];

        int g = 1000;
        int t = 10;
        Thread[] threads = new Thread[t];
        for(int i = 0; i < t; i++) {
            threads[i] = new Thread(new Runnable(){
                @Override
                public void run() {
                    int[] wins2 = new AITester(randomPlayer, null, new MC(1.4, 25, 100), null).run(g/t);
                    arrayAdd(wins, wins2);
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            }
            catch (Exception e) {}
        }
        System.out.println(wins[0] + ", " + wins[1] + ", " + wins[2] + ", " + wins[3]);
    }

    private static void arrayAdd(int[] base, int[] addition)
    {
        for (int i = 0; i < base.length; i++)
        {
            base[i] += addition[i];
        }
    }
}
