package utils;

import entity.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static utils.GenerateTasks.*;
import static utils.StaticParameters.*;

public class Utilization {

    public static int utilization_Scaling(double U_goal, List<Task> hi_task, List<Task> lo_task) {

        double U_HI_LO = 0;
        double U_LO_LO = 0;
        double U_HI_HI = 0;
        double U_LO_HI = 0;


        BigDecimal b = new BigDecimal(n * CP);
        BigDecimal b1 = new BigDecimal(n * (1 - CP));
        for (int i = 0; i < b.setScale(1, RoundingMode.HALF_UP).doubleValue(); i++) {
            U_HI_LO += hi_task.get(i).getUi_LO();
            U_HI_HI += hi_task.get(i).getUi_HI();
        }
        for (int i = 0; i < b1.setScale(1, RoundingMode.HALF_UP).doubleValue(); i++) {
            U_LO_LO += lo_task.get(i).getUi_LO();
            U_LO_HI += lo_task.get(i).getUi_HI();
        }

        if (U_LO_LO + U_HI_LO < U_goal){//如果要放大利用率，则重新生成所有任务
            return 0;
        }
        double multiplier = (U_LO_LO + U_HI_LO) / U_goal;
        for (int i = 0; i < b.setScale(1, RoundingMode.HALF_UP).doubleValue(); i++) {
            hi_task.get(i).setUi_LO(hi_task.get(i).getUi_LO() / multiplier);
            hi_task.get(i).setUi_HI(hi_task.get(i).getUi_HI() / multiplier);
            hi_task.get(i).setCi_LO((hi_task.get(i).getCi_LO() / multiplier));
            hi_task.get(i).setCi_HI((hi_task.get(i).getCi_HI() / multiplier));
        }
        for (int i = 0; i < b1.setScale(1, RoundingMode.HALF_UP).doubleValue(); i++) {
            lo_task.get(i).setUi_LO(lo_task.get(i).getUi_LO() / multiplier);
            lo_task.get(i).setUi_HI(lo_task.get(i).getUi_HI() / multiplier);
            lo_task.get(i).setCi_LO((lo_task.get(i).getCi_LO() / multiplier));
            lo_task.get(i).setCi_HI((lo_task.get(i).getCi_HI() / multiplier));

        }

        double UHI = 0;
        for (Task task : lo_task) {
            UHI += task.getUi_HI();
        }
        for (Task task : hi_task) {
            UHI += task.getUi_HI();
        }
            return 1;

    }

    /**
     * Java封装python的DRS接口函数
     * 封装功能：
     * 1、根据预设参数去计算任务集LO mode的目标利用率ULO，以及对应的UHIHI、ULOHI、UHILO、ULOLO等利用率和信息
     * 2、根据1计算的各个利用率，去生成对应的利用率集合
     *
     * @param totalProcessor 处理器总数
     * @param totalTask      总的任务数
     * @param targetU        系统在LO mode下的目标利用率值
     * @param cp             高关键层次任务占所有任务的比例，以及高关键层次任务在LO mode利用率和占系统在LO mode下的目标利用率值的比例
     * @param cf             高关键层次任务在高模式和低模式利用率和的比例
     * @param xf             低关键层次任务在高模式和低模式利用率和的比例
     */
    public static String[] generateUtilization(int totalProcessor, int totalTask, double targetU, double cp, double cf, double xf) {
        Process proc;
        String[] results = new String[4];
        try {
//            String imc_drsAddress=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"imc_drs.py";//imc_drs.py的绝对路径地址
            //String imc_drsAddress=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"demo2.py";//imc_drs.py的绝对路径地址
            //System.out.println(imc_drsAddress);

            String py_adress = "D:\\env\\py38\\python.exe";//python解释器的地址
            String imc_drsAddress = "D:\\Drs\\drs\\imc_drs.py"; //执行的drs.py文件的地址

            //String imcpa_drsAddress=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"../../src/main/java/demo2.py";//imcpa_drs.py的绝对路径地址
            String[] cmd = new String[]{py_adress, imc_drsAddress, String.valueOf(totalProcessor),
                    String.valueOf(totalTask), String.valueOf(targetU), String.valueOf(cp), String.valueOf(cf), String.valueOf(xf)};

//            String[] cmd = new String[]{py_adress, imc_drsAddress, String.valueOf(totalProcessor),
//                    String.valueOf(totalTask)};
            proc = Runtime.getRuntime().exec(cmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            int i = 0;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);//按行输出从控制台得到的输出信息
                //这里写上从控制台输出到自定义POJO封装
                results[i] = line;
                i++;
            }
            in.close();
            int res = proc.waitFor();//查看返回值是否为1。如果是1，说明运行Python文件出错啦。
            if (res != 0) {//本地执行imcpa_drs.py时出现异常，打印异常信息
                InputStream errorStream = proc.getErrorStream();
                InputStreamReader isr = new InputStreamReader(errorStream, "gbk");//读取
//            System.out.println(isr.getEncoding());//输出编码格式
                BufferedReader bufr = new BufferedReader(isr);//缓冲
                String line2 = null;
                while ((line2 = bufr.readLine()) != null) {
                    System.out.println(line2);
                }
                isr.close();
            }
            // System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return results;
    }


    /**
     * 提供测试函数
     */
    public static void main(String[] args) {
        //generateUtilization(1, 4, 0.6, 0.5, 2, 0.5);//根据参数，去调用函数接口，控制台输出为各个任务的高低利用率集合
        String[] results = generateUtilization(4, 10, 0.05, 0.5, 2, 0.5);

        U_HI_HI = new double[10];//对于results[0]
        U_LO_HI = new double[10];//对于results[1]
        U_HI_LO = new double[10];//对于results[2]
        U_LO_LO = new double[10];//对于results[3]

        String[] result0 = results[0].substring(1, results[0].length() - 1).split(",");
        for (int i = 0; i < result0.length; i++) {
            U_HI_HI[i] = Float.parseFloat(result0[i]);
            System.out.print(U_HI_HI[i] + " ");
        }
        System.out.println();
        String[] result1 = results[1].substring(1, results[1].length() - 1).split(",");
        for (int i = 0; i < result1.length; i++) {
            U_LO_HI[i] = Float.parseFloat(result1[i]);
            System.out.print(U_LO_HI[i] + " ");
        }
        System.out.println();
        String[] result2 = results[2].substring(1, results[2].length() - 1).split(",");
        for (int i = 0; i < result2.length; i++) {
            U_HI_LO[i] = Float.parseFloat(result2[i]);
            System.out.print(U_HI_LO[i] + " ");
        }
        System.out.println();
        String[] result3 = results[3].substring(1, results[3].length() - 1).split(",");
        for (int i = 0; i < result3.length; i++) {
            U_LO_LO[i] = Float.parseFloat(result3[i]);
            System.out.print(U_LO_LO[i] + " ");
        }

    }

    public static void sumProcessorU(List<Task>[] M_List, double[] U_LO_LO, double[] U_LO_HI, double[] U_HI_LO, double[] U_HI_HI) {

        for (int i = 1; i <= M; i++) {
            //System.out.println(M_List[i]);
            int size = M_List[i].size();
            for (int j = 0; j < size; j++) {
                if (M_List[i].get(j).getLi() == 1) {
                    U_HI_LO[i] += M_List[i].get(j).getUi_LO();//低模式下高关键层次任务利用率总和
                    U_HI_HI[i] += M_List[i].get(j).getUi_HI();//高模式下高关键层次任务利用率总和
                } else {
                    U_LO_LO[i] += M_List[i].get(j).getUi_LO();
                    U_LO_HI[i] += M_List[i].get(j).getUi_HI();
                }
            }
        }

    }

    public static List<Double> sumProcessorUByOne(List<Task> M_List) {
        List<Double> U_List = new ArrayList<>();
        //System.out.println(M_List[i]);
        double U_LO_LO = 0;
        double U_LO_HI = 0;
        double U_HI_LO = 0;
        double U_HI_HI = 0;
        int size = M_List.size();
        for (int j = 0; j < size; j++) {
            if (M_List.get(j).getLi() == 1) {
                U_HI_LO += M_List.get(j).getUi_LO();
                U_HI_HI += M_List.get(j).getUi_HI();
            } else {
                U_LO_LO += M_List.get(j).getUi_LO();
                U_LO_HI += M_List.get(j).getUi_HI();
            }
        }
        U_List.add(U_LO_LO);
        U_List.add(U_LO_HI);
        U_List.add(U_HI_LO);
        U_List.add(U_HI_HI);
        return U_List;
    }


}

