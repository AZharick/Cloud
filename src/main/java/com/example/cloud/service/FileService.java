package com.example.cloud.service;

import com.example.cloud.domain.File;
import com.example.cloud.domain.FileResponse;
import com.example.cloud.domain.User;
import com.example.cloud.repository.FileRepository;
import com.example.cloud.repository.UserTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.cloud.util.DeBearerer.deBearerize;

@AllArgsConstructor
@Service
public class FileService {
   private final FileRepository fileRepository;
   private final UserTokenRepository userTokenRepository;

   public File save(MultipartFile file, String filename, User user) throws IOException {
      File fileEntity = File.builder()
              .filename(filename)
              .size(file.getSize())
              .fileData(file.getBytes())
              .user(user)
              .build();

      return fileRepository.save(fileEntity);
   }

   public List<FileResponse> getFiles(String authToken, int limit) {
      String username = userTokenRepository.getUserByToken(deBearerize(authToken)).getUsername();
      return fileRepository.findAllByUsername(username).stream().limit(limit).toList();
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