package test;

import algorithms.*;
import entity.Task;
import utils.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import static utils.StaticParameters.*;

public class testNwe_CP {
    public static void main(String[] args) throws IOException {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);//定义输出能耗的精度,小数点后4位,可按需求修改

        //CP = 0.5;
        n = 20;
        double Ub=0.5;
        M=4;
        //System.out.println("CU_WFD_FFD_F\tIMCPA_F");
        //System.out.println("CU_WFD_Energy\tMS_E_Energy");
        //打开控制台输出到文件函数
        WriteFileTool.write("CP.txt");//
        List<Task>[] M_List_MS_E = new List[M + 1];
        List<Task>[] M_List_CU_WFD = new List[M + 1];
        List<Task>[] M_List_CU_BFD = new List[M + 1];
        List<Task>[] M_List_CU_FFD = new List[M + 1];
        List<Task>[] M_List_CU_NFD = new List[M + 1];
        for (CP = 0.10; CP <= 0.91; CP+=0.1){
            int count = 0;
            int MS_E_Feasibility = 0;
            int CU_WFD_Feasibility = 0;
            int CU_BFD_Feasibility = 0;
            int CU_FFD_Feasibility = 0;
            int CU_NFD_Feasibility = 0;

            double MS_E_Energy = 0;
            double CU_WFD_Energy = 0;
            double CU_BFD_Energy = 0;
            double CU_FFD_Energy = 0;
            double CU_NFD_Energy = 0;
            for (int i = 0; i < 1000; i++) {

                List<Task> Task_HI = GenerateTasks.generateHiTasks();
                List<Task> Task_LO = GenerateTasks.generateLOTasks();
                Utilization.utilization_Scaling(Ub * M, Task_HI, Task_LO);
                SortUtils.UiHISort(Task_HI);
                SortUtils.UiLOSort(Task_LO);

                List<Task> taskList = SortUtils.maxUiMergeAndSort(Task_HI, Task_LO);

                int  f_CU_WFD = 0, f_MS_E = 0,f_CU_BFD = 0, f_CU_FFD = 0, f_CU_NFD = 0;
                if (CU_WFD.CU_WFD_Partitioning(taskList, M_List_CU_WFD) == 1) {
                    CU_WFD_Feasibility++;
                    f_CU_WFD = MC_QPA.Dbf_Schedule(M_List_CU_WFD);

                }
                if(Sum_WFD.Sum_WFD(taskList,M_List_MS_E) == 1){
                    MS_E_Feasibility++;
                    f_MS_E = MC_QPA.Dbf_Schedule(M_List_MS_E);
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
                if (f_CU_WFD == 1 && f_MS_E == 1 && f_CU_BFD == 1 && f_CU_FFD == 1 && f_CU_NFD == 1) {
                    CU_WFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_WFD);
                    MS_E_Energy += Energy_Caculate.Caculate_Energe(M_List_MS_E);
                    CU_BFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_BFD);
                    CU_FFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_FFD);
                    CU_NFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_NFD);
                    count++;
                }
            }
            //System.out.println(nf.format(CU_WFD_Feasibility / 100.0) + "\t" + nf.format(IMCPA_Feasibility / 100.0));
//            System.out.println(nf.format(CU_WFD_Energy / count)+"\t" +nf.format(MS_E_Energy/count));
            System.out.print("CP=" + CP);
            System.out.print("\tCUWFD\t" + CU_WFD_Energy/count);
            System.out.print("\tEEPSCMC\t" + MS_E_Energy/count);
            System.out.print("\tCUBFD\t" + CU_BFD_Energy/count);
            System.out.print("\tCUFFD\t" + CU_FFD_Energy/count);
            System.out.println("\tCUNFD\t" + CU_NFD_Energy/count);

//            System.out.print("\tCUWFD\t" + CU_WFD_Feasibility*1.0/1000);
//            System.out.print("\tEEPSCMC\t" + MS_E_Feasibility*1.0/1000);
//            System.out.print("\tCUBFD\t" + CU_BFD_Feasibility*1.0/1000);
//            System.out.print("\tCUFFD\t" + CU_FFD_Feasibility*1.0/1000);
//            System.out.println("\tCUNFD\t" + CU_NFD_Feasibility*1.0/1000);
        }
    }
}
