package com.github.appreciated.designer.service;

import com.github.appreciated.designer.model.ProjectPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectPath, Integer> {

}