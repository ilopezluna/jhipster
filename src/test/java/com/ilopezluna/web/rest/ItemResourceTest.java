package com.ilopezluna.web.rest;

import com.ilopezluna.Application;
import com.ilopezluna.domain.Item;
import com.ilopezluna.service.ItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItemResource REST controller.
 *
 * @see ItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItemResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CREATED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.print(DEFAULT_CREATED_AT);

    private static final DateTime DEFAULT_UPDATED_AT = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_UPDATED_AT = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_UPDATED_AT_STR = dateTimeFormatter.print(DEFAULT_UPDATED_AT);

    private static final BigDecimal DEFAULT_PRICE = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_PRICE = BigDecimal.ONE;

    private static final Integer DEFAULT_STATUS = 0;
    private static final Integer UPDATED_STATUS = 1;
    private static final String DEFAULT_LATITUDE = "SAMPLE_TEXT";
    private static final String UPDATED_LATITUDE = "UPDATED_TEXT";
    private static final String DEFAULT_LONGITUDE = "SAMPLE_TEXT";
    private static final String UPDATED_LONGITUDE = "UPDATED_TEXT";

    @Inject
    private ItemService itemService;

    private MockMvc restItemMockMvc;

    private Item item;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemResource itemResource = new ItemResource();
        ReflectionTestUtils.setField(itemResource, "itemService", itemService);
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource).build();
    }

    @Before
    public void initTest() {
        item = new Item();
        item.setName(DEFAULT_NAME);
        item.setDescription(DEFAULT_DESCRIPTION);
        item.setCreatedAt(DEFAULT_CREATED_AT);
        item.setUpdatedAt(DEFAULT_UPDATED_AT);
        item.setPrice(DEFAULT_PRICE);
        item.setStatus(DEFAULT_STATUS);
        item.setLatitude(DEFAULT_LATITUDE);
        item.setLongitude(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    public void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemService.findAll().size();

        // Create the Item
        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItem.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testItem.getUpdatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testItem.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testItem.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(itemService.findAll()).hasSize(0);
        // set the field null
        item.setName(null);

        // Create the Item, which fails.
        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(0);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(itemService.findAll()).hasSize(0);
        // set the field null
        item.setDescription(null);

        // Create the Item, which fails.
        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(0);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(itemService.findAll()).hasSize(0);
        // set the field null
        item.setCreatedAt(null);

        // Create the Item, which fails.
        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(0);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(itemService.findAll()).hasSize(0);
        // set the field null
        item.setPrice(null);

        // Create the Item, which fails.
        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(0);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(itemService.findAll()).hasSize(0);
        // set the field null
        item.setStatus(null);

        // Create the Item, which fails.
        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(0);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(itemService.findAll()).hasSize(0);
        // set the field null
        item.setLatitude(null);

        // Create the Item, which fails.
        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(0);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(itemService.findAll()).hasSize(0);
        // set the field null
        item.setLongitude(null);

        // Create the Item, which fails.
        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemService.saveAndFlush(item);

        // Get all the items
        restItemMockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)))
                .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT_STR)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.toString())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.toString())));
    }

    @Test
    @Transactional
    public void getItem() throws Exception {
        // Initialize the database
        itemService.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT_STR))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItem() throws Exception {
        // Initialize the database
        itemService.saveAndFlush(item);

		int databaseSizeBeforeUpdate = itemService.findAll().size();

        // Update the item
        item.setName(UPDATED_NAME);
        item.setDescription(UPDATED_DESCRIPTION);
        item.setCreatedAt(UPDATED_CREATED_AT);
        item.setUpdatedAt(UPDATED_UPDATED_AT);
        item.setPrice(UPDATED_PRICE);
        item.setStatus(UPDATED_STATUS);
        item.setLatitude(UPDATED_LATITUDE);
        item.setLongitude(UPDATED_LONGITUDE);
        restItemMockMvc.perform(put("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(databaseSizeBeforeUpdate);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getCreatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testItem.getUpdatedAt().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testItem.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testItem.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void deleteItem() throws Exception {
        // Initialize the database
        itemService.saveAndFlush(item);

		int databaseSizeBeforeDelete = itemService.findAll().size();

        // Get the item
        restItemMockMvc.perform(delete("/api/items/{id}", item.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Item> items = itemService.findAll();
        assertThat(items).hasSize(databaseSizeBeforeDelete - 1);
    }
}
