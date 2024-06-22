package com.example.cavityapiserver.service;

import com.example.cavityapiserver.common.exception.DatabaseException;
import com.example.cavityapiserver.common.exception.PredictionException;
import com.example.cavityapiserver.dao.PredictionDAO;
import com.example.cavityapiserver.dao.QueryDAO;
import com.example.cavityapiserver.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.cavityapiserver.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredicitonService {

    private final PredictionDAO predDao;

    private final QueryDAO queryDao;

    /*
    데이터베이스에 저장된 판별 결과를 불러오는 메소드
     */
    public PredictionGetResponse getResult(PredictionGetRequest getRequest) {
        log.info("PredicitonService::getResult()");

        if(!predDao.hasResult(getRequest)){ // 결과가 있는지 확인
            throw new PredictionException(RESULT_NOT_FOUND);
        }

        List<Prediction> prediction = predDao.getPrediction(getRequest); // 결과를 가져옴
        return new PredictionGetResponse(prediction);
    }

    /*
    데이터베이스에 모델의 판별 결과를 저장하는 메소드
     */
    @Transactional
    public void addResult(PredictionResponse response) {
        log.info("PredicitonService::addResult()");

        Optional<Long> optional = queryDao.findQueryId(response);
        if(optional.isEmpty()){   // 데이터베이스에 해당 요청이 있었는지 확인
            throw new PredictionException(QUERY_NOT_FOUND);
        }

        if(queryDao.queryIsfinished(response)){ // 해당 요청에 대한 결과가 이미 도착했는지 확인
            throw new PredictionException(QUERY_FINISHED);
        }

        List<Prediction> pred = response.getData().getPred(); // 모델로부터 전달받은 치아별 판별 결과를 저장
        pred.forEach(prediction -> {
            int affectedRow = predDao.addResult(response, optional.get(), prediction);
            if(affectedRow == -1){
                throw new DatabaseException(DATABASE_ERROR);
            }
        });

        int affectedRow = queryDao.modifyStatus_finished(optional.get()); // 요청의 상태를 모델로부터 결과 도착으로 변경
        if(affectedRow == -1){
            throw new DatabaseException(DATABASE_ERROR);
        }
    }
}