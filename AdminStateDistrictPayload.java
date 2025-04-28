package com.hsbc.hap.cer.java;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonComparator {

    public static void main(String[] args) {
        try {
            String filePath1 = "C:\\Users\\45423980\\Documents\\dothrakiaudit(1jan-8april).json";
            String filePath2 = "C:\\Users\\45423980\\Documents\\hapaudit(1jan-8april).json";

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> list1 = objectMapper.readValue(new File(filePath1), new TypeReference<List<Map<String, Object>>>() {});
            List<Map<String, Object>> list2 = objectMapper.readValue(new File(filePath2), new TypeReference<List<Map<String, Object>>>() {});

            Map<String, Map<String, Object>> map1 = list1.stream()
                    .filter(obj -> obj.containsKey("id") && obj.containsKey("cr"))
                    .collect(Collectors.toMap(obj -> obj.get("id") + "-" + obj.get("cr"), obj -> obj));

            Map<String, Map<String, Object>> map2 = list2.stream()
                    .filter(obj -> obj.containsKey("id") && obj.containsKey("cr"))
                    .collect(Collectors.toMap(obj -> obj.get("id") + "-" + obj.get("cr"), obj -> obj));

            Set<String> onlyInFile1 = map1.keySet().stream()
                    .filter(key -> !map2.containsKey(key))
                    .collect(Collectors.toSet());

            Set<String> onlyInFile2 = map2.keySet().stream()
                    .filter(key -> !map1.containsKey(key))
                    .collect(Collectors.toSet());

            if (onlyInFile1.isEmpty() && onlyInFile2.isEmpty()) {
                System.out.println("Both files contain the same objects based on 'id' and 'cr'.");
            } else {
                System.out.println("Differences found based on 'id' and 'cr':");
                if (!onlyInFile1.isEmpty()) {
                    System.out.println("Objects only in file1:");
                    onlyInFile1.forEach(key -> System.out.println(map1.get(key)));
                }
                if (!onlyInFile2.isEmpty()) {
                    System.out.println("Objects only in file2:");
                    onlyInFile2.forEach(key -> System.out.println(map2.get(key)));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
					      }
