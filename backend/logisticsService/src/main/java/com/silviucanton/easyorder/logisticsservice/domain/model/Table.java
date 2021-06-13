package com.silviucanton.easyorder.logisticsservice.domain.model;

import com.silviucanton.easyorder.commons.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@javax.persistence.Table(name = "tables", catalog = "easyOrder")
public class Table implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "unique_id")
    private int uniqueId;

    @Override
    public Long getId() {
        return id;
    }
}
