package utils;

import java.util.Random;

public class UUnifast {
    public static double[] uunifast(int n, double U){
        double []vectU = new double[n];
        double sumU = U;
        Random random=new Random();
        for (int i = 0; i < n; i++) {
            double temp=random.nextDouble();
            double nextSumU = sumU * Math.pow(temp,1.0/(n-i));
            vectU[i] = sumU -nextSumU;
            sumU = nextSumU;
        }
        vectU[n-1] = sumU;
        return vectU;
    }

}
