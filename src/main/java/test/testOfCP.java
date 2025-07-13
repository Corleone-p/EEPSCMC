package test;

import algorithms.*;
import entity.Task;
import utils.GenerateTasks;
import utils.SortUtils;
import utils.Utilization;

import java.text.NumberFormat;
import java.util.List;

import static utils.StaticParameters.*;
import static utils.StaticParameters.M;

public class testOfCP {
    public static void main(String[] args) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);//定义输出能耗的精度,小数点后4位,可按需求修改

        CP = 0.5;
        n = 40;
        double Ub;
        M=4;
        System.out.println("CA_FFD_Feasibility\tCA_BFD_Feasibility\tCA_WFD_Feasibility\tCA_NFD_Feasibility\tCU_WFD_Feasibility\tCU_BFD_Feasibility\tCU_NFD_Feasibility\tCU_FFD_Feasibility"
                // +"\tCA_FFD_EnergyConsumption\tCA_BFD_EnergyConsumption\tCA_WFD_EnergyConsumption\tCA_NFD_EnergyConsumption"
        );

        double weight = 0;
        for (Ub = 0.5; Ub < 0.91; Ub += 0.05) {
            weight += Ub * 1000;
        }
        List<Task>[] M_List_CA_FFD = new List[M + 1];
        List<Task>[] M_List_CA_BFD = new List[M + 1];
        List<Task>[] M_List_CA_WFD = new List[M + 1];
        List<Task>[] M_List_CA_NFD = new List[M + 1];
        List<Task>[] M_List_CU_WFD = new List[M + 1];
        List<Task>[] M_List_CU_BFD = new List[M + 1];
        List<Task>[] M_List_CU_FFD = new List[M + 1];
        List<Task>[] M_List_CU_NFD = new List[M + 1];
        for (CP = 0.1; CP <= 0.91; CP+=0.1) {

            int count = 0;
            double CA_FFD_Energy = 0;
            double CA_BFD_Energy = 0;
            double CA_WFD_Energy = 0;
            double CA_NFD_Energy = 0;

            double CA_FFD_weight = 0;
            double CA_BFD_weight = 0;
            double CA_WFD_weight = 0;
            double CA_NFD_weight = 0;
            double CU_WFD_weight = 0;
            double CU_BFD_weight = 0;
            double CU_FFD_weight = 0;
            double CU_NFD_weight = 0;

            for (Ub = 0.5; Ub < 0.91; Ub += 0.05) {

                int CA_FFD_Feasibility = 0;
                int CA_BFD_Feasibility = 0;
                int CA_WFD_Feasibility = 0;
                int CA_NFD_Feasibility = 0;
                int CU_WFD_Feasibility = 0;
                int CU_BFD_Feasibility = 0;
                int CU_FFD_Feasibility = 0;
                int CU_NFD_Feasibility = 0;

                for (int i = 0; i < 1000; i++) {

                    List<Task> Task_HI = GenerateTasks.generateHiTasks();
                    List<Task> Task_LO = GenerateTasks.generateLOTasks();
                    Utilization.utilization_Scaling(Ub * M, Task_HI, Task_LO);
                    SortUtils.UiHISort(Task_HI);
                    SortUtils.UiLOSort(Task_LO);

                    List<Task> taskList = SortUtils.maxUiMergeAndSort(Task_HI, Task_LO);

                    int f_CA_FFD = 0, f_CA_BFD = 0, f_CA_WFD = 0, f_CU_WFD = 0, f_CA_NFD = 0;
                    if (CA_FFD.CA_FFD_Partitioning(Task_HI, Task_LO, M_List_CA_FFD) == 1) {
                        CA_FFD_Feasibility++;
                        f_CA_FFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_FFD);
                    }
                    if (CA_BFD.CA_BFD_Partitioning(Task_HI, Task_LO, M_List_CA_BFD) == 1) {
                        CA_BFD_Feasibility++;
                        f_CA_BFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_BFD);

                    }
                    if (CA_WFD.CA_WFD_Partitioning(Task_HI, Task_LO, M_List_CA_WFD) == 1) {
                        CA_WFD_Feasibility++;
                        f_CA_WFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_WFD);

                    }
                    if (CU_WFD.CU_WFD_Partitioning(taskList, M_List_CU_WFD) == 1) {
                        CU_WFD_Feasibility++;
                        f_CU_WFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CU_WFD);

                    }
                    if (CA_NFD.CA_NFD_Partitioning(Task_HI, Task_LO, M_List_CA_NFD) == 1) {
                        CA_NFD_Feasibility++;
                        f_CA_NFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_NFD);
                    }
                    if (CU_BFD.CU_BFD_Partitioning(taskList, M_List_CU_BFD) == 1) {
                        CU_BFD_Feasibility++;
                    }
                    if (CU_FFD.CU_FFD_Partitioning(taskList, M_List_CU_FFD) == 1) {
                        CU_FFD_Feasibility++;
                    }
                    if (CU_NFD.CU_NFD_Partitioning(taskList, M_List_CU_NFD) == 1) {
                        CU_NFD_Feasibility++;
                    }



                    if (f_CA_FFD == 1 && f_CA_BFD == 1 && f_CA_WFD == 1 && f_CA_NFD == 1) {
                        CA_FFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CA_FFD);
                        CA_BFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CA_BFD);
                        CA_WFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CA_WFD);
                        CA_NFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CA_NFD);
                        count++;
                    }

                }
                CA_FFD_weight += CA_FFD_Feasibility * Ub / weight*1.0;
                CA_BFD_weight += CA_BFD_Feasibility * Ub / weight*1.0;
                CA_WFD_weight += CA_WFD_Feasibility * Ub / weight*1.0;
                CA_NFD_weight += CA_NFD_Feasibility * Ub / weight*1.0;

                CU_WFD_weight += CU_WFD_Feasibility * Ub / weight*1.0;
                CU_BFD_weight += CU_BFD_Feasibility * Ub / weight*1.0;
                CU_NFD_weight += CU_NFD_Feasibility * Ub / weight*1.0;
                CU_FFD_weight += CU_FFD_Feasibility * Ub / weight*1.0;
            }


            //System.out.println("Ub=" + nf.format(Ub));
            System.out.println(nf.format(CA_FFD_weight) + "\t\t\t\t" + nf.format(CA_BFD_weight) + "\t\t\t\t" + nf.format(CA_WFD_weight) + "\t\t\t\t" + nf.format(CA_NFD_weight)
                            + "\t\t\t\t"+ nf.format(CU_WFD_weight) + "\t\t\t\t"+ nf.format(CU_BFD_weight) + "\t\t\t\t"+ nf.format(CU_NFD_weight) + "\t\t\t\t"+ nf.format(CU_FFD_weight) + "\t\t\t\t"
                    //+nf.format(CA_FFD_Energy / count) + "\t\t\t\t\t\t" + nf.format(CA_BFD_Energy / count) + "\t\t\t\t\t\t" + nf.format(CA_WFD_Energy / count) + "\t\t\t\t\t\t" + nf.format(CA_NFD_Energy / count)
            );

        }


    }
}
