package com.main.traveltour.repository;

import com.main.traveltour.entity.SensitiveWords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensitiveWordsRepository extends JpaRepository<SensitiveWords, Integer> {
}
