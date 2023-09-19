package com.example.demo.common.gencode.gencodeRepo;

import com.example.demo.common.gencode.dtogencode.MethodEntity;
import com.example.demo.common.gencode.dtogencode.ObjectEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GenCodeRepositoryJpa {

    public static void writeClassRepositoryJpa(ObjectEntity objectEntity) {
        String className = objectEntity.getClassName();
        List<MethodEntity> repositoryMethods = objectEntity.getListMethod();

        StringBuilder code = new StringBuilder();

        code.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        code.append("import org.springframework.stereotype.Repository;\n\n");

        code.append("@Repository\n");
        code.append("public interface ").append(className).append("RepositoryJpa extends JpaRepository<").append(className).append(", Long> {\n\n");
        code.append("}");
//        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\repository\\jpa";
        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\test";
        String codeString = code.toString();

        try {
            // Lưu mã nguồn vào tệp
            String fileName = className + "RepositoryJpa.java";
            Path filePath = Paths.get(saveDirectory, fileName);
            Files.write(filePath, codeString.getBytes());

            System.out.println("Đã lưu mã nguồn vào: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(code.toString());
    }
}
