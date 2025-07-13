package test;

import algorithms.*;
import algorithms.EDF_VD_Schedulability;
import entity.Task;
import utils.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static utils.StaticParameters.*;
import static utils.StaticParameters.n;

public class testIMCPA {
    public static void main(String[] args) throws IOException {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);//定义输出能耗的精度,小数点后4位,可按需求修改

        CP = 0.5;
        n = 20;

        M = 4;
        double Ub;
        //System.out.println("CU_WFD_FFD_F\tIMCPA_F");
//        System.out.println("CU_WFD_Energy\tIMCPA_Energy");
        //打开控制台输出到文件函数
        WriteFileTool.write("U.txt");//考虑任务数对指标的影响
        List<Task>[] M_List_CU_WFD = new List[M + 1];
        List<Task>[] M_List_IMCPA = new List[M + 1];
        List<Task>[] M_List_CU_BFD = new List[M + 1];
        List<Task>[] M_List_CU_FFD = new List[M + 1];
        List<Task>[] M_List_CU_NFD = new List[M + 1];
        List<Task>[] M_List_CA_WFD = new List[M + 1];
        for (Ub = 0.50; Ub < 0.86; Ub += 0.05){
            int count = 0;
            int IMCPA_Feasibility = 0;
            int CU_WFD_Feasibility = 0;
            int CU_BFD_Feasibility = 0;
            int CU_FFD_Feasibility = 0;
            int CU_NFD_Feasibility = 0;
            int CA_WFD_Feasibility = 0;

            double CU_WFD_Energy = 0;
            double IMCPA_Energy = 0;
            double CU_BFD_Energy = 0;
            double CU_FFD_Energy = 0;
            double CU_NFD_Energy = 0;
            double CA_WFD_Energy = 0;

            for (int i = 0; i < 1000; i++){
                //修改为只能缩小利用率，放大的重新生成
                List<Task> Task_LO = new ArrayList<>();
                List<Task> Task_HI = new ArrayList<>();
                int flag = 0;
                do {
                    Task_HI = GenerateTasks.generateHiTasks();//
                    Task_LO = GenerateTasks.generateLOTasks();
                    flag = Utilization.utilization_Scaling(Ub * M, Task_HI, Task_LO);//只缩小不放大
                }while (flag==0);
                List<Task> taskLO = new ArrayList<>();
                taskLO.addAll(Task_LO);
                SortUtils.UiLOSortIncrense(taskLO);//升序
                SortUtils.UiHISort(Task_HI);//降序
                SortUtils.UiLOSort(Task_LO);//降序
                List<Task> taskList = SortUtils.maxUiMergeAndSort(Task_HI, Task_LO);
                int f_CU_WFD = 0,f_IMCPA =0,f_CU_BFD = 0, f_CU_FFD = 0, f_CU_NFD = 0,f_CA_WFD = 0;
                if (CU_WFD.CU_WFD_Partitioning(taskList, M_List_CU_WFD) == 1) {
                    CU_WFD_Feasibility++;
                    f_CU_WFD = MC_QPA.Dbf_Schedule(M_List_CU_WFD);
                }
                if(Sum_WFD.Sum_WFD(taskList,M_List_IMCPA) == 1){
                    IMCPA_Feasibility++;
                    f_IMCPA = MC_QPA.Dbf_Schedule(M_List_IMCPA);
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
//                if (CA_WFD.CA_WFD_Partitioning(Task_HI,Task_LO, M_List_CA_WFD) == 1) {
//                    CA_WFD_Feasibility++;
//                    f_CA_WFD = MC_QPA.Dbf_Schedule(M_List_CA_WFD);
//                }

                if (f_CU_WFD==1 && f_IMCPA == 1 && f_CU_BFD == 1 && f_CU_FFD == 1 && f_CU_NFD == 1){
                    CU_WFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_WFD);
                    IMCPA_Energy += Energy_Caculate.Caculate_Energe(M_List_IMCPA);
                    CU_BFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_BFD);
                    CU_FFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_FFD);
                    CU_NFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_NFD);
                    //CA_WFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CA_WFD);
                    count++;
                }
            }
            //System.out.println(nf.format(CU_WFD_Feasibility / 100.0) + "\t" + nf.format(IMCPA_Feasibility / 100.0));
            //System.out.println(nf.format(CU_WFD_Energy / count)+"\t" +nf.format(IMCPA_Energy/count));
            System.out.print("U=" + Ub);
            System.out.print("\tCUWFD\t" + CU_WFD_Energy/count);
            System.out.print("\tEEPSCMC\t" + IMCPA_Energy/count);
            System.out.print("\tCUBFD\t" + CU_BFD_Energy/count);
            System.out.print("\tCUFFD\t" + CU_FFD_Energy/count);
            System.out.println("\tCUNFD\t" + CU_NFD_Energy/count);
            //System.out.println("\tCAWFD\t" + CA_WFD_Energy/count);



        }
    }
}
