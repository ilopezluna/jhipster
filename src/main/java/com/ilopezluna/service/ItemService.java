package com.ilopezluna.service;

import com.ilopezluna.domain.ElasticItem;
import com.ilopezluna.domain.Item;
import com.ilopezluna.repository.ItemRepository;
import com.ilopezluna.repository.search.ElasticItemSearchRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private ElasticItemSearchRepository itemSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    public void save(Item item) {
        itemRepository.save(item);
        ElasticItem elasticItem = getElasticItem(item);
        itemSearchRepository.save(elasticItem);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }

    public void delete(Long id) {
        itemRepository.delete(id);
        itemSearchRepository.delete(id);
    }

    public void saveAndFlush(Item item) {
        itemRepository.saveAndFlush(item);
    }

    public Iterable<ElasticItem> search(QueryBuilder query) {
        return itemSearchRepository.search(query);
    }

    public Iterable<ElasticItem> searchByGeoLocation(double latitude, double longitude) {
        CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(
                new Criteria("location").within(new GeoPoint(latitude, longitude), "100km"));
        return elasticsearchTemplate.queryForList(geoLocationCriteriaQuery, ElasticItem.class);
    }

    public void save(List<Item> items) {
        itemRepository.save(items);
        List<ElasticItem> elasticItems = new ArrayList<>();
        for (Item item : items) {
            ElasticItem elasticItem = getElasticItem(item);
            elasticItems.add(elasticItem);
        }
        itemSearchRepository.save(elasticItems);
    }

    private ElasticItem getElasticItem(Item item) {
        ElasticItem elasticItem = new ElasticItem();
        elasticItem.setId(item.getId());
        elasticItem.setName(item.getName());
        elasticItem.setDescription(item.getDescription());

        try {
            Double latitude = Double.parseDouble(item.getLatitude());
            Double longitude = Double.parseDouble(item.getLongitude());
            elasticItem.setLocation( new GeoPoint(latitude, longitude) );
        } catch (NumberFormatException ex) {
            log.error("Invalid latitude or longitude for item: " + item.getId()
                    + ", latitude: " +  item.getLatitude() + ", longitude: " + item.getLongitude());
        }
        return elasticItem;
    }
}
