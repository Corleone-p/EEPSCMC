package test;

import algorithms.*;
import entity.Task;
import utils.*;

import java.text.NumberFormat;
import java.util.List;

import static utils.StaticParameters.*;
import static utils.StaticParameters.CP;
import static utils.StaticParameters.M;

public class testCPnew {
    public static void main(String[] args) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);//定义输出能耗的精度,小数点后4位,可按需求修改

        CP = 0.5;
        n = 40;
        double Ub = 0.5;
        M=4;
        //System.out.println("CU_WFD_FFD_F\tIMCPA_F");
        System.out.println("CU_WFD_Energy\tMS_E_Energy");

        double weight = 0;
        List<Task>[] M_List_MS_E = new List[M + 1];
        List<Task>[] M_List_CU_WFD = new List[M + 1];

        for (CP = 0.1; CP <= 0.91; CP+=0.1) {

            int count = 0;

            double MS_E_Energy = 0;
            double CU_WFD_Energy = 0;


            double CU_WFD_weight = 0;
            double MS_E_weight = 0;

            int MS_E_Feasibility = 0;
            int CU_WFD_Feasibility = 0;

            for (int i = 0; i < 100; i++) {

                List<Task> Task_HI = GenerateTasks.generateHiTasks();
                List<Task> Task_LO = GenerateTasks.generateLOTasks();
                Utilization.utilization_Scaling(Ub * M, Task_HI, Task_LO);
                SortUtils.UiHISort(Task_HI);
                SortUtils.UiLOSort(Task_LO);

                List<Task> taskList = SortUtils.maxUiMergeAndSort(Task_HI, Task_LO);

                int  f_CU_WFD = 0, f_MS_E = 0;
                if (CU_WFD.CU_WFD_Partitioning(taskList, M_List_CU_WFD) == 1) {
                    CU_WFD_Feasibility++;
                    f_CU_WFD = MC_QPA.Dbf_Schedule(M_List_CU_WFD);

                }

                if(Sum_WFD.Sum_WFD(taskList,M_List_MS_E) == 1){
                    MS_E_Feasibility++;
                    f_MS_E = MC_QPA.Dbf_Schedule(M_List_MS_E);
                }

                if (f_CU_WFD == 1 && f_MS_E == 1) {
                    CU_WFD_Energy += Energy_Caculate.Caculate_Energe(M_List_CU_WFD);
                    MS_E_Energy += Energy_Caculate.Caculate_Energe(M_List_MS_E);
                    count++;
                }

            }

            //System.out.println(nf.format(CU_WFD_Feasibility / 100.0) + "\t" + nf.format(IMCPA_Feasibility / 100.0));
            System.out.println(nf.format(CU_WFD_Energy / count)+"\t" +nf.format(MS_E_Energy/count));

        }


    }
}
