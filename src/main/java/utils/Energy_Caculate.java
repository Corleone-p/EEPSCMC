package utils;

import entity.Task;

import java.util.List;

import static utils.StaticParameters.M;
import static utils.StaticParameters.Ps;

/**
 * 传入当前处理器上的任务
 * 输出能耗
 * **/

public class Energy_Caculate {
    public static double Caculate_Energe(List<Task>[] M_List){
        //计算系统的能耗
        double[] U_LO_LO = ArrayUtils.arrayInitializationZero();//将M个处理器上的利用率初始化为0
        double[] U_LO_HI = ArrayUtils.arrayInitializationZero();
        double[] U_HI_LO = ArrayUtils.arrayInitializationZero();
        double[] U_HI_HI = ArrayUtils.arrayInitializationZero();
        Utilization.sumProcessorU(M_List, U_LO_LO, U_LO_HI, U_HI_LO, U_HI_HI);//当前处理器上的任务的四种利用率


        double energy = 0;
        double energy_sum = 0;
        for (int i = 1; i <= M; i++){//对每个处理器上的任务进行处理
            //double S_lo=Math.max(0.17,U_LO_LO[i]+U_HI_LO[i]);
            double S_lo = 0.2;
            double Energy = Ps+(0.01+1)*(U_HI_LO[i] + U_LO_LO[i])/1.0;
            for (;S_lo <= 1; S_lo+=0.1){
                int ans = MC_QPA.MC_QPA_SLO(M_List[i], S_lo);//在此速度下dbf可行性分析也可行
                if (ans == 1){//在速度为Slo的情况下，可调度
                    //System.out.println("pause");
                    energy=Ps+(0.01+S_lo*S_lo*S_lo)*(U_HI_LO[i] + U_LO_LO[i])/S_lo;
                    Energy = Math.min(energy,Energy);

                }
            }
            energy_sum+=Energy;
        }
        return energy_sum;
    }


    public static double Caculate_Energe(List<Task> taskList){
        //计算单处理器上面的能耗
        double U_LO_LO = 0;
        double U_LO_HI = 0;
        double U_HI_LO = 0;
        double U_HI_HI = 0;
        //Utilization.sumProcessorU(taskList, U_LO_LO, U_LO_HI, U_HI_LO, U_HI_HI);//当前处理器上的人任务的四种利用率
        for (Task task:taskList){
            if (task.getLi()==1){
                U_HI_LO+=task.getUi_LO();
                U_HI_HI+=task.getUi_HI();
            }else {
                U_LO_LO+=task.getUi_LO();
                U_LO_HI+=task.getUi_HI();
            }
        }
        double Energy = Ps+(0.01+1)*(U_HI_LO + U_LO_LO)/1.0;
        double energy = 0;
        double energy_sum = 0;
            double S_lo=0.2;
            for (;S_lo < 1; S_lo+=0.01){
                int ans = MC_QPA.MC_QPA_SLO(taskList, S_lo);//在此速度下dbf可行性分析也可行
                if (ans == 1){//在速度为Slo的情况下，可调度
                    energy=Ps+(0.01+S_lo*S_lo*S_lo)*(U_HI_LO + U_LO_LO)/S_lo;
                    //System.out.println(S_lo);
                    Energy = Math.min(energy,Energy);
                }
            }
            energy_sum+=Energy;
        return energy_sum;
    }

}
