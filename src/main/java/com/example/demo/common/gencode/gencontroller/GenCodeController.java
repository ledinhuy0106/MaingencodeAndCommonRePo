package com.example.demo.common.gencode.gencontroller;

import com.example.demo.common.gencode.dtogencode.MethodEntity;
import com.example.demo.common.gencode.dtogencode.ObjectEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GenCodeController {

    public static void writeClassController(ObjectEntity objectEntity) {
        String className = objectEntity.getClassName();
        List<MethodEntity> controllerMethods = objectEntity.getListMethod();

        StringBuilder code = new StringBuilder();
        code.append("package com.example.demo.controller;\n");
        code.append("import org.springframework.http.ResponseEntity;\n");
        code.append("import org.springframework.web.bind.annotation.*;\n\n");
        code.append("import com.example.demo.common.sqlcommon.Constants;\n");
        code.append("import org.springframework.http.HttpStatus;\n");
        code.append("import com.example.demo.entity.model.").append(className).append(";\n");

        code.append("@RestController\n");
        code.append("@RequestMapping(Constants.API_V1 + \"").append(className.toLowerCase()).append("\")\n");
        code.append("public class ").append(className).append("Controller {\n\n");

        for (MethodEntity method : controllerMethods) {
            if (method.getTypeRequest().equals("get")) {
                code.append("    @").append(capitalizeFirstLetter(method.getTypeRequest())).append("Mapping").append("(\"").append(className.toLowerCase()).append("\")\n");
                code.append("    public ResponseEntity<").append("Object").append(">").append(" ").append(method.getNameMethod()).append("(");
                code.append("@RequestParam").append("(\"").append(method.getRequestParam()).append("\") ").append(method.getRequestParam());
                code.append(") {\n");
                code.append("        // TODO: Implement ").append(method.getNameMethod()).append(" method\n");
                                code.append("   Object resultObj;\n");

                code.append("        return new ResponseEntity<>(resultObj, HttpStatus.OK);\n");
                code.append("    }\n\n\n\n");
            }
//            if (method.getTypeRequest().equals("get") && method.getPathVariable() != null) {
//                code.append("    @").append(capitalizeFirstLetter(method.getTypeRequest())).append("Mapping").append("(\"").append("{").append(method.getPathVariable()).append("}").append("\")\n");
//                code.append("    public ResponseEntity<").append("Object").append(">").append(" ").append(method.getNameMethod()).append("(");
//                code.append("@PathVariable" +
//                        "(\"").append(method.getNameMethod()).append("\") ").append(method.getNameMethod());
//                code.append(") {\n");
//                code.append("        // TODO: Implement ").append(method.getNameMethod()).append(" method\n");
//                code.append("   Object resultObj;\n");
//                code.append("       return new ResponseEntity<>(resultObj, HttpStatus.OK);\n");
//                code.append("    }\n\n\n\n");
//            }
//            if (method.getTypeRequest().equals("post")) {
//                code.append("   @").append(capitalizeFirstLetter(method.getTypeRequest())).append("Mapping").append("(\"").append(method.getParamMethod()).append("\")\n");
//                code.append("    public ResponseEntity<").append(className).append(">").append(" ").append(method.getNameMethod()).append("(");
//                code.append("@RequestParam(\"").append(method.getNameMethod()).append("\") ").append(method.getNameMethod());
//                code.append(") {\n");
//                code.append("        // TODO: Implement ").append(method.getNameMethod()).append(" method\n");
//                code.append("        return ResponseEntity.ok().build();\n");
//                code.append("    }\n\n");
//            }
//            if (method.getTypeRequest().equals("put")) {
//                code.append("    @").append(capitalizeFirstLetter(method.getTypeRequest())).append("Mapping").append("(\"").append(method.getParamMethod()).append("\")\n");
//                code.append("    public ResponseEntity<").append(className).append(">").append(" ").append(method.getNameMethod()).append("(");
//                code.append("@PathVariable(\"").append(method.getNameMethod()).append("\") ").append(method.getNameMethod());
//                code.append(") {\n");
//                code.append("        // TODO: Implement ").append(method.getNameMethod()).append(" method\n");
//                code.append("        return ResponseEntity.ok().build();\n");
//                code.append("    }\n\n");
//            }
//            if (method.getTypeRequest().equals("delete")) {
//                code.append("    @").append(capitalizeFirstLetter(method.getTypeRequest())).append("Mapping").append("(\"").append(method.getParamMethod()).append("\")\n");
//                code.append("    public ResponseEntity<").append(className).append(">").append(" ").append(method.getNameMethod()).append("(");
//                code.append("@RequestParam(\"").append(method.getNameMethod()).append("\") ").append(method.getNameMethod());
//                code.append(") {\n");
//                code.append("        // TODO: Implement ").append(method.getNameMethod()).append(" method\n");
//                code.append("        return ResponseEntity.ok().build();\n");
//                code.append("    }\n\n");
//            }
//        }
        }
        code.append("}");
        // Thay đổi thư mục đích
//        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\controller";
        String saveDirectory = "C:\\Users\\user\\Desktop\\jv03\\demo2\\src\\main\\java\\com\\example\\demo\\test";


        String codeString = code.toString();

        try {
            // Lưu mã nguồn vào tệp
            String fileName = className + "Controller.java";
            Path filePath = Paths.get(saveDirectory, fileName);
            Files.write(filePath, codeString.getBytes());

            System.out.println("Đã lưu mã nguồn vào: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

}
