package com.example.cloud.service;

import com.example.cloud.domain.File;
import com.example.cloud.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
   private final FileRepository fileRepository;
   public FileService(FileRepository fileRepository) {
      this.fileRepository = fileRepository;
   }


   public File save(File file) {
      return fileRepository.save(file);
   }

   public List<File> findAllByUserId(Long id) {
      return fileRepository.findAllByUserId(id);
   }

   public void deleteFileByName(String name) {
      fileRepository.deleteFileByName(name);
   }
}