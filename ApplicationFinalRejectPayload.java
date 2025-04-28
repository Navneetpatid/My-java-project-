package com.hsbc.hap.cer.java;

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
            String jsonData1 = "[\n" +
                    "  {\"id\": \"1\", \"cr\": \"A\", \"name\": \"John\"},\n" +
                    "  {\"id\": \"2\", \"cr\": \"B\", \"name\": \"Doe\"}\n" +
                    "]";

            String jsonData2 = "[\n" +
                    "  {\"id\": \"1\", \"cr\": \"A\", \"name\": \"John\"},\n" +
                    "  {\"id\": \"2\", \"cr\": \"B\", \"name\": \"DoeUpdated\"},\n" +
                    "  {\"id\": \"3\", \"cr\": \"C\", \"name\": \"Jane\"}\n" +
                    "]";

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> list1 = objectMapper.readValue(jsonData1, new TypeReference<List<Map<String, Object>>>() {});
            List<Map<String, Object>> list2 = objectMapper.readValue(jsonData2, new TypeReference<List<Map<String, Object>>>() {});

            // Create maps based only on 'id'
            Map<String, Map<String, Object>> map1 = list1.stream()
                    .filter(obj -> obj.containsKey("id"))
                    .collect(Collectors.toMap(obj -> obj.get("id").toString(), obj -> obj));

            Map<String, Map<String, Object>> map2 = list2.stream()
                    .filter(obj -> obj.containsKey("id"))
                    .collect(Collectors.toMap(obj -> obj.get("id").toString(), obj -> obj));

            // Find differences
            Set<String> onlyInFile1 = map1.keySet().stream()
                    .filter(key -> !map2.containsKey(key))
                    .collect(Collectors.toSet());

            Set<String> onlyInFile2 = map2.keySet().stream()
                    .filter(key -> !map1.containsKey(key))
                    .collect(Collectors.toSet());

            Set<String> commonKeys = map1.keySet().stream()
                    .filter(map2::containsKey)
                    .collect(Collectors.toSet());

            if (onlyInFile1.isEmpty() && onlyInFile2.isEmpty()) {
                System.out.println("Both files contain same 'id' objects.");
            } else {
                System.out.println("Differences found based on 'id':");
                if (!onlyInFile1.isEmpty()) {
                    System.out.println("Objects only in File1:");
                    onlyInFile1.forEach(key -> System.out.println(map1.get(key)));
                }
                if (!onlyInFile2.isEmpty()) {
                    System.out.println("Objects only in File2:");
                    onlyInFile2.forEach(key -> System.out.println(map2.get(key)));
                }
            }

            if (!commonKeys.isEmpty()) {
                System.out.println("\nCommon 'id' objects comparison:");
                for (String key : commonKeys) {
                    Map<String, Object> obj1 = map1.get(key);
                    Map<String, Object> obj2 = map2.get(key);

                    if (obj1.equals(obj2)) {
                        System.out.println("Same object for id: " + key);
                    } else {
                        System.out.println("Different object for id: " + key);
                        System.out.println("File1 Object: " + obj1);
                        System.out.println("File2 Object: " + obj2);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
		    }
