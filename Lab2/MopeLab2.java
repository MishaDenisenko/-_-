import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MopeLab2 {
    public static void main(String[] args) {

        final int MinX1 = -5;
        final int MaxX1 = 15;
        final int MinX2 = -15;
        final int MaxX2 = 35;
        final int MinY = 110;
        final int MaxY = 210;
        int m;
        int[][] X = new int[][]{
                {-1, -1},
                {1, -1},
                {-1, 1}};
        List<String> symbols = new ArrayList<>();
        double[] mx = new double[2];
        double my = 0;
        double[] a = new double[3];
        double a11;
        double a22;
        double[] b = new double[3];
        double[] y_average = new double[3];
        final double[] RkrValue = {1.73, 2.16, 2.43, 2.62, 2.75, 2.9, 3.08};
        double[] dispersion = new double[3];
        double deviation;
        double[] Fuv = new double[3];
        double[] Theta_uv = new double[3];
        double[] Ruv = new double[3];
        double Rkr;
        boolean work = false; // поставил изначчально отрицательное значение
        boolean ok = false; // добавил еще один критерий, чтоб избежать ошибку при работе

        // определение кол-ва экспериментов

        Scanner st = new Scanner(System.in);
        System.out.println("Задайте значення m: ");
        try {
            m = st.nextInt();
            if (m > 0) {
                if (m > 20) { // добавил проверку m для чисел >20
                    System.out.println("m має бути меньше 20 ...");
                } else {
                    symbols.add("\u2219");
                    for (int i = 0; i <= m; i++) {
                        symbols.add(""+i);
                    }
                    work = true;
                    ok = true;
                }
            }
            else{
                System.out.println("Потрибно задати додатнє значення...");
            }
        }
        catch (Exception e){
            System.out.println("Потрибно задати ціле значення...");
            m = 0;
        }

        //проверка по критерию Романовского

        while (work) {
            System.out.printf("Лінійне рівняння регресії має вигляд: y = b%s + b%s%sx%s + b%s%sx%s",
                    symbols.get(1), symbols.get(2), symbols.get(0), symbols.get(2), symbols.get(3), symbols.get(0),
                                                                                                    symbols.get(3));
            System.out.println();

            System.out.println("Нормована матриця планування експерименту : ");
            System.out.printf("X%s\tX%s\t", symbols.get(2), symbols.get(3));
            for (int i = 0; i < m; i++) {
                System.out.print("Y" + symbols.get(i + 2) + "\t");
            }
            System.out.println();

            List<int[]> Y = new ArrayList<>();

            for (int[] x : X) {
                int[] y = new int[m];
                for (int j = 0; j < 2; j++) {
                    if (x[j] > 0){
                        System.out.print(" ");
                    }
                    System.out.print(x[j] + "\t");
                }
                for (int j = 0; j < m; j++) {
                    y[j] = (int) (Math.random() * (MaxY - MinY)) + MinY;
                    System.out.print(y[j] + "\t");
                }
                Y.add(y);
                System.out.println();
            }

            for (int i = 0; i < 3; i++) {
                int[] y = Y.get(i);
                for (int j = 0; j < m; j++) {
                    y_average[i] += (double) y[j] / m;
                }
            }

            for (int i = 0; i < 3; i++) {
                int[] y = Y.get(i);
                for (int j = 0; j < m; j++) {
                    dispersion[i] += Math.pow((y[j] - y_average[i]), 2);
                }
            }

            deviation = Math.sqrt((2 * (2 * m - 2)) / (double) (m * (m - 4)));

            Fuv[0] = dispersion[0] / dispersion[1];
            Fuv[1] = dispersion[2] / dispersion[0];
            Fuv[2] = dispersion[2] / dispersion[1];

            if (m <= 4) {
                Rkr = RkrValue[0];
            }
            else if (m <= 6) Rkr = RkrValue[1];
            else if (m <= 8) Rkr = RkrValue[2];
            else if (m <= 10) Rkr = RkrValue[3];
            else if (m <= 13) Rkr = RkrValue[4];
            else if (m <= 17) Rkr = RkrValue[5];
            else if (m <= 20) Rkr = RkrValue[6];
            else Rkr = 3.1;

            System.out.println("\nRkr = " + Rkr);
            System.out.println("\nRuv:");
            boolean[] comparison = new boolean[3];
            for (int i = 0; i < 3; i++) {
                Theta_uv[i] = ((m - 2) / (double) m) * Fuv[i];
                Ruv[i] = Math.abs((Theta_uv[i] - 1) / deviation);
                if (Ruv[i] < Rkr){
                    comparison[i] = true;
                    System.out.println(Ruv[i] + " < " + Rkr);
                }
                else {
                    comparison[i] = false;
                    System.out.println(Ruv[i] + " > " + Rkr);
                }
            }
            if (Ruv[0] < Rkr && Ruv[1] < Rkr && Ruv[2] < Rkr ) System.out.println("Дисперсії однорідні\n");
            work = !comparison[0] || !comparison[1] || !comparison[2];
            m++;
            symbols.add(""+m);
            if (work){
                System.out.println("Ruv > Rкр\nЗБІЛЬШУЄМО КІЛЬКІСТЬ ДОСЛІДІВ: m + 1 ...\n");
            }
        }

        // рассчет нормированных коэффициентов уравнения
        if (ok){ // при m > 21 || m < 0 || m != int дальнейшая работа выполняться не будет
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    mx[i] += (double) X[j][i]/3;
                }
            }

            for (double y : y_average) {
                my += y/3;
            }

            a[0] = (Math.pow(X[0][0],2) + Math.pow(X[1][0],2) + Math.pow(X[2][0],2))/3;
            a[1] = (double) (X[0][0]*X[0][1] + X[1][0]*X[1][1] + X[2][0]*X[2][1])/3;
            a[2] = (Math.pow(X[0][1],2) + Math.pow(X[1][1],2) + Math.pow(X[2][1],2))/3;

            a11 = (X[0][0]*y_average[0] + X[1][0]*y_average[1] + X[2][0]*y_average[2])/3;
            a22 = (X[0][1]*y_average[0] + X[1][1]*y_average[1] + X[2][1]*y_average[2])/3;

            double det11,det12,det21,det22,det31,det32;

            det11 = (my*a[0]*a[2]) + (mx[0]*a[1]*a22) + (mx[1]*a11*a[1]) -
                    (a22*a[0]*mx[1]) - (my*a[1]*a[1]) - (mx[0]*a11*a[2]);

            det12 = (1*a[0]*a[2]) + (mx[0]*a[1]*mx[1]) + (mx[1]*mx[0]*a[1]) -
                    (mx[1]*mx[1]*a[0]) - (mx[0]*mx[0]*a[2]) - (1*a[1]*a[1]);

            det21 = (1*a11*a[2]) + (my*a[1]*mx[1]) + (mx[0]*a22*mx[1]) -
                    (mx[1]*a11*mx[1]) - (mx[0]*my*a[2]) - (1*a22*a[1]);

            det22 = (1*a[0]*a[2]) + (mx[0]*a[1]*mx[1]) + (mx[1]*mx[0]*a[1]) -
                    (mx[1]*mx[1]*a[0]) - (mx[0]*mx[0]*a[2]) - (a[1]*a[1]*1);

            det31 = (1*a[0]*a22) + (mx[0]*a11*mx[1]) + (mx[0]*a[1]*my) -
                    (mx[1]*a[0]*my) - (mx[0]*mx[0]*a22) - (1*a[1]*a11);

            det32 = (1*a[0]*a[2]) + (mx[0]*a[1]*mx[1]) + (mx[0]*a[1]*mx[1]) -
                    (mx[1]*a[0]*mx[1]) - (mx[0]*mx[0]*a[2]) - (a[1]*a[1]*1);

            b[0] = det11/det12;
            b[1] = det21/det22;
            b[2] = det31/det32;

            System.out.println("Нормоване рівняння регресії:");
            System.out.printf("y = %.2f",b[0]);
            if (b[1] < 0 ) System.out.print(" - "); else System.out.print(" + ");
            System.out.printf("%.2f%sx%s", Math.abs(b[1]), symbols.get(0), symbols.get(2));
            if (b[2] < 0 ) System.out.print(" - "); else System.out.print(" + ");
            System.out.printf("%.2f%sx%s\n", Math.abs(b[2]), symbols.get(0), symbols.get(3));

            System.out.println("\nПеревірка:");
            boolean[] correctly = new boolean[3];
            for (int i = 0; i < 3; i++) {
                correctly[i] = (float) (b[0] + b[1] * X[i][0] + b[2] * X[i][1]) == (float) y_average[i];
                System.out.printf("%.2f = %.2f\n", (b[0] + b[1]*X[i][0] + b[2]*X[i][1]), y_average[i]);
            }
            if (correctly[0] && correctly[1] && correctly[2]){
                System.out.printf("\nНормовані коефіцієнти рівняння регресії b%s, b%s, b%s визначено правильно",
                                                                        symbols.get(1), symbols.get(2), symbols.get(3));
                System.out.println();
            }
            else{
                System.out.printf("Нормовані коефіцієнти рівняння регресії b%s, b%s, b%s визначено неправильно",
                                                                        symbols.get(1), symbols.get(2), symbols.get(3));
                System.out.println();
            }

            // натурализация коэффициентов

            double deltaX1, deltaX2, x10, x20, a0, a1, a2;

            deltaX1 = (double) Math.abs(MaxX1 - MinX1)/2;
            deltaX2 = (double) Math.abs(MaxX2 - MinX2)/2;
            x10 = (double) (MaxX1 + MinX1)/2;
            x20 = (double) (MaxX2 + MinX2)/2;

            a0 = b[0] - b[1]*x10/deltaX1 - b[2]*x20/deltaX2;
            a1 = b[1]/deltaX1;
            a2 = b[2]/deltaX2;

            System.out.println();
            System.out.println("Натуралізоване рівнання регресії:");

            System.out.printf("y = %.2f",a0);
            if (a1 < 0 ){
                System.out.print(" - ");
            } else System.out.print(" + ");
            System.out.printf("%.2f%sx%s", Math.abs(a1), symbols.get(0), symbols.get(2));
            if (a2 < 0 ){
                System.out.print(" - ");
            } else System.out.print(" + ");
            System.out.printf("%.2f%sx%s\n", Math.abs(a2), symbols.get(0), symbols.get(3));

            System.out.println();
            System.out.println("Перевірка:");

            System.out.printf("%.2f = %.2f\n", (a0 + a1*MinX1 + a2*MinX2),y_average[0]);
            System.out.printf("%.2f = %.2f\n", (a0 + a1*MaxX1 + a2*MinX2),y_average[1]);
            System.out.printf("%.2f = %.2f\n", (a0 + a1*MinX1 + a2*MaxX2),y_average[2]);

            if ((float)(a0 + a1*MinX1 + a2*MinX2) == (float)y_average[0] &&
                    (float)(a0 + a1*MaxX1 + a2*MinX2) == (float)y_average[1] &&
                    (float)(a0 + a1* MinX1 + a2*MaxX2) == (float)y_average[2]){
                System.out.println();
                System.out.printf("Коефіцієнти натуралізованого рівняння регресії a%s, a%s, a%s визначено правильно",
                                                                        symbols.get(1), symbols.get(2), symbols.get(3));
            }
            else{
                System.out.printf("Коефіцієнти натуралізованого рівняння регресії a%s, a%s, a%s визначено неправильно",
                                                                        symbols.get(1), symbols.get(2), symbols.get(3));
            }
        }
    }
}


