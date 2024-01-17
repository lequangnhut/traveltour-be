package com.main.traveltour.service.utils;

import java.util.Map;

public interface ThymeleafService {

    String createContent(String template, Map<String, Object> variables);
}
