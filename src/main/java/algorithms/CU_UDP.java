package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.MC_QPA;
import utils.SortUtils;

import java.util.List;

import static utils.StaticParameters.M;

public class CU_UDP {
    public static  int CU_UDP_Partitioning(List<Task> taskList, List<Task>[] M_List){
        M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务
        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率

        int count = taskList.size();
        int position = 0;

        double uiLO, uiHI;  //任务i的利用率

        while (position < count) {
            //对处理器进行排序，按照此处理器上高关键层次高模式下的利用率-低模式下的利用率进行排序
            SortUtils.UDPSort(M_List);
            for (int i = 1; i <=M ; i++) {
                double Ulo = 0;
                double Uhi = 0;
                for (int j = 0; j < M_List[i].size(); j++) {
                    Ulo+= M_List[i].get(j).getUi_LO();//统计处理器已用的利用率
                    Uhi+= M_List[i].get(j).getUi_HI();
                }
                ULO[i] = 1 - Ulo;//记录剩余的可用利用率
                UHI[i] = 1 - Uhi;
            }

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
                        position++;
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
