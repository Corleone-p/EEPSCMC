package test;

import algorithms.CA_WFD;
import algorithms.CU_WFD;
import algorithms_new.*;
import entity.Task;
import utils.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import static utils.StaticParameters.*;
import static utils.StaticParameters.M;

public class test_new_M {
    public static void main(String[] args) throws IOException {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);//定义输出能耗的精度,小数点后4位,可按需求修改

        CP = 0.5;
        n = 40;
        double Ub;
        double t = 100.0;
        WriteFileTool.write("algonew_M.txt");
                System.out.println("CA_BFD_FFD_F\tCA_BFD_NFD_F\tCA_BFD_WFD_F\tCA_FFD_BFD_F\tCA_FFD_NFD_F\tCA_FFD_WFD_F" +
                "\tCA_NFD_BFD_F\tCA_NFD_FFD_F\tCA_NFD_WFD_F\tCA_WFD_BFD_F\tCA_WFD_FFD_F\tCA_WFD_NFD_F\tCA_WFD_F\tCU_WFD_F");

//        System.out.println("CA_BFD_FFD_E\tCA_BFD_NFD_E\tCA_BFD_WFD_E\tCA_FFD_BFD_E\tCA_FFD_NFD_E\tCA_FFD_WFD_E" +
//                "\tCA_NFD_BFD_E\tCA_NFD_FFD_E\tCA_NFD_WFD_E\tCA_WFD_BFD_E\tCA_WFD_FFD_E\tCA_WFD_NFD_E\tCA_WFD_E\tCU_WFD_E");
        double weight = 0;
        for (Ub = 0.5; Ub < 0.91; Ub += 0.05) {
            weight += Ub * t;
        }
        for (M = 4; M <= 16; M++){
            List<Task>[] M_List_CA_BFD_FFD = new List[M + 1];
            List<Task>[] M_List_CA_BFD_NFD = new List[M + 1];
            List<Task>[] M_List_CA_BFD_WFD = new List[M + 1];
            List<Task>[] M_List_CA_FFD_NFD = new List[M + 1];
            List<Task>[] M_List_CA_FFD_WFD = new List[M + 1];
            List<Task>[] M_List_CA_FFD_BFD = new List[M + 1];
            List<Task>[] M_List_CA_NFD_FFD = new List[M + 1];
            List<Task>[] M_List_CA_NFD_BFD = new List[M + 1];
            List<Task>[] M_List_CA_NFD_WFD = new List[M + 1];
            List<Task>[] M_List_CA_WFD_FFD = new List[M + 1];
            List<Task>[] M_List_CA_WFD_BFD = new List[M + 1];
            List<Task>[] M_List_CA_WFD_NFD = new List[M + 1];
            List<Task>[] M_List_CA_WFD = new List[M + 1];
            List<Task>[] M_List_CU_WFD = new List[M + 1];
            int count = 0;
            double CA_BFD_FFD_E = 0;
            double CA_BFD_NFD_E = 0;
            double CA_BFD_WFD_E = 0;
            double CA_FFD_BFD_E = 0;
            double CA_FFD_NFD_E = 0;
            double CA_FFD_WFD_E = 0;
            double CA_NFD_BFD_E = 0;
            double CA_NFD_FFD_E = 0;
            double CA_NFD_WFD_E = 0;
            double CA_WFD_BFD_E = 0;
            double CA_WFD_FFD_E = 0;
            double CA_WFD_NFD_E = 0;
            double CA_WFD_E = 0;
            double CU_WFD_E = 0;

            double CA_BFD_FFD_W = 0;
            double CA_BFD_NFD_W = 0;
            double CA_BFD_WFD_W = 0;
            double CA_FFD_BFD_W = 0;
            double CA_FFD_NFD_W = 0;
            double CA_FFD_WFD_W = 0;
            double CA_NFD_BFD_W = 0;
            double CA_NFD_FFD_W = 0;
            double CA_NFD_WFD_W = 0;
            double CA_WFD_BFD_W = 0;
            double CA_WFD_FFD_W = 0;
            double CA_WFD_NFD_W = 0;
            double CA_WFD_W = 0;
            double CU_WFD_W = 0;
            for (Ub = 0.5; Ub < 0.91; Ub += 0.05) {
                int CA_BFD_FFD_F = 0;
                int CA_BFD_NFD_F = 0;
                int CA_BFD_WFD_F = 0;
                int CA_FFD_BFD_F = 0;
                int CA_FFD_NFD_F = 0;
                int CA_FFD_WFD_F = 0;
                int CA_NFD_BFD_F = 0;
                int CA_NFD_FFD_F = 0;
                int CA_NFD_WFD_F = 0;
                int CA_WFD_BFD_F = 0;
                int CA_WFD_FFD_F = 0;
                int CA_WFD_NFD_F = 0;
                int CA_WFD_F = 0;
                int CU_WFD_F = 0;

                for (int i = 0; i < t; i++){
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
                    int f_CA_BFD_FFD = 0, f_CA_BFD_NFD = 0, f_CA_BFD_WFD = 0, f_CA_FFD_BFD = 0, f_CA_FFD_NFD = 0, f_CA_FFD_WFD = 0,
                            f_CA_NFD_BFD = 0, f_CA_NFD_FFD = 0,f_CA_NFD_WFD = 0,f_CA_WFD_BFD = 0,f_CA_WFD_FFD = 0,f_CA_WFD_NFD = 0,f_CA_WFD = 0,f_CU_WFD = 0;
                    if (CA_BFD_FFD.CA_BFD_FFD_Partitioning(Task_HI, Task_LO, M_List_CA_BFD_FFD) == 1) {
                        CA_BFD_FFD_F++;
                        f_CA_BFD_FFD = MC_QPA.Dbf_Schedule(M_List_CA_BFD_FFD);
                    }
                    if(CA_BFD_NFD.CA_BFD_NFD_Partitioning(Task_HI,Task_LO,M_List_CA_BFD_NFD)==1){
                        CA_BFD_NFD_F++;
                        f_CA_BFD_NFD = MC_QPA.Dbf_Schedule(M_List_CA_BFD_NFD);
                    }
                    if(CA_BFD_WFD.CA_BFD_WFD_Partitioning(Task_HI,Task_LO,M_List_CA_BFD_WFD)==1){
                        CA_BFD_WFD_F++;
                        f_CA_BFD_WFD = MC_QPA.Dbf_Schedule(M_List_CA_BFD_WFD);
                    }
                    //
                    if(CA_FFD_BFD.CA_FFD_BFD_Partitioning(Task_HI,Task_LO,M_List_CA_FFD_BFD)==1){
                        CA_FFD_BFD_F++;
                        f_CA_FFD_BFD = MC_QPA.Dbf_Schedule(M_List_CA_FFD_BFD);
                    }
                    if(CA_FFD_WFD.CA_FFD_WFD_Partitioning(Task_HI,Task_LO,M_List_CA_FFD_WFD)==1){
                        CA_FFD_WFD_F++;
                        f_CA_FFD_WFD = MC_QPA.Dbf_Schedule(M_List_CA_FFD_WFD);
                    }
                    if(CA_FFD_NFD.CA_FFD_NFD_Partitioning(Task_HI,Task_LO,M_List_CA_FFD_NFD)==1){
                        CA_FFD_NFD_F++;
                        f_CA_FFD_NFD = MC_QPA.Dbf_Schedule(M_List_CA_FFD_NFD);
                    }
                    //
                    if(CA_NFD_BFD.CA_NFD_BFD_Partitioning(Task_HI,Task_LO,M_List_CA_NFD_BFD)==1){
                        CA_NFD_BFD_F++;
                        f_CA_NFD_BFD = MC_QPA.Dbf_Schedule(M_List_CA_NFD_BFD);
                    }
                    if(CA_NFD_WFD.CA_NFD_WFD_Partitioning(Task_HI,Task_LO,M_List_CA_NFD_WFD)==1){
                        CA_NFD_WFD_F++;
                        f_CA_NFD_WFD = MC_QPA.Dbf_Schedule(M_List_CA_NFD_WFD);
                    }
                    if(CA_NFD_FFD.CA_NFD_FFD_Partitioning(Task_HI,Task_LO,M_List_CA_NFD_FFD)==1){
                        CA_NFD_FFD_F++;
                        f_CA_NFD_FFD = MC_QPA.Dbf_Schedule(M_List_CA_NFD_FFD);
                    }
                    //
                    if(CA_WFD_FFD.CA_WFD_FFD_Partitioning(Task_HI,Task_LO,M_List_CA_WFD_FFD)==1){
                        CA_WFD_FFD_F++;
                        f_CA_WFD_FFD = MC_QPA.Dbf_Schedule(M_List_CA_WFD_FFD);
                    }
                    if(CA_WFD_BFD.CA_WFD_BFD_Partitioning(Task_HI,Task_LO,M_List_CA_WFD_BFD)==1){
                        CA_WFD_BFD_F++;
                        f_CA_WFD_BFD = MC_QPA.Dbf_Schedule(M_List_CA_WFD_BFD);
                    }
                    if(CA_WFD_NFD.CA_WFD_NFD_Partitioning(Task_HI,Task_LO,M_List_CA_WFD_NFD)==1){
                        CA_WFD_NFD_F++;
                        f_CA_WFD_NFD = MC_QPA.Dbf_Schedule(M_List_CA_WFD_NFD);
                    }
                    //
                    if(CA_WFD.CA_WFD_Partitioning(Task_HI,Task_LO,M_List_CA_WFD)==1){
                        CA_WFD_F++;
                        f_CA_WFD = MC_QPA.Dbf_Schedule(M_List_CA_WFD);
                    }
                    if(CU_WFD.CU_WFD_Partitioning(taskList, M_List_CU_WFD) == 1){
                        CU_WFD_F++;
                        f_CU_WFD = MC_QPA.Dbf_Schedule(M_List_CU_WFD);
                    }

                    if (f_CA_BFD_FFD == 1 && f_CA_BFD_NFD == 1 && f_CA_BFD_WFD == 1 && f_CA_FFD_BFD == 1 && f_CA_FFD_NFD == 1 && f_CA_FFD_WFD == 1 &&
                            f_CA_NFD_BFD == 1 &&f_CA_NFD_FFD == 1 &&f_CA_NFD_WFD == 1 &&f_CA_WFD_BFD == 1 &&f_CA_WFD_FFD == 1 &&f_CA_WFD_NFD == 1 &&f_CA_WFD == 1 &&f_CU_WFD == 1) {
                        CA_BFD_FFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_BFD_FFD);
                        CA_BFD_NFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_BFD_NFD);
                        CA_BFD_WFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_BFD_WFD);

                        CA_WFD_FFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_WFD_FFD);
                        CA_WFD_BFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_WFD_BFD);
                        CA_WFD_NFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_WFD_NFD);

                        CA_NFD_FFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_NFD_FFD);
                        CA_NFD_BFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_NFD_BFD);
                        CA_NFD_WFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_NFD_WFD);

                        CA_FFD_NFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_FFD_NFD);
                        CA_FFD_BFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_FFD_BFD);
                        CA_FFD_WFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_FFD_WFD);

                        CA_WFD_E += Energy_Caculate.Caculate_Energe(M_List_CA_WFD);
                        CU_WFD_E += Energy_Caculate.Caculate_Energe(M_List_CU_WFD);
                        count++;
                    }
                }
                CA_BFD_FFD_W = CA_BFD_FFD_F * Ub / weight * 1.0;
                CA_BFD_NFD_W = CA_BFD_NFD_F * Ub / weight * 1.0;
                CA_BFD_WFD_W = CA_BFD_WFD_F * Ub / weight * 1.0;
                CA_FFD_BFD_W = CA_FFD_BFD_F * Ub / weight * 1.0;
                CA_FFD_NFD_W = CA_FFD_NFD_F * Ub / weight * 1.0;
                CA_FFD_WFD_W = CA_FFD_WFD_F * Ub / weight * 1.0;
                CA_NFD_BFD_W = CA_NFD_BFD_F * Ub / weight * 1.0;
                CA_NFD_FFD_W = CA_NFD_FFD_F * Ub / weight * 1.0;
                CA_NFD_WFD_W = CA_NFD_WFD_F * Ub / weight * 1.0;
                CA_WFD_BFD_W = CA_WFD_BFD_F * Ub / weight * 1.0;
                CA_WFD_FFD_W = CA_WFD_FFD_F * Ub / weight * 1.0;
                CA_WFD_NFD_W = CA_WFD_NFD_F * Ub / weight * 1.0;
                CA_WFD_W = CA_WFD_F * Ub / weight * 1.0;
                CU_WFD_W = CU_WFD_F * Ub / weight * 1.0;
            }
            System.out.println(nf.format(CA_BFD_FFD_W) + "\t" + nf.format(CA_BFD_NFD_W) + "\t" + nf.format(CA_BFD_WFD_W) + "\t" + nf.format(CA_FFD_BFD_W) + "\t" + nf.format(CA_FFD_NFD_W) +
                    "\t" + nf.format(CA_FFD_WFD_W) + "\t" + nf.format(CA_NFD_BFD_W) + "\t" + nf.format(CA_NFD_FFD_W) + "\t" + nf.format(CA_NFD_WFD_W) + "\t" +nf.format(CA_WFD_BFD_W) + "\t"
                    +nf.format(CA_WFD_FFD_W) +"\t" +nf.format(CA_WFD_NFD_W)+"\t" +nf.format(CA_WFD_W)+"\t" +nf.format(CU_WFD_W));
//          System.out.println(nf.format(CA_BFD_FFD_E / count) + "\t" + nf.format(CA_BFD_NFD_E / count) + "\t" + nf.format(CA_BFD_WFD_E / count) + "\t" + nf.format(CA_FFD_BFD_E / count) + "\t" + nf.format(CA_FFD_NFD_E / count) +
//                            "\t" + nf.format(CA_FFD_WFD_E / count) + "\t" + nf.format(CA_NFD_BFD_E / count) + "\t" + nf.format(CA_NFD_FFD_E / count) + "\t" + nf.format(CA_NFD_WFD_E / count) + "\t" +nf.format(CA_WFD_BFD_E/ count) + "\t"
//                            +nf.format(CA_WFD_FFD_E / count) +"\t" +nf.format(CA_WFD_NFD_E / count)+"\t" +nf.format(CA_WFD_E / count)+"\t" +nf.format(CU_WFD_E / count));

        }
    }
}
