package com.sideproject.musinsa_backend.Employee.service;

import com.sideproject.musinsa_backend.Employee.domain.Employee;
import com.sideproject.musinsa_backend.Employee.dto.EmployeeLoginDto;
import com.sideproject.musinsa_backend.Employee.dto.EmployeeSaveDto;

public interface EmployeeService {

    Employee create(EmployeeSaveDto employeeSaveDto);
    Employee login(EmployeeLoginDto employeeLoginDto);
    Employee findByEmail(String email);
}
