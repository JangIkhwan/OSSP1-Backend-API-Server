package com.example.cavityapiserver.dao;

import com.example.cavityapiserver.dto.RequestPostRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Repository
public class PredictionRequestDAO {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PredictionRequestDAO(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public long addRequest(RequestPostRequest postRequest) {
        log.info("PredictionRequestDAO::addRequest");
        log.info("postRequestDTO=" + postRequest.toString());

        String sql = "insert into prediction_requests(device_token, request_id, image_url) values(:device_token, :request_id, :image_url)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(postRequest);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, param, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public boolean hasDuplicateRequest(RequestPostRequest postRequest) {
        String sql = "SELECT EXISTS(SELECT * FROM prediction_requests WHERE device_token = :device_token AND request_id = :request_id)";

        Map<String, Object> param = Map.of(
                "device_token", postRequest.getDevice_token(),
                "request_id", postRequest.getRequest_id()
        );

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, param, boolean.class));

    }
}
