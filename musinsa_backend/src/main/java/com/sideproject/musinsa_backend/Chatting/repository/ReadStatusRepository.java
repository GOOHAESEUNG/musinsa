package com.sideproject.musinsa_backend.Chatting.repository;

import com.sideproject.musinsa_backend.Chatting.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
}
