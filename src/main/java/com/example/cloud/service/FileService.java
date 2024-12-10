package com.example.cloud.service;

import com.example.cloud.domain.File;
import com.example.cloud.domain.User;
import com.example.cloud.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

   public Optional<File> findByFilename(String filename) {
      return fileRepository.findByFilename(filename);
   }

   public void delete(File file) {
      fileRepository.delete(file);
   }

   public Optional<File> renameFile(String currentFilename, String newName) throws FileNotFoundException {
      Optional<File> file = fileRepository.findByFilename(currentFilename);
      if (file.isEmpty()) {
         throw new FileNotFoundException("File \"" + currentFilename + "\" not found");
      } else {
         file.get().setFilename(newName);
      }
      return Optional.ofNullable(fileRepository.save(file.get()));
   }

}