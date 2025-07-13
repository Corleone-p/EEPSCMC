package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * 将控制台输出写入out.txt文件中
 */
public class WriteFileTool {
    /**
     * 写入方法
     *
     * @throws IOException IO异常
     */
    public static void write(String Filename) throws IOException {
        File file = new File(Filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);//以追加内容的方式进行写入
        PrintStream printStream = new PrintStream(fileOutputStream);
        System.setOut(printStream);
    }
}
