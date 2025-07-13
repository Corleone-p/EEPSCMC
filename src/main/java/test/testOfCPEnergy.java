package test;

import algorithms.*;
import entity.Task;
import utils.GenerateTasks;
import utils.SortUtils;
import utils.Utilization;
import utils.WriteFileTool;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import static utils.StaticParameters.*;
import static utils.StaticParameters.M;

public class testOfCPEnergy {

    public static void main(String[] args) throws IOException {
        WriteFileTool.write("aaa.txt");

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);//定义输出能耗的精度,小数点后4位,可按需求修改

        CP = 0.5;
        n = 40;

        M = 4;
        double Ub=0.5;
        System.out.println("\tCA_FFD_EnergyConsumption\tCA_BFD_EnergyConsumption\tCA_WFD_EnergyConsumption\tCA_NFD_EnergyConsumption"
                +"\tCU_FFD_EnergyConsumption\tCU_BFD_EnergyConsumption\tCU_WFD_EnergyConsumption\tCU_NFD_EnergyConsumption"

        );
        List<Task>[] M_List_CA_FFD = new List[M + 1];
        List<Task>[] M_List_CA_BFD = new List[M + 1];
        List<Task>[] M_List_CA_WFD = new List[M + 1];
        List<Task>[] M_List_CA_NFD = new List[M + 1];
        List<Task>[] M_List_CU_WFD = new List[M + 1];
        List<Task>[] M_List_CU_BFD = new List[M + 1];
        List<Task>[] M_List_CU_FFD = new List[M + 1];
        List<Task>[] M_List_CU_NFD = new List[M + 1];
        for (CP = 0.1; CP < 0.91; CP += 0.1) {
            int count = 0;

            double CA_FFD_Energy = 0;
            double CA_BFD_Energy = 0;
            double CA_WFD_Energy = 0;
            double CA_NFD_Energy = 0;
            double CU_WFD_Energy = 0;
            double CU_BFD_Energy = 0;
            double CU_FFD_Energy = 0;
            double CU_NFD_Energy = 0;

            for (int i = 0; i < 10000; i++) {
                int f_CA_FFD = 0, f_CA_BFD = 0, f_CA_WFD = 0, f_CA_NFD = 0, f_CU_WFD = 0, f_CU_BFD = 0, f_CU_FFD = 0, f_CU_NFD = 0;
                //double ulo = Ub / 1.25;
                //GenerateTasks.utilizationTranFormat(M, n, ulo, CP, CF, XF);
                List<Task> Task_HI = GenerateTasks.generateHiTasks();
                List<Task> Task_LO = GenerateTasks.generateLOTasks();
                Utilization.utilization_Scaling(Ub * M, Task_HI, Task_LO);
                SortUtils.UiHISort(Task_HI);
                SortUtils.UiLOSort(Task_LO);

                List<Task> taskList = SortUtils.maxUiMergeAndSort(Task_HI, Task_LO);

                if (CA_FFD.CA_FFD_Partitioning(Task_HI, Task_LO, M_List_CA_FFD) == 1) {

                    f_CA_FFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_FFD);
                }
                if (CA_BFD.CA_BFD_Partitioning(Task_HI, Task_LO, M_List_CA_BFD) == 1) {

                    f_CA_BFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_BFD);

                }
                if (CA_WFD.CA_WFD_Partitioning(Task_HI, Task_LO, M_List_CA_WFD) == 1) {

                    f_CA_WFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_WFD);

                }
                if (CU_WFD.CU_WFD_Partitioning(taskList, M_List_CU_WFD) == 1) {

                    f_CU_WFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CU_WFD);
                }
                if (CA_NFD.CA_NFD_Partitioning(Task_HI, Task_LO, M_List_CA_NFD) == 1) {

                    f_CA_NFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_NFD);
                }
                if (CU_BFD.CU_BFD_Partitioning(taskList, M_List_CU_BFD) == 1) {
                    f_CU_BFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_NFD);
                }
                if (CU_FFD.CU_FFD_Partitioning(taskList, M_List_CU_FFD) == 1) {
                    f_CU_FFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_NFD);
                }
                if (CU_NFD.CU_NFD_Partitioning(taskList, M_List_CU_NFD) == 1) {
                    f_CU_NFD = EDF_VD_Schedulability.schedulabilityJudge2(M_List_CA_NFD);
                }


                if (f_CA_FFD == 1 && f_CA_BFD == 1 && f_CA_WFD == 1 && f_CA_NFD == 1 && f_CU_WFD == 1 && f_CU_BFD == 1 && f_CU_FFD == 1 && f_CU_NFD == 1) {
                    CA_FFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CA_FFD);
                    CA_BFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CA_BFD);
                    CA_WFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CA_WFD);
                    CA_NFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CA_NFD);

                    CU_FFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CU_FFD);
                    CU_BFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CU_BFD);
                    CU_WFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CU_WFD);
                    CU_NFD_Energy += EnergyConsumption.EnergyConsumption(M_List_CU_NFD);
                    count++;
                }

            }
            //System.out.println("Ub=" + nf.format(Ub));
            System.out.println(nf.format(CA_FFD_Energy / count) + "\t" + nf.format(CA_BFD_Energy / count) + "\t" + nf.format(CA_WFD_Energy / count) + "\t" + nf.format(CA_NFD_Energy / count)+ "\t"
                    +nf.format(CU_FFD_Energy / count) + "\t" + nf.format(CU_BFD_Energy / count) + "\t" + nf.format(CU_WFD_Energy / count) + "\t" + nf.format(CU_NFD_Energy / count)
            );
        }


    }


}
