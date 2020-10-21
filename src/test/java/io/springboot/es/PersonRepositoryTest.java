package io.springboot.es;

import cn.hutool.json.JSONUtil;
import io.springboot.SpringBootAppTest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * @author lee
 * @Description 测试 Repository 操作ES
 * @Date 20/10/16 23:22
 */
public class PersonRepositoryTest extends SpringBootAppTest {

//    @Autowired
//    private PersonRepository personRepository;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testCreateIndex() throws IOException {
        boolean exists = restHighLevelClient.indices()
                .exists(new GetIndexRequest("person"), RequestOptions.DEFAULT);
        System.out.println("exists = " + exists);
//        CreateIndexRequest indexRequest = new CreateIndexRequest("person");
        Person person = new Person(1L, "张3", "联合国", 28, new Date(), "巴拉巴拉小傻瓜");
        IndexRequest indexRequest = new IndexRequest("person")
                .id(String.valueOf(person.getId()))
                .source(JSONUtil.toJsonStr(person), XContentType.JSON);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

}