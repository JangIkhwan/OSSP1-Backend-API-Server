package com.example.cavityapiserver.service;

import com.example.cavityapiserver.common.exception.PredictionException;
import com.example.cavityapiserver.dao.QueryDAO;
import com.example.cavityapiserver.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.cavityapiserver.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryService {

    private final QueryDAO queryDao;

    /*
    앱의 요청을 저장하는 메소드
     */
    public long addQuery(QueryPostRequest postRequest) {
        log.info("QueryService::addQuery()");
        if(queryDao.hasDuplicateQuery(postRequest)){ // 중복되는 요청이 있는지 확인
            throw new PredictionException(DUPLICATE_QUERY);
        }
        return queryDao.addQuery(postRequest); // 요청을 저장
    }
}