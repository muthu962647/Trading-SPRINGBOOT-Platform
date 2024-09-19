package com.platform.repositry;

import com.platform.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepositry extends JpaRepository<Wallet,Long> {
    Wallet findByUserId(Long userId);
}
