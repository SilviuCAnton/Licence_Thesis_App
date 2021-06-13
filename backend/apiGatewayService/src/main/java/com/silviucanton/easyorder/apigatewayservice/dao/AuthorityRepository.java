package com.silviucanton.easyorder.apigatewayservice.dao;

import com.silviucanton.easyorder.apigatewayservice.domain.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    public List<Authority> findByNameIn(List<String> names);
}
