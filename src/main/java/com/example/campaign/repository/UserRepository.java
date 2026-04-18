package com.example.campaign.repository;

import com.example.campaign.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByArea(String area);

}