/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int count = 0;
        String ans = "";
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            count += 1;
            double p = 1.0 / count;
            boolean choose = StdRandom.bernoulli(p);
            if (choose) {
                ans = s;
            }
        }
        System.out.println(ans);
    }
}
