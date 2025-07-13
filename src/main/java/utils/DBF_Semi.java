package utils;

import entity.Task;

import java.util.List;

public class DBF_Semi {
    public static double dbf_LOMode(List<Task> taskList,double t){//低模式下的总需求
        double dbf_sum = 0;
        for (Task task : taskList) {
            double Di = task.getDi();
            double T = task.getTi();
            double a = Math.max(Math.floor((t - Di) / T) + 1, 0);  //当前任务最大的作业数
            double dbf = a * task.getCi_LO();   //当前任务的需求
            dbf_sum += dbf;      //总的所有任务的需求
        }
        return dbf_sum;
    }
    public static double dbf_HIMode_LoTask(Task taskLO,double ts, double t){
        double dbf_hi_lo = 0;
        double Ci_HI = taskLO.getCi_HI();
        double Ci_Lo = taskLO.getCi_LO();
        double Ti  = taskLO.getTi();
        double Di = taskLO.getDi();
        double num = Math.max(Math.floor((t-Di)/Ti)+1,0);//区间长度为t时，最大的作业数
        dbf_hi_lo = num*Ci_HI+Math.min(num,Math.floor(ts/Ti)+1)*(Ci_Lo-Ci_HI);
        return dbf_hi_lo;
    }
    public static double dbf_HIMode_HiTask(Task taskHI,double ts, double t){
        double dbf_hi_hi = 0;
        double Ci_HI = taskHI.getCi_HI();
        double Ci_Lo = taskHI.getCi_LO();
        double Ti  = taskHI.getTi();
        double Di = taskHI.getDi();
        double num = Math.max(Math.floor((t-Di)/Ti)+1,0);//区间长度为t时，最大的作业数
        double num_2 = Math.max(Math.floor((t-ts-Di)/Ti)+1,0);//区间长度为t-ts时，最大的作业数
        dbf_hi_hi = num*Ci_Lo+num_2*(Ci_HI - Ci_Lo);
        return dbf_hi_hi;
    }
    public static double dbf_HIMode(List<Task> taskList,double ts,double t){//高模式下的总需求
        double dbf_HIMode = 0;
        for (Task task:taskList){
            if (task.getLi() == 0){
                dbf_HIMode+=dbf_HIMode_LoTask(task,ts,t);
            }else {
                dbf_HIMode+=dbf_HIMode_HiTask(task,ts,t);
            }
        }
        return dbf_HIMode;
    }


    /**
     *
     * 带S_LO的DBF分析
     * **/
    public static double dbf_LOMode(List<Task> taskList,double t,double Slo){//低模式下的总需求
        double dbf_sum = 0;
        for (Task task : taskList) {
            double Di = task.getDi();
            double T = task.getTi();
            double a = Math.max(Math.floor((t - Di) / T) + 1, 0);  //当前任务最大的作业数
            double dbf = a * task.getCi_LO()/Slo;   //当前任务的需求
            dbf_sum += dbf;      //总的所有任务的需求
        }
        return dbf_sum;
    }
    public static double dbf_HIMode_LoTask(Task taskLO,double ts, double t,double Slo){
        double dbf_hi_lo = 0;
        double Ci_HI = taskLO.getCi_HI();
        double Ci_Lo = taskLO.getCi_LO();
        double Ti  = taskLO.getTi();
        double Di = taskLO.getDi();
        double num = Math.max(Math.floor((t-Di)/Ti)+1,0);//区间长度为t时，最大的作业数
        dbf_hi_lo = num*Ci_HI+Math.min(num,Math.floor(ts/Ti)+1)*(Ci_Lo/Slo-Ci_HI);
        return dbf_hi_lo;
    }
    public static double dbf_HIMode_HiTask(Task taskHI,double ts, double t,double Slo){
        double dbf_hi_hi = 0;
        double Ci_HI = taskHI.getCi_HI();
        double Ci_Lo = taskHI.getCi_LO();
        double Ti  = taskHI.getTi();
        double Di = taskHI.getDi();
        double num = Math.max(Math.floor((t-Di)/Ti)+1,0);//区间长度为t时，最大的作业数
        double num_2 = Math.max(Math.floor((t-ts-Di)/Ti)+1,0);//区间长度为t-ts时，最大的作业数
        dbf_hi_hi = num*Ci_Lo/Slo+num_2*(Ci_HI - Ci_Lo/Slo);
        return dbf_hi_hi;
    }
    public static double dbf_HIMode(List<Task> taskList,double ts,double t,double Slo){//高模式下的总需求
        double dbf_HIMode = 0;
        for (Task task:taskList){
            if (task.getLi() == 0){
                dbf_HIMode+=dbf_HIMode_LoTask(task,ts,t,Slo);
            }else {
                dbf_HIMode+=dbf_HIMode_HiTask(task,ts,t,Slo);
            }
        }
        return dbf_HIMode;
    }
}
