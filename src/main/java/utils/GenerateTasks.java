package utils;


import entity.Task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static utils.StaticParameters.*;

public class GenerateTasks {


    /**
     * 随机生成高层次任务
     * 个数:N*Pr
     * 返回:一个高层次任务集
     */
//    public static void utilizationTranFormat(int totalProcessor, int totalTask, double targetU, double cp, double cf, double xf) {
//
//        String[] results = Utilization.generateUtilization(totalProcessor, totalTask, targetU, cp, cf, xf);
//        U_HI_HI = new double[totalTask];//对于results[0]
//        U_LO_HI = new double[totalTask];//对于results[1]
//        U_HI_LO = new double[totalTask];//对于results[2]
//        U_LO_LO = new double[totalTask];//对于results[3]
//
//        String[] result0 = results[0].substring(1, results[0].length() - 1).split(",");
//        for (int i = 0; i < result0.length; i++) {
//            U_HI_HI[i] = Double.parseDouble(result0[i]);
//        }
//
//        String[] result1 = results[1].substring(1, results[1].length() - 1).split(",");
//        for (int i = 0; i < result1.length; i++) {
//
//            U_LO_HI[i] = Double.parseDouble(result1[i]);
//        }
//
//        String[] result2 = results[2].substring(1, results[2].length() - 1).split(",");
//        for (int i = 0; i < result2.length; i++) {
//            U_HI_LO[i] = Double.parseDouble(result2[i]);
//        }
//
//        String[] result3 = results[3].substring(1, results[3].length() - 1).split(",");
//        for (int i = 0; i < result3.length; i++) {
//            U_LO_LO[i] = Double.parseDouble(result3[i]);
//        }
//
//    }

    public static List<Task> generateHiTasks() {
        List<Task> HITaskList = new ArrayList<>();
        BigDecimal b = new BigDecimal(n * CP);
        for (int i = 0; i < b.setScale(1, RoundingMode.HALF_UP).doubleValue(); i++) {
            //System.out.println(b.setScale(1, RoundingMode.HALF_UP).doubleValue());
            Task task = new Task();
            //double Ti = Math.ceil(Math.random() * 990 + 10);
            double Ti =Math.ceil(Math.random() * 490 + 10);
            task.setTi(Ti);
//            double u_hi = U_HI_HI[i];
//            double u_lo = U_LO_HI[i];
            double u_hi = Math.random();
            double u_lo = Math.random()*u_hi;
            task.setNum(i);
            task.setUi_HI(u_hi);
            task.setUi_LO(u_lo);
            //System.out.println(task.getUi_LO());
            task.setCi_HI(Math.ceil(task.getTi() * u_hi));
            task.setCi_LO(Math.ceil(task.getTi() * u_lo));
            task.setLi(1);
            double Di = Math.random()*(Ti-task.getCi_HI())+task.getCi_HI();
            //double Di = Ti;
            task.setDi(Di);
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
        BigDecimal b = new BigDecimal(n * (1 - CP));
        for (int i = 0; i < b.setScale(1, RoundingMode.HALF_UP).doubleValue(); i++) {
            //System.out.println(b.setScale(1, RoundingMode.HALF_UP).doubleValue());
            Task task = new Task();
            //double Ti = Math.ceil(Math.random() * 990 + 10);
            double Ti =Math.ceil(Math.random() * 490 + 10);
            task.setTi(Ti);
            task.setNum((int) (i+n*CP));
//            double u_lo = U_LO_LO[i];
//            double u_hi = U_HI_LO[i];
            double u_lo = Math.random();
            double u_hi = Math.random()*u_lo;
            task.setUi_HI(u_hi);
            task.setUi_LO(u_lo);
            task.setCi_HI(Math.ceil(task.getTi() * u_hi));
            task.setCi_LO(Math.ceil(task.getTi() * u_lo));
            task.setLi(0);
            double Di = Math.random()*(Ti-task.getCi_LO())+task.getCi_LO();
            //double Di = Ti;
            task.setDi(Di);
            LOTaskList.add(task);
        }
        return LOTaskList;
    }



}
