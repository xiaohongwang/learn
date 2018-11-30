package com.learn.resttemplate;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RestTemplateLearn {

    private static final  String createUrl  = "http://ip:port/verify/target/add";
    private static final  String deleteUrl  = "http://ip:port/verify/target/deletes";
    private static final  String clearUrl  = "http://ip:port/verify/target/clear";

    public static void main(String[] args) {
        db(createUrl, MediaType.APPLICATION_FORM_URLENCODED);
        db(clearUrl, MediaType.APPLICATION_FORM_URLENCODED);
        db(deleteUrl, MediaType.APPLICATION_FORM_URLENCODED);
    }

    /**
     * 处理请求 接收返回结果
     * @param url
     * @param params
     * @param mediaType
     * @return
     */
    public static ResponseEntity _post(String url, Map<String, ?> params, MediaType mediaType) {
        RestTemplate restTemplate = new RestTemplate();
        //设置header信息
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置请求头信息
        requestHeaders.setContentType(mediaType);

        HttpEntity<?> requestEntity = (
                mediaType == MediaType.APPLICATION_JSON
                        || mediaType == MediaType.APPLICATION_JSON_UTF8)
                ? new HttpEntity<>(JSONObject.toJSON(params), requestHeaders)
                : (mediaType == MediaType.APPLICATION_FORM_URLENCODED
                        || mediaType == MediaType.MULTIPART_FORM_DATA
                    ? new HttpEntity<MultiValueMap>(
                    createMultiValueMap(params), requestHeaders)
                    : new HttpEntity<>(null, requestHeaders));

        ResponseEntity result = (mediaType == MediaType.APPLICATION_JSON
                || mediaType == MediaType.MULTIPART_FORM_DATA)
                ? restTemplate.postForObject(url, requestEntity, ResponseEntity.class)
                : restTemplate.postForObject(url, requestEntity, ResponseEntity.class, JSONObject.toJSON(params));

        return result;
    }

    /**
     * @param params
     * @return
     */
    private static MultiValueMap<String, Object> createMultiValueMap(Map<String, ?> params) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        for(String key : params.keySet()) {
            if(params.get(key) instanceof List) {
                for(Iterator<String> it = ((List<String>) params.get(key)).iterator(); it.hasNext(); ) {
                    String value = it.next();
//                    map.add(key, value);
                    //传递图片信息
                    File photo = new File(value);
                    if (photo.exists()){
                        map.add(key, new FileSystemResource(photo));
                    }
                }
            } else {
                map.add(key, params.get(key).toString());
            }
        }
        return map;
    }

    public static void db(String url, MediaType mediaType){
        HashMap<String, Object> params = new HashMap<>();
        params.put("dbName","test");
        ResponseEntity responseEntity =  _post(url, params, mediaType);
        System.out.println("目标库 === " +  responseEntity.getResult()
                + "===" + responseEntity.getErrorMessage() + "===" + responseEntity.getData() == null ? "":
                responseEntity.getData().getDbId());
    }
}
