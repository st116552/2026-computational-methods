public class ResultInfo {
    public ResultInfo(String name, int stepNumber, double root, double mod) {
        this.methodName = name;
        this.stepNumber = stepNumber;
        this.root = root;
        this.mod = mod;
    }
    public String methodName;
    public int stepNumber;
    public double root;
    public double mod;
    public void print() {
        System.out.print("name: " + methodName + "; stepNumber: " + stepNumber + "; root: " + root + "; mod: ");
        System.out.printf("%.2e\n", mod);
    }
}
