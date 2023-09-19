package com.example.demo.common.gencode.gencodeRepo;

import com.example.demo.common.gencode.dtogencode.MethodEntity;
import com.example.demo.common.gencode.dtogencode.ObjectEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GenCodeRepositoryService {
    public static void writeClassRepositoryService(ObjectEntity objectEntity) {
        String className = objectEntity.getClassName();
        List<MethodEntity> repositoryMethods = objectEntity.getListMethod();

        StringBuilder code = new StringBuilder();

        code.append("package com.example.demo.repository;\n");
        code.append("import org.springframework.data.repository.CrudRepository;\n");
        code.append("import org.springframework.stereotype.Repository;\n\n");

        code.append("public interface ").append(className).append("RepositoryService {\n\n");

        // Generate repository methods
        for (MethodEntity method : repositoryMethods) {
            code.append(" List<").append(className).append("> ").append(method.getNameMethod()).append("(");

            for (MethodEntity parameter : repositoryMethods) {
                code.append(parameter.getParamMethod());
            }

            code.append(");\n");
        }

        code.append("}");
//        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\repository";
        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\test";

        String codeString = code.toString();

        try {
            // Lưu mã nguồn vào tệp
            String fileName = className + "RepositoryService.java";
            Path filePath = Paths.get(saveDirectory, fileName);
            Files.write(filePath, codeString.getBytes());

            System.out.println("Đã lưu mã nguồn vào: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(code.toString());
    }
}
