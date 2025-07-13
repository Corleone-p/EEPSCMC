package utils;

import entity.Task;

import java.util.ArrayList;
import java.util.List;

public class StaticParameters {

    public static List<Task> PriorityTasks; //通过可行性测试的任务集
    public static double[] U_LO_LO;  //低关键层次任务在低模式下的利用率
    public static double[] U_HI_LO;  //低关键层次任务在高模式下的利用率
    public static double[] U_LO_HI;  //高关键层次任务在低模式下的利用率
    public static double[] U_HI_HI;  //高关键层次任务在高模式下的利用率

    public static int n;  //总的任务数

    public static int M;  //处理器数量

    public static double CP;     //产生高关键层次任务的概率
    public static double Ps = 0.05;

    public static double CF=2.0;  //Ci_HI/Ci_LO for HI task

    public static double XF=0.5;  //Ci_HI/CI_LO for LO task






}
