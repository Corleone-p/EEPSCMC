package algorithms_new;

import entity.Task;
import utils.ArrayUtils;
import utils.MC_QPA;

import java.util.List;

import static utils.StaticParameters.M;

/***
 * 先高后低，高关键层次任务用BFD，低关键层次任务采用WFD
 * **/
public class CA_BFD_WFD {
    public static int CA_BFD_WFD_Partitioning(List<Task> Task_HI, List<Task> Task_LO, List<Task>[] M_List){
        M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务
        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率

        int count_HI = Task_HI.size();
        int count_LO = Task_LO.size();

        int position_HI = 0;
        int position_LO = 0;

        double uiLO, uiHI;  //任务i的利用率

        double min_residual;  //最大剩余利用率
        int min_M;   //最大剩余利用率的处理器
        while (position_HI < count_HI || position_LO < count_LO){
            //高关键层次任务采用BFD
            if (position_HI < count_HI) {
                uiHI = Task_HI.get(position_HI).getUi_HI();
                uiLO = Task_HI.get(position_HI).getUi_LO();
                //int num = Task_HI.get(position_HI).getNum();
                min_residual = 99;
                min_M = 0;
                for (int i = 1; i <= M; i++) {   //遍历处理器
                    if (UHI[i] >= uiHI && ULO[i] >= uiLO) {  //判断当前任务是否可放入该处理器

                        M_List[i].add(Task_HI.get(position_HI));  //把该任务放入处理器

                        if (MC_QPA.Dbf_Schedule(M_List[i]) == 1) {  //判断当前处理器是否满足可行性条件

                            M_List[i].remove(Task_HI.get(position_HI));    //从处理器中移除该任务

                            if (min_residual > UHI[i] - uiHI) {             //记录当前处理器利用率的剩余空间和最小剩余空间的处理器位置
                                min_residual = UHI[i] - uiHI;
                                min_M = i;
                            }
                        } else {
                            M_List[i].remove(Task_HI.get(position_HI));    //从处理器中移除该任务
                        }

                    }
                }
                if (min_residual == 99) {
                    return 0;
                } else {
                    UHI[min_M] -= uiHI;
                    ULO[min_M] -= uiLO;
                    M_List[min_M].add(Task_HI.get(position_HI)); //把任务放入该处理器中
                    position_HI++;
                    //System.out.println("处理器:" + min_M + " HI任务" + num + ":已放入");
                }

            }//低关键层次任务采用WFD
            else if (position_LO < count_LO) {

                uiHI = Task_LO.get(position_LO).getUi_HI();
                uiLO = Task_LO.get(position_LO).getUi_LO();
                int num = Task_LO.get(position_LO).getNum();
                double max_residual = -1;
                int max_M = -1;
                for (int i = 1; i <= M; i++) {
                    if (UHI[i] >= uiHI && ULO[i] >= uiLO) {

                        M_List[i].add(Task_LO.get(position_LO));  //把该任务放入处理器

                        if (MC_QPA.Dbf_Schedule(M_List[i]) == 1) {  //判断当前处理器是否满足可行性条件

                            M_List[i].remove(Task_LO.get(position_LO));    //从处理器中移除该任务

                            if (max_residual < ULO[i] - uiLO) {
                                max_residual = ULO[i] - uiLO;
                                max_M = i;
                            }
                        } else {
                            M_List[i].remove(Task_LO.get(position_LO));    //从处理器中移除该任务
                        }

                    }
                }
                if (max_residual == -1) {
                    return 0;
                } else {
                    UHI[max_M] -= uiHI;
                    ULO[max_M] -= uiLO;
                    M_List[max_M].add(Task_LO.get(position_LO)); //把任务放入该处理器中
                    position_LO++;
                    //System.out.println("处理器:" + max_M + " LO任务" + num + ":已放入");
                }
            }
        }
        return 1;
    }
}
