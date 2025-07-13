package algorithms;

import entity.Task;
import utils.ArrayUtils;
import utils.MC_QPA;
import utils.SortUtils;

import java.util.List;

import static utils.StaticParameters.M;

public class CA_UDP {
    public static  int CA_UDP_Partitioning(List<Task> Task_HI, List<Task> Task_LO, List<Task>[] M_List){
        M_List = ArrayUtils.listArrayInitializationOfProcessor(M_List);//用于处理器中存放的任务
        double[] ULO = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的ULO剩余利用率
        double[] UHI = ArrayUtils.arrayInitializationOne();   //初始化，表示第i个处理器的UHI剩余利用率

        int count_HI = Task_HI.size();
        int count_LO = Task_LO.size();

        int position_HI = 0;
        int position_LO = 0;

        double uiLO, uiHI;  //任务i的利用率


        while (position_HI < count_HI || position_LO < count_LO) {//对任务进行分配

            if (position_HI < count_HI) {
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
                uiHI = Task_HI.get(position_HI).getUi_HI();
                uiLO = Task_HI.get(position_HI).getUi_LO();
                int i;
                for ( i = 1; i <= M; i++) {   //遍历处理器
                    if (UHI[i] >= uiHI && ULO[i] >= uiLO) {  //判断当前任务是否可放入该处理器

                        M_List[i].add(Task_HI.get(position_HI));  //把该任务放入处理器

                        if (MC_QPA.Dbf_Schedule(M_List[i]) == 1) {  //判断当前处理器是否满足可行性条件
                            UHI[i] -= uiHI;
                            ULO[i] -= uiLO;
                            position_HI++;
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
                //对处理器进行排序，按照此处理器上低关键层次低模式下的利用率-高模式下的利用率进行排序
                SortUtils.UDPSort_LO(M_List);
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
                uiHI = Task_LO.get(position_LO).getUi_HI();
                uiLO = Task_LO.get(position_LO).getUi_LO();
                int i;
                for ( i = 1; i <= M; i++){
                    if (UHI[i] >= uiHI && ULO[i] >= uiLO){
                        M_List[i].add(Task_LO.get(position_LO));
                        if (MC_QPA.Dbf_Schedule(M_List[i]) == 1){
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
