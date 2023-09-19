package com.example.demo.common.gencode.dtogencode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectEntity {
    private String className;
    private List<MethodEntity> listMethod;
    private List<Field> Field;
}