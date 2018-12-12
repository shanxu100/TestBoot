/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 补充：以上都是瞎扯
 */

package com.example.boottest.demo.recommendation.rcmd;

import org.apache.commons.io.Charsets;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.common.iterator.FileLineIterable;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 本质上是 {@see org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel}
 * 只是更改了GroupLensDataModel的读文件方式。因为自带的MongoDBDataModel太旧了
 *
 * @author Guan
 * @date Created on 2018/12/7
 */
public final class GroupLensDataModel2 extends FileDataModel {

    private static final String COLON_DELIMTER = "::";
    private static final Pattern COLON_DELIMITER_PATTERN = Pattern.compile(COLON_DELIMTER);


    /**
     * 数据量很大，需要写到文件中读
     *
     * @param ratingsFile GroupLens ratings.dat file in its native format
     * @throws IOException if an error occurs while reading or writing files
     */
    public GroupLensDataModel2(File ratingsFile) throws IOException {
        super(convertGLFile(ratingsFile));
    }

    /**
     * 数据量很小，直接从内存读就好了。。。
     *
     * @param originalData GroupLens ratings.dat file in its native format
     * @throws IOException if an error occurs while reading or writing files
     */
    public GroupLensDataModel2(List<String> originalData) throws IOException {
        super(convertGLFile(originalData));
    }

    private static File convertGLFile(List<String> originalData) throws IOException {
        // Now translate the file; remove commas, then convert "::" delimiter to comma
        File resultFile = new File(new File(System.getProperty("java.io.tmpdir")), "ratings.txt");
        if (resultFile.exists()) {
            resultFile.delete();
        }
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(resultFile), Charsets.UTF_8)) {
            for (String line : originalData) {
                write(line, writer);
            }
        } catch (IOException ioe) {
            resultFile.delete();
            throw ioe;
        }
        return resultFile;
    }

    private static File convertGLFile(File originalFile) throws IOException {
        // Now translate the file; remove commas, then convert "::" delimiter to comma
        File resultFile = new File(new File(System.getProperty("java.io.tmpdir")), "ratings.txt");
        if (resultFile.exists()) {
            resultFile.delete();
        }
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(resultFile), Charsets.UTF_8)) {
            for (String line : new FileLineIterable(originalFile, false)) {
                write(line, writer);
            }
        } catch (IOException ioe) {
            resultFile.delete();
            throw ioe;
        }
        return resultFile;
    }

    /**
     * 写入到临时文件
     *
     * @param line
     * @param writer
     * @throws IOException
     */
    private static void write(String line, Writer writer) throws IOException {
        int lastDelimiterStart = line.lastIndexOf(COLON_DELIMTER);
        if (lastDelimiterStart < 0) {
            throw new IOException("Unexpected input format on line: " + line);
        }
        String subLine = line.substring(0, lastDelimiterStart);
        String convertedLine = COLON_DELIMITER_PATTERN.matcher(subLine).replaceAll(",");
        writer.write(convertedLine);
        writer.write('\n');
    }

    @Override
    public String toString() {
        return "GroupLensDataModel";
    }

}
