import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100, true);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100, true);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100, true);
    public static int countMaxA = 0;
    public static int countMaxB = 0;
    public static int countMaxC = 0;
    public static String stringMaxA = null;
    public static String stringMaxB = null;
    public static String stringMaxC = null;

    public static void main(String[] args) {

        new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String string = generateText("abc", 100000);
                try {
                    queueA.put(string);
                } catch (InterruptedException e) {
                    return;
                }
                try {
                    queueB.put(string);
                } catch (InterruptedException e) {
                    return;
                }
                try {
                    queueC.put(string);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            String[] w1 = checkABC(queueA, countMaxA, stringMaxA, 'a');
            System.out.println(w1[0]);
        }).start();

        new Thread(() -> {
            String[] w2 = checkABC(queueB, countMaxB, stringMaxB, 'b');
            System.out.println(w2[0]);
        }).start();

        new Thread(() -> {
            String[] w3 = checkABC(queueC, countMaxC, stringMaxC, 'c');
            System.out.println(w3[0]);
        }).start();
    }

    public static String[] checkABC(BlockingQueue<String> queueABC, int countMaxABC, String stringMaxABC, char abc) {
        for (int i = 0; i < 10000; i++) {
            int countABC = 0;
            String string;
            try {
                string = queueABC.take();
            } catch (InterruptedException e) {
                break;
            }
            char[] charsABC = string.toCharArray();
            for (char c : charsABC) {
                if (c == abc) {
                    countABC++;
                }
            }
            if (countABC > countMaxABC) {
                countMaxABC = countABC;
                stringMaxABC = string;
            }
        }
        return new String[]{String.valueOf(countMaxABC), stringMaxABC};
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}