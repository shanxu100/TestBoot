package com.example.boottest.demo.recommendation.seq.fpgrowth;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2019/1/12
 */
public class FileUtil {

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static List<String> readFileByLines(String fileName) {
        List<String> result = new ArrayList<>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                result.add(tempString);
                line++;
            }
            System.out.println("一共读了行数：" + line);
            reader.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 写入TXT文件
     */
    public static void writeFile(String path, List<?> data) {
        try {
            // 相对路径，如果没有则要建立一个新的output.txt文件
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(writer);
            for (Object object : data) {
                out.write(object.toString()+"\r\n");
            }
            out.flush(); // 把缓存区内容压入文件

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
