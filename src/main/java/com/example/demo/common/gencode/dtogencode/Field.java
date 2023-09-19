package com.example.demo.common.gencode.dtogencode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Field {
    private String datatype;
    private String field;
}
