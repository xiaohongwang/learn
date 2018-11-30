package com.learn.es;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

/**
 * ES 客户端 TransportClient
 */
public class TransportClientTest {
    private static TransportClient client;
    //建立连接
   static {
        client = new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", "elasticsearch") //集群名称  默认为 elasticsearch 可不进行设置
                .put("client.transport.sniff", true).build());//启用嗅探
        try {
            client.addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName("10.10.8.101"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加/更新文档到索引
     * _id 文档存在时进行更新
     * _id 不存在，进行添加，文档 _id为传入值
     * 也可不设置_id,进行添加
     * @param index
     * @param type
     * @param source
     * @param _id
     */
    public static void saveAsSource(String index, String type, String source, String _id){
//       IndexResponse response = client.prepareIndex(index, type)
//                .setSource(source, XContentType.JSON).get();
        IndexResponse response = client.prepareIndex(index, type, _id)
                .setSource(source, XContentType.JSON).get();
        System.out.println(response.getId());
    }

    public static void updateSource(String index, String type, String id, String source) throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest(index, type,id);
        request.doc(source, XContentType.JSON);
        UpdateResponse response = client.update(request).get();
        System.out.println(response.status());
    }
    /**
     * 获取文档信息
     * @param index
     * @param type
     * @param id
     */
    public static void getSource(String index, String type, String id){
        GetResponse response = client.prepareGet(index, type, id).get();
        String json = response.getSourceAsString();
        System.out.println(json);
    }

    /**
     * 删除文档信息
     * @param index
     * @param type
     * @param id
     */
    public static void delSource(String index, String type, String id){
        DeleteResponse response = client.prepareDelete(index, type, id).get();
        System.out.println(response.status());
    }

    /**
     * 批量处理
     */
    public static void multDeal(){
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest
                .add(client.prepareIndex("learn_test", "doc")
                                .setSource("{\"song\":\"mult\"}", XContentType.JSON))
                .add(client.prepareDelete("learn_test", "doc","AWdbcG42J_uq9P8KL10P"));
        BulkResponse responses = bulkRequest.get();
        System.out.println(responses.hasFailures());
    }

    /**
     * 判断索引是否存在
     * @param index
     */
    public static void existsIndex(String index){
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();
        //根据IndicesExistsResponse对象的isExists()方法的boolean返回值可以判断索引库是否存在.
        System.out.println("是否删除成功:"+ inExistsResponse.isExists());
    }


    /**
     * 创建索引并添加映射
     */
    public static void createIndex(){
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("song") //设置之定义字段
                    .field("type", "text")
                    .field("index","not_analyzed")//设置数据类型
                    .endObject()
                    .startObject("price")
                    .field("type", "float")
                    .endObject()
                    .endObject()
                    .endObject();
            CreateIndexRequestBuilder cib = client.admin().indices().prepareCreate("learn_test");
            cib.addMapping("doc", mapping);
            CreateIndexResponse res = cib.execute().actionGet();

            System.out.println("----------添加映射成功----------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     * @param index
     */
    public static void deleteIndex(String index){
        DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index)
                .execute().actionGet();
        System.out.println("是否删除成功:" + dResponse.isAcknowledged());
    }

    /**
     * 条件查询文档
     * @param index
     * @param type
     * @param termName
     * @param termValue
     */
    public static void query(String index, String type, String termName, String termValue){
        //简单查询
        QueryBuilder queryBuilder = QueryBuilders.termQuery(termName ,termValue);
//        QueryBuilder queryBuilder = QueryBuilders.termsQuery(termName, termValue, "mult");
//        QueryBuilder queryBuilder =
//                QueryBuilders.rangeQuery("price") // 文档域的名称
//                .from(3)                            // 范围的开始
//                .to(5)                             // 范围的结束
//                .includeLower(true)                 // 包括范围的开始
//                .includeUpper(false);
//        QueryBuilder queryBuilder = QueryBuilders.existsQuery(termName);
//        QueryBuilder queryBuilder = QueryBuilders.prefixQuery(termName, termValue);
//        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery(termName, termValue);

        //复合查询

//        QueryBuilder queryBuilder =
//                QueryBuilders.constantScoreQuery(QueryBuilders.termQuery(termName, termValue)).boost(1.0f);

//        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
//                .must(QueryBuilders.termQuery(termName, termValue))
//                .mustNot(QueryBuilders.termQuery(termName, termValue))

//                .should(QueryBuilders.termQuery(termName, "down"))
//                .should(QueryBuilders.termQuery(termName, "sd"));
//                  .filter(QueryBuilders.termQuery(termName, termValue));
        //                .must(QueryBuilders.termQuery("price", 3));

        System.out.println(queryBuilder.toString());
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH) //搜索类型
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(5)
                .execute()
                .actionGet();
       for (SearchHit searchHit : response.getHits()){
           System.out.println(searchHit.getSourceAsString());
       }
    }

//select song, count(*) as count_song from learn_test group by song;
    public static void aggregation(String index){
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("learn_test").setTypes("doc");
        TermsAggregationBuilder areIdTermsBuilder = AggregationBuilders.terms("aggs-areId").field("song");
        // 添加分组信息
        searchRequestBuilder.addAggregation(areIdTermsBuilder);
        // 执行搜索
        System.out.println(searchRequestBuilder.toString());
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        // 解析返回数据，获取分组名称为aggs-class的数据
        Terms areIdTerms = searchResponse.getAggregations().get("aggs-areId");

    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String index = "learn_test";
        String type = "doc";
        aggregation(index);
        //        query(index, type, "song", "down");

//        saveAsSource(index, type, "{\"song\":\"front\",\"price\":" + 5 + "}", String.valueOf(5));
    }


    /**
     * 根据筛选条件进行文档删除
     * @param index
     * @param termKey
     * @param termValue
     */
//    public static void delByQuery(String index, String termKey, String termValue){
//        BulkIndexByScrollResponse response =
//                DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
//                        .filter(QueryBuilders.matchQuery(termKey, termValue)) // 查询
//                        .source(index) // 索引
//                        .get(); // 执行操作
//
//        long deleted = response.getDeleted(); // 被删除的文档数量
//    }

}
