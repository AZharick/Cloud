package com.example.cloud.service;

import com.example.cloud.domain.File;
import com.example.cloud.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class FileService {
   private final FileRepository fileRepository;

   public FileService(FileRepository fileRepository) {
      this.fileRepository = fileRepository;
   }

   public File save(File file) { //сделай метод, переводящий MultiPartFile в FIle
      return fileRepository.save(file);
   }

   public List<File> getFilesInQtyOf(int limit) {
      return fileRepository.findAll().stream().limit(limit).toList();
   }

   //n
//   public void saveFileToDatabase(MultipartFile file) {
//      //user + hash + filedata
//
////      try {
////         byte[] fileData = file.getBytes();
////         String fileName = file.getOriginalFilename();
////
////         File fileEntity = new File(fileName, fileData);
////         fileRepository.save(fileEntity);
////      } catch (Exception e) {
////         // Обработка ошибок при сохранении файла
////         throw new RuntimeException("Failed to save file to database: " + e.getMessage());
////      }
//   }

   //n
//   public String getHashFromMultipartFile(MultipartFile file) {
//      try {
//         MessageDigest md = MessageDigest.getInstance("MD5");
//         byte[] bytes = file.getBytes();
//         byte[] digest = md.digest(bytes);
//         // Преобразование байтов хэша в строку
//         StringBuilder sb = new StringBuilder();
//         for (byte b : digest) {
//            sb.append(String.format("%02x", b));
//         }
//         return sb.toString();
//      } catch (IOException | NoSuchAlgorithmException e) {
//         e.printStackTrace();
//         return null;
//      }
//   }
//
//   public void deleteFileByName(String name) {
//      fileRepository.deleteFileByName(name);
//   }
}