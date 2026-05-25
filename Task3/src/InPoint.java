public class InPoint {
    public int k;
    public double x;
    public double y;
    public double dfT;
    public double df_O2;
    public double error_d_O2;
    public double df_O4;
    public double error_d_O4;
    public double ddfT;
    public double ddf_O2;
    public double error_dd_O2;

    InPoint (double x0, double h, int k, Functions fuction) {
        this.k = k;
        this.x = x0 + k * h;
        this.y = fuction.func(this.x);
    }

    public void printfXYRow() {
        System.out.printf("%d" + "\t" + "%15.10f" + "\t" + "%15.10f" + "\n", k , x, y);
    }

    public void fillAllFilds(double df_O2, double df_O4, double ddf_O2, Functions fuction) {
        this.df_O2 = df_O2;
        this.df_O4 = df_O4;
        this.ddf_O2 = ddf_O2;
        dfT = fuction.dfunc(x);
        ddfT = fuction.ddfunc(x);
        error_d_O2 = df_O2 - dfT;
        error_d_O4 = df_O4 - dfT;
        error_dd_O2 = ddf_O2 - ddfT;
    }

    void printfAllRow() {
        System.out.printf("%d" + "\t" + "%10.8f" + "\t" + "%10.8f" + "\t" + "%10.8f" + "\t" + "%10.8f" + "\t" + "%12.8e" + "\t" + "%10.8f" + "\t" + "%12.8e" + "\t" + "%10.8f" + "\t" + "%10.8f" + "\t" + "%12.8e" + "\n",
                         k, x, y, dfT, df_O2, error_d_O2, df_O4, error_d_O4, ddfT, ddf_O2, error_dd_O2);
    }
}
