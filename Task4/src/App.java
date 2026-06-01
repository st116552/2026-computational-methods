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
            if (a >= b) {
                System.out.println("a < b\n");
                continue;
            }

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
            System.out.println(integral(a, b, 0, n));
            System.out.printf("1: func1(x) = x  \t");
            System.out.println(integral(a, b, 1, n));
            System.out.printf("2: func2(x) = x^2\t");
            System.out.println(integral(a, b, 2, n));
            System.out.printf("3: func3(x) = x^3\t");
            System.out.println(integral(a, b, 3, n));
            System.out.printf("4: func4(x) = x^(N - 1)\t");
            System.out.println(integral(a, b, 4, n));
            System.out.print("5: func5(x) = sin(x)\t");
            System.out.println(integral(a, b, 5, n));

            //5 Предложить пользователю выбрать функцию для численного интегрирования.
            System.out.println("Выбрать функцию для численного интегрирования (номер):");
            System.out.println("0: func0(x) = 1.0");
            System.out.println("1: func1(x) = x");
            System.out.println("2: func2(x) = x^2");
            System.out.println("3: func3(x) = x^3");
            System.out.println("4: func4(x) = x^(N - 1)");
            System.out.println("5: func5(x) = sin(x)");
            int func_number = sc.nextInt();
            while (func_number < 0 || func_number > 5) {
                System.out.print("от 0 до 5: ");
                func_number = sc.nextInt();
            }
            
            //6  Вычисление моментов mu_k = int_a^b x^k * rho(x) dx            
            System.out.println("\nВычисление моментов веса rho(x)");
            double[] mu = new double[n];
            for (int k = 0; k < n; k++) {
                mu[k] = Func_Xk_rho(b, k) - Func_Xk_rho(a, k);
                System.out.printf("mu[%d] = %.16f\n", k, mu[k]);
            }

            //7 Построение матрицы Вандермонда и решение СЛАУ для коэффициентов A_k
            System.out.println("\nРешение СЛАУ для коэффициентов ИКФ");
            double[][] V = new double[n][n];
            for (int i = 0; i < n; i++) {       // степень одночлена k
                for (int j = 0; j < n; j++) {   // узел x_j
                    V[i][j] = Math.pow(points[j], i);
                }
            }
            double[] A = solveSystem(V, mu);
            for (int i = 0; i < n; i++) {
                System.out.printf("A[%d] = %.16f\n", i + 1, A[i]);
            }

            //7 Проверка точности ИКФ на многочлене степени N-1 (func4)
            System.out.println("\nПроверка на многочлене степени N-1");
            double exactPoly = integral(a, b, 4, n);
            double approxPoly = 0;
            for (int i = 0; i < n; i++) approxPoly = approxPoly + A[i] * getFunc(points[i], 4, n);
            System.out.printf("Точное значение:      %.16f\n", exactPoly);
            System.out.printf("Приближенное:         %.16f\n", approxPoly);
            System.out.printf("Погрешность:          %.2e\n", Math.abs(exactPoly - approxPoly));

            //8 Вычисление интеграла и погрешностей
            double exactVal = integral(a, b, func_number, n);
            double approxVal = 0;
            for (int i = 0; i < n; i++) approxVal = approxVal + A[i] * getFunc(points[i], func_number, n);

            double absErr = Math.abs(exactVal - approxVal);
            double relErr = Math.abs(exactVal) > 1e-15 ? (absErr / Math.abs(exactVal)) * 100 : 0.0;

            System.out.println("\nРезультаты интегрирования");
            System.out.printf("Точное значение:           %.16f\n", exactVal);
            System.out.printf("Приближенное значение:     %.16f\n", approxVal);
            System.out.printf("Абсолютная погрешность:    %.2e\n", absErr);
            System.out.printf("Относительная погрешность: %.2e%%\n", relErr);





            System.out.print("Продолжить?(1/0): ");
            if (1 != sc.nextInt()) break;
        }
        sc.close();
    }

    
   /**
    * @param x
    * @param idx f number
    * @param n
    * @return f(x)
    */
    private static double getFunc(double x, int idx, int n) {
        switch (idx) {
            case 0: return 1.0;
            case 1: return x;
            case 2: return x * x;
            case 3: return x * x * x;
            case 4: return Math.pow(x, n - 1);
            case 5: return Math.sin(x);
            default: return 0.0;
        }
    }

    
   /**
    * 
    * @param a
    * @param b
    * @param idx f number
    * @param n
    * @return
    */
    private static double integral(double a, double b, int idx, int n) {
        double Fa = 0;
        double Fb = 0;
        switch (idx) {
            case 0: Fa = -Math.exp(-a); 
                    Fb = -Math.exp(-b); break;
            case 1: Fa = -(a + 1) * Math.exp(-a); 
                    Fb = -(b + 1) * Math.exp(-b); break;
            case 2: Fa = -(a*a + 2*a + 2) * Math.exp(-a); 
                    Fb = -(b*b + 2*b + 2) * Math.exp(-b); break;
            case 3: Fa = -(a*a*a + 3*a*a + 6*a + 6) * Math.exp(-a); 
                    Fb = -(b*b*b + 3*b*b + 6*b + 6) * Math.exp(-b); break;
            case 4: Fa = Func_Xk_rho(a, n - 1); 
                    Fb = Func_Xk_rho(b, n - 1); break;
            case 5: Fa = -Math.sqrt(2)/2 * Math.exp(-a) * Math.sin(a + Math.PI/4); 
                    Fb = -Math.sqrt(2)/2 * Math.exp(-b) * Math.sin(b + Math.PI/4); 
                    break;
            default: return 0.0;
        }
        return Fb - Fa;
    }

    /**
     * Gause
     * @param A
     * @param b
     * @return
     */
    private static double[] solveSystem(double[][] A, double[] b) {
        int n = A.length;
        double[][] mat = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, mat[i], 0, n);
            mat[i][n] = b[i];
        }

        for (int i = 0; i < n; i++) {
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(mat[j][i]) > Math.abs(mat[pivot][i])) pivot = j;
            }
            double[] temp = mat[i]; mat[i] = mat[pivot]; mat[pivot] = temp;

            double div = mat[i][i];
            if (Math.abs(div) < 1e-12) {
                System.out.println("Матрица вырождена.");
                return null;
            }
            
            for (int j = i; j <= n; j++) mat[i][j] = mat[i][j] / div;

            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = mat[k][i];
                    for (int j = i; j <= n; j++) mat[k][j] = mat[k][j] - factor * mat[i][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = 0; i < n; i++) x[i] = mat[i][n];
        return x;
    }

    /**
     * func_xk = x^k
     * @param x - arg
     * @return 
     */
    private static double Func_Xk_rho(double x, int k) {
        double res = -Math.exp(-x);
        double xPow = 1.0;
        for (int i = 1; i <= k; i++) {
            xPow = xPow * x;
            res = -xPow * Math.exp(-x) + i * res;
        }
        return res;
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
