package com.example.boottest.demo.recommendation.offline;

import com.example.boottest.demo.recommendation.offline.model.Evaluation;
import com.example.boottest.demo.recommendation.offline.model.Rating;
import com.example.boottest.demo.recommendation.offline.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Guan
 * @date Created on 2019/3/9
 */
public class FileManager {

    /**
     * 输出到文件,覆盖之前的文件内容，不追加
     */
    public static void outputFile(File file, Map<User, List<Rating>> dataMap, String separator) {

        BufferedWriter bufferedWriter = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(file, false));

            for (User user : dataMap.keySet()) {
                List<Rating> list = dataMap.get(user);

                for (Rating rating : list) {
                    bufferedWriter.write(rating.toFormattedString(separator));
                    bufferedWriter.newLine();
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void outputFile(File file, OutputListener listener, boolean append) {
        BufferedWriter bufferedWriter = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(file, append));

            listener.output(bufferedWriter);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static List<String> inputFile(File file) {
        BufferedReader br = null;
        List<String> list = new ArrayList<>(4096);
        try {
            // 建立一个对象，它把文件内容转成计算机能读懂的语言
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                list.add(line);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void inputFile(File file, InputListener inputListener) {
        BufferedReader br = null;

        try {
            // 建立一个对象，它把文件内容转成计算机能读懂的语言
            br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                inputListener.input(line);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public interface OutputListener {
        void output(BufferedWriter bufferedWriter);
    }

    public interface InputListener {
        void input(String line);
    }

}
