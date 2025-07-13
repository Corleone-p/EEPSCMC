package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.MC_QPA;

import java.util.List;

import static utils.StaticParameters.M;

public class CU_BFD {
    public static int CU_BFD_Partitioning(List<Task> taskList, List<Task>[] M_List) {

        M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务

        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率

        int count = taskList.size();
        int position = 0;

        double uiLO, uiHI;  //任务i的利用率

        double min_residual;  //最大剩余利用率
        int min_M;   //最大剩余利用率的处理器
        int Li;
        while (position < count) {
            Li = taskList.get(position).getLi();
            uiHI = taskList.get(position).getUi_HI();
            uiLO = taskList.get(position).getUi_LO();
            int num = taskList.get(position).getNum();
            min_residual = 99;
            min_M = -1;
            for (int i = 1; i <= M; i++) {//遍历处理器
                if (UHI[i] >= uiHI && ULO[i] >= uiLO) {//判断当前任务是否可放入该处理器

                    M_List[i].add(taskList.get(position));  //把该任务放入处理器

                    if (MC_QPA.Dbf_Schedule(M_List[i]) == 1) {  //判断当前处理器是否满足可行性条件

                        M_List[i].remove(taskList.get(position));    //从处理器中移除该任务
                        if (Li == 1) {
                            if (min_residual > UHI[i] - uiHI) {//记录当前处理器利用率的剩余空间和最大剩余空间的处理器位置
                                min_residual = UHI[i] - uiHI;
                                min_M = i;
                            }
                        } else {
                            if (min_residual > ULO[i] - uiLO) {//记录当前处理器利用率的剩余空间和最大剩余空间的处理器位置
                                min_residual = ULO[i] - uiLO;
                                min_M = i;
                            }
                        }
                    } else {
                        M_List[i].remove(taskList.get(position));    //从处理器中移除该任务
                    }
                }
            }

            if (min_residual == 99) {
                return 0;
            } else {
                UHI[min_M] -= uiHI;
                ULO[min_M] -= uiLO;
                M_List[min_M].add(taskList.get(position)); //把任务放入该处理器中
                position++;
                //System.out.println("处理器:" + max_M + " 任务" + num + ":已放入");
            }
        }
        return 1;
    }
}
