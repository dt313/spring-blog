package com.blog.api.controller;

import com.blog.api.entities.Image;
import com.blog.api.entities.ResponseObject;
import com.blog.api.service.ImageService;
import com.blog.api.service.impl.ImageServiceImp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageController {
    ImageService imageService;

    @PostMapping("/api/v1/upload-image")
    @CrossOrigin
    ResponseEntity<ResponseObject> upload(@RequestBody MultipartFile file) throws IOException {

        System.out.println("Upload");
        System.out.println(file);
        var result = imageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK,"sucess", result));
    }

    @GetMapping("/api/v1/image/{id}")
    ResponseEntity<?> getImage(@PathVariable String id) throws IOException, DataFormatException {
        byte[] imageData = imageService.downloadImage(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(MediaType.IMAGE_PNG_VALUE))
                .body(imageData);
    }

    @GetMapping("/api/v1/image")
    ResponseEntity<ResponseObject> getAll() throws IOException, DataFormatException {
        List<Image> imageData = imageService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "success", imageData));
    }
}
