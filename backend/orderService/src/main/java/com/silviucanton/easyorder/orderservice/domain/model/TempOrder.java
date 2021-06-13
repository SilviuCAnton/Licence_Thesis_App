package com.silviucanton.easyorder.orderservice.domain.model;


import com.silviucanton.easyorder.commons.model.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
import java.awt.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@javax.persistence.Table(name = "tempOrders", catalog = "easyOrder", uniqueConstraints = {@UniqueConstraint(columnNames = {"nickname", "sessionId"})})
public class TempOrder implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ElementCollection
    private List<Long> menuItemIds;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "sessionId")
    private String sessionId;

    @Override
    public Long getId() {
        return id;
    }
}