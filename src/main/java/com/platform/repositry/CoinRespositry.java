package com.platform.repositry;

import com.platform.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRespositry extends JpaRepository<Coin,String> {
}
