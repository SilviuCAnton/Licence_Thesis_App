package com.silviucanton.easyorder.orderservice.domain.model;

import com.silviucanton.easyorder.commons.dto.MenuItemDTO;
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
import javax.persistence.ManyToOne;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@javax.persistence.Table(name = "orders", catalog = "easyOrder")
public class Order implements Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "comments")
    private String comments;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "done")
    private boolean done;

    @Column(name = "table_id")
    private long tableId;

    @Column(name = "waiter_id")
    private long waiterId;

    @ElementCollection
    private List<Long> menuItemIds;

    @Override
    public Long getId() {
        return id;
    }
}
