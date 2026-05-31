import java.util.Scanner;

public class App {
    /**
     * вариант 13
     * rho(x) = exp(-x)
     * f(x) = sin(x)
     * [a, b] = [0, 1]
     * @param args
     * @throws Exception
     */
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) { 
            //2 Запросить у пользователя концы промежутка интегрирования (a, b).
            double a = 0;
            double b = 0;
            System.out.println("Ввести [a, b]");
            System.out.print("a: ");
            a = sc.nextDouble();
            System.out.print("b: ");
            b = sc.nextDouble();

            //6 Запросить у пользователя количество узлов N и сами узлы.
            int n = 0;
            System.out.print("Количество узлов N: ");
            n = sc.nextInt();
            System.out.print("Ввести узлы: ");
            double[] points = new double[n];
            for (int i = 0; i < n; i++) {
                points[i] = sc.nextDouble();
            }

            //4 Найти "точные" значения интеграла для всех функций с использованием матпакета.
            System.out.println("\"точные\" значения интеграла для всех функций:");
            System.out.printf("0: func0(x) = 1.0\t");
            System.out.println(intg_a_b_func0(a, b));
            System.out.printf("1: func1(x) = x  \t");
            System.out.println(intg_a_b_func1(a, b));
            System.out.printf("2: func2(x) = x^2\t");
            System.out.println(intg_a_b_func2(a, b));
            System.out.printf("3: func3(x) = x^3\t");
            System.out.println(intg_a_b_func3(a, b));
            System.out.printf("4: func4(x) = x^(N - 1)\t");
            System.out.println(intg_a_b_func4(a, b, n));
            System.out.print("5: func5(x) = sin(x)\t");
            System.out.println(intg_a_b_func5(a, b));

            //5 Предложить пользователю выбрать функцию для численного интегрирования.
            System.out.println("Выбрать функцию для численного интегрирования (номер):");
            System.out.println("0: func0(x) = 1.0");
            System.out.println("1: func1(x) = x");
            System.out.println("2: func2(x) = x^2");
            System.out.println("3: func3(x) = x^3");
            System.out.println("4: func4(x) = x^(N - 1)");
            System.out.println("5: func5(x) = sin(x)");



            System.out.print("Продолжить?(y/n): ");
            if ('y' != sc.nextDouble()) break;
        }
        sc.close();
    }

    private static double rho(double x) {
        return Math.pow(Math.E, -x);
    }

    /**
     * func0 = 1.0
     * @param x - arg
     * @return 1.0
     */
    private static double func0(double x) {
        return 1.0;
    }

    /**
     * Func0 = 1.0
     * @param x - arg
     * @return -exp(-x)
     */
    private static double Func0_rho(double x) {
        return -Math.exp(-x);
    }

    /**
     * func1 = x
     * @param x - arg
     * @return x
     */
    private static double func1(double x) {
        return x;
    }

    /**
     * Func1 = x
     * @param x - arg
     * @return -(x + 1) * e^(-x)
     */
    private static double Func1_rho(double x) {
        return - (x + 1) * Math.exp(-x);
    }

    /**
     * func2 = x * x
     * @param x - arg
     * @return x ^ 2
     */
    private static double func2(double x) {
        return x * x;
    }

    /**
     * Func2 = x^2
     * @param x - arg
     * @return -(x^2 + 2x + 2) * e^(-x)
     */
    private static double Func2_rho(double x) {
        return - (x * x + 2 * x + 2) * Math.exp(-x);
    }

    /**
     * func3 = x ^ 3
     * @param x - arg
     * @return x ^ 3
     */
    private static double func3(double x) {
        return Math.pow(x, 3);
    }

    /**
     * Func3 = x^3
     * @param x - arg
     * @return -(x^3 + 3x^2 + 6x + 6) * e^(-x)
     */
    private static double Func3_rho(double x) {
        return - (x * x * x + 3 * x * x + 6 * x + 6) * Math.exp(-x);
    }

    /**
     * func4 = x ^ (n-1)
     * @param x - arg
     * @return x ^ (n-1)
     */
    private static double func4(double x, int n) {
        return Math.pow(x, n - 1);
    }

    /**
     * Func4 = x^n/n
     * @param x - arg
     * @return 
     */
    private static double Func4_rho(double x, int n){
        double ans = Math.pow(x, n - 1);
        int tmp = 1;
        for (int i = 1; i < n - 1; i++) {
            tmp = tmp * (n - i);
            ans = ans + tmp * Math.pow(x, n - 1 - i);
        }
        ans = ans + tmp;
        return - ans * Math.exp(-x);
    }

    /**
     * func5 = sin(x)
     * @param x - arg
     * @return sin(x)
     */
    private static double func5(double x) {
        return Math.sin(x);
    }

    /**
     * Func5 = sin(x)
     * @param x - arg
     * @return 
     */
    private static double Func5_rho(double x) {
        return -Math.sqrt(2) / 2 * Math.exp(-x) * Math.sin(x + Math.PI / 4);
    }

    private static double intg_a_b_func0(double a, double b) {
        return Func0_rho(b) - Func0_rho(a);
    }

    private static double intg_a_b_func1(double a, double b) {
        return Func1_rho(b) - Func1_rho(a);
    }

    private static double intg_a_b_func2(double a, double b) {
        return Func2_rho(b) - Func2_rho(a);
    }

    private static double intg_a_b_func3(double a, double b) {
        return Func3_rho(b) - Func3_rho(a);
    }

    private static double intg_a_b_func4(double a, double b, int n) {
        return Func4_rho(b, n) - Func4_rho(a, n);
    }

    private static double intg_a_b_func5(double a, double b) {
        return Func5_rho(b) - Func5_rho(a);
    }
}
