import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //(Вариант 13)
        double a = 1.0;
        double b = 10.0;
        int m = 30; // число значений в таблице m + 1 = 31
        double[][] table = new double[m + 1][2];
        double h = (b - a) / m;

        //создание таблицы
        System.out.println("\n Задача алгебраического интерполирования");
        System.out.println("Вариант 13");
        System.out.println("f(x) = ln(1 + x) - exp(x)");
        System.out.printf("Интервал: [%.1f, %.1f], число узлов m+1 = %d\n\n", a, b, m + 1);

        for (int i = 0; i <= m; i++) {
            table[i][0] = a + i * h;
            table[i][1] = function13(table[i][0]);
        }
        
        System.out.println("\nИсходная таблица значений функции:");
        printTable(table, m + 1);

        
        while (true) {
            // Запрос x и n
            double x = inputX(sc);
            int n = inputN(sc, m);

            // Сортировка по удалённости от x
            double[][] sortedTable = sortByDistanceToX(table, x);
            System.out.println("\nОтсортированная таблица:");
            System.out.printf("   k\t    zk\t\t    f(zk)\t\t|zk - x|\n");
            for (int i = 0; i <= m; i++) {
                System.out.printf("     %2d\t\t%10.6f\t%12.6f\t%12.6e\n",
                        i, sortedTable[i][0], sortedTable[i][1], Math.abs(sortedTable[i][0] - x));
            }

            // Выбор первых n+1 узлов для построения многочлена
            double[][] workTable = new double[n + 1][2];
            System.arraycopy(sortedTable, 0, workTable, 0, n + 1);
            System.out.println("\nРабочая часть таблицы:");
            for (int i = 0; i <= n; i++) {
                System.out.printf("x%d = %.6f\n", i, workTable[i][0]);
            }

            // Точное значение f(x)
            double exactF = function13(x);

            //Форма Лагранжа
            double[] l_kn = fillL_kn(x, workTable, n + 1);
            double sumL = 0.0;
            for (double v : l_kn) sumL += v;
            double P_L = countLagrange(l_kn, workTable, n + 1);
            double errL = Math.abs(exactF - P_L);

            System.out.println("\nФорма Лагранжа");
            System.out.printf("Сумма коэффициентов Лагранжа: %.10f\n", sumL);
            System.out.printf("PnL(x) = %.10f\n", P_L);
            System.out.printf("|f(x) - PnL(x)| = %.6e\n", errL);

            //Форма Ньютона
            double[] newtonCoeffs = computeDividedDifferences(workTable, n);
            System.out.println("\nТаблица разделённых разностей");
            printDividedDifferences(workTable, newtonCoeffs, n);

            double P_N = evaluateNewton(x, workTable, newtonCoeffs);
            double errN = Math.abs(exactF - P_N);

            System.out.println("\nФорма Ньютона");
            System.out.printf("PnN(x) = %.10f\n", P_N);
            System.out.printf("|f(x) - PnN(x)| = %.6e\n", errN);

            // Контроль результатов
            System.out.println("\nПроверка");
            System.out.printf("|PnL(x) - PnN(x)| = %.6e\n", Math.abs(P_L - P_N));
            if (Math.abs(errL) < 1e-12 || Math.abs(errN) < 1e-12) {
                System.out.println("Точка x совпадает с узлом интерполяции.");
            }

            System.out.print("\nПродолжить? (1 - да/0 - нет): ");
            int cont = sc.nextInt();
            if (cont == 0) {
                break;
            }
        }
        sc.close();
    }

    

    private static double inputX(Scanner sc) {
        System.out.print("Введите точку интерполирования x: ");
        return sc.nextDouble();
    }

    private static int inputN(Scanner sc, int m) {
        int n;
        while (true) {
            System.out.printf("Введите степень интерполяционного многочлена n (0 <= n <= %d): ", m);
            n = sc.nextInt();
            if (n < 0 || n > m) {
                System.out.println("Недопустимое значение n.");
            } else {
                break;
            }
        }
        return n;
    }

    private static void printTable(double[][] table, int size) {
        System.out.println("     zk\t\t    f(zk)");
        for (int i = 0; i < size; i++) {
            System.out.printf("   %2d: %10.6f\t%12.6f\n", i, table[i][0], table[i][1]);
        }
    }

    private static double function13(double z) {
        return Math.log(1 + z) - Math.exp(z);
    }

    /** Сортировка слиянием по расстоянию |zk - x| */
    public static double[][] sortByDistanceToX(double[][] table, double x) {
        double[][] sorted = new double[table.length][2];
        for(int i=0; i<table.length; i++) {
            sorted[i][0] = table[i][0];
            sorted[i][1] = table[i][1];
        }
        mergeSort(sorted, 0, sorted.length - 1, x);
        return sorted;
    }

    private static void mergeSort(double[][] arr, int left, int right, double x) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid, x);
            mergeSort(arr, mid + 1, right, x);
            merge(arr, left, mid, right, x);
        }
    }

    private static void merge(double[][] arr, int left, int mid, int right, double x) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        double[][] L = new double[n1][2];
        double[][] R = new double[n2][2];
        for (int i = 0; i < n1; i++) { 
            L[i][0] = arr[left + i][0]; 
            L[i][1] = arr[left + i][1]; 
        }
        for (int j = 0; j < n2; j++) { 
            R[j][0] = arr[mid + 1 + j][0]; 
            R[j][1] = arr[mid + 1 + j][1]; 
        }

        int i = 0;
        int j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (Math.abs(L[i][0] - x) <= Math.abs(R[j][0] - x)) {
                arr[k][0] = L[i][0]; arr[k][1] = L[i][1]; i++;
            } else {
                arr[k][0] = R[j][0]; arr[k][1] = R[j][1]; j++;
            }
            k++;
        }
        while (i < n1) { 
            arr[k][0] = L[i][0]; 
            arr[k][1] = L[i][1]; 
            i++; 
            k++; 
        }
        while (j < n2) { 
            arr[k][0] = R[j][0];
            arr[k][1] = R[j][1]; 
            j++; 
            k++; 
        }
    }

    //Вычисление коэффициентов Лагранжа L_k(x)
    private static double[] fillL_kn(double x, double[][] workTable, int size) {
        double[] l_kn = new double[size];
        double w_n1 = 1.0;
        for (int i = 0; i < size; i++) w_n1 *= (x - workTable[i][0]);

        for (int i = 0; i < size; i++) {
            double dW = 1.0;
            for (int j = 0; j < size; j++) {
                if (i != j) dW *= (workTable[i][0] - workTable[j][0]);
            }
            //если x точно совпадает с узлом
            if (Math.abs(x - workTable[i][0]) < 1e-15) {
                l_kn[i] = 1.0;
            } else {
                l_kn[i] = w_n1 / ((x - workTable[i][0]) * dW);
            }
        }
        return l_kn;
    }

    private static double countLagrange(double[] l_kn, double[][] workTable, int size) {
        double ans = 0;
        for (int i = 0; i < size; i++) ans += l_kn[i] * workTable[i][1];
        return ans;
    }

    //Построение таблицы разделённых разностей и возврат коэффициентов Ньютона
    private static double[] computeDividedDifferences(double[][] workTable, int n) {
        int size = n + 1;
        double[][] dd = new double[size][size];
        for (int i = 0; i < size; i++) dd[i][0] = workTable[i][1];

        for (int j = 1; j < size; j++) {
            for (int i = 0; i < size - j; i++) {
                dd[i][j] = (dd[i+1][j-1] - dd[i][j-1]) / (workTable[i+j][0] - workTable[i][0]);
            }
        }
        //A_k находятся в 1 строке
        double[] coeffs = new double[size];
        for (int j = 0; j < size; j++) coeffs[j] = dd[0][j];
        return coeffs;
    }

    private static void printDividedDifferences(double[][] workTable, double[] coeffs, int n) {
        int size = n + 1;
        System.out.printf("   x_i\t\tf(x_i)\t\tf[.,.]\t\tf[.,.,.]\t\t...\n");
        for (int i = 0; i < size; i++) {
            System.out.printf("%10.6f\t%10.6f", workTable[i][0], workTable[i][1]);
            for (int j = 1; j < size - i; j++) {
                System.out.printf("\t%10.6e", coeffs[j + i - 1 > i ? j + i - 1 : 0]);
            }
            System.out.println();
        }
        System.out.print("A_k: ");
        for(double c : coeffs) System.out.printf("%.6f  ", c);
        System.out.println();
    }

    //Вычисление PnN(x) по схеме Горнера
    private static double evaluateNewton(double x, double[][] workTable, double[] coeffs) {
        int n = coeffs.length - 1;
        double res = coeffs[n];
        for (int i = n - 1; i >= 0; i--) {
            res = res * (x - workTable[i][0]) + coeffs[i];
        }
        return res;
    }
}
