package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.MC_QPA;

import java.util.ArrayList;
import java.util.List;

import static utils.StaticParameters.M;

public class Grouping {
    public static int Grouping_Partitioning(List<Task> taskList, List<Task>[] M_List){
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
            int f = MinSumEnergy.MinsumPartitioning(Heavytask,M_List);
            if (f == 0)
                return 0;
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

        //对正常任务进行分组，两个两个一组
        List<Task> Normaltask_lo = new ArrayList<>();
        List<Task> Normaltask_hi = new ArrayList<>();
        for (Task task:Normaltask){
            if (task.getLi()==1){
                Normaltask_hi.add(task);
            }else {
                Normaltask_lo.add(task);
            }
        }
        int count_HI = Normaltask_hi.size();
        int count_LO = Normaltask_lo.size();
        int position_HI = 0;
        int position_LO = 0;
        int flag = 1;
        double uiLO, uiHI;  //任务i的利用率
        double max_residual;  //最大剩余利用率
        int max_M;   //最大剩余利用率的处理器
        int current_p = 1;
        while (position_HI < count_HI && position_LO < count_LO){
            if (flag > 0){//分配高的
                uiHI = Normaltask_hi.get(position_HI).getUi_HI();
                uiLO = Normaltask_hi.get(position_HI).getUi_LO();
                int num = Normaltask_hi.get(position_HI).getNum();
                max_residual = -1;
                max_M = -1;
                for (int i = 1; i <= M; i++){//遍历处理器将该任务进行分配
                    if (UHI[i] >= uiHI && ULO[i] >= uiLO && UHI[i]-uiHI>=Normaltask_lo.get(position_LO).getUi_HI() && ULO[i]-uiLO>=Normaltask_lo.get(position_LO).getUi_LO()){//如果当前可放入该处理器
                        M_List[i].add(Normaltask_hi.get(position_HI));  //把该任务放入处理器

                        if (MC_QPA.Dbf_Schedule(M_List[i]) == 1){
                            M_List[i].remove(Normaltask_hi.get(position_HI));    //从处理器中移除该任务
                            if (max_residual < UHI[i] - uiHI) {//记录当前处理器利用率的剩余空间和最大剩余空间的处理器位置
                                max_residual = UHI[i] - uiHI;
                                max_M = i;
                            }
                        } else {
                            M_List[i].remove(Normaltask_hi.get(position_HI));    //从处理器中移除该任务
                        }
                    }
                }
                if (max_residual == -1) {
                    break;
                } else {
                    UHI[max_M] -= uiHI;
                    ULO[max_M] -= uiLO;
                    current_p = max_M;
                    M_List[max_M].add(Normaltask_hi.get(position_HI)); //把任务放入该处理器中
                    position_HI++;
                    //System.out.println("处理器:" + max_M + " HI任务" + num + ":已放入");
                }
            }else {
                //分配低的
                uiHI = Normaltask_lo.get(position_LO).getUi_HI();
                uiLO = Normaltask_lo.get(position_LO).getUi_LO();
                int count_M = 0;
                while (count_M < M) {
                    if (UHI[current_p] >= uiHI && ULO[current_p] >= uiLO) {
                        M_List[current_p].add(Normaltask_lo.get(position_LO));  //把该任务放入处理器
                        if (MC_QPA.Dbf_Schedule(M_List[current_p]) == 1) {  //判断当前处理器是否满足可行性条件)
                            UHI[current_p] -= uiHI;
                            ULO[current_p] -= uiLO;
                            position_LO++;
                            //System.out.println("处理器:" + i + " LO任务" + num + "：已放入");
                            break;
                        }else {
                            M_List[current_p].remove(Normaltask_lo.get(position_LO));    //从处理器中移除该任务
                        }
                    }
                    count_M++;
                    current_p = (current_p + 1) % M;
                    if (current_p == 0)
                        current_p = M;
                }
                if (count_M == M) {
                    return 0;
                }
            }
            flag *= -1;

        }
        while (position_HI < count_HI){
            uiHI = Normaltask_hi.get(position_HI).getUi_HI();
            uiLO = Normaltask_hi.get(position_HI).getUi_LO();
            int num = Normaltask_hi.get(position_HI).getNum();
            max_residual = -1;
            max_M = -1;
            for (int i = 1; i <= M; i++){//遍历处理器将该任务进行分配
                if (UHI[i] >= uiHI && ULO[i] >= uiLO){//如果当前可放入该处理器
                    M_List[i].add(Normaltask_hi.get(position_HI));  //把该任务放入处理器

                    if (MC_QPA.Dbf_Schedule(M_List[i]) == 1){
                        M_List[i].remove(Normaltask_hi.get(position_HI));    //从处理器中移除该任务
                        if (max_residual < UHI[i] - uiHI) {//记录当前处理器利用率的剩余空间和最大剩余空间的处理器位置
                            max_residual = UHI[i] - uiHI;
                            max_M = i;
                        }
                    } else {
                        M_List[i].remove(Normaltask_hi.get(position_HI));    //从处理器中移除该任务
                    }

                }
            }
            if (max_residual == -1) {
                return 0;
            } else {
                UHI[max_M] -= uiHI;
                ULO[max_M] -= uiLO;
                M_List[max_M].add(Normaltask_hi.get(position_HI)); //把任务放入该处理器中
                position_HI++;
                //System.out.println("处理器:" + max_M + " HI任务" + num + ":已放入");
            }
        }
        while (position_LO < count_LO){
            uiHI = Normaltask_lo.get(position_LO).getUi_HI();
            uiLO = Normaltask_lo.get(position_LO).getUi_LO();
            int num = Normaltask_lo.get(position_LO).getNum();
            max_residual = -1;
            max_M = -1;
            for (int i = 1; i <= M; i++) {
                if (UHI[i] >= uiHI && ULO[i] >= uiLO) {

                    M_List[i].add(Normaltask_lo.get(position_LO));  //把该任务放入处理器

                    if (MC_QPA.Dbf_Schedule(M_List[i]) == 1) {  //判断当前处理器是否满足可行性条件

                        M_List[i].remove(Normaltask_lo.get(position_LO));    //从处理器中移除该任务

                        if (max_residual < ULO[i] - uiLO) {
                            max_residual = ULO[i] - uiLO;
                            max_M = i;
                        }
                    } else {
                        M_List[i].remove(Normaltask_lo.get(position_LO));    //从处理器中移除该任务
                    }

                }
            }
            if (max_residual == -1) {
                return 0;
            } else {
                UHI[max_M] -= uiHI;
                ULO[max_M] -= uiLO;
                M_List[max_M].add(Normaltask_lo.get(position_LO)); //把任务放入该处理器中
                position_LO++;
                //System.out.println("处理器:" + max_M + " LO任务" + num + ":已放入");
            }
        }
        return 1;
    }
}
