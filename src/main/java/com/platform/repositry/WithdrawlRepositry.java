package com.platform.repositry;

import com.platform.model.Withdrawl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawlRepositry extends JpaRepository<Withdrawl,Long> {
    List<Withdrawl> findByUserId(Long userId);
}
