package org.star.apigateway.core.routes.auth;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

@Component
public class RoutesReader {
    public HashMap<String, Routes> read(String path) {
        Yaml yaml = new Yaml();
        System.out.println("check1");
        try {
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            System.out.println("check");
            Map<String, Map<String, List<String>>> data = yaml.load(inputStream);
            System.out.println("check2");
            System.out.println(data.get("auth-service"));
            HashMap<String, Routes> microservices = new HashMap<>();
            for (Map.Entry<String,Map<String,List<String>>> routes : data.entrySet()) {
                System.out.println("try cycle");
                System.out.println(routes.getValue());
                microservices.put(routes.getKey(), new Routes(routes.getValue()));
            }
            return microservices;
        } catch (Exception e) {
            throw new RuntimeException("can't read file");
        }
    }
}
