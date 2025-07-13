package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.Utilization;

import java.util.List;

import static utils.StaticParameters.M;

public class EnergyConsumption {

    public static double EnergyConsumption(List<Task>[] M_List) {


        double[] U_LO_LO = ArrayUtils.arrayInitializationZero();
        double[] U_LO_HI = ArrayUtils.arrayInitializationZero();
        double[] U_HI_LO = ArrayUtils.arrayInitializationZero();
        double[] U_HI_HI = ArrayUtils.arrayInitializationZero();
        Utilization.sumProcessorU(M_List, U_LO_LO, U_LO_HI, U_HI_LO, U_HI_HI);

        double energy = 0;

        for (int i = 1; i <= M; i++) {

            double xlo = U_HI_LO[i] / (1 - U_LO_LO[i]);
            double xup = (1 - U_HI_HI[i] - U_LO_HI[i]) / (U_LO_LO[i] - U_LO_HI[i]);

            if (xup > 1)
                xup = 1;
            double x = (xlo + xup) / 2;

            double s1 = U_LO_LO[i] + U_HI_LO[i] / x;
            double st2 = x * U_LO_LO[i] / (1 - U_HI_HI[i] - U_LO_HI[i] + x * U_LO_HI[i]);
            double st1 = Math.max(s1, st2);
            if (st1 < 0.17)
                st1 = 0.17;
            if (st1 > 1)
                st1 = 1;
            if (st1 != 0) {
                energy += (0.01 + st1 * st1 * st1) * (U_HI_LO[i] + U_LO_LO[i]) / st1;
            }

        }


        return energy;
    }

}
