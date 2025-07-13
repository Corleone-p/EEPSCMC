package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.Energy_Caculate;
import utils.MC_QPA;

import java.util.List;

import static utils.StaticParameters.M;

public class MinSumEnergy {
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
    public static int MinsumPartitioning(List<Task> taskList, List<Task>[] M_List){
        M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务

        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率

        int count = taskList.size();
        int position = 0;//任务的个数

        double uiLO, uiHI;  //任务i的利用率
        while (position < count){
            uiHI = taskList.get(position).getUi_HI();
            uiLO = taskList.get(position).getUi_LO();//该任务的利用率
            //如果是第一个任务，则直接分配
            if (position ==0){
                for (int i = 1; i <=M ; i++){
                    if (UHI[i] >= uiHI && ULO[i] >= uiLO){
                        M_List[i].add(taskList.get(position));  //把该任务放入处理器
                        if (MC_QPA.Dbf_Schedule(M_List[i]) == 0){
                            M_List[i].remove(taskList.get(position));    //从处理器中移除该任务
                        }else {
                            UHI[i] -= uiHI;
                            ULO[i] -= uiLO;
                            break;
                        }
                    }
                }
            }else {
                //将该任务分配给处理器在满足可调度性的条件下选择能耗最小的处理器
                int ans = Select_Procession(M_List,taskList.get(position),ULO,UHI);
                if (ans != 0){//找到能耗最小的处理器
                    UHI[ans] -= uiHI;
                    ULO[ans] -= uiLO;
                    M_List[ans].add(taskList.get(position)); //把任务放入该处理器中

                }else {
                    return 0;
                }
            }
            position++;
        }
        return 1;
    }
}
