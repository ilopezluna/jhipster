package com.ilopezluna.service;

import com.ilopezluna.domain.Item;
import com.ilopezluna.repository.ItemRepository;
import com.ilopezluna.repository.search.ItemSearchRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private ItemSearchRepository itemSearchRepository;

    public void save(Item item) {
        itemRepository.save(item);
        itemSearchRepository.save(item);
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

    public Iterable<Item> search(QueryBuilder query) {
        return itemSearchRepository.search(query);
    }
}