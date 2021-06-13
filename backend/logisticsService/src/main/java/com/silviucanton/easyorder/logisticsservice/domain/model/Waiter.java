package com.silviucanton.easyorder.logisticsservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@DiscriminatorValue("Waiter")
public class Waiter extends Employee {
    @Column(name = "working")
    private boolean working;
}
