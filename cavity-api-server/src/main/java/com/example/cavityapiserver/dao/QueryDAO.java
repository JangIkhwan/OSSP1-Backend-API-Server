package com.example.cavityapiserver.dao;

import com.example.cavityapiserver.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

import static java.util.List.of;

/*
querys 테이블에 관련 데이터접근객체
 */
@Slf4j
@Repository
public class QueryDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public QueryDAO(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /*
    앱의 요청을 querys 테이블에 저장
     */
    public long addQuery(QueryPostRequest postRequest) {
        log.info("QueryDAO::addQuery");

        String sql = "insert into querys(device_token, request_id, image_url)" +
                " values(:device_token, :request_id, :image_url)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(postRequest);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, param, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    /*
    중복된 요청이 존재하는지 확인
     */
    public boolean hasDuplicateQuery(QueryPostRequest postRequest) {
        String sql = "SELECT EXISTS(SELECT * FROM querys" +
                " WHERE device_token = :device_token AND request_id = :request_id)";

        Map<String, Object> param = Map.of(
                "device_token", postRequest.getDevice_token(),
                "request_id", postRequest.getRequest_id()
        );

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, param, boolean.class));

    }

    /*
    쿼리id에 해당하는 쿼리를 반환
     */
    public Optional<Long> findQueryId(PredictionResponse patchRequest) {
        log.info("QueryDAO::findQueryId");
        log.info("patchRequestDTO=" + patchRequest.toString());
        String sql = "SELECT query_id FROM querys" +
                " WHERE device_token = :device_token AND request_id = :request_id";

        Map<String, Object> param = Map.of(
                "device_token", patchRequest.getDevice_token(),
                "request_id", patchRequest.getRequest_id()
        );

        try{
            Long queryId = jdbcTemplate.queryForObject(sql, param, Long.class);
            return Optional.of(queryId);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    /*
    해당 요청이 처리된 상태로 변경
     */
    public int modifyStatus_finished(Long queryId) {
        String sql = "UPDATE querys SET status='finished'" +
                " WHERE query_id = :query_id";

        Map<String, Object> param = Map.of(
                "query_id", queryId
        );

        return jdbcTemplate.update(sql, param);
    }

    /*
    해당 요청이 처리된 상태인지 여부를 반환
     */
    public boolean queryIsfinished(PredictionResponse patchRequest) {
        String sql = "SELECT EXISTS(SELECT * FROM querys " +
                "WHERE device_token = :device_token AND request_id = :request_id AND status='finished')";

        Map<String, Object> param = Map.of(
                "device_token", patchRequest.getDevice_token(),
                "request_id", patchRequest.getRequest_id()
        );

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, param, boolean.class));
    }
}