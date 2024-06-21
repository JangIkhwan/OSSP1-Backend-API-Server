package com.example.cavityapiserver.service;

import com.example.cavityapiserver.common.exception.PredictionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

import static com.example.cavityapiserver.common.response.status.BaseExceptionResponseStatus.IMAGE_UPLOAD_FAILED;


@Slf4j
@Service
public class ImageService {
    @Value("${images.path}")
    private String imageDir;

    /*
    앱의 요청으로 전달받은 이미지를 서버에 저장하는 메소드
     */
    public String uploadImage(MultipartFile image) {
        log.info("ImageService::uploadImage()");
        final String extension = image.getContentType().split("/")[1];
        final String imageName = UUID.randomUUID() + "." + extension;

        log.info("extension=" + extension + ", imageName=" + imageName);

        try {
            final File file = new File(imageDir + imageName);
            image.transferTo(file);
        } catch (Exception e) {
            throw new PredictionException(IMAGE_UPLOAD_FAILED);
        }
        return imageName;
    }
}
