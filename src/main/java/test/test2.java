package test;

import algorithms.*;
import entity.Task;
import utils.*;

import java.util.List;

import static utils.StaticParameters.*;

public class test2 {
    public static void main(String[] args) {
        CP = 0.5;
        n = 40;

        M = 4;
        double Ub;
        List<Task>[] M_List_CA_UDP = new List[M + 1];
        List<Task>[] M_List_CU_UDP = new List[M + 1];
        List<Task>[] M_List_CU_WFD_UDP = new List[M + 1];
        System.out.println("\tCA_UDP_EnergyConsumption\tCU_UDP_EnergyConsumption\tCA_UDP_f");
        for (Ub = 0.5; Ub < 0.91; Ub += 0.05){
            int count = 0;
            int CA_UDP_Feasibility = 0;
            int CU_UDP_Feasibility = 0;
            int CU_WFD_UDP_Feasibility = 0;
            double CA_UDP_Energy = 0;
            double CU_UDP_Energy = 0;
            double CU_WFD_UDP_Energy = 0;
            //修改为只能缩小利用率，放大的重新生成
            for (int i = 0; i < 100; i++){
                List<Task> Task_HI;
                List<Task> Task_LO;
                int flag = 0;
                do {
                    Task_HI = GenerateTasks.generateHiTasks();//
                    Task_LO = GenerateTasks.generateLOTasks();
                    flag = Utilization.utilization_Scaling(Ub * M, Task_HI, Task_LO);//只缩小不放大
                }while (flag==0);
                //Utilization.utilization_Scaling(Ub * M, Task_HI, Task_LO);
                SortUtils.UiHISort(Task_HI);//降序
                SortUtils.UiLOSort(Task_LO);//降序

                List<Task> taskList = SortUtils.maxUiMergeAndSort(Task_HI, Task_LO);
                int f_CA_UDP = 0;
                int f_CU_UDP = 0;
                int f_CU_WFD_UDP = 0;
                if (CA_UDP.CA_UDP_Partitioning(Task_HI, Task_LO, M_List_CA_UDP) == 1) {
                    CA_UDP_Feasibility++;
                    f_CA_UDP = MC_QPA.Dbf_Schedule(M_List_CA_UDP);
                }
                if (CU_UDP.CU_UDP_Partitioning(taskList, M_List_CU_UDP) == 1) {
                    CU_UDP_Feasibility++;
                    f_CU_UDP = MC_QPA.Dbf_Schedule(M_List_CU_UDP);
                }
//                if (CU_WFD_UDP.CU_WFD_UDP_Partitioning(taskList, M_List_CU_WFD_UDP) == 1) {
//                    CU_WFD_UDP_Feasibility++;
//                    f_CU_WFD_UDP = MC_QPA.Dbf_Schedule(M_List_CU_WFD_UDP);
//                }
                if (f_CU_UDP==1 &&f_CA_UDP==1){
                    CA_UDP_Energy += Energy_Caculate.Caculate_Energe(M_List_CA_UDP);
                    CU_UDP_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_UDP);
                    count++;
                }
            }
            //System.out.println("f="+CA_UDP_Feasibility / 100.0);
            //System.out.println(CU_UDP_Feasibility / 1000.0);
            //System.out.println(CU_WFD_UDP_Feasibility / 1000.0);
            System.out.println("\t"+CA_UDP_Energy / count + "\t" + CU_UDP_Energy / count + "\t"+CA_UDP_Feasibility / 100.0+"\t");
        }

    }
}
