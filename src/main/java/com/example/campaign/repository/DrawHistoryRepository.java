package com.example.campaign.repository;

import com.example.campaign.model.DrawHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrawHistoryRepository extends JpaRepository<DrawHistory, Long> {
}