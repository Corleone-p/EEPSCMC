package test;

import algorithms.*;
import entity.Task;
import utils.Energy_Caculate;
import utils.SortUtils;

import java.util.ArrayList;
import java.util.List;
import static utils.StaticParameters.*;

public class test {
    //论文中的测试例子
    public static void main(String[] args) {
        Ps = 0;
        List<Task> Task_HI = new ArrayList<>();
        List<Task> Task_LO = new ArrayList<>();
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        Task task4 = new Task();
        Task task5 = new Task();

        task1.setTi(10);
        task1.setDi(10);
        task1.setCi_LO(2);
        task1.setCi_HI(0.15);
        task1.setUi_LO(task1.getCi_LO()/task1.getTi());
        task1.setUi_HI(task1.getCi_HI()/task1.getTi());
        task1.setLi(0);
        task1.setNum(1);

        task2.setTi(20);
        task2.setDi(20);
        task2.setCi_LO(9);
        task2.setCi_HI(0.8);
        task2.setUi_LO(task2.getCi_LO()/task2.getTi());
        task2.setUi_HI(task2.getCi_HI()/task2.getTi());
        task2.setLi(0);
        task2.setNum(2);

        task3.setTi(15);
        task3.setDi(15);
        task3.setCi_LO(0.9);
        task3.setCi_HI(4.5);
        task3.setUi_LO(task3.getCi_LO()/task3.getTi());
        task3.setUi_HI(task3.getCi_HI()/task3.getTi());
        task3.setLi(1);
        task3.setNum(3);



        task4.setTi(30);
        task4.setDi(30);
        task4.setCi_LO(1.2);
        task4.setCi_HI(4.2);
        task4.setUi_LO(task4.getCi_LO()/task4.getTi());
        task4.setUi_HI(task4.getCi_HI()/task4.getTi());
        task4.setLi(1);
        task4.setNum(4);


        task5.setTi(20);
        task5.setDi(20);
        task5.setCi_LO(1.2);
        task5.setCi_HI(5.2);
        task5.setUi_LO(task5.getCi_LO()/task5.getTi());
        task5.setUi_HI(task5.getCi_HI()/task5.getTi());
        task5.setLi(1);
        task5.setNum(5);



        Task_LO.add(task1);
        Task_LO.add(task2);
        Task_HI.add(task3);
        Task_HI.add(task4);
        Task_HI.add(task5);
        List<Task> taskList = SortUtils.maxUiMergeAndSort(Task_HI, Task_LO);
        M=2;
        List<Task>[] M_List_IMCPA = new List[M + 1];
        List<Task>[] M_List_CUWFD = new List[M + 1];
        List<Task>[] M_List_CU_BFD = new List[M + 1];
        List<Task>[] M_List_CU_FFD = new List[M + 1];
        List<Task>[] M_List_CU_NFD = new List[M + 1];
        //int ans = IMCPA.IMCPA_Partitioning(Task_HI,Task_LO,M_List_IMCPA);
        //int ans  = BM_WFD.BM_WFD_Partitioning(taskList,M_List_IMCPA);
        int ans  = Sum_WFD.Sum_WFD(taskList,M_List_IMCPA);
        System.out.println(ans);
        double e=Energy_Caculate.Caculate_Energe(M_List_IMCPA);
        System.out.println(e);
        int ans2  = CU_WFD.CU_WFD_Partitioning(taskList,M_List_CUWFD);
        System.out.println(ans2);
        double e2=Energy_Caculate.Caculate_Energe(M_List_CUWFD);
        System.out.println(e2);
        int ans3 = CU_BFD.CU_BFD_Partitioning(taskList,M_List_CU_BFD);
        System.out.println(Energy_Caculate.Caculate_Energe(M_List_CU_BFD));
        int ans4 = CU_FFD.CU_FFD_Partitioning(taskList,M_List_CU_FFD);
        System.out.println(Energy_Caculate.Caculate_Energe(M_List_CU_FFD));
        int ans5 = CU_NFD.CU_NFD_Partitioning(taskList,M_List_CU_NFD);
        System.out.println(Energy_Caculate.Caculate_Energe(M_List_CU_NFD));

//        System.out.println("CA_FFD");
//        CA_FFD.CA_FFD_Partitioning(Task_HI,Task_LO);
//        System.out.println("CA_WFD");
//        CA_WFD.CA_WFD_Partitioning(Task_HI,Task_LO);

    }
}