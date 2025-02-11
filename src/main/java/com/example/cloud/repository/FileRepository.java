package com.example.cloud.repository;

import com.example.cloud.domain.File;
import com.example.cloud.domain.FileResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, String> {

   File save(File file);

   List<FileResponse> findAllByUsername(String username);

   Optional<File> findByFilename(String filename);

   void delete(File file);

}