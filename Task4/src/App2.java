import java.util.Scanner;

public class App2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);        
        while (true) {
            // 1. Запрос пределов интегрирования [a, b]
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

            // 2. Выбор функции
            System.out.println("\nВыберите функцию для интегрирования(номер):");
            System.out.println("0: f0(x) = 1");
            System.out.println("1: f1(x) = x");
            System.out.println("2: f2(x) = x^2");
            System.out.println("3: f3(x) = x^3");
            System.out.println("4: f4(x) = sin(x)");
            int func_number = sc.nextInt();
            while (func_number < 0 || func_number > 4) {
                System.out.print("Введите число от 0 до 4: ");
                func_number = sc.nextInt();
            }

            // 3. Точное значение через первообразные
            double exact = integral(func_number, a, b);
            System.out.printf("\n Точное значение интеграла: %.10f\n", exact);

            // 4. Приближенные значения по 5 КФ
            System.out.println("\n Результаты приближенного вычисления:");
            String[] names = {
                "КФ левого прямоугольника",
                "КФ правого прямоугольника",
                "КФ среднего прямоугольника",
                "КФ трапеции",
                "КФ Симпсона"
            };
            double[] approx = new double[5];
            double[] absErr = new double[5];

            approx[0] = quadL(func_number, a, b);
            approx[1] = quadR(func_number, a, b);
            approx[2] = quadM(func_number, a, b);
            approx[3] = quadTrap(func_number, a, b);
            approx[4] = quadSimp(func_number, a, b);

            for (int i = 0; i < 5; i++) {
                absErr[i] = Math.abs(exact - approx[i]);
                System.out.printf("%-28s: %.10f  Абс. погрешность: %.2e\n", names[i], approx[i], absErr[i]);
            }

            // 5. Меню продолжения
            System.out.print("\nПродолжить? (1/0): ");
            if (sc.nextInt() != 1) break;
        }
        sc.close();
    }

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

    private static double integral(int idx, double a, double b) {
        switch (idx) {
            case 0: return b - a;
            case 1: return (b*b - a*a) / 2.0;
            case 2: return (b*b*b - a*a*a) / 3.0;
            case 3: return (b*b*b*b - a*a*a*a) / 4.0;
            case 4: return -Math.cos(b) + Math.cos(a);
            default: return 0.0;
        }
    }

    
    private static double quadL(int idx, double a, double b) {
        return (b - a) * f(idx, a);
    }
    private static double quadR(int idx, double a, double b) {
        return (b - a) * f(idx, b);
    }
    private static double quadM(int idx, double a, double b) {
        return (b - a) * f(idx, (a + b) / 2.0);
    }
    private static double quadTrap(int idx, double a, double b) {
        return (b - a) / 2.0 * (f(idx, a) + f(idx, b));
    }
    private static double quadSimp(int idx, double a, double b) {
        double m = (a + b) / 2.0;
        return (b - a) / 6.0 * (f(idx, a) + 4.0 * f(idx, m) + f(idx, b));
    }
}