package utils;

import entity.Task;

import java.util.ArrayList;
import java.util.List;

import static utils.StaticParameters.M;

public class ArrayUtils {

    //数组初始化1
    public static double[] arrayInitializationOne() {
        double[] U = new double[M + 1];
        for (int i = 1; i <= M; i++) {
            U[i] = 1;
        }
        return U;
    }
    //数组初始化0
    public static double[] arrayInitializationZero() {
        double[] U = new double[M + 1];
        for (int i = 1; i <= M; i++) {
            U[i] = 0;
        }
        return U;
    }
    public static void SplitTaskList(List<Task> taskList, List<Task> taskHI, List<Task> taskLO) {
        for (Task task : taskList) {
            if (task.getLi() == 1) {
                taskHI.add(task);
            } else {
                taskLO.add(task);
            }
        }
    }

    public static List<Task> MergeTask(List<Task> taskHI, List<Task> taskLO) {
        List<Task> taskList = new ArrayList<>();
        taskList.addAll(taskHI);
        taskList.addAll(taskLO);
        return taskList;
    }
    //任务实体列表的实例化
    public static List<Task>[] listArrayInitializationOfProcessor(List<Task>[] M_Task){
         int m_size = M_Task.length;

        for (int i=1;i<m_size;i++){
            M_Task[i] = new ArrayList<>();
        }
        return M_Task;
    }
}
