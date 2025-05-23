package com.sideproject.musinsa_backend.Employee.repository;

import com.sideproject.musinsa_backend.Chatting.domain.ChatRoomType;
import com.sideproject.musinsa_backend.Employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByFloor(String floor);
}
