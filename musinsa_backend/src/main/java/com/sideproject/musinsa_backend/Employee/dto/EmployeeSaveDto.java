package com.sideproject.musinsa_backend.Employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSaveDto {
    private String name;
    private String email;
    private String password;
    private String position;
    private String floor;
    private Date join_date;
}
