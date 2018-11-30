# Elasticsearch
[]()
https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html
-----
## 一、head 界面使用  进行数据查询
- 基础查询
    - 选择索引 learn_test
    -
      - must 返回的文档必须满足must子句的条件，类似于 ==   and 
      - must not返回的文档必须不满足must not 子句的条件  类似于!=  not 
      - should 返回的文档只要满足should中的一个条件即可  类似于 ||  or
    - 条件
     - term 相当  （== 相等）
     - text  片段
     - prefix 前缀
     - wildcard 通配符查询  例：*商品*
     - fuzzy 区间，分词模糊查询   结合max_expansions 和min_similarity，数值则表示在此数值的增加，减小数量在多少范围之内的数据；字符则为搜索文本最多可以纠正几个字母去跟你的数据进行匹配（ElasticSearch误拼写时的fuzzy模糊搜索技术）
     - range 区间查询，如果type是时间类型，可用内置now表示当前，-1d/h/m/s来进行时间操作
     - query_string 可以对int, long, string查询，对int,long只能本身查询，对string进行分词和本身查询
     - missing 返回没有字段或值为null的文档
## 二、客户端使用
 []()
    参考资料：https://www.jianshu.com/p/5cb91ed22956
-----
### RestHighLevelClient
```
 private static final RestHighLevelClient client;
    private static RestClient restClient;
    static {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost",9200, "http"));//ip  http通信端口port
        restClient = builder.build();
        client =
                new RestHighLevelClient(restClient);
    }
```
 - IndexRequest 进行数据操作   保存更新
    ```
    IndexRequest indexRequest = new IndexRequest(index, type);//save
    IndexRequest indexRequest = new IndexRequest(index, id); //update
    ```
    - 提供source的方式
       - 字符串
       ```
       request.source(string, XContentType.JSON);
       ```
       - Map
       ```
       request.source(map);
       ```
       - XContentBuilder
       ```
         XContentBuilder builder = XContentFactory.jsonBuilder();
                builder.startObject();
                {
                    builder.field("number", "3");
                    builder.field("song", "云烟成雨");
                }
                builder.endObject();
         request.source(builder);
       ```
       - 直接设置参数
       ```
       request.source("number",4,"song","jumper");
       ```
     - 执行操作
        - 同步执行
         ```
         IndexResponse indexResponse = client.index(request);
         ```
        - 异步执行
         ```
         client.indexAsync(request, new ActionListener<IndexResponse>() {
                    //监听器处理结果信息
                     @Override
                     public void onResponse(IndexResponse indexResponse) {}
                     @Override
                     public void onFailure(Exception e) {}
                 });
         ```
        - IndexResponse 待更新
        - es处理更新冲突   乐观并发控制  Elasticsearch使用这个_version保证所有修改都被正确排序。
        ```
        IndexRequest request = new IndexRequest("posts", "doc", "1")
                .source("field", "value")
                .version(1);//在版本 1 来进行更改。如果es中那个版本号不是 1 的，则请求失败。
        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        } catch(ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {

            }
        }
        ```
 - GetRequest  根据_id获取数据信息
```
 GetRequest getRequest = new GetRequest(
                index,//index
                type,  //type
                id);//_id
 GetResponse getResponse = client.get(getRequest);
```
 - DelRequest  根据_id删除数据信息
```
DeleteRequest request = new DeleteRequest(
                index,
                type,
                id);
client.delete(request);
```
 - UpdateRequest 更新数据
```
UpdateRequest request = new UpdateRequest(index,type,id);
request.doc(String,XContentType.JSON);//字符串
request.doc(map);//map
request.doc(builder);//XContentBuilder
client.update(request);
```
 - BulkRequest 批量操作es
```
request = new BulkRequest();
request.add(new IndexRequest("learn_test", "doc")  //添加操作
                .source(XContentType.JSON,"number", "b1", "song","foo"));
//不同类型的request可以写在同一个bulk request里
request.add(new DeleteRequest("learn_test", "doc", "1234"));
BulkResponse responses = client.bulk(request);
```
### TransportClient
[]()
 参考资料：https://www.jianshu.com/p/4f77efdd2c55
---
```
TransportClient client = new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", "elasticsearch")
                //集群名称  默认为 elasticsearch 可不进行设置
                .put("client.transport.sniff", true).build());
                //启用嗅探 集群嗅探特性
        try {
            client.addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName("10.10.8.101"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
```
 - 索引API
    ```
    //       IndexResponse response = client.prepareIndex(index, type)
    //                .setSource(source, XContentType.JSON).get();
            IndexResponse response = client.prepareIndex(index, type, _id)
                    .setSource(source, XContentType.JSON).get();
            System.out.println(response.getId());
    ```
    -  _id 文档存在时进行更新 ; _id 不存在，进行添加，文档 _id为传入值; 也可不设置_id,进行添加
 - 获取API
     ```
      GetResponse response = client.prepareGet(index, type, id).get();
      String json = response.getSourceAsString();
      System.out.println(json);
     ```
 - 删除API
      ```
        DeleteResponse response = client.prepareDelete(index, type, id).get();
        System.out.println(response.status());
      ```
 - 更新API
    ```
        UpdateRequest request = new UpdateRequest(index, type,id);
        request.doc(source, XContentType.JSON);
        UpdateResponse response = client.update(request).get();
    ```
  - 批量操作API 允许你在单个请求里添加或者删除多个文档
    ```
    BulkRequestBuilder bulkRequest = client.prepareBulk();
            bulkRequest
                    .add(client.prepareIndex("learn_test", "doc")
                                    .setSource("{\"song\":\"mult\"}", XContentType.JSON))
                    .add(client.prepareDelete("learn_test", "doc","AWdbcG42J_uq9P8KL10P"));
            BulkResponse responses = bulkRequest.get();
    ```
  -  自动批量处理  批量导出   https://www.cnblogs.com/wpcnblog/p/7903716.html
  -  判断索引是否存在

    ```
    IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
            IndicesExistsResponse inExistsResponse = client.admin().indices()
                    .exists(inExistsRequest).actionGet();
            //根据IndicesExistsResponse对象的isExists()方法的boolean返回值可以判断索引库是否存在.
            System.out.println("是否删除成功:"+ inExistsResponse.isExists());
    ```
  - 删除索引
    ```
    DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index)
                    .execute().actionGet();
            System.out.println("是否删除成功:" + dResponse.isAcknowledged());
    ```
#### Qquery DSL 基于JSON的查询语句，查询构建器的工厂类是QueryBuilders
- 简单查询
- 复合查询
- 聚合查询

|类型| 查询                                        |  解释       | 说明|
|:----------:|-----------------------------------------|:-------------:|:--------:|
|简单查询|QueryBuilders.termQuery(termName ,termValue);|精确查询包含指定词的文档|精确匹配中文失败 设置term.keyword即可匹配 ik分词器  待更新|
|简单查询|termsQuery(termName, termValue, "mult");|精确查询包含任一指定词语的文档||
|简单查询|rangeQuery("price").from(3).to(5).includeLower(true).includeUpper(false);|文档域的名称 范围的开始 范围的结束 包括范围的开始 不包括范围的结束||
|简单查询|QueryBuilders.existsQuery(termName);|查询指定域里有不是null值的所有文档||
|简单查询|prefixQuery(termName, prefix);|所有指定域的值包含特定前缀的文档|
|简单查询|QueryBuilders.wildcardQuery(termName,termValue);|匹配查询 ? 单个字符匹配 * 匹配多个字符|
|复合查询|QueryBuilders.boolQuery()|must must_not filter should|
||must |返回的文档必须满足must子句的条件，并且参与计算分值|
||must_not|返回的文档必须不满足must_not定义的条件|
||should|返回的文档可能满足should子句的条件。在一个Bool查询中，如果没有must或者filter，有一个或者多个should子句，那么只要满足一个就可以返回|
||filter|必须满足filter子句的条件。但是不会像Must一样，参与计算分值|
|聚合查询|||
- 相关概念
  - 搜索类型

  []()
  https://www.cnblogs.com/donlianli/p/3857500.html
  -----
  ```
          QueryBuilder queryBuilder = QueryBuilders.termQuery(termName ,termValue);
          System.out.println(queryBuilder.toString());
          SearchResponse response = client.prepareSearch(index)
                  .setTypes(type)
                  .setSearchType(SearchType.DFS_QUERY_THEN_FETCH) //搜索类型
                  .setQuery(queryBuilder)
                  .setFrom(0)
                  .setSize(2)
                  .execute()
                  .actionGet();
  ```
    - query and fetch
     向索引的所有分片（shard）都发出查询请求，各分片返回的时候把元素文档（document）和计算后的排名信息一起返回。这种搜索方式是最快的。因为相比下面的几种搜索方式，这种查询方法只需要去shard查询一次。但是各个shard返回的结果的数量之和可能是用户要求的size的n倍。
    - query then fetch（默认的搜索方式）
      如果你搜索时，没有指定搜索方式，就是使用的这种搜索方式。这种搜索方式，大概分两个步骤，第一步，先向所有的shard发出请求，各分片只返回排序和排名相关的信息（注意，不包括文档document)，然后按照各分片返回的分数进行重新排序和排名，取前size个文档。然后进行第二步，去相关的shard取document。这种方式返回的document与用户要求的size是相等的。
    - DFS query and fetch
       这种方式比第一种方式多了一个初始化散发(initial scatter)步骤，有这一步，据说可以更精确控制搜索打分和排名。
    - DFS query then fetch
       比第2种方式多了一个初始化散发(initial scatter)步骤,搜索精度应该是最高的
  - 评分

  []()
  https://www.cnblogs.com/didda/p/5283753.html
  -----
    ```
    {
      "term" : {
        "song" : {
          "value" : "jumper",
          "boost" : 1.0
        }
      }
    }
    ```


## 二、字段类型
 []()
    参考资料： https://blog.csdn.net/chengyuqiang/article/details/79048800
---
 - 核心类型
----
   | 二级分类       |类型        |
   | ------------- |:-------------:|
   | 字符串        | String、Text、keyword|
   | 整数类型      | integer,long,short,byte|
   | 浮点类型      | double,float,half_float,scaled_float|
   | 逻辑类型      | boolean|
   | 日期类型       |date|
   | 范围类型      |range|
   | 二进制类型    |binary|