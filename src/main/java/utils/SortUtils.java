package utils;

import entity.Task;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static utils.StaticParameters.M;

public class SortUtils {


    /**
     *按照任务的周期降序排序
     * */
    public static void TiSort(List<Task> taskList) {

        Collections.sort(taskList, (o1, o2) -> o1.getTi() < o2.getTi() ? 1 : (o1.getTi() == o2.getTi() ? 0 : -1));
    }
    /**
     *按照任务的UiLO降序排序
     * */
    public static void UiLOSort(List<Task> taskList) {

        Collections.sort(taskList, (o1, o2) -> o1.getUi_LO() < o2.getUi_LO() ? 1 : (o1.getUi_LO() == o2.getUi_LO() ? 0 : -1));
    }

    /**
     *按照任务的UiHI降序排序
     * */
    public static void UiHISort(List<Task> taskList) {

        Collections.sort(taskList, (o1, o2) -> o1.getUi_HI() < o2.getUi_HI() ? 1 : (o1.getUi_HI() == o2.getUi_HI() ? 0 : -1));
    }
    public static void UiHISortIncrense(List<Task> taskList){
        Collections.sort(taskList, (o1, o2) -> o1.getUi_HI() > o2.getUi_HI() ? 1 : (o1.getUi_HI() == o2.getUi_HI() ? 0 : -1));
    }
    public static void UiLOSortIncrense(List<Task> taskList) {

        Collections.sort(taskList, (o1, o2) -> o1.getUi_LO() > o2.getUi_LO() ? 1 : (o1.getUi_LO() == o2.getUi_LO() ? 0 : -1));
    }
   public static void UDPSort( List<Task>[] M_List){
       double [] U_H_H = new double [M+1];
       double [] U_L_H = new double [M+1];
       for (int i = 1; i <= M; i++) {
           for (int j = 0; j < M_List[i].size(); j++) {
               if (M_List[i].get(j).getLi() == 1)//对高关键层次任务的利用率进行统计
               {
                   U_H_H[i] += M_List[i].get(j).getUi_HI();//统计每个芯片的U_H_H
                   U_L_H[i] += M_List[i].get(j).getUi_LO();//统计每个芯片的U_L_H
               }
           }
       }
       //进行排序,降序
       for (int i = 1; i <M; i++) {
           for (int j = 1; j <M-i ; j++) {
               if (U_H_H[j]-U_L_H[j] > U_H_H[j+1]-U_L_H[j+1]){
                    List<Task> task;
                    double t;
                    t = U_H_H[j+1];
                    U_H_H[j] = t;
                    t = U_L_H[j+1];
                    U_L_H[j] = t;
                    task = M_List[j+1];
                    M_List[j+1] = M_List[j];
                    M_List[j] = task;
               }
           }
       }

   }
    public static void UDPSort_LO( List<Task>[] M_List){
        double [] U_H_L = new double [M+1];
        double [] U_L_L = new double [M+1];
        for (int i = 1; i <= M; i++) {
            for (int j = 0; j < M_List[i].size(); j++) {
                if (M_List[i].get(j).getLi() == 0)//对低关键层次任务的利用率进行统计
                {
                    U_H_L[i] += M_List[i].get(j).getUi_HI();//统计每个芯片的U_H_L
                    U_L_L[i] += M_List[i].get(j).getUi_LO();//统计每个芯片的U_L_L
                }
            }
        }
        //进行排序
        for (int i = 1; i <M+1 ; i++) {
            for (int j = 1; j <M-i ; j++) {
                if (U_L_L[j]-U_H_L[j] > U_L_L[j+1]-U_H_L[j+1]){
                    List<Task> task;
                    double t;
                    t = U_L_L[j+1];
                    U_L_L[j] = t;
                    t = U_H_L[j+1];
                    U_H_L[j] = t;
                    task = M_List[j+1];
                    M_List[j+1] = M_List[j];
                    M_List[j] = task;
                }
            }
        }

    }

    public static List<Task> maxUiMergeAndSortIncrense(List<Task> task_HI, List<Task> task_LO){
        List<Task> taskList = new ArrayList<>();
        for (int i=0;i<task_HI.size();i++){
            Task task = task_HI.get(i);
            task.setMax_ui(task.getUi_HI());
            taskList.add(task);
        }
        for (int i=0;i<task_LO.size();i++){
            Task task = task_LO.get(i);
            task.setMax_ui(task.getUi_LO());
            taskList.add(task);
        }
        Collections.sort(taskList, (o1, o2) -> o1.getMax_ui() > o2.getMax_ui() ? 1 : (o1.getMax_ui() == o2.getMax_ui() ? 0 : -1));

        return taskList;
    }
    public static List<Task> maxUiMergeAndSort(List<Task> task_HI, List<Task> task_LO){

        List<Task> taskList = new ArrayList<>();
        for (int i=0;i<task_HI.size();i++){
            Task task = task_HI.get(i);
            task.setMax_ui(task.getUi_HI());
            taskList.add(task);
        }
        for (int i=0;i<task_LO.size();i++){
            Task task = task_LO.get(i);
            task.setMax_ui(task.getUi_LO());
            taskList.add(task);
        }

        Collections.sort(taskList, (o1, o2) -> o1.getMax_ui() < o2.getMax_ui() ? 1 : (o1.getMax_ui() == o2.getMax_ui() ? 0 : -1));

        return taskList;
    }

}
