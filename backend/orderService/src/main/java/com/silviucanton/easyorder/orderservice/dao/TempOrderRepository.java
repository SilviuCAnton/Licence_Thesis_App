package com.silviucanton.easyorder.orderservice.dao;

import com.silviucanton.easyorder.orderservice.domain.model.TempOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TempOrderRepository extends JpaRepository<TempOrder, Long> {
    boolean existsByNicknameAndSessionId(String nickname, String sessionId);

    List<TempOrder> getAllBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);

    Optional<TempOrder> findBySessionIdAndNickname(String sessionId, String nickname);
}
