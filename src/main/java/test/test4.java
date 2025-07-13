package test;

import algorithms.CU_WFD;
import algorithms.IMCPA;
import entity.Task;
import utils.Energy_Caculate;
import utils.SortUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static utils.StaticParameters.*;

public class test4 {
    public static void main(String[] args) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(4);//定义输出能耗的精度,小数点后4位,可按需求修改
        CP = 0.5;
        n = 6;

        M = 2;
        List<Task>[] M_List_CU_WFD = new List[M + 1];
        List<Task>[] M_List_IMCPA = new List[M + 1];
        List<Task> taskList = new ArrayList<>();
        List<Task> hitask = new ArrayList<>();
        List<Task> lotask = new ArrayList<>();
        Task task1 = new Task(0,1000,1,0.273, 0.280);
        Task task2 = new Task(1, 1000, 1, 0.015, 0.107);
        Task task3 = new Task(2, 1000, 1, 0.012, 0.062);
        Task task4 = new Task(3, 1000, 0, 0.133, 0.044);
        Task task5 = new Task(4, 1000, 0, 0.124, 0.105);
        Task task6 = new Task(5, 1000, 0, 0.10, 0.057);
        hitask.add(task1);
        hitask.add(task2);
        hitask.add(task3);
        lotask.add(task4);
        lotask.add(task5);
        lotask.add(task6);
//        task1.setLi(0);
//        task1.setTi(1000);
//        task1.setUi_LO(1.0/5);
//        task1.setUi_HI(0.5/5);
//        task1.setCi_LO(1);
//        task1.setCi_HI(0.5);
//        task1.setNum(0);
//
//        task2.setLi(0);
//
//        task2.setTi(20);
//        task2.setDi(20);
//        task2.setCi_LO(6);
//        task2.setCi_HI(4);
//        task2.setUi_LO(6/20.0);
//        task2.setUi_HI(4/20.0);
//        task2.setNum(1);
//        task3.setLi(1);
//        task3.setTi(10);
//        task3.setDi(10);
//        task3.setCi_LO(1);
//        task3.setCi_HI(3);
//        task3.setUi_LO(1 / 10.0);
//        task3.setUi_HI(3/ 10.0);
//        task3.setNum(2);
        taskList = SortUtils.maxUiMergeAndSort(hitask,lotask);
        CU_WFD.CU_WFD_Partitioning(taskList,M_List_CU_WFD);
        double e = Energy_Caculate.Caculate_Energe(M_List_CU_WFD);
        System.out.println(nf.format(e));
        IMCPA.IMCPA_Partitioning(hitask,lotask,M_List_IMCPA);
        double e2 = Energy_Caculate.Caculate_Energe(M_List_IMCPA);
        System.out.println(e2);


    }
}
