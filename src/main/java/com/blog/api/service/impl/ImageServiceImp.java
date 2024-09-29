package com.blog.api.service.impl;

import com.blog.api.entities.Image;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.repository.ImageRepository;
import com.blog.api.service.ImageService;
import com.blog.api.utils.ImageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ImageServiceImp implements ImageService {
    ImageRepository imageRepository;

    @Override
    public String uploadImage(MultipartFile img) throws IOException {
        try {
            var imageToSave = Image.builder()
                    .name(img.getOriginalFilename())
                    .type(img.getContentType())
                    .imageData(ImageUtils.compressImage(img.getBytes()))
                    .build();

            var savedImg = imageRepository.save(imageToSave);
            return savedImg.getId();
        }catch (IOException e) {
            return null;
        }


    }
    @Override
    public byte[] downloadImage(String id) throws DataFormatException, IOException {
        Image dbImage = imageRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));

        return ImageUtils.decompressImage(dbImage.getImageData());

    }

    @Override
    public List<Image> getAll() {
        return imageRepository.findAll();
    }
}
