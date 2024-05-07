package com.sk.aiprocessor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.WeaviateVectorStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.ai.vectorstore.WeaviateVectorStore;
import org.springframework.context.annotation.Bean;

@Slf4j
@Configuration
public class VectorStoreConfig {

     private OllamaApi ollamaApi = new OllamaApi();

     @Bean
     public OllamaEmbeddingClient ollamaEmbeddingClient(OllamaApi ollamaApi) {
          var embeddingClient = new OllamaEmbeddingClient(ollamaApi)
                  .withDefaultOptions(OllamaOptions.create()
                          .withModel("llama3"));
          return embeddingClient;
     }

     @Bean
     public VectorStore vectorStore(OllamaEmbeddingClient ollamaEmbeddingClient) {
          WeaviateVectorStore.WeaviateVectorStoreConfig config = WeaviateVectorStore.WeaviateVectorStoreConfig.builder()
                  .withScheme("http")
                  .withHost("localhost:8081")

                  // Define the metadata fields to be used
                  // in the similarity search filters.

                  // Add payload properties
//                  .withFilterableMetadataFields(List.of(
//                          MetadataField.text("country"),
//                          MetadataField.number("year"),
//                          MetadataField.bool("active")))
                  // Consistency level can be: ONE, QUORUM, or ALL.
                  .withConsistencyLevel(WeaviateVectorStore.WeaviateVectorStoreConfig.ConsistentLevel.ONE)
                  .build();

          return new WeaviateVectorStore(config, ollamaEmbeddingClient);
     }
}