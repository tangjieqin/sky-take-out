package com.tang.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePageQueryDTO implements Serializable {

    private Integer page;

    private Integer pageSize;

    private String name;
}