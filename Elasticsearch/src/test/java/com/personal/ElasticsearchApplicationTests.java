package com.personal;

import com.personal.dao.ArticleSearchRepository;
import com.personal.entity.Article;
import com.personal.entity.Author;
import com.personal.entity.Tutorial;
import com.personal.util.JSONUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class ElasticsearchApplicationTests {

    @Autowired
    private ArticleSearchRepository articleSearchRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testSaveArticleIndex(){
        Author author = new Author();
        author.setId(1L);
        author.setName("Jacknolfskin");
        author.setRemark("java developer");

        Tutorial tutorial = new Tutorial();
        tutorial.setId(1L);
        tutorial.setName("elastic search");

        for(int i=10;i<20;i++){
            Article article1 = new Article();
            article1.setId((long)i);
            article1.setTitle("蓝月亮指的导手册" +i);
            article1.setAbstracts("蓝月亮指导手册的书" + i);
            article1.setTutorial(tutorial);
            article1.setAuthor(author);
            article1.setContent("蓝月亮书中自有黄金屋" + i);
            article1.setPostTime(new Date());
            article1.setClickCount((long)i);
            articleSearchRepository.save(article1);
        }

        for(int i=40;i<50;i++){
            Article article1 = new Article();
            article1.setId((long)i);
            article1.setTitle("床前明月光" +i);
            article1.setAbstracts("疑是地上霜" + i);
            article1.setTutorial(tutorial);
            article1.setAuthor(author);
            article1.setContent("李白--静夜思" + i);
            article1.setPostTime(new Date());
            article1.setClickCount((long)i);
            articleSearchRepository.save(article1);
        }

        for(int i=50;i<60;i++){
            Article article1 = new Article();
            article1.setId((long)i);
            article1.setTitle("蓝月亮" +i);
            article1.setAbstracts("好亮啊" + i);
            article1.setTutorial(tutorial);
            article1.setAuthor(author);
            article1.setContent("白白亮亮" + i);
            article1.setPostTime(new Date());
            article1.setClickCount((long)i);
            articleSearchRepository.save(article1);
        }
    }

    @Test
    public void testSearch(){
        String queryString="白";//搜索关键字
        QueryStringQueryBuilder builder=new QueryStringQueryBuilder(queryString);
        //排序
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        //分页
        Pageable pageable = new PageRequest(0,100, sort);
        Iterable<Article> searchResult = articleSearchRepository.search(builder,pageable);
        Iterator<Article> iterator = searchResult.iterator();
        while(iterator.hasNext()){
            System.out.println("elasticsearch 搜索结果---"+iterator.next());
        }
    }

    @Test
    public void testSearchPage(){
        String queryString="啊";//搜索关键字
        Pageable pageable = new PageRequest(0,5);
        Page<Article> searchResult = articleSearchRepository.findByTitle(queryString,pageable);
        System.out.println("elasticsearch 搜索结果---"+searchResult.getContent());
    }

    @Test
    public void testSearchAll(){
        Iterable<Article> searchResult = articleSearchRepository.findAll();
        Iterator<Article> iterator = searchResult.iterator();
        while(iterator.hasNext()){
            System.out.println("elasticsearch 搜索结果---"+iterator.next());
        }
    }

    @Test
    public void testSearchTitle(){
        String queryString="蓝月亮";//搜索关键字
        List<Article> searchResult = articleSearchRepository.findByTitle(queryString);
        Iterator<Article> iterator = searchResult.iterator();
        while(iterator.hasNext()){
            System.out.println("elasticsearch 搜索结果---" + iterator.next());
        }
    }

    @Test
    public void getIkAnalyzeSearchTerms(){
        // 调用 IK 分词分词
        AnalyzeRequestBuilder ikRequest = new AnalyzeRequestBuilder(elasticsearchTemplate.getClient(),
                AnalyzeAction.INSTANCE,"article","蓝月亮");
        ikRequest.setTokenizer("ik");
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        List<AnalyzeResponse.AnalyzeToken> ikTokenList = ikRequest.execute().actionGet().getTokens();
        for(AnalyzeResponse.AnalyzeToken analyzeToken :ikTokenList){
            System.out.println("IK 分词-----" + analyzeToken.getTerm());
            System.out.print(',');
            //这里可以用must 或者 should 视情况而定
        }
    }
    @Test
    public void getListIkAnalyzeSearchTerms(){
        //排序
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        //分页
        Pageable pageable = new PageRequest(0,50, sort);

        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("title").contains("蓝"));

        //long count = elasticsearchTemplate.count(criteriaQuery);
        //System.out.println("================"+count);

        criteriaQuery.setPageable(pageable);
        List<Article> article = elasticsearchTemplate.queryForList(criteriaQuery, Article.class);
        System.out.println(article);
    }

    @Test
    public void getIkAnalyzeTerms(){
        List<Article> articles = new ArrayList<Article>();
        try {
            // 创建查询索引,参数productindex表示要查询的索引库为productindex
            SearchRequestBuilder searchRequestBuilder = elasticsearchTemplate.getClient()
                    .prepareSearch("article");

            String key = "月亮";
            // 设置查询索引类型,setTypes("productType1", "productType2","productType3");
            // 用来设定在多个类型中搜索
            searchRequestBuilder.setTypes("articleType");
            // 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN
            // = 扫描查询,无序
            searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
            // 设置查询关键词
            /*searchRequestBuilder
                  .setQuery(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("title", key))
                          .should(QueryBuilders.termQuery("content", key)));*/
            QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(key);
            queryBuilder.analyzer("ik_smart");
            queryBuilder.field("title").field("content");
            searchRequestBuilder.setQuery(queryBuilder);
            // 分页应用
            searchRequestBuilder.setFrom(1).setSize(3000);

            // 设置是否按查询匹配度排序
            searchRequestBuilder.setExplain(true);
            // 按照字段排序
            searchRequestBuilder.addSort("id", SortOrder.DESC);
            // 设置高亮显示
           /* searchRequestBuilder.addHighlightedField("title");
            searchRequestBuilder.addHighlightedField("content");
            searchRequestBuilder
                    .setHighlighterPreTags("<span style=\"color:red\">");
            searchRequestBuilder.setHighlighterPostTags("</span>");*/
            //searchRequestBuilder.setHighlighterPreTags("<em>");
            //searchRequestBuilder.setHighlighterPostTags("<em>");
            // 执行搜索,返回搜索响应信息
            SearchResponse response = searchRequestBuilder.execute()
                    .actionGet();

            // 获取搜索的文档结果
            SearchHits searchHits = response.getHits();
            SearchHit[] hits = searchHits.getHits();
            // ObjectMapper mapper = new ObjectMapper();
            for (int i = 0; i < hits.length; i++) {
                SearchHit hit = hits[i];
                // 将文档中的每一个对象转换json串值
                String json = hit.getSourceAsString();
                // 将json串值转换成对应的实体对象
                Article article = JSONUtils.parseObject(json, Article.class);
                // 获取对应的高亮域
                Map<String, HighlightField> result = hit.highlightFields();
                // 从设定的高亮域中取得指定域
                HighlightField titleField = result.get("title");
                if (titleField !=null) {
                    // 取得定义的高亮标签
                    Text[] titleTexts = titleField.fragments();
                    // 为title串值增加自定义的高亮标签
                    String title = "";
                    for (Text text : titleTexts) {
                        title += text;
                    }
                    article.setTitle(title);
                }
                // 从设定的高亮域中取得指定域
                HighlightField contentField = result.get("content");
                if (contentField !=null) {
                    // 取得定义的高亮标签
                    Text[] contentTexts = contentField.fragments();
                    // 为title串值增加自定义的高亮标签
                    String content = "";
                    for (Text text : contentTexts) {
                        content += text;
                    }
                    // 将追加了高亮标签的串值重新填充到对应的对象
                    article.setContent(content);
                }
                articles.add(article);
//              System.out.println(newsInfo.toString());
                // 打印高亮标签追加完成后的实体对象
            }
            // 防止出现：远程主机强迫关闭了一个现有的连接
//          Thread.sleep(10000);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        System.out.println(JSONUtils.toJSONString(articles));
    }
}
