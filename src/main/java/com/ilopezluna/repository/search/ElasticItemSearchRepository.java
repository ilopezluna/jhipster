package com.ilopezluna.repository.search;

import com.ilopezluna.domain.ElasticItem;
import com.ilopezluna.domain.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Item entity.
 */
public interface ElasticItemSearchRepository extends ElasticsearchRepository<ElasticItem, Long> {
}
