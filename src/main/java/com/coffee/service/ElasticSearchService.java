package com.coffee.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
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

    public void index(List<CoffeeDto> coffeeDtoList) throws IOException {
        Date currentDate = new Date();
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (CoffeeDto coffeeDto : coffeeDtoList) {
            coffeeDto.setEnabled(true);
            coffeeDto.setLastUpdated(currentDate    );

            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(coffeeDto.getId())
                            .document(coffeeDto)
                    )
            );
        }

        BulkResponse result = esClient.bulk(br.build());

        // Log errors, if any
        if (result.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
    }
}
