package com.example.demo.common.gencode.genservice;

import com.example.demo.common.gencode.dtogencode.MethodEntity;
import com.example.demo.common.gencode.dtogencode.ObjectEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GenCodeServiceImpl {

    public static void writeClassServiceImpl(ObjectEntity objectEntity) {
        String className = objectEntity.getClassName();
        List<MethodEntity> serviceMethods = objectEntity.getListMethod();

        StringBuilder code = new StringBuilder();
        code.append("package com.example.demo.service.impl;\n");
        code.append("import com.example.demo.service.UserService;\n");
        code.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        code.append("import org.springframework.stereotype.Service;\n\n");

        code.append("@Service\n");
        code.append("public class ").append(className).append("ServiceImpl implements ").append(className).append("Service {\n\n");
        // Generate service methods
        for (MethodEntity method : serviceMethods) {
            code.append("    @Override\n");
            code.append("    public ").append(method.getTypeMethod()).append(" ").append(method.getNameMethod()).append("(");


            for (int i = 0; i < serviceMethods.size(); i++) {
                MethodEntity parameter = serviceMethods.get(i);
                code.append(parameter.getParamMethod());
                if (i < serviceMethods.size() - 1) {
                    code.append(", ");
                }
            }

            code.append(") {\n");
            code.append("        // TODO: Implement ").append(method.getNameMethod()).append(" method\n");
            code.append("     ;\n");
            code.append("    }\n\n");
        }
        code.append("}");
//        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\service\\impl";
        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\test";

        String codeString = code.toString();

        try {
            // Lưu mã nguồn vào tệp
            String fileName = className + "ServiceImpl.java";
            Path filePath = Paths.get(saveDirectory, fileName);
            Files.write(filePath, codeString.getBytes());

            System.out.println("Đã lưu mã nguồn vào: " + filePath.toString());
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        System.out.println(code.toString());
    }
}
