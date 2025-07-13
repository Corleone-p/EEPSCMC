package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.MC_QPA;

import java.util.ArrayList;
import java.util.List;
import static utils.StaticParameters.M;
/**
 * 对CU-WFD进行剪枝回溯算法
 * **/
public class BM_WFD {
    //回溯递归的分配剩余的Normal 任务
    public static int Backtrac_Mapping(List<Task> NormalList,List<Task>[] M_List){
        if (NormalList.isEmpty()){
            return 1;
        }
        Task task = NormalList.get(0);//取出第一个待分配任务
        NormalList.remove(0);
        double uihi = task.getUi_HI();
        double uilo = task.getUi_LO();
        //初始化可调度处理器为空
        List<Task>[] M_List_sched = new List[M + 1];
        M_List_sched = ArrayUtils.listArrayInitializationOfProcessor(M_List_sched);//用于处理器中存放的任务
        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率
        for (int i = 1; i <= M; i++) {
            for (int j = 0; j < M_List[i].size(); j++) {//更新每个处理器的剩余利用率
                ULO[i] -= M_List[i].get(j).getUi_LO();
                UHI[i] -= M_List[i].get(j).getUi_HI();
            }

        }
        for (int i = 1; i <= M; i++) {//遍历每个处理器，尝试将其分配给每个处理器
            if (UHI[i] >= uihi && ULO[i] >= uilo){
                M_List[i].add(task);//尝试将其分配给该处理器
                if (MC_QPA.Dbf_Schedule(M_List[i]) == 1){//可分配给该处理器
                    //计算将该任务分配给此处理器所增加的总能耗，并将此处理器与该任务放到可调度的处理器当中


                }
            }

        }
        //如果所有处理器都不可调度，则返回0
        if(M_List_sched.length == 0){
            return 0;
        }



        return 0;
    }
    public static int BM_WFD_Partitioning(List<Task> taskList, List<Task>[] M_List){
        M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务
        int count = taskList.size();
        double U_SUM = 0;
        for (Task task:taskList){
            U_SUM += task.getMax_ui();
        }
        double Ut = U_SUM/count;//分类标准
        //首先对任务进行分类，分为重任务和轻任务
        List<Task> Heavytask = new ArrayList<>();
        List<Task> Normaltask = new ArrayList<>();
        for (Task task:taskList){
            if (task.getMax_ui() >= Ut){
                Heavytask.add(task);
            }else {
                Normaltask.add(task);
            }
        }
        //首先对heavy使用CU-WFD进行分配
        if (!Heavytask.isEmpty()){
            int f = CU_WFD.CU_WFD_Partitioning(Heavytask,M_List);
            if (f == 0)
                return 0;
        }else {
            M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务
        }
        //对剩余的Normaltask利用回溯算法进行分配，这里的任务已经是根据最大利用率降序排序过的



        return 0;

    }

}
