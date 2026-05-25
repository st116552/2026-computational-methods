import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("choose an option:");
        System.out.println("test13 - 1");
        System.out.println("test26 - 2");
        System.out.println("ball - 0");
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();


        if (option == 1) 
        {
            System.out.println("write borders:"); 
            System.out.print("A:"); 
            double A = sc.nextDouble(); 
            System.out.print("B:");
            double B = sc.nextDouble();
            int N = -1;
            ArrayList<Integer> signChangeSegments = null;
            while (true) 
            {
                System.out.print("write partitions number or 0 to stop:");
                int tmp = sc.nextInt();
                if (tmp == 0) break; 
                
                N = tmp;
                signChangeSegments = rootSeparationTest13(A, B, N);
                printSegments(A, B, N, signChangeSegments);

            }
            while (true) 
            {
                System.out.println("write segment number or -1 to stop");
                System.out.print("segment number:");
                int segmentNumber = sc.nextInt();
                if (segmentNumber == -1) break; 
                double h = (B - A) / N;            
                double segmentA = A + signChangeSegments.get(segmentNumber) * h;                
                double segmentB = segmentA + h;
                System.out.println("write accuracy");
                System.out.print("accuracy:");
                double eps = sc.nextDouble();
                ResultInfo bisection = bisectionTest13(segmentA, segmentB, eps);
                bisection.print();
                ResultInfo newtonMethod = newtonMethodTest13(segmentA, segmentB, eps);
                newtonMethod.print();
                ResultInfo ModifiedNewtonMethod = ModifiedNewtonMethodTest13(segmentA, segmentB, eps);
                ModifiedNewtonMethod.print();
                ResultInfo secant = secantTest13(segmentA, segmentB, eps);
                secant.print();

            }
        } 
        else if (option == 2) 
        {
            System.out.println("write borders:"); 
            System.out.print("A:"); 
            double A = sc.nextDouble(); 
            System.out.print("B:");
            double B = sc.nextDouble();
            int N = -1;
            ArrayList<Integer> signChangeSegments = null;
            while (true) 
            {
                System.out.print("write partitions number or 0 to stop:");
                int tmp = sc.nextInt();
                if (tmp == 0) break; 
                
                N = tmp;
                signChangeSegments = rootSeparationTest26(A, B, N);
                printSegments(A, B, N, signChangeSegments);

            }
            while (true) 
            {
                System.out.println("write segment number or -1 to stop");
                System.out.print("segment number:");
                int segmentNumber = sc.nextInt();
                if (segmentNumber == -1) break; 
                double h = (B - A) / N;            
                double segmentA = A + signChangeSegments.get(segmentNumber) * h;                
                double segmentB = segmentA + h;
                System.out.println("write accuracy");
                System.out.print("accuracy:");
                double eps = sc.nextDouble();
                ResultInfo bisection = bisectionTest26(segmentA, segmentB, eps);
                bisection.print();
                ResultInfo newtonMethod = newtonMethodTest26(segmentA, segmentB, eps);
                newtonMethod.print();
                ResultInfo ModifiedNewtonMethod = ModifiedNewtonMethodTest26(segmentA, segmentB, eps);
                ModifiedNewtonMethod.print();
                ResultInfo secant = secantTest26(segmentA, segmentB, eps);
                secant.print();
            }  
        }
        else if (option == 0)
        {
            while (true) {
                System.out.print("write radius or 0 to stop: ");
                double r = sc.nextDouble();
                if (r == 0) break;
                HashMap<Double, String> material = makeMaterialMap();
                System.out.print("write accuracy: ");
                double eps = sc.nextDouble();                
                System.out.println("Вещество:            Плотность:          Глубина погружения: ");
                for (Double rho : material.keySet()) {                    
                ResultInfo res = newtonMethodBall(r, eps, rho);
                System.out.print(material.get(rho) + "             " + rho + "              ");
                System.out.printf(String.format("%.3f", res.root) + "\n");
                }
            }
        }
        else System.out.println("Uncorrect option or format");
        sc.close();
    }

    private static ResultInfo newtonMethodBall(double r, double eps, Double rho) {
        double x = r;
        if (ballFunction(x, rho, r) * ddballFunction(x, rho, r) < 0) x = 0;
        int stepNumber = 0;
        while (true) {
            stepNumber++;
            double oldX = x;
            double f = ballFunction(x, rho, r);
            double df = dballFunction(x, rho, r);
            x = x - f / df;
            if (Math.abs(x - oldX) < eps) break;
            if (stepNumber > 100) break;
        }
        double mod = Math.abs((float) ballFunction(x, rho, r));
        return new ResultInfo("newtonMethod", stepNumber, x, mod);
    }


    private static double ddballFunction(double x, Double rho, double r) {
        double rhoWater = 1000.0;
        double ddwaterWeight = 2 * rhoWater * Math.PI * r - 2 * rhoWater * Math.PI * x;
        return ddwaterWeight;
    }

    private static double dballFunction(double x, Double rho, double r) {
        double rhoWater = 1000.0;
        double dwaterWeight = 2 * rhoWater * Math.PI * x * r - rhoWater * Math.PI * x * x;
        return dwaterWeight;
    }

    private static double ballFunction(double dk, double rho, double r) {
        double rhoWater = 1000.0;
        double ballWeight = rho * (4.0 / 3.0) * Math.PI * Math.pow(r, 3);
        double waterWeight = rhoWater * Math.PI * dk * dk * (r - dk / 3.0);
        return waterWeight - ballWeight;
    }

    private static ResultInfo secantBall(double r, double eps, Double rho) {
        double dk_1 = 0;
        double dk = 2 * r;
        double fk_1 = ballFunction(dk_1, rho, r);
        double fk = ballFunction(dk, rho, r);
        int stepNumber = 0;
        while (true) {
            stepNumber++;
            double dk1 = dk - fk * (dk - dk_1) / (fk - fk_1);
            dk_1 = dk;
            fk_1 = fk;
            dk = dk1;
            fk = ballFunction(dk, rho, r);
            if (Math.abs(dk - dk_1) < eps) break;            
            if (stepNumber > 100) break;
        }
        double mod = Math.abs((float) ballFunction(dk, rho, r));
        return new ResultInfo("secant", stepNumber, dk, mod);
    }


    private static HashMap<Double, String> makeMaterialMap() {
        HashMap<Double, String> material = new HashMap<>();
        material.put(250.0, "Пробка");
        material.put(430.0, "Бамбук");
        material.put(500.0, "Сосна");
        material.put(555.0, "Кедр");
        material.put(700.0, "Дуб");
        material.put(750.0, "Бук");
        material.put(800.0, "Красное дерево");
        material.put(850.0, "Тиковое дерево");
        material.put(900.0, "Парафин");
        material.put(920.0, "Полиэтилен");
        material.put(950.0, "Пчелиный воск");
        return material;
    }

    private static ResultInfo secantTest26(double segmentA, double segmentB, double eps) {
        double xk_1 = segmentA;
        double xk = segmentB;
        double fk_1 = test26Function(xk_1);
        double fk = test26Function(xk);
        int stepNumber = 0;
        while (true) {
            stepNumber++;
            double xk1 = xk - fk * (xk - xk_1) / (fk - fk_1);
            xk_1 = xk;
            fk_1 = fk;
            xk = xk1;
            fk = test26Function(xk);
            if (Math.abs(xk - xk_1) < eps) break;            
            if (stepNumber > 100) break;
        }
        double mod = Math.abs((float) test26Function(xk));
        return new ResultInfo("secant", stepNumber, xk, mod);
    }

    private static ResultInfo ModifiedNewtonMethodTest26(double segmentA, double segmentB, double eps) {
        double x = segmentA;
        if (test26Function(x) * ddtest26Function(x) < 0) x = segmentB;
        double df0 = dtest26Function(x);
        int stepNumber = 0;
        while (true) {
            stepNumber++;
            double oldX = x;
            double fx = test26Function(x);
            x = x - fx / df0;
            if (Math.abs(x - oldX) < eps) break;            
            if (stepNumber > 100) break;
        }
        double mod = Math.abs(test26Function(x));
        return new ResultInfo("ModifiedNewtonMethod", stepNumber, x, mod);
    }

    private static double ddtest26Function(double x) {
        return 2 + 20 * Math.sin(x);
    }

    private static ResultInfo newtonMethodTest26(double segmentA, double segmentB, double eps) {
        double x = segmentA;
        if (test26Function(x) * ddtest26Function(x) < 0) x = segmentB;
        int stepNumber = 0;
        while (true) {
            stepNumber++;
            double oldX = x;
            double f = test26Function(x);
            double df = dtest26Function(x);
            x = x - f / df;
            if (Math.abs(x - oldX) < eps) break;
            if (stepNumber > 100) break;
        }
        double mod = Math.abs((float) test26Function(x));
        return new ResultInfo("newtonMethod", stepNumber, x, mod);
    }

    private static double dtest26Function(double x) {
        return 2 * x - 20 * Math.cos(x);
    }

    private static ResultInfo bisectionTest26(double a, double b, double eps) {
        int stepNumber = 0;
        while (true) { 
            stepNumber++;
            double c = (a + b) / 2;
            if (test26Function(a) * test26Function(c) <= 0) {
                b = c;
            } else {
                a = c;
            }
            if (b - a <= 2 * eps) break;
            if (stepNumber > 100) break;
            //System.out.println(a + " " + b + " " +c);
        }
        return new ResultInfo("bisection", stepNumber, (a + b) / 2, (b - a) / 2);
    }

    private static ResultInfo secantTest13(double segmentA, double segmentB, double eps) {
        double xk_1 = segmentA;
        double xk = segmentB;
        double fk_1 = test13Function(xk_1);
        double fk = test13Function(xk);
        int stepNumber = 0;
        while (true) {
            stepNumber++;
            double xk1 = xk - fk * (xk - xk_1) / (fk - fk_1);
            xk_1 = xk;
            fk_1 = fk;
            xk = xk1;
            fk = test13Function(xk);
            if (Math.abs(xk - xk_1) < eps) break;            
            if (stepNumber > 100) break;
        }
        double mod = Math.abs((float) test13Function(xk));
        return new ResultInfo("secant", stepNumber, xk, mod);
    }

    private static ResultInfo ModifiedNewtonMethodTest13(double segmentA, double segmentB, double eps) {
        double x = segmentA;
        if (test13Function(x) * ddtest13Function(x) < 0) x = segmentB;
        double df0 = dtest13Function(x);
        int stepNumber = 0;
        while (true) {
            stepNumber++;
            double oldX = x;
            double fx = test13Function(x);
            x = x - fx / df0;
            if (Math.abs(x - oldX) < eps) break;            
            if (stepNumber > 100) break;
        }
        double mod = Math.abs(test13Function(x));
        return new ResultInfo("ModifiedNewtonMethod", stepNumber, x, mod);
    }

    private static ResultInfo newtonMethodTest13(double segmentA, double segmentB, double eps) {
        double x = segmentA;
        if (test13Function(x) * ddtest13Function(x) < 0) x = segmentB;
        int stepNumber = 0;
        while (true) {
            stepNumber++;
            double oldX = x;
            double f = test13Function(x);
            double df = dtest13Function(x);
            x = x - f / df;
            if (Math.abs(x - oldX) < eps) break;
            if (stepNumber > 100) break;
        }
        double mod = Math.abs((float) test13Function(x));
        return new ResultInfo("newtonMethod", stepNumber, x, mod);
    }

    private static double ddtest13Function(double x) {
        return -Math.sin(x) + 6 * x;
    }

    private static double dtest13Function(double x) {
        return Math.cos(x) + 3 * x * x - 9;
    }

    private static ResultInfo bisectionTest13(double a, double b, double eps) {
        int stepNumber = 0;
        while (true) { 
            stepNumber++;
            double c = (a + b) / 2;
            if (test13Function(a) * test13Function(c) <= 0) {
                b = c;
            } else {
                a = c;
            }
            if (b - a <= 2 * eps) break;
            if (stepNumber > 100) break;
            //System.out.println(a + " " + b + " " +c);
        }
        return new ResultInfo("bisection", stepNumber, (a + b) / 2, (b - a) / 2);
    }

    private static void printSegments(double a, double b, int n, ArrayList<Integer> signChangeSegments) {
        double h = (b - a) / n;
        int segmentNumber = signChangeSegments.size();
        System.out.println("Найдено " + segmentNumber + " отрезков перемены знака с шагом h = " + h + ":");
        for (int i = 0; i < segmentNumber; i++) {
            double x1 = a + signChangeSegments.get(i) * h;
            double x2 = x1 + h;
            System.out.println("[" + x1 + "; " + x2 + "]");
        }
    }

    private static ArrayList<Integer> rootSeparationTest26(double a, double b, int n) {
        ArrayList<Integer> signChangeSegments = new ArrayList<>();
        double h = (b - a) / n;
        double x1 = a;
        double f1 = test26Function(x1);
        for (int i = 0; i < n; i++) {
            double x2 = x1 + h;
            if (x2 > n) break; 
            double f2 = test26Function(x2);
            if (f1 * f2 < 0) signChangeSegments.add(i);
            x1 = x2;
            f1 = f2;
        }
        return signChangeSegments;
    }

    private static double test26Function(double x1) {
        return x1 * x1 - 20 * Math.sin(x1);
    }

    private static ArrayList<Integer> rootSeparationTest13(double a, double b, int n) {
        ArrayList<Integer> signChangeSegments = new ArrayList<>();
        double h = (b - a) / n;
        double x1 = a;
        double f1 = test13Function(x1);
        for (int i = 0; i < n; i++) {
            double x2 = x1 + h;
            if (x2 > n) break; 
            double f2 = test13Function(x2);
            if (f1 * f2 < 0) signChangeSegments.add(i);
            x1 = x2;
            f1 = f2;
        }
        return signChangeSegments;
    }

    private static double test13Function(double x1) {
        return Math.sin(x1) + Math.pow(x1, 3) - 9 * x1 + 3;
    }
}
