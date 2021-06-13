package com.silviucanton.easyroder.inventoryservice.domain.model;

import com.silviucanton.easyorder.commons.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@javax.persistence.Table(name = "menu_items", catalog = "easyOrder")
public class Item implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "integer")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private float price;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "available")
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Override
    public Long getId() {
        return this.id;
    }

}
