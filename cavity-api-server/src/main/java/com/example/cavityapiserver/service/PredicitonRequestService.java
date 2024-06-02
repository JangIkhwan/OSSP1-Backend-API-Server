package com.example.cavityapiserver.service;

import com.example.cavityapiserver.common.exception.PredictionRequestException;
import com.example.cavityapiserver.dao.PredictionRequestDAO;
import com.example.cavityapiserver.dto.RequestPostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.cavityapiserver.common.response.status.BaseExceptionResponseStatus.DUPLICATE_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredicitonRequestService {
    private final PredictionRequestDAO prDao;
    public long addRequest(RequestPostRequest postRequest) {
        if(prDao.hasDuplicateRequest(postRequest)){
            throw new PredictionRequestException(DUPLICATE_REQUEST);
        }
        return prDao.addRequest(postRequest);
    }
}
