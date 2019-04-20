##### 配置
```
spring-boot-starter-data-elasticsearch

# cluster
spring.data.elasticsearch.cluster-name=elasticsearch
spring.data.elasticsearch.cluster-nodes= # Comma-separated list of cluster node addresses.
#spring.data.elasticsearch.properties.*= # Additional properties used to configure the client.
spring.data.elasticsearch.repositories.enabled=tru
# https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
```

##### Example
```java
@Document(indexName - "blog", type = "article")
class Article {

}

@Component
interface ArticleRepository extends ElasticsearchRepository<Article, Long> {}

class Controller {
  @Autowried
  privata ArticleRepository articleRepository

  @GetMapping("save")
  void save() {
    // Article: id, content, title, summary
    articleRepository.save(article)
  }

  @GetMapping("search")
  void search(String title) {
    // matchAllQuery()
    QueryBuilder query = QueryBuilders.matchQuery("title", title)
    articleRepository.search(query)
  }
}
```

##### 查看 es 数据
- 查看索引：/_cat/indices?v
- 查看索引库结构：/blog
- 查看某个对象：/blog/article/1
