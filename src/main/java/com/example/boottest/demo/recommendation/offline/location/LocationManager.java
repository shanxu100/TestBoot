package com.example.boottest.demo.recommendation.offline.location;

import com.example.boottest.demo.recommendation.offline.FileManager;
import com.example.boottest.demo.recommendation.offline.model.Location;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Guan
 * @date Created on 2019/3/14
 */
public class LocationManager {

    public static void main(String[] args) {

        File file = new File("C:\\Users\\Guan\\dataset\\locationRcmd\\mappedpoiinfo.txt");
        Map<String, Location> map = new TreeMap<>();
        FileManager.inputFile(file, new FileManager.InputListener() {
            @Override
            public void input(String line) {
                String[] ss = line.split("\t");
                Location location = new Location(ss[0], Double.parseDouble(ss[1]), Double.parseDouble(ss[2]));
                map.put(ss[0], location);
            }
        });
        System.out.println(map.size());
        for (int i=0;i<10;i++){
            System.out.println(map.get(i+""));
        }

    }


}
