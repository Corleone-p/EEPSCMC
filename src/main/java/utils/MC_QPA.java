package utils;

import entity.Task;

import java.util.ArrayList;
import java.util.List;
import static utils.StaticParameters.M;

public class MC_QPA {
    /***
     * 判断每个处理器当前分配的任务是否可行任务
     * 输入当前处理器上的任务列表
     * 输出 0或1
     * **/
    public static int Dbf_Schedule(List<Task> M_List){
        if(McQpa_dbf(M_List)==1){
            return 1;
        }else {
            return 0;
        }


    }
    public static int McQpa_dbf(List<Task> taskList){
        double U_LO=0,U_HI=0;
        for (Task task:taskList){
            U_LO+=task.getUi_LO(); //计算低模式的利用率
            U_HI+=task.getUi_HI();//计算高模式下的利用率
        }
        U_LO=DoubleArithUtil.round(U_LO,2);
        U_HI=DoubleArithUtil.round(U_HI,2);
        double U_max = Math.max(U_HI,U_LO);
       if ( U_max < 1){
           double B = caulate_B(taskList,U_max);
           double t = Math.floor(B);
           while (t > 0){
               //double dbf_LoMode = DBF_Semi.dbf_LOMode(taskList,t);//计算低模式下的需求
//               if (dbf_LoMode > t){
//                   return 0;
//               }else {
                   List<Double> S = CaculateSt(taskList,t);
                   double tmax = 0;
                   for (double ts :S){
                       //计算在区间长度t，模式转换时间ts下，高模式下所有任务所需的需求
                       double dbf_HiMode = DBF_Semi.dbf_HIMode(taskList,ts,t);
                       if (dbf_HiMode > t){
                           //如果该需求超过时间区间上界，则不可调度
                           return 0;
                       }
                       if (dbf_HiMode > tmax){//超过上限
                           tmax = dbf_HiMode;
                       }
                   }
                   if (tmax == t){
                       t = t - 1;
                   }else if(tmax < t){
                       t = Math.floor(tmax);
                   }
               //}
           }
           return 1;
       }else {
           return 0;
       }


    }

    public static int Dbf_Schedule(List<Task> [] M_List){
        int  i = 1;
       while (i <= M){
            if (McQpa_dbf(M_List[i]) == 1){//当前处理器上分配的任务是可行的
                i++;
            }else{
                return 0;
            }
        }
       return 1;
    }
    public static int MC_QPA_SLO(List<Task> taskList,double Slo){
        double U_LO_Slo=0,U_HI_Slo=0;
        for (Task task:taskList){
            U_LO_Slo+=task.getUi_LO()/Slo; //计算ULO/SLO的利用率
            //U_HI_Slo+=task.getUi_HI();
            if (task.getLi() == 1){
                //U_HI_Slo+=(task.getCi_HI()-task.getCi_LO()+task.getCi_LO()/Slo)/task.getTi();
                U_HI_Slo+=task.getCi_HI()/task.getTi();//计算的是UHI·
            }else {
                U_HI_Slo+=task.getCi_HI()/task.getTi();//计算的是UHI·
            }
        }
        U_LO_Slo=DoubleArithUtil.round(U_LO_Slo,2);
        U_HI_Slo=DoubleArithUtil.round(U_HI_Slo,2);
        double U_max = Math.max(U_HI_Slo,U_LO_Slo);
        if ( U_max < 1){
            double B = caulate_B(taskList,U_max,Slo);
            double t = Math.floor(B);
            while (t > 0){
//                double dbf_LoMode = DBF_Semi.dbf_LOMode(taskList,t,Slo);//计算低模式下的需求
//                if (dbf_LoMode > t){
//                    return 0;
//                }else {
                    List<Double> S = CaculateSt(taskList,t);
                    double tmax = 0;
                    for (double ts :S){
                        //计算在区间长度t，模式转换时间ts下，高模式下所有任务所需的需求
                        double dbf_HiMode = DBF_Semi.dbf_HIMode(taskList,ts,t,Slo);
                        if (dbf_HiMode > t){
                            //如果该需求超过时间区间上界，则不可调度
                            return 0;
                        }
                        if (dbf_HiMode > tmax){//超过上限
                            tmax = dbf_HiMode;
                        }
                    }
                    if (tmax == t){
                        t = t - 1;
                    }else if(tmax < t){
                        t = Math.floor(tmax);
                    }
                }
           // }
            return 1;
        }else {
            return 0;
        }
    }
    public static double caulate_B(List<Task> taskList,double U_max,double Slo){
        double B = 0;
        double Ci_sum = 0;
        double Ci_2 = 0;
        for (Task task:taskList){
            //Ci_sum+=task.getCi_LO()/Slo;
                if (task.getLi() == 1){
                    //Ci_2+=task.getCi_HI()-task.getCi_LO();
                    Ci_2+= task.getCi_HI();
                }
                else {
                    Ci_sum+=task.getCi_LO()/Slo;
                }

        }
        B = (Ci_sum+Ci_2)/(1-U_max);
        return B;
    }
    public static double caulate_B(List<Task> taskList,double U_max){
        double B = 0;
        double Ci_sum = 0;
        for (Task task:taskList){
            if(task.getLi() == 0){
                Ci_sum+=task.getCi_LO();
            }else {
                Ci_sum+=task.getCi_HI();
            }
        }
        B = Ci_sum/(1-U_max);
        return B;
    }
    public static List<Double> CaculateSt(List<Task> taskList,double t){//计算S(t)的集合
        List<Double> tsList = new ArrayList<>();//可能的模式转换的时间集合
        for (Task task:taskList){
            if (task.getLi() == 1){
                double Ti = task.getTi();
                double Di = task.getDi();
                double num = Math.max(0,Math.floor((t-Di)/Ti)+1);//区间长度为t时，最大的作业数
                for (int k = 0; k < num; k++) {
                    double ts = t - k*Ti - Di;
                    tsList.add(ts);
                }
            }
        }
        tsList.add(t);
        return tsList;
    }

}
