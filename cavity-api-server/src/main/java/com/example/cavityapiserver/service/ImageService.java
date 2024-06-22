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
    private String imageDir; // 업로드된 이미지가 저장되는 디렉토리 경로

    /*
    앱의 요청으로 전달받은 이미지를 서버에 저장하는 메소드
     */
    public String uploadImage(MultipartFile image) {
        log.info("ImageService::uploadImage()");

        // UUID로 유일한 파일명 생성
        final String extension = image.getContentType().split("/")[1];
        final String imageName = UUID.randomUUID() + "." + extension;

        log.info("extension=" + extension + ", imageName=" + imageName);

        // 파일을 경로에 저장
        try {
            final File file = new File(imageDir + imageName);
            image.transferTo(file);
        } catch (Exception e) {
            throw new PredictionException(IMAGE_UPLOAD_FAILED);
        }
        return imageName;
    }
}
