package com.platform.repositry;

import com.platform.model.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepositry extends JpaRepository<WatchList,Long> {
    WatchList findByUserId(Long userId);
}
