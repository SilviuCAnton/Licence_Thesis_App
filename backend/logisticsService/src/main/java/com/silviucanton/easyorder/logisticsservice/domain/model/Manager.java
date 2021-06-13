package com.silviucanton.easyorder.logisticsservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@javax.persistence.Entity
@DiscriminatorValue("Manager")
public class Manager extends Employee {
}
