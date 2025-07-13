package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.Utilization;

import java.util.ArrayList;
import java.util.List;

import static utils.StaticParameters.M;

public class EDF_VD_Schedulability {

    public static int schedulabilityJudge(List<Task>[] M_List) {

        double[] U_LO_LO = ArrayUtils.arrayInitializationZero();
        double[] U_LO_HI = ArrayUtils.arrayInitializationZero();
        double[] U_HI_LO = ArrayUtils.arrayInitializationZero();
        double[] U_HI_HI = ArrayUtils.arrayInitializationZero();
        Utilization.sumProcessorU(M_List, U_LO_LO, U_LO_HI, U_HI_LO, U_HI_HI);

        boolean judgeCondition1;
        boolean judgeCondition2;

        for (int i = 1; i <= M; i++) {
            //System.out.println(M_List[i]);
            double xlo = U_HI_LO[i] / (1 - U_LO_LO[i]);
            double xup = (1 - U_HI_HI[i] - U_LO_HI[i]) / (U_LO_LO[i] - U_LO_HI[i]);
            if (xup > 1)
                xup = 1;
            judgeCondition1 = U_HI_HI[i] + U_LO_LO[i] <= 1;
            judgeCondition2 = (U_HI_HI[i] + U_LO_HI[i] < 1) && (U_LO_LO[i] < 1) && (xlo < xup);

            if (!judgeCondition1 && !judgeCondition2) {
                return 0;
            }
        }
        return 1;

    }

    public static int schedulabilityJudgeByOneProcessor(List<Task> M_List) {
        double U_LO_LO = 0;
        double U_LO_HI = 0;
        double U_HI_LO = 0;
        double U_HI_HI = 0;
        List<Double> result = Utilization.sumProcessorUByOne(M_List);
        U_LO_LO=result.get(0);
        U_LO_HI=result.get(1);
        U_HI_LO=result.get(2);
        U_HI_HI=result.get(3);
        //System.out.println(U_HI_LO);

        boolean judgeCondition1;
        boolean judgeCondition2;
        //System.out.println(M_List[i]);
        double xlo = U_HI_LO / (1 - U_LO_LO);
        double xup = (1 - U_HI_HI - U_LO_HI) / (U_LO_LO - U_LO_HI);
        if (xup > 1)
            xup = 1;
        judgeCondition1 = U_HI_HI + U_LO_LO <= 1;
        judgeCondition2 = (U_HI_HI + U_LO_HI < 1) && (U_LO_LO < 1) && (xlo < xup);

        if (!judgeCondition1 && !judgeCondition2) {
            return 0;
        }
        return 1;
    }

    public static int schedulabilityJudge2(List<Task>[] M_List) {

        double[] U_LO_LO = ArrayUtils.arrayInitializationZero();
        double[] U_LO_HI = ArrayUtils.arrayInitializationZero();
        double[] U_HI_LO = ArrayUtils.arrayInitializationZero();
        double[] U_HI_HI = ArrayUtils.arrayInitializationZero();
        Utilization.sumProcessorU(M_List, U_LO_LO, U_LO_HI, U_HI_LO, U_HI_HI);

//        boolean judgeCondition1;
        boolean judgeCondition2;

        for (int i = 1; i <= M; i++) {
            //System.out.println(M_List[i]);
            double xlo = U_HI_LO[i] / (1 - U_LO_LO[i]);
            double xup = (1 - U_HI_HI[i] - U_LO_HI[i]) / (U_LO_LO[i] - U_LO_HI[i]);
            if (xup > 1)
                xup = 1;
//            judgeCondition1 = U_HI_HI[i] + U_LO_LO[i] <= 1;
            judgeCondition2 = (U_HI_HI[i] + U_LO_HI[i] < 1) && (U_LO_LO[i] < 1) && (xlo < xup);

            if (!judgeCondition2) {
                return 0;
            }
        }
        return 1;

    }

}
