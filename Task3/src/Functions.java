public enum Functions {
    QUADRATIC("2x^2 + x - 1"),
    CUBIC("3x^3 + x^2 - x + 1"),
    EXPONENTIAL("e^4x"),
    SMOOTH("sin(2x) - 1.25x^2 + 0.35");

    private String functionInText;

    Functions(String functionInText) {
        this.functionInText = functionInText;
    }

    public void print() {
        System.out.print(this.functionInText);
    }

    public double func(double x) {
        if (this == QUADRATIC) return 2 * x * x + x - 1;
        if (this == CUBIC) return 3 * Math.pow(x, 3) + x * x - x + 1;
        if (this == EXPONENTIAL) return Math.exp(4 * x);
        if (this == SMOOTH) return Math.sin(2 * x) - 1.25 * x * x + 0.35;
        System.out.println("No such func, check Functions. java 22");
        return 0;
    }

    public double dfunc(double x) {
        if (this == QUADRATIC) return 4 * x + 1;
        if (this == CUBIC) return 9 * x * x + 2 * x - 1;
        if (this == EXPONENTIAL) return 4 * Math.exp(4 * x);
        if (this == SMOOTH) return 2 * Math.cos(2 * x) - 2.5 * x;
        System.out.println("No such func, check Functions. java 22");
        return 0;
    }

    public double ddfunc(double x) {
        if (this == QUADRATIC) return 4;
        if (this == CUBIC) return 18 * x + 2;
        if (this == EXPONENTIAL) return 16 * Math.exp(4 * x);
        if (this == SMOOTH) return -4 * Math.sin(2 * x) - 2.5;
        System.out.println("No such func, check Functions. java 22");
        return 0;
    }

}