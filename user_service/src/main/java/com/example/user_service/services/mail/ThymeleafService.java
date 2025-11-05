package com.example.user_service.services.mail;

import java.util.Map;

public interface ThymeleafService {
    String createContext(String template, Map<String, Object> variables);
}
