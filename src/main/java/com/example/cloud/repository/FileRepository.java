package com.example.cloud.repository;

import com.example.cloud.domain.File;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, String> {

   File save(File file);

   List<File> findAllByUserId(long userId);

}