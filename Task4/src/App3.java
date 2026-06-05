import java.util.Scanner;

public class App3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("4.3: Составные квадратурные формулы и уточнение по Рунге-Ромбергу");

        while (true) {
            // 1. Запрос пределов интегрирования [a, b]
            double a = 0;
            double b = 0;
            System.out.println("\nВведите пределы интегрирования [a, b]:");
            System.out.print("a: ");
            a = sc.nextDouble();
            System.out.print("b: ");
            b = sc.nextDouble();
            if (a >= b) {
                System.out.println("a < b");
                continue;
            }

            // 2. Запрос числа промежутков деления m
            int m = 0;
            System.out.print("Введите число промежутков деления m: ");
            m = sc.nextInt();

            // 3. Выбор функции
            System.out.println("\nВыберите функцию f(x) для интегрирования:");
            System.out.println("0: f0(x) = 1.0");
            System.out.println("1: f1(x) = x");
            System.out.println("2: f2(x) = x^2");
            System.out.println("3: f3(x) = x^3");
            System.out.println("4: f4(x) = sin(x)");
            System.out.print("Введите номер функции (0-4): ");
            int funcNumber = sc.nextInt();
            while (funcNumber < 0 || funcNumber > 4) {
                System.out.print("от 0 до 4: ");
                funcNumber = sc.nextInt();
            }

            double h = (b - a) / m;
            double J = integral(funcNumber, a, b);

            System.out.println("\nПараметры задачи");
            System.out.printf("A = %.4f, B = %.4f, m = %d, h = %.6e\n", a, b, m, h);
            System.out.printf("Точное значение интеграла J = %.10f\n", J);

            // Вычисление J(h) для всех 5 методов
            double[] jh = new double[5];
            jh[0] = quadL(funcNumber, a, b, m);
            jh[1] = quadR(funcNumber, a, b, m);
            jh[2] = quadM(funcNumber, a, b, m);
            jh[3] = quadTrap(funcNumber, a, b, m);
            jh[4] = quadSimp(funcNumber, a, b, m);

            System.out.println("\nРезультаты приближенного вычисления");
            String[] names = {"Левые прямоуг", "Правые прямоуг", "Средние прямоуг", "Трапеции", "Симпсон"};
            for (int i = 0; i < 5; i++) {
                double absErr = Math.abs(J - jh[i]);
                double relErr = (Math.abs(J) > 1e-15) ? (absErr / Math.abs(J)) * 100 : 0.0;
                System.out.printf("%-18s: J(h) = %-15.10f  Абс. погр. = %-10.2e  Отн. погр. = %.2e%%\n", 
                                            names[i], jh[i], absErr, relErr);
            }

            // 4. Этап уточнения по Рунге-Ромбергу
            System.out.print("\nУточнению по Рунге-Ромбергу? (1/0): ");
            int rungeChoice = sc.nextInt();
            
            if (rungeChoice == 1) {
                System.out.print("Введите коэффициент измельчения сетки l: ");
                int l = sc.nextInt();
                while (l <= 0) {
                    System.out.print("l натуральнре: ");
                    l = sc.nextInt();
                }

                int mNew = m * l;
                double hNew = (b - a) / mNew;
                
                // Вычисление J(h/l)
                double[] jhl = new double[5];
                jhl[0] = quadL(funcNumber, a, b, mNew);
                jhl[1] = quadR(funcNumber, a, b, mNew);
                jhl[2] = quadM(funcNumber, a, b, mNew);
                jhl[3] = quadTrap(funcNumber, a, b, mNew);
                jhl[4] = quadSimp(funcNumber, a, b, mNew);

                // Порядок точности методов (p): Лев/Прав=1, Сред/Трап=2, Симпсон=4
                int[] p = {1, 1, 2, 2, 4};
                double[] jp = new double[5]; // Уточненные значения

                for (int i = 0; i < 5; i++) {
                    // Формула Рунге: Jp = J(h/l) + (J(h/l) - J(h)) / (l^p - 1)
                    jp[i] = jhl[i] + (jhl[i] - jh[i]) / (Math.pow(l, p[i]) - 1);
                }

                // Вывод таблицы результатов уточнения
                System.out.printf("  %-18s   %-12s   %-12s   %-12s   %-12s   %-12s   %-12s  \n", 
                        "Метод (СКФ)", "J(h)", "|J - J(h)|", "J(h/l)", "|J - J(h/l)|", "Jp", "|J - Jp|");
                
                for (int i = 0; i < 5; i++) {
                    double errH = Math.abs(J - jh[i]);
                    double errHl = Math.abs(J - jhl[i]);
                    double errP = Math.abs(J - jp[i]);
                    
                    System.out.printf("  %-18s   %-12.6e   %-12.2e   %-12.6e   %-12.2e   %-12.6e   %-12.2e  \n",
                            names[i], jh[i], errH, jhl[i], errHl, jp[i], errP);
                }
                
                
            }

            System.out.print("\nПродолжить? (1/0): ");
            if (sc.nextInt() != 1) {
                break;
            }
        }
        sc.close();
    }

    /**
     * Вычисление значения функции f(x) по ее номеру
     */
    private static double f(int idx, double x) {
        switch (idx) {
            case 0: return 1.0;
            case 1: return x;
            case 2: return x * x;
            case 3: return x * x * x;
            case 4: return Math.sin(x);
            default: return 0.0;
        }
    }

    /**
     * Точное вычисление интеграла через первообразную
     */
    private static double integral(int idx, double a, double b) {
        switch (idx) {
            case 0: return (b - a);
            case 1: return (b * b - a * a) / 2.0;
            case 2: return (b * b * b - a * a * a) / 3.0;
            case 3: return (b * b * b * b - a * a * a * a) / 4.0;
            case 4: return -Math.cos(b) + Math.cos(a);
            default: return 0.0;
        }
    }

    /**
     * Составная формула левых прямоугольников
     */
    private static double quadL(int idx, double a, double b, int m) {
        double h = (b - a) / m;
        double sum = 0.0;
        for (int i = 0; i < m; i++) {
            sum += f(idx, a + i * h);
        }
        return h * sum;
    }

    /**
     * Составная формула правых прямоугольников
     */
    private static double quadR(int idx, double a, double b, int m) {
        double h = (b - a) / m;
        double sum = 0.0;
        for (int i = 1; i <= m; i++) {
            sum += f(idx, a + i * h);
        }
        return h * sum;
    }

    /**
     * Составная формула средних прямоугольников
     */
    private static double quadM(int idx, double a, double b, int m) {
        double h = (b - a) / m;
        double sum = 0.0;
        for (int i = 0; i < m; i++) {
            sum += f(idx, a + (i + 0.5) * h);
        }
        return h * sum;
    }

    /**
     * Составная формула трапеций
     */
    private static double quadTrap(int idx, double a, double b, int m) {
        double h = (b - a) / m;
        double sum = 0.5 * (f(idx, a) + f(idx, b));
        for (int i = 1; i < m; i++) {
            sum += f(idx, a + i * h);
        }
        return h * sum;
    }

    /**
     * Составная формула Симпсона
     */
    private static double quadSimp(int idx, double a, double b, int m) {
        double h = (b - a) / m;
        double sum = f(idx, a) + f(idx, b);
        for (int i = 1; i < m; i++) {
            if (i % 2 == 1) {
                sum += 4.0 * f(idx, a + i * h);
            } else {
                sum += 2.0 * f(idx, a + i * h);
            }
        }
        return (h / 3.0) * sum;
    }
}
