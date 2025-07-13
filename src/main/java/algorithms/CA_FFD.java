package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.MC_QPA;

import java.util.List;

import static utils.StaticParameters.M;

public class CA_FFD {


    public static int CA_FFD_Partitioning(List<Task> Task_HI, List<Task> Task_LO,List<Task>[] M_List) {

         M_List = ArrayUtils.listArrayInitializationOfProcessor( M_List);//用于处理器中存放的任务
        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率

        int count_HI = Task_HI.size();
        int count_LO = Task_LO.size();

        int position_HI = 0;
        int position_LO = 0;

        double uiLO, uiHI;  //任务i的利用率


        while (position_HI < count_HI || position_LO < count_LO) {

            if (position_HI < count_HI) {

                uiHI = Task_HI.get(position_HI).getUi_HI();
                uiLO = Task_HI.get(position_HI).getUi_LO();
                int num = Task_HI.get(position_HI).getNum();

                int i;
                for (i = 1; i <= M; i++) {
                    if (UHI[i] >= uiHI && ULO[i] >= uiLO) {

                        M_List[i].add(Task_HI.get(position_HI));  //把该任务放入处理器
                        /**
                         * 修改这个条件
                         * **/
                        if (MC_QPA.Dbf_Schedule(M_List[i]) == 1) {  //判断当前处理器是否满足可行性条件
                            UHI[i] -= uiHI;
                            ULO[i] -= uiLO;
                            //System.out.println(Task_HI.get(position_HI));
                            position_HI++;
                            //System.out.println("处理器:" + i + " HI任务" + num + ":已放入");
                            break;
                        } else {
                            M_List[i].remove(Task_HI.get(position_HI));    //从处理器中移除该任务
                        }

                    }
                }
                if (i == M + 1) {
                    //说明分配失败
                    return 0;
                }


            } else if (position_LO < count_LO) {
                uiHI = Task_LO.get(position_LO).getUi_HI();
                uiLO = Task_LO.get(position_LO).getUi_LO();
                int num = Task_LO.get(position_LO).getNum();
                int i;
                for (i = 1; i <= M; i++) {
                    if (UHI[i] >= uiHI && ULO[i] >= uiLO) {
                        M_List[i].add(Task_LO.get(position_LO));  //把该任务放入处理器
                        if (MC_QPA.Dbf_Schedule(M_List[i]) == 1) {  //判断当前处理器是否满足可行性条件)
                            UHI[i] -= uiHI;
                            ULO[i] -= uiLO;
                            position_LO++;
                            //System.out.println("处理器:" + i + " LO任务" + num + "：已放入");
                            break;
                        }else {
                            M_List[i].remove(Task_LO.get(position_LO));    //从处理器中移除该任务
                        }

                    }
                }
                if (i == M + 1) {
                    //说明分配失败
                    return 0;
                }
            }
        }

        return 1;
    }
}
