package com.main.traveltour.restcontroller.customer.rating;

import com.main.traveltour.entity.SensitiveWords;
import com.main.traveltour.service.customer.SensitiveWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class SensitiveWordsAPI {
    @Autowired
    SensitiveWordsService sensitiveWordsService;

    @GetMapping("customer/sensitive-words/findAllSensitiveWords")
    public ResponseEntity<List<String>> findAllSensitiveWords() {
        List<String> sensitiveWords = sensitiveWordsService.findAllSensitiveWords();
        return ResponseEntity.ok(sensitiveWords);
    }
}
