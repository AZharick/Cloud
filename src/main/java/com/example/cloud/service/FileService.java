package com.example.cloud.service;

import com.example.cloud.domain.File;
import com.example.cloud.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
   private final FileRepository fileRepository;
   private final UserService userService;

   public FileService(FileRepository fileRepository, UserService userService) {
      this.fileRepository = fileRepository;
      this.userService = userService;
   }

   public File save(String authToken, MultipartFile file, String filename) throws IOException {
      File fileEntity = File.builder()
              .filename(filename)
              .size((int) file.getSize())
              .file(file.getBytes())
              .user(userService.getUserByToken(authToken))
              .build();

      return fileRepository.save(fileEntity);
   }

   public List<File> getFilesInQtyOf(int limit, long userId) {
      return fileRepository.findAllByUserId(userId).stream().limit(limit).toList();
   }

   public List<File> findAllByUserId(long userId) {
      return fileRepository.findAllByUserId(userId);
   }

}