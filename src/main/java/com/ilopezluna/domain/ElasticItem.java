package com.ilopezluna.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import javax.persistence.Id;

/**
 * Created by ignasi on 1/5/15.
 */

@Document(indexName = "test-geo-index", type = "geo-class-point-type")
public class ElasticItem {

    @Id
    private Long id;

    private String name;

    private GeoPoint location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
