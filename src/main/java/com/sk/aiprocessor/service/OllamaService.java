package com.sk.aiprocessor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OllamaService {

     @Value("classpath:/prompts/customer-info.st")
     private Resource customerPromptResource;

     private final OllamaChatClient ollamaChatClient;

     private final VectorStore vectorStore;

     public OllamaService(OllamaChatClient ollamaChatClient, VectorStore vectorStore) {
          this.ollamaChatClient = ollamaChatClient;
          this.vectorStore = vectorStore;
     }

     public String askQuestion(String question) {

          //   Create the system prompt
          var systemMessage = new SystemMessage("You primary function is to answer questions about the customer data that you have been provided with.");


          List<Document> sourceDocuments =
                  List.of(new Document("The users firstName is Andre and he works for a private comapny", Map.of("meta1", "meta1")),
                          new Document("Andre also like F1 motor racing"),
                          new Document("Andre is also keen on using JAva every day"));
          vectorStore.add(sourceDocuments);

          List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(2));
          List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();

          //   Create the user prompt using the template provided.
          PromptTemplate promptTemplate = new PromptTemplate(customerPromptResource);
          Map<String, Object> promptParameters = new HashMap<>();
          promptParameters.put("question", question);
          promptParameters.put("documents", String.join("\n", contentList));

          Message userMessage = promptTemplate.createMessage(promptParameters);

          //   This works with the customer-info.st with a context part to accept the context for the question.
          //Message userMessage = promptTemplate.createMessage(Map.of("question", question,"context","The users firstName is Andre and he works for a private comapny"));

          //   Create the prompt that we can provide to the llm client
          Prompt prompt = new Prompt(List.of(systemMessage, userMessage));


          //   Return the response

          ChatResponse chatResponse = ollamaChatClient.call(prompt);
          return chatResponse.getResult().getOutput().getContent();
     }
}
