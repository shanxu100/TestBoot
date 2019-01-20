package com.example.boottest.demo.recommendation.seq.fpgrowth;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import org.apache.commons.io.Charsets;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;
import org.apache.mahout.common.iterator.FileLineIterable;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Guan
 * @date Created on 2019/1/12
 */
public class Client4 {
    private static String PATH_FINAM_TRAIN = "C:\\Users\\Guan\\dataset\\locationRcmd\\train.txt";

    public static void main(String[] args) throws Exception {
        //使用定制的GrouplensDataModel,如果没有转换数据集成为csv格式的
        DataModel dataModel = new MyDataModel(new File(PATH_FINAM_TRAIN));
        //皮尔逊相关系数，衡量用户相似度
//        UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
//        //构建用户邻居，100个
//        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100,
//                userSimilarity, dataModel);
//        //推荐引擎
//        Recommender recommender = new GenericUserBasedRecommender(dataModel,
//                userNeighborhood, userSimilarity);
        //运行
//        LoadEvaluator.runLoad(recommender);
//        stats(dataModel, 5);
//        stats(dataModel, 10);
//        stats(dataModel, 15);
//        stats(dataModel, 20);
//        stats(dataModel, 25);
        List<IRStatistics> list = new ArrayList();
        for (int i = 0; i < 1; i++) {
            IRStatistics statistics = stats(dataModel, 5 + i * 5);
            list.add(statistics);
        }
        for (int i = 0; i < 1; i++) {
            System.err.println("at:" + (5 + i * 5));
            IRStatistics stats = list.get(i);
            System.out.println("准确率：" + stats.getPrecision() + "   召回率：" + stats.getRecall()
                    + "    Fmeasure:" + stats.getF1Measure());
        }

    }

    private static IRStatistics stats(DataModel dataModel, int at) throws Exception {
        //记录开始时间，计算耗时
        long timestamp = System.currentTimeMillis();

        RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
        AverageAbsoluteDifferenceRecommenderEvaluator myEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();

        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, 0.000001, similarity, model, 0.000001);
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };
        // 计算推荐4个结果时的查准率和召回率
        //使用评估器，并设定评估期的参数
        //4表示"precision and recall at 4"即相当于推荐top4，然后在top-4的推荐上计算准确率和召回率
//        IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, dataModel,
//                null, at, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.8);
//        System.out.println("准确率：" + stats.getPrecision() + "   召回率：" + stats.getRecall()
//                + "    Fmeasure:" + stats.getF1Measure() + "  耗时："
//                + (System.currentTimeMillis() - timestamp) + " ms");

        double mae = myEvaluator.evaluate(recommenderBuilder, null, dataModel, 0.5, 1);
        System.out.println(mae);
        return null;
    }

    public final static class MyDataModel extends FileDataModel {

        private static final String COLON_DELIMTER = ",";
        private static final Pattern COLON_DELIMITER_PATTERN = Pattern.compile(COLON_DELIMTER);


        /**
         * @param ratingsFile GroupLens ratings.dat file in its native format
         * @throws IOException if an error occurs while reading or writing files
         */
        public MyDataModel(File ratingsFile) throws IOException {
            super(convertGLFile(ratingsFile));
        }

        private static File convertGLFile(File originalFile) throws IOException {
            // Now translate the file; remove commas, then convert "::" delimiter to comma
            File resultFile = new File(new File(System.getProperty("java.io.tmpdir")), "ratings.txt");
            if (resultFile.exists()) {
                resultFile.delete();
            }
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(resultFile), Charsets.UTF_8)) {
                for (String line : new FileLineIterable(originalFile, false)) {
                    int lastDelimiterStart = line.lastIndexOf(COLON_DELIMTER);
                    if (lastDelimiterStart < 0) {
                        throw new IOException("Unexpected input format on line: " + line);
                    }
                    String subLine = line.substring(0, lastDelimiterStart);
                    String convertedLine = COLON_DELIMITER_PATTERN.matcher(subLine).replaceAll(",");
                    writer.write(convertedLine);
                    writer.write('\n');
                }
            } catch (IOException ioe) {
                resultFile.delete();
                throw ioe;
            }
            return resultFile;
        }

        public static File readResourceToTempFile(String resourceName) throws IOException {
            InputSupplier<? extends InputStream> inSupplier;
            try {
                URL resourceURL = Resources.getResource(org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel.class, resourceName);
                inSupplier = Resources.newInputStreamSupplier(resourceURL);
            } catch (IllegalArgumentException iae) {
                File resourceFile = new File("src/main/java" + resourceName);
                inSupplier = Files.newInputStreamSupplier(resourceFile);
            }
            File tempFile = File.createTempFile("taste", null);
            tempFile.deleteOnExit();
            Files.copy(inSupplier, tempFile);
            return tempFile;
        }

        @Override
        public String toString() {
            return "GroupLensDataModel";
        }

    }
}
