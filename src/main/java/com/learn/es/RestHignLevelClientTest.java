package com.learn.es;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class RestHignLevelClientTest {

    private static final Logger logger = LoggerFactory.getLogger(RestHignLevelClientTest.class);

    private static final RestHighLevelClient client;
    private static RestClient restClient;

    static {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost",9200, "http"));
        restClient = builder.build();
        client =
                new RestHighLevelClient(restClient);
    }


    //HTTP请求，Index API包括index request和index response
    public static void saveStrToES(String index, String type, String params){
        //校验Index，type，source，contentType不为空
        IndexRequest request = new IndexRequest(index,type);
        try {
//            JSONObject json = new JSONObject();json.put("number","123456");json.put("song","假如我年少有为"); //OK
//            String jsonString = new String("{\"number\":1123456,\"song\":假如我年少有为}".getBytes(),
//                    "UTF-8"); // NO
//            String jsonString = new String("{\"number\":\"1123456\",\"song\":\"test\"}".getBytes(),
//                    "UTF-8");  // OK
            request.source(params, XContentType.JSON);
            IndexResponse indexResponse = client.index(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveMapToES(String index, String type, Map<String, Object> params){
        IndexRequest request = new IndexRequest(index,type);
        try {
            request.source(params);
            IndexResponse indexResponse = client.index(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveBilderToES(String index, String type, XContentBuilder builder){
        IndexRequest request = new IndexRequest(index,type);
        try {
            request.source(builder);
            IndexResponse indexResponse = client.index(request);
            restClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //直接设置参数
    public static void saveToES(String index, String type){
        IndexRequest request = new IndexRequest(index,type);
        try {
            request.source("number",4,"song","jumper");
            IndexResponse indexResponse = client.index(request);
            restClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //批量操作ES
    public static void batchSaveToEs(){
//        BulkRequest可以在一起从请求执行批量添加、更新和删除，至少需要添加一个操作
        BulkRequest request = new BulkRequest(); //创建BulkRequest
        request.add(new IndexRequest("learn_test", "doc")  //添加操作
                .source(XContentType.JSON,"number", "b1", "song","foo"));
        request.add(new IndexRequest("learn_test", "doc")  //添加操作
                .source(XContentType.JSON,"number", "b2", "song", "down"));
        request.add(new IndexRequest("learn_test", "doc")  //添加操作
                .source(XContentType.JSON,"number", "b3","song","up"));

        //不同类型的request可以写在同一个bulk request里
        request.add(new DeleteRequest("learn_test", "doc", "1234"));

        try {
            BulkResponse responses = client.bulk(request);
            System.out.println(responses.status());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void getResuest(String index, String type, String id) throws IOException {
        GetRequest getRequest = new GetRequest(
                index,//index name
                type,  //type
                id);//数据_id
        try {
            GetResponse getResponse =  client.get(getRequest);
            String index1 = getResponse.getIndex();
            String type1 = getResponse.getType();
            String id1 = getResponse.getId();
            if (getResponse.isExists()) {
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString();
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
//                byte[] sourceAsBytes = getResponse.getSourceAsBytes();

                System.out.println("==版本==" + version + "==type==" + type
                        + "==数据信息==" + sourceAsString);
            } else {
                System.out.println("===index存在,数据不存在===");
            }
            restClient.close();
        } catch (ElasticsearchException  e) {
            e.printStackTrace();
            if (e.status() == RestStatus.NOT_FOUND) {
                //index不存在
                System.out.println("index不存在");
            }
            if (e.status() == RestStatus.CONFLICT) {

            }
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delResuest(String index, String type, String id){
        DeleteRequest request = new DeleteRequest(
                index,
                type,
                id);

        try {
            DeleteResponse deleteResponse = client.delete(request);
            System.out.println(deleteResponse.status());
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //更新数据
    public static void updateEs(){
        UpdateRequest request = new UpdateRequest("learn_test",
                "doc","AWdWWNrUF_N3VVWMPPUt");
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("number", 2);
//        jsonMap.put("song", "假如我年少有为");
//        request.doc(jsonMap);
        request.doc("{\"number\":\"1\",\"song\":\"let it go\"}", XContentType.JSON);
        try {
            UpdateResponse response = client.update(request);
            System.out.println(response.status());
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                restClient.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public static void exists(){
        GetRequest request = new GetRequest("learn_test","doc","AWdWOpmqF_N3VVPPUo");
        try {
            boolean flag =  client.exists(request);
            System.out.println(flag);
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                restClient.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }





    public static void main(String[] args) throws IOException {
    }

}
