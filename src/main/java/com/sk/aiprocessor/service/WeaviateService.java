package com.sk.aiprocessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sk.aiprocessor.dto.CustomerObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeaviateService {

     @Qualifier("objectMapper")
     private final ObjectMapper objectMapper;

     public void loadJsonObject(CustomerObject customerObject){

     }
}
