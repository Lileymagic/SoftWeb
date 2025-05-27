package com.example.softengineerwebpr.domain.group.repository;

import com.example.softengineerwebpr.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}