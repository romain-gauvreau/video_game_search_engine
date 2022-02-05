package fr.lernejo.search.api;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
class ElasticSearchController {
    private final RestHighLevelClient restHighLevelClient;

    public ElasticSearchController(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @GetMapping("/api/games")
    List<Map<String, Object>> searchGames(@RequestParam(value = "query") List<String> query) throws IOException {
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(query.get(0));
        SearchRequest searchRequest = new SearchRequest().source(SearchSourceBuilder.searchSource().query(queryBuilder));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<Map<String, Object>> games = new ArrayList<>();
        searchResponse.getHits().forEach(hit -> games.add(hit.getSourceAsMap()));
        return games;
    }

    @ExceptionHandler({ElasticsearchException.class, RuntimeException.class, IOException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    List<Map<String, Object>> handleException() {
        return new ArrayList<>();
    }

}
