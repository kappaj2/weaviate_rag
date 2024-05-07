package com.sk.aiprocessor.controller;

import com.sk.aiprocessor.service.OllamaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerQuestionController {

     private final OllamaService ollamaService;

     @GetMapping("/ask")
     public String getCustomerInformation(@RequestParam(value = "question") String question) {
          return ollamaService.askQuestion(question);
     }
}
