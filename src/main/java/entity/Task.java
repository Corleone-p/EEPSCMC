package entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    int Num;   //任务编号
    double Ti;    //任务周期[10,1000]
    int Li;       //任务层次 LO：0;  HI:1;
    double Ci_LO; //低模式下的最坏截至期限
    double Ci_HI; //高模式下的最坏截至期限
    double Di;    //相对截至期限，默认Di=Ti。
    double ui_LO;  //低模式下的利用率
    double ui_HI;  //高模式下的利用率
    double max_ui;


    public Task(int Num,double Ti,int Li,double ui_LO,double ui_HI){
        this.Num = Num;
        this.Li = Li;
        this.Ti = Ti;
        this.ui_LO = ui_LO;
        this.Ci_LO=Ti*ui_LO;
        this.ui_HI=ui_HI;
        this.Ci_HI = Ti*ui_HI;
        this.Di = Ti;
    }
}
