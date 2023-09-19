package com.example.demo.common.gencode.dtogencode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class MethodEntity {
    private String typeRequest;
    private String nameMethod;
    private String typeMethod;
    private String paramMethod;
    private String nativeSql;
    private String table;
    private String typeParamMethod;
    private String pathVariable;
    private String requestParam;
    private String object;
}