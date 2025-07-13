package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.Energy_Caculate;
import utils.MC_QPA;

import java.util.ArrayList;
import java.util.List;

import static utils.StaticParameters.M;

/***
 * 对重任务采用CU-WFD,其他的任务采用贪婪
 * */
public class Sum_WFD {
    public static int Select_Procession(List<Task>[] M_List, Task task, double [] ULO, double[] UHI){
        double Min_energy = Double.MAX_VALUE;
        int flag = -1;
        for (int i = 1; i <=M ; i++) {
            if (UHI[i] >= task.getUi_HI() && ULO[i] >= task.getUi_LO())//判断利用率条件是否满足
            {
                M_List[i].add(task);//把该任务放入其中
                if (MC_QPA.Dbf_Schedule(M_List[i]) == 1){//放入该任务可行，再去寻找放到哪个处理器的能耗最小
                    double e = Energy_Caculate.Caculate_Energe(M_List);
                    if (e < Min_energy){
                        Min_energy = e;
                        flag = i;//记录是哪个处理器
                    }
                }
                M_List[i].remove(task);//将任务移除
            }
        }
        //最后找到该任务分配给哪个处理器能耗最小，将该任务分配给该处理器
        if (flag != -1){
            return flag;//返回这个能耗最小的处理器
        }
        return 0;
    }
    public static int Sum_WFD(List<Task> taskList, List<Task>[] M_List){
        int count = taskList.size();
        double U_SUM_LO = 0,U_SUM_HI = 0;
        double Uavg = 0;
        for (Task task:taskList){
            Uavg += Math.max(task.getUi_HI(),task.getUi_LO());
//            if (task.getLi()==1){
//                U_SUM_HI+=task.getUi_HI();
//            }else {
//                U_SUM_LO+=task.getUi_LO();
//            }
        }
//        double Ut_lo = 3*U_SUM_LO/count;//分类标准
//        double Ut_hi = 3*U_SUM_HI/count;
        Uavg = Uavg/count;
        //首先对任务进行分类，分为重任务和轻任务
        List<Task> Heavytask = new ArrayList<>();
        List<Task> Normaltask = new ArrayList<>();
        for (Task task:taskList){
            if (Math.max(task.getUi_HI(),task.getUi_LO())>Uavg){
                Heavytask.add(task);
            }else {
                Normaltask.add(task);
            }
//            if (task.getLi() == 1){
//                if (task.getUi_HI()>Ut_hi){
//                    Heavytask.add(task);
//                }else {
//                    Normaltask.add(task);
//                }
//            }else {
//                if (task.getUi_LO()>Ut_lo){
//                    Heavytask.add(task);
//                }else {
//                    Normaltask.add(task);
//                }
//            }
        }
        //首先对heavy使用CU-WFD进行分配
        if (!Heavytask.isEmpty()){
            int f = CU_WFD.CU_WFD_Partitioning(Heavytask,M_List);
            if (f == 0)
            {
                return 0;
            }

        }else {
            M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务
        }
        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率
        //更新所有处理器剩余的利用率
        for (int i = 1; i <= M; i++) {
            if (M_List[i] != null){
                for (int j = 0; j < M_List[i].size(); j++) {
                    ULO[i] -= M_List[i].get(j).getUi_LO();
                    UHI[i] -= M_List[i].get(j).getUi_HI();
                }

            }
        }
        int normal = Normaltask.size();
        int position = 0;//任务的个数
        double uiLO, uiHI;  //任务i的利用率
        while (position < normal){
            uiHI = Normaltask.get(position).getUi_HI();
            uiLO = Normaltask.get(position).getUi_LO();//该任务的利用率
            int ans = Select_Procession(M_List,Normaltask.get(position),ULO,UHI);
            if (ans != 0){//找到能耗最小的处理器
                UHI[ans] -= uiHI;
                ULO[ans] -= uiLO;
                M_List[ans].add(Normaltask.get(position)); //把任务放入该处理器中

            }else {
                return 0;
            }
            position++;
        }
        return 1;

    }
}
