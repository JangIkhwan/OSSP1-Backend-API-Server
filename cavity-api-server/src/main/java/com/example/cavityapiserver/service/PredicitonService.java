package com.example.cavityapiserver.service;

import com.example.cavityapiserver.common.exception.DatabaseException;
import com.example.cavityapiserver.common.exception.PredictionException;
import com.example.cavityapiserver.dao.PredictionRequestDAO;
import com.example.cavityapiserver.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.cavityapiserver.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredicitonService {

    private final PredictionRequestDAO prDao;
    private final String imageDir = "/Users/apple/Documents/file/";
    public long addRequest(PredictionPostRequest postRequest) {
        if(prDao.hasDuplicateRequest(postRequest)){
            throw new PredictionException(DUPLICATE_REQUEST);
        }
        return prDao.addRequest(postRequest);
    }

    public PredictionGetResponse getResult(PredictionGetRequest getRequest) {
        if(!prDao.hasResult(getRequest)){
            throw new PredictionException(RESULT_NOT_FOUND);
        }
        Prediction result = prDao.getClassAndProbability(getRequest);
        List<List<Integer>> bboxPoints = prDao.getBboxPoints(getRequest);
        result.setBbox(bboxPoints);
        List<Prediction> pred = new ArrayList<>();
        pred.add(result);
        return new PredictionGetResponse(pred);
    }

    public void addResult(PredictionPatchRequest patchRequest) {
        if(!prDao.requestExists(patchRequest)){
            throw new PredictionException(REQUEST_NOT_FOUND);
        }
        int affectedRow = prDao.addResult(patchRequest);
        if(affectedRow == -1){
            throw new DatabaseException(DATABASE_ERROR);
        }
    }
}
