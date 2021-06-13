package com.silviucanton.easyorder.commons.model;

import java.io.Serializable;

public interface Entity<ID extends Serializable> extends Serializable {
    ID getId();
}
