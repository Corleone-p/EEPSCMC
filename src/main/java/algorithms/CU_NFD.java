package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.MC_QPA;

import java.util.List;

import static utils.StaticParameters.M;

public class CU_NFD {

    public static int CU_NFD_Partitioning(List<Task> taskList, List<Task>[] M_List) {

        M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务

        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率

        int count = taskList.size();
        int position = 0;

        double uiLO, uiHI;  //任务i的利用率

        int curr_processor = 1;   //当前处理器
        int count_M;         //当前遍历处理器的个数

        while (position < count) {

            uiHI = taskList.get(position).getUi_HI();
            uiLO = taskList.get(position).getUi_LO();
            count_M = 0;
            while (count_M < M) {

                if (UHI[curr_processor] >= uiHI && ULO[curr_processor] >= uiLO) {
                    M_List[curr_processor].add(taskList.get(position));  //把该任务放入处理器
                    if (MC_QPA.Dbf_Schedule(M_List[curr_processor]) == 1) {  //判断当前处理器是否满足可行性条件)
                        UHI[curr_processor] -= uiHI;
                        ULO[curr_processor] -= uiLO;
                        //System.out.println(Task_HI.get(position_HI));
                        position++;
                        //System.out.println("处理器:" + i + " HI任务" + num + ":已放入");
                        break;
                    } else {
                        M_List[curr_processor].remove(taskList.get(position));    //从处理器中移除该任务
                    }

                }
                count_M++;
                curr_processor = (curr_processor + 1) % M;
                if (curr_processor == 0)
                    curr_processor = M;
            }
            if (count_M == M) {
                return 0;
            }
        }

        return 1;
    }


}
