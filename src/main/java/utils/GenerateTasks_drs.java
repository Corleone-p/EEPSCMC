package utils;


import entity.Task;

import java.util.ArrayList;
import java.util.List;

import static utils.StaticParameters.*;

public class GenerateTasks_drs {

    /**
     * 把drs生成的利用率进行转换。
     */

    public static void utilizationTranFormat(int totalProcessor, int totalTask, double targetU, double cp, double cf, double xf) {

        String[] results = Utilization.generateUtilization(totalProcessor, totalTask, targetU, cp, cf, xf);
        U_HI_HI = new double[totalTask];//对于results[0]
        U_HI_LO = new double[totalTask];//对于results[1]
        U_LO_HI = new double[totalTask];//对于results[2]
        U_LO_LO = new double[totalTask];//对于results[3]

        String[] result0 = results[0].substring(1, results[0].length() - 1).split(",");
        for (int i = 0; i < result0.length; i++) {
            U_HI_HI[i] = Double.parseDouble(result0[i]);
        }
        String[] result1 = results[1].substring(1, results[1].length() - 1).split(",");
        for (int i = 0; i < result1.length; i++) {
            U_HI_LO[i] = Double.parseDouble(result1[i]);
        }
        String[] result2 = results[2].substring(1, results[2].length() - 1).split(",");
        for (int i = 0; i < result2.length; i++) {
            U_LO_HI[i] = Double.parseDouble(result2[i]);
        }
        String[] result3 = results[3].substring(1, results[3].length() - 1).split(",");
        for (int i = 0; i < result3.length; i++) {
            U_LO_LO[i] = Double.parseDouble(result3[i]);
        }
    }

    /**
     * 随机生成高层次任务
     * 个数:N*Pr
     * 返回:一个高层次任务集
     */
    public static List<Task> generateHiTasks() {
        List<Task> HITaskList = new ArrayList<>();
        for (int i = 0; i < DoubleArithUtil.round(n * CP, 1); i++) {
            //System.out.println( DoubleArithUtil.round(N * CP, 1));
            Task task = new Task();
//            double Di = Math.ceil(Math.random() * 990 + 10);
//            task.setDi(Di);
//            task.setDv(Di);
            double Ti = Math.ceil(Math.random() * 490 + 10);
            task.setTi(Ti);
            double u_hi = U_HI_HI[i];
            double u_lo = U_HI_LO[i];
            task.setUi_HI(u_hi);
            task.setUi_LO(u_lo);
            task.setCi_HI(Ti * u_hi);
            task.setCi_LO(Ti * u_lo);
            task.setLi(1);
            // double Di = Math.ceil(Math.random() * (1000 - task.getCi_HI()) + task.getCi_HI());
            double Di = Math.random() * (Ti - task.getCi_HI()) + task.getCi_HI();
            task.setDi(Di);
            //task.setTi(Math.ceil(Math.random() * (1000 - Di) + Di));
            //task.setAi(0);
            HITaskList.add(task);
        }
        return HITaskList;
    }

    /**
     * 随机生成低层次任务
     * 个数：N*(1-Pr)
     * 返回:一个低层次任务集
     */
    public static List<Task> generateLOTasks() {
        List<Task> LOTaskList = new ArrayList<>();
        for (int i = 0; i < DoubleArithUtil.round(n * (1 - CP), 1); i++) {
            //System.out.println(DoubleArithUtil.round(N * (1 - CP),1));
            Task task = new Task();
//            double Di = Math.ceil(Math.random() * 990 + 10);
//            task.setDi(Di);
//            task.setDv(Di);
            double Ti = Math.ceil(Math.random() * 490 + 10);
            task.setTi(Ti);
            double u_lo = U_LO_LO[i];
            double u_hi = U_LO_HI[i];
            task.setUi_HI(u_hi);
            task.setUi_LO(u_lo);
            task.setCi_HI(Ti * u_hi);
            task.setCi_LO(Ti * u_lo);
            task.setLi(0);
            //task.setTi(Math.ceil(Math.random() * (1000 - Di) + Di));
            //double Di = Math.ceil(Math.random() * (1000 - task.getCi_LO()) + task.getCi_LO());
            double Di = Math.random() * (Ti - task.getCi_LO()) + task.getCi_LO();
            task.setDi(Di);
            //task.setAi(0);
            LOTaskList.add(task);
        }
        return LOTaskList;
    }

}
