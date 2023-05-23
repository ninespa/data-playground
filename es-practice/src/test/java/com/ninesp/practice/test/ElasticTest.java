/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package com.ninesp.practice.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.annotation.Resource;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;

/**
 * @author chenjun
 * @since 2023/5/23
 */
public class ElasticTest extends AbstractAppTest{
    @Resource
    private RestHighLevelClient client;
    @Test
    public void testRawCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("movies1");
        request.mapping("{\n"
            + "    \"properties\": {\n"
            + "      \"title\":{\n"
            + "        \"type\": \"text\"\n"
            + "      },\n"
            + "      \"director\":{\n"
            + "        \"type\": \"text\"\n"
            + "      },\n"
            + "      \"actors\":{\n"
            + "        \"type\": \"text\"\n"
            + "      },\n"
            + "      \"release_date\":{\n"
            + "        \"type\": \"date\"\n"
            + "      },\n"
            + "      \"rating\":{\n"
            + "        \"type\": \"float\"\n"
            + "      }\n"
            + "    }\n"
            + "  }", XContentType.JSON);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    public void testRawBulk() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        List<String> lines = Files.readAllLines(Paths.get("/pathToLocal/movies.json"));

        for (String line : lines) {
            JSONObject data = JSON.parseObject(line);
            bulkRequest.add(new IndexRequest("movies1").source(data));
        }
        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    @Test
    public void testRawQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("movies");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.rangeQuery("rating").gt(8.0));
        searchSourceBuilder.sort(new FieldSortBuilder("release_date").order(SortOrder.DESC));
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }
}
