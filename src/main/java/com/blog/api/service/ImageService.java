package com.blog.api.service;

import com.blog.api.entities.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@Service
public interface ImageService {
    String uploadImage(MultipartFile img) throws IOException;
    byte[] downloadImage(String imgName) throws DataFormatException, IOException;
    List<Image> getAll();
}
