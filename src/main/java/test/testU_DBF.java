package test;

import algorithms.*;
import entity.Task;
import utils.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import static utils.StaticParameters.*;
import static utils.StaticParameters.M;

public class testU_DBF {
    public static void main(String[] args) throws IOException {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);//定义输出能耗的精度,小数点后4位,可按需求修改

        CP = 0.5;
        n = 40;

        M = 4;
        double Ub;
        WriteFileTool.write("udpnew.txt");
        System.out.println("CA_FFD_Feasibility\tCA_BFD_Feasibility\tCA_WFD_Feasibility\tCA_NFD_Feasibility\tCU_WFD_Feasibility\tCU_BFD_Feasibility\tCU_FFD_Feasibility\tCU_NFD_Feasibility\tCU_UDP_Feasibility");

//        System.out.println("\tCA_FFD_EnergyConsumption\tCA_BFD_EnergyConsumption\tCA_WFD_EnergyConsumption\tCA_NFD_EnergyConsumption"
//                +"\tCU_FFD_EnergyConsumption\tCU_BFD_EnergyConsumption\tCU_WFD_EnergyConsumption\tCU_NFD_EnergyConsumption\tCU_UDP_EnergyConsumption");
        List<Task>[] M_List_CA_FFD = new List[M + 1];
        List<Task>[] M_List_CA_BFD = new List[M + 1];
        List<Task>[] M_List_CA_WFD = new List[M + 1];
        List<Task>[] M_List_CA_NFD = new List[M + 1];
        List<Task>[] M_List_CU_WFD = new List[M + 1];
        List<Task>[] M_List_CU_BFD = new List[M + 1];
        List<Task>[] M_List_CU_FFD = new List[M + 1];
        List<Task>[] M_List_CU_NFD = new List[M + 1];
        List<Task>[] M_List_CU_UDP = new List[M + 1];
        for (Ub = 0.5; Ub < 0.91; Ub += 0.05) {
            int count = 0;

            int CA_FFD_Feasibility = 0;
            int CA_BFD_Feasibility = 0;
            int CA_WFD_Feasibility = 0;
            int CA_NFD_Feasibility = 0;
            int CU_WFD_Feasibility = 0;
            int CU_BFD_Feasibility = 0;
            int CU_FFD_Feasibility = 0;
            int CU_NFD_Feasibility = 0;
            int CU_UDP_Feasibility = 0;

            double CA_FFD_Energy = 0;
            double CA_BFD_Energy = 0;
            double CA_WFD_Energy = 0;
            double CA_NFD_Energy = 0;
            double CU_WFD_Energy = 0;
            double CU_BFD_Energy = 0;
            double CU_FFD_Energy = 0;
            double CU_NFD_Energy = 0;
            double CU_UDP_Energy = 0;
            for (int i = 0; i < 100; i++) {
                //修改为只能缩小利用率，放大的重新生成
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

                int f_CA_FFD = 0, f_CA_BFD = 0, f_CA_WFD = 0, f_CA_NFD = 0, f_CU_WFD = 0, f_CU_BFD = 0, f_CU_FFD = 0, f_CU_NFD = 0,f_CU_UDP = 0;


                if (CA_FFD.CA_FFD_Partitioning(Task_HI, Task_LO, M_List_CA_FFD) == 1) {
                    CA_FFD_Feasibility++;
                    f_CA_FFD = MC_QPA.Dbf_Schedule(M_List_CA_FFD);
                }
                if (CA_BFD.CA_BFD_Partitioning(Task_HI, Task_LO, M_List_CA_BFD) == 1) {
                    CA_BFD_Feasibility++;
                    f_CA_BFD = MC_QPA.Dbf_Schedule(M_List_CA_BFD);

                }
                if (CA_WFD.CA_WFD_Partitioning(Task_HI, Task_LO, M_List_CA_WFD) == 1) {
                    CA_WFD_Feasibility++;
                    f_CA_WFD = MC_QPA.Dbf_Schedule(M_List_CA_WFD);

                }
                if (CU_WFD.CU_WFD_Partitioning(taskList, M_List_CU_WFD) == 1) {
                    CU_WFD_Feasibility++;
                    f_CU_WFD = MC_QPA.Dbf_Schedule(M_List_CU_WFD);
                }
                if (CA_NFD.CA_NFD_Partitioning(Task_HI, Task_LO, M_List_CA_NFD) == 1) {
                    CA_NFD_Feasibility++;
                    f_CA_NFD = MC_QPA.Dbf_Schedule(M_List_CA_NFD);
                }
                if (CU_BFD.CU_BFD_Partitioning(taskList, M_List_CU_BFD) == 1) {
                    CU_BFD_Feasibility++;
                    f_CU_BFD = MC_QPA.Dbf_Schedule(M_List_CU_BFD);
                }
                if (CU_FFD.CU_FFD_Partitioning(taskList, M_List_CU_FFD) == 1) {
                    CU_FFD_Feasibility++;
                    f_CU_FFD = MC_QPA.Dbf_Schedule(M_List_CU_FFD);
                }
                if (CU_NFD.CU_NFD_Partitioning(taskList, M_List_CU_NFD) == 1) {
                    CU_NFD_Feasibility++;
                    f_CU_NFD = MC_QPA.Dbf_Schedule(M_List_CU_NFD);
                }
                if (CU_UDP.CU_UDP_Partitioning(taskList, M_List_CU_UDP) == 1) {
                    CU_UDP_Feasibility++;
                    f_CU_UDP = MC_QPA.Dbf_Schedule(M_List_CU_UDP);
                }
                if (f_CA_FFD == 1 && f_CA_BFD == 1 && f_CA_WFD == 1 && f_CA_NFD == 1 && f_CU_WFD == 1 && f_CU_BFD == 1 && f_CU_FFD == 1 && f_CU_NFD == 1 && f_CU_UDP==1) {
                    CA_FFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CA_FFD);
                    CA_BFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CA_BFD);
                    CA_WFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CA_WFD);
                    CA_NFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CA_NFD);

                    CU_FFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_FFD);
                    CU_BFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_BFD);
                    CU_WFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_WFD);
                    CU_NFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_NFD);
                    CU_UDP_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_UDP);
                    count++;
                }

            }
            //System.out.println("Ub=" + nf.format(Ub));
            System.out.println(nf.format(CA_FFD_Feasibility / 100.0) + "\t" + nf.format(CA_BFD_Feasibility / 100.0) + "\t" + nf.format(CA_WFD_Feasibility / 100.0) + "\t" + nf.format(CA_NFD_Feasibility / 100.0) + "\t" + nf.format(CU_WFD_Feasibility / 100.0) +
                    "\t" + nf.format(CU_BFD_Feasibility / 100.0) + "\t" + nf.format(CU_FFD_Feasibility / 100.0) + "\t" + nf.format(CU_NFD_Feasibility / 100.0) + "\t" + nf.format(CU_UDP_Feasibility / 100.0) + "\t" );
//
//            System.out.println("\t"+nf.format(CA_FFD_Energy / count) + "\t" + nf.format(CA_BFD_Energy / count) + "\t" + nf.format(CA_WFD_Energy / count) + "\t" + nf.format(CA_NFD_Energy / count)+ "\t"
//                    +nf.format(CU_FFD_Energy / count) + "\t" + nf.format(CU_BFD_Energy / count) + "\t" + nf.format(CU_WFD_Energy / count) + "\t" + nf.format(CU_NFD_Energy / count )+ "\t" + nf.format(CU_UDP_Energy / count )+ "\t");
        }


    }
}
