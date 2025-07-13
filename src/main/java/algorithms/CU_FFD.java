package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.MC_QPA;

import java.util.List;

import static utils.StaticParameters.M;

public class CU_FFD {
    public static int CU_FFD_Partitioning(List<Task> taskList, List<Task>[] M_List) {

        M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务

        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率

        int count = taskList.size();
        int position = 0;

        double uiLO, uiHI;  //任务i的利用率

        while (position < count) {

            uiHI = taskList.get(position).getUi_HI();
            uiLO = taskList.get(position).getUi_LO();
            int num = taskList.get(position).getNum();

            int i;
            for (i = 1; i <= M; i++) {

                if (UHI[i] >= uiHI && ULO[i] >= uiLO) {

                    M_List[i].add(taskList.get(position));  //把该任务放入处理器
                    if (MC_QPA.Dbf_Schedule(M_List[i]) == 1) {  //判断当前处理器是否满足可行性条件)
                        UHI[i] -= uiHI;
                        ULO[i] -= uiLO;
                        //System.out.println(Task_HI.get(position_HI));
                        position++;
                        //System.out.println("处理器:" + i + " HI任务" + num + ":已放入");
                        break;
                    } else {
                        M_List[i].remove(taskList.get(position));    //从处理器中移除该任务
                    }

                }
            }
            if (i == M + 1) {
                //说明分配失败
                return 0;
            }
        }
        return 1;
    }


}
