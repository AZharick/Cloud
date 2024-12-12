package com.example.cloud.service;

import com.example.cloud.domain.File;
import com.example.cloud.domain.User;
import com.example.cloud.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileServiceTests {

   @Mock
   private FileRepository fileRepository;

   @InjectMocks
   private FileService fileService;

   private MultipartFile mockFile;
   private User mockUser;

   @BeforeEach
   public void setUp() throws IOException {
      MockitoAnnotations.openMocks(this);
      mockFile = mock(MultipartFile.class);
      mockUser = new User();
      when(mockFile.getSize()).thenReturn(1024L);
      when(mockFile.getBytes()).thenReturn(new byte[1024]);
   }

   @Test
   public void testSave() throws IOException {
      String filename = "test.txt";
      byte[] fileData = new byte[1024];
      File fileEntity = new File(1L, mockUser, filename, fileData, fileData.length);

      when(fileRepository.save(any(File.class))).thenReturn(fileEntity);

      File savedFile = fileService.save(mockFile, filename, mockUser);

      assertNotNull(savedFile);
      assertEquals(filename, savedFile.getFilename());
      assertEquals(1024, savedFile.getSize());
      verify(fileRepository).save(any(File.class));
   }

   @Test
   public void testGetFilesInQtyOf() {
      long userId = 1L;
      byte[] fileData = new byte[1024];
      List<File> files = Collections.singletonList(new File(1L, mockUser, "test.txt", fileData, fileData.length));

      when(fileRepository.findAllByUserId(userId)).thenReturn(files);

      List<File> result = fileService.getFilesInQtyOf(1, userId);

      assertEquals(1, result.size());
      assertEquals("test.txt", result.get(0).getFilename());
      verify(fileRepository).findAllByUserId(userId);
   }

   @Test
   public void testFindByFilename() {
      String filename = "test.txt";
      byte[] fileData = new byte[1024];
      File fileEntity = new File(1L, mockUser, filename, fileData, fileData.length);

      when(fileRepository.findByFilename(filename)).thenReturn(Optional.of(fileEntity));

      Optional<File> foundFile = fileService.findByFilename(filename);

      assertTrue(foundFile.isPresent());
      assertEquals(filename, foundFile.get().getFilename());
      verify(fileRepository).findByFilename(filename);
   }

   @Test
   public void testDelete() {
      byte[] fileData = new byte[1024];
      File fileEntity = new File(1L, mockUser, "test.txt", fileData, fileData.length);

      fileService.delete(fileEntity);
      verify(fileRepository).delete(fileEntity);
   }

   @Test
   public void testRenameFile_FileNotFound() {
      String currentFilename = "nonExistentFile.txt";
      String newName = "newFileName.txt";

      when(fileRepository.findByFilename(currentFilename)).thenReturn(Optional.empty());

      assertThrows(FileNotFoundException.class, () -> fileService.renameFile(currentFilename, newName));
      verify(fileRepository).findByFilename(currentFilename);
   }

   @Test
   public void testRenameFile_Success() throws FileNotFoundException {
      String currentFilename = "currentName.txt";
      String newName = "newName.txt";
      byte[] fileData = new byte[1024];
      File fileEntity = new File(1L, mockUser, "test.txt", fileData, fileData.length);

      when(fileRepository.findByFilename(currentFilename)).thenReturn(Optional.of(fileEntity));
      when(fileRepository.save(any(File.class))).thenReturn(fileEntity);

      Optional<File> renamedFile = fileService.renameFile(currentFilename, newName);

      assertTrue(renamedFile.isPresent());
      assertEquals(newName, renamedFile.get().getFilename());
      verify(fileRepository).findByFilename(currentFilename);
      verify(fileRepository).save(fileEntity);
   }

}