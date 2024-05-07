package com.sk.aiprocessor.controller;

import com.sk.aiprocessor.service.OllamaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerQuestionController {

     private final OllamaService ollamaService;

     @Autowired
     private VectorStore vectorStore;

     @GetMapping("/ask")
     public String getCustomerInformation(@RequestParam(value = "question") String question) {
          return ollamaService.askQuestion(question);
     }

     @GetMapping("/askraw")
     public List<Document> getCustomerInformationRaw(@RequestParam(value = "question") String question) {
          List<Document> results = vectorStore.similaritySearch(SearchRequest.defaults()
                  .withQuery(question)
                  .withSimilarityThreshold(0.80) // default 0.75
                  .withTopK(2));
          return results;
     }
}
