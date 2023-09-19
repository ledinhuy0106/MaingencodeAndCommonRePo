package com.example.demo.common.gencode.gencodeEntity;

import com.example.demo.common.gencode.dtogencode.Field;
import com.example.demo.common.gencode.dtogencode.ObjectEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GenCodeEntity {

    public static void writeClassEntity(ObjectEntity objectEntity) {
        String className = objectEntity.getClassName();
        List<Field> list = objectEntity.getField();
        StringBuilder code = new StringBuilder();
        code.append("package com.example.demo.entity;\n" +
                "\n" +
                "import lombok.AllArgsConstructor;\n" +
                "import lombok.Builder;\n" +
                "import lombok.Data;\n" +
                "import lombok.NoArgsConstructor;\n" +
                "\n" +
                "@Data\n" +
                "@NoArgsConstructor\n" +
                "@AllArgsConstructor\n" +
                "@Builder\n");
        code.append("public class ").append(className).append(" {\n\n");
        for (Field field: list) {
            code.append("   private ").append(field.getDatatype()).append(" ").append(field.getField()).append(";\n");
        }
        code.append("}");
//        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\entity\\model";
        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\test";

        String codeString = code.toString();

        try {
            // Lưu mã nguồn vào tệp
            String fileName = className + ".java";
            Path filePath = Paths.get(saveDirectory, fileName);
            Files.write(filePath, codeString.getBytes());

            System.out.println("Đã lưu mã nguồn vào: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(code.toString());
    }
}
