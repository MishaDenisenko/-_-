import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MopeLab1 {
    public static void main(String[] args) {

        int[] X1 = new int[8];
        int[] X2 = new int[8];
        int[] X3 = new int[8];
        double[] X_NORM_1 = new double[8];
        double[] X_NORM_2 = new double[8];
        double[] X_NORM_3 = new double[8];
        double[] X0 = new double[3];
        double[] DX = new double[3];
        int[] X_MIN = {21, 21, 21};
        int[] X_MAX = {-1, -1, -1};
        int[] Y = new int[8];
        double Y_ETALON;
        int[] a = new int[4];

        for (int i = 1; i < 5; i++) {
            a[i-1] = createNumbersA(i);
        }

        for (int i = 0; i < 8; i++) {
            X1[i] = (int) (Math.random()*20);
            X2[i] = (int) (Math.random()*20);
            X3[i] = (int) (Math.random()*20);
        }

        for (int i = 0; i < 8; i++) {
            if (X1[i] < X_MIN[0]){
                X_MIN[0] = X1[i];
            }
            if (X2[i] < X_MIN[1]){
                X_MIN[1] = X2[i];
            }
            if (X3[i] < X_MIN[2]){
                X_MIN[2] = X3[i];
            }
            if (X1[i] > X_MAX[0]){
                X_MAX[0] = X1[i];
            }
            if (X2[i] > X_MAX[1]){
                X_MAX[1] = X2[i];
            }
            if (X3[i] > X_MAX[2]){
                X_MAX[2] = X3[i];
            }
        }

        for (int i = 0; i < 3; i++) {
            X0[i] = (double) (X_MIN[i] + X_MAX[i])/2;
            DX[i] = X0[i] - X_MIN[i];
        }

        for (int i = 0; i < 8; i++) {
            Y[i] = a[0] + X1[i]*a[1] + X2[i]*a[2] + X3[i]*a[3];
        }

        for (int i = 0; i < 8; i++) {
            X_NORM_1[i] = (X1[i]-X0[0])/DX[0];
            X_NORM_2[i] = (X2[i]-X0[1])/DX[1];
            X_NORM_3[i] = (X3[i]-X0[1])/DX[2];
        }

        Y_ETALON = a[0] + X0[0]*a[1] + X0[1]*a[2] + X0[2]*a[3];

        System.out.println("------------------------------------");
        System.out.println("№\tX1\tX2\tX3\tY\t \tXH1\t\tXH2\t\tXH3");
        for (int i = 0; i < 8; i++) {
            System.out.print((i+1) + "\t" + X1[i] + "\t" + X2[i] + "\t" + X3[i] + "\t" + Y[i] + "\t \t");
            System.out.printf("%.2f\t%.2f\t%.2f", X_NORM_1[i], X_NORM_2[i], X_NORM_3[i]);
            System.out.println();
        }
        System.out.println("------------------------------------");
        for (int i = 0; i < 3; i++) {
            System.out.printf("a%d = %d\t\tX0%d = %.2f\t\t DX%d = %.2f", i, a[i], i, X0[i], i, DX[i]);
            System.out.println();
        }
        System.out.println("a3 = "+a[3]);
        System.out.println("------------------------------------");
        System.out.println("Функція відгуку Yэт = " + Y_ETALON);

        int t;
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {
                if (Y[j] < Y[i]) {
                    t = Y[i];
                    Y[i] = Y[j];
                    Y[j] = t;
                }
            }
        }

        int index;
        for (int i = 7; i > -1; i--) {
            if (Y[i] < Y_ETALON) {
                index = i;
                System.out.println("------------------------------------");
                System.out.println("Точка плану за варіантом 107: Y("+X1[index]+"; "+X2[index]+"; "+X3[index]+") = "
                        +Y[index]);
                break;
            }
        }
    }

    public static int createNumbersA(int number) {
        int numberA;
        String message = "Хочу хорошую оценку по \"Методам оптимизации и планирования эксперементов\" :)";
        String[] words;
        List<char[]> chars = new ArrayList<>();
        List<Integer> numbersA = new ArrayList<>();
        words = message.split(" ");
        for (String word : words) {
            chars.add(word.toCharArray());
        }

        for (char[] c : chars) {
            for (char c1 : c) {
                if ((int) c1 > 1000 && number == 4) {
                    numbersA.add((int) c1 - 250 * number - (int) Math.pow(number, 3));
                } else if ((int) c1 > 1000 && number == 3) {
                    numbersA.add((int) c1 - 333 * number - (int) Math.pow(number, 4));
                } else if ((int) c1 > 1000 && number == 2) {
                    numbersA.add((int) c1 - 500 * number - (int) Math.pow(number, 5));
                } else if ((int) c1 > 1000 && number == 1) {
                    numbersA.add((int) c1 - 1150);
                }
            }
        }


        int A = new Random().nextInt(numbersA.size() - 1);
        numberA = numbersA.get(A);

        if (numberA > 20) {
            numberA /= 15;
        } else if (numberA < 0) {
            numberA = -1 * numberA;
            if (numberA > 20) {
                numberA /= 15;
            }
        }

        return numberA;
    }
}
