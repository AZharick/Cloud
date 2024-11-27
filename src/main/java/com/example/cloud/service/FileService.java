package com.example.cloud.service;

import com.example.cloud.domain.File;
import com.example.cloud.domain.User;
import com.example.cloud.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class FileService {
   private final FileRepository fileRepository;

   public File save(MultipartFile file, String filename, User user) throws IOException {
      File fileEntity = File.builder()
              .filename(filename)
              .size((int) file.getSize())
              .file(file.getBytes())
              .user(user)
              .build();

      return fileRepository.save(fileEntity);
   }

   public List<File> getFilesInQtyOf(int limit, long userId) {
      return fileRepository.findAllByUserId(userId).stream().limit(limit).toList();
   }

}