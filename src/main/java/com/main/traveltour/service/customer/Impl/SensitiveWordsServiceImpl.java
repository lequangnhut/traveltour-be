package com.main.traveltour.service.customer.Impl;

import com.main.traveltour.entity.SensitiveWords;
import com.main.traveltour.repository.SensitiveWordsRepository;
import com.main.traveltour.service.customer.SensitiveWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensitiveWordsServiceImpl implements SensitiveWordsService {
    @Autowired
    private SensitiveWordsRepository sensitiveWordsRepository;
    @Override
    public List<String> findAllSensitiveWords() {
        return sensitiveWordsRepository.findAll().stream()
                .map(SensitiveWords::getWord)
                .toList();
    }
}
