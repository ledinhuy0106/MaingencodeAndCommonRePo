package com.example.demo.common.gencode.gencodeRepo;
import com.example.demo.common.gencode.dtogencode.MethodEntity;
import com.example.demo.common.gencode.dtogencode.ObjectEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GenCodeRepositorysImpl {

    public static void writeClassRepositoryImpl(ObjectEntity objectEntity) {
        String className = objectEntity.getClassName();
        List<MethodEntity> repositoryMethods = objectEntity.getListMethod();

        StringBuilder code = new StringBuilder();

        code.append("package com.example.demo.service;\n\n");
        code.append("import org.springframework.stereotype.Repository;\n\n");

        code.append("@Repository\n");
        code.append("public class ").append(className).append("RepositoryImpl implements ").append(className).append("RepositoryService {\n\n");

        // Generate repository methods
        for (MethodEntity method : repositoryMethods) {
            code.append("    @Override\n");
            code.append("    public ").append(method.getTypeMethod()).append("  ").append(method.getNameMethod()).append("(");

            for (int i = 0; i < repositoryMethods.size(); i++) {
                MethodEntity parameter = repositoryMethods.get(i);
                code.append(parameter.getParamMethod());
                if (i < repositoryMethods.size() - 1) {
                    code.append(", ");
                }
            }

            code.append(") {\n");
            code.append("        // TODO: Implement ").append(method.getNameMethod()).append(" method\n");
            code.append("        return null;\n");
            code.append("    }\n\n");
        }
        code.append("}");
//        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\repository\\repositoryimpl";
        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\test";

        String codeString = code.toString();

        try {
            // Lưu mã nguồn vào tệp
            String fileName = className + "RepositoryImpl.java";
            Path filePath = Paths.get(saveDirectory, fileName);
            Files.write(filePath, codeString.getBytes());

            System.out.println("Đã lưu mã nguồn vào: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(code.toString());
    }
}
