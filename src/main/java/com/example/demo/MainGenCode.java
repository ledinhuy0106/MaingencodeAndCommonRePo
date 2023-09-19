package com.example.demo;

import com.example.demo.common.gencode.FunctionCommon;
import com.example.demo.common.gencode.dtogencode.ObjectEntity;
import com.example.demo.common.gencode.gencodeEntity.GenCodeEntity;
import com.example.demo.common.gencode.gencontroller.GenCodeController;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainGenCode {

    public static void main(String[] args) throws IOException {
        String strPath = System.getProperty("user.dir") + "\\src\\main\\resources\\";
        Reader reader = Files.newBufferedReader(Paths.get(strPath + "template_gencode.json"));
        genCodeAll(reader);
    }

    private static void genCodeAll(Reader reader) {
        try {
            ObjectEntity itemObject = (ObjectEntity) FunctionCommon.convertJsonToObject(reader, ObjectEntity.class);
            if (itemObject != null) {

                      GenCodeEntity.writeClassEntity(itemObject);
//                       GenCodeController.writeClassController(itemObject);
//                     GenCodeService.writeClassService(itemObject);
//                     GenCodeServiceImpl.writeClassServiceImpl(itemObject);
//                     GenCodeRepositoryService.writeClassRepositoryService(itemObject);
//                     GenCodeRepositorysImpl.writeClassRepositoryImpl(itemObject);
//                     GenCodeRepositoryJpa.writeClassRepositoryJpa(itemObject);
            } else {
                System.out.println("Không thể đọc thông tin từ tệp JSON.");
            }
        } catch (Exception ex) {
            Logger.getLogger(GenCodeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
