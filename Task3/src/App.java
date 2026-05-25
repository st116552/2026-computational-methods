import java.util.Scanner;



public class App {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {   
            /*
            выбор функции    
            */     
            printFunctonOptions();
            System.out.print("Выбор: ");
            int choice = sc.nextInt();
            if (choice == 0) break;
            if (choice < 1 || choice > 4) {
                System.out.println("Неверный номер функции.");
                continue;
            }
            Functions taskFunction;
            if (choice == 1) {
                taskFunction = Functions.QUADRATIC;
            } else if (choice == 2) {
                taskFunction = Functions.CUBIC;
            } else if (choice == 3) {
                taskFunction = Functions.EXPONENTIAL;
            } else {
                taskFunction = Functions.SMOOTH;
            }
            /*
            ввод параметров
            */
            int m = 0;
            double x0 = 0.0;
            double h = 0.0;
            System.out.println("Введите количество точек (m + 1), введите начальную точку x0, введите шаг h");
            System.out.print("m + 1: ");
            m = sc.nextInt() - 1;
            if (m < 4) {
                System.out.println("m + 1 должно быть > 5");
                continue;
            }
            System.out.print("x0: ");
            x0 = sc.nextDouble();
            System.out.print("h: ");
            h = sc.nextDouble();
            if (h <= 0) {
               System.out.println("h должно быть > o");
                continue; 
            }
            /*
            построить таблицу xy
            */
            InPoint[] table = new InPoint[m + 1];
            for (int i = 0; i < m + 1; i++) {
               table[i] = new InPoint(x0, h, i, taskFunction);
            }
            printXYtable(table);
            /*
            построить таблицу полностью
            */
            table[0].fillAllFilds(form4(table[0].y, table[1].y, table[2].y, h), 
                                  form7(table[0].y, table[1].y, table[2].y, table[3].y, table[4].y, h), 
                                  form12(table[0].y, table[1].y, table[2].y, table[3].y, h), 
                                  taskFunction);
            table[1].fillAllFilds(form3(table[0].y, table[2].y, h),
                                  form8(table[0].y, table[1].y, table[2].y, table[3].y, table[4].y, h), 
                                  form6(table[0].y, table[1].y, table[2].y, h), 
                                  taskFunction);
            for (int i = 2; i < table.length - 2; i++) {
                table[i].fillAllFilds(form3(table[i - 1].y, table[i + 1].y, h),
                                      form9(table[i - 2].y, table[i - 1].y, table[i + 1].y, table[i + 2].y, h),
                                      form6(table[i - 1].y, table[i].y, table[i + 1].y, h),
                                      taskFunction);
            }
            table[table.length - 2].fillAllFilds(form3(table[table.length - 3].y, table[table.length - 1].y, h), 
                                                 form10(table[table.length - 5].y, table[table.length - 4].y, table[table.length - 3].y, table[table.length - 2].y, table[table.length - 1].y, h), 
                                                 form6(table[table.length - 3].y, table[table.length - 2].y, table[table.length - 1].y, h), 
                                                 taskFunction);
            table[table.length - 1].fillAllFilds(form5(table[table.length - 3].y, table[table.length - 2].y, table[table.length - 1].y, h), 
                                                 form11(table[table.length - 5].y, table[table.length - 4].y, table[table.length - 3].y, table[table.length - 2].y, table[table.length - 1].y, h), 
                                                 form13(table[table.length - 4].y, table[table.length - 3].y, table[table.length - 2].y, table[table.length - 1].y, h), 
                                                 taskFunction);
            printAllTable(table);

        }

    }

    private static void printAllTable(InPoint[] table) {
        System.out.println("значение функции и производных в точках"); 
        System.out.println(
            "k" + "\t\t" + "x" + "\t\t" + "y" + "\t\t" + "dfT" + "\t" + "df O(h^2)" + "\t" + "погр O(h^2)" + "\t" + "df O(h^4)" + "\t" + "погр O(h^4)" + "\t" + "ddfT" + "\t\t" + "ddf O(h^2)" + "\t" + "погр O(h^2)");
        for (InPoint row : table) {
            row.printfAllRow();
        }
    }

    private static double form13(double y_3, double y_2, double y_1, double y, double h) {
        return (2 * y - 5 * y_1 + 4 * y_2 - y_3) / (h * h);
    }

    private static double form11(double y_4, double y_3, double y_2, double y_1, double y, double h) {
        return (25 * y - 48 * y_1 + 36 * y_2 - 16 * y_3 + 3 * y_4) / (12 * h);
    }

    private static double form5(double y, double y2, double y3, double h) {
        return (3 * y3 - 4 * y2 + y) / (2 * h);
    }

    private static double form10(double y_4, double y_3, double y_2, double y_1, double y, double h) {
        return (3 * y + 10 * y_1 - 18 * y_2 + 6 * y_3 - y_4) / (12 * h);
    }

    private static double form9(double y, double y2, double y3, double y4, double h) {
        return (y - 8 * y2 + 8 * y3 - y4) / (12 * h);
    }

    private static double form6(double y, double y2, double y3, double h) {
        return (y3 - 2 * y2 + y) / (h * h);
    }

    private static double form8(double y0, double y1, double y2, double y3, double y4, double h) {
        return (-3 * y0 - 10 * y1 + 18 * y2 - 6 * y3 + y4) / (12 * h);
    }

    private static double form3(double y, double y2, double h) {
        return (y2 - y) / (2 * h);
    }

    private static double form12(double y, double y1, double y2, double y3, double h) {
        return (2 * y - 5 * y1 + 4 * y2 - y3) / (h * h);
    }

    private static double form7(double y0, double y1, double y2, double y3, double y4, double h) {
        return (-25 * y0 + 48 * y1 - 36 * y2 + 16 * y3 - 3 * y4) / (12 * h);
    }

    private static double form4(double y, double y2, double y3, double h) {
        return (-3 * y + 4 * y2 - y3) / (2 * h);
    }

    private static void printXYtable(InPoint[] table) {
        System.out.println("значение функции в точках(k, x, y):");
        for (InPoint row : table) {
            row.printfXYRow();
        }
    }

    private static void printFunctonOptions() {
        System.out.println("Выберите функцию для численного дифференцирования:");
        Functions.QUADRATIC.print();
        System.out.println("    1");
        Functions.CUBIC.print();
        System.out.println("    2");
        Functions.EXPONENTIAL.print();
        System.out.println("    3");
        Functions.SMOOTH.print();
        System.out.println("    4");
        System.out.println("Выход    0");
    }

}

