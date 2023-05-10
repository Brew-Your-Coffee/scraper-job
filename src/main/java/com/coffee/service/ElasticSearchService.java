package com.coffee.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import com.coffee.domain.CoffeeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElasticSearchService {

    @Value("${es.index:coffee}")
    private String indexName;

    private final ElasticsearchClient esClient;

    public void index(List<CoffeeDto> coffeeDtoList, Date currentDate) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (CoffeeDto coffeeDto : coffeeDtoList) {
            coffeeDto.setEnabled(true);
            coffeeDto.setLastUpdated(currentDate);

            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(coffeeDto.getId())
                            .document(coffeeDto)
                    )
            );
        }

        BulkResponse result = esClient.bulk(br.build());

        if (result.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
    }

    public void updateDisabledEntities(Date currentDate) throws IOException {
        UpdateByQueryRequest request = new UpdateByQueryRequest.Builder()
                .index(indexName)
                .query(fn -> fn.range(rq -> rq.field("lastUpdated").lt(JsonData.of(currentDate))))
                .script(fn -> fn.inline(is -> is.source("ctx._source.enabled = false")))
                .build();

        esClient.updateByQuery(request);
    }
}
