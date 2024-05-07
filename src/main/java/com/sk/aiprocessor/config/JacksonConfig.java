package com.sk.aiprocessor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;

@Configuration
public class JacksonConfig {

     /**
      * ObjectMapper for JSON data.
      *
      * @return {@link ObjectMapper} Return a customer Jackson ObjectMapper instance.
      */
     @Bean(name = "objectMapper")
     @Qualifier("objectMapper")
     @Primary
     public ObjectMapper objectMapper() {
          var javaTimeModule = new JavaTimeModule();

          var objectMapper = new ObjectMapper();
          objectMapper = objectMapper.registerModule(javaTimeModule);

          objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                  .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                  .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                  .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                  .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                  .getFactory().configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);

          objectMapper = objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
          objectMapper = objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
          objectMapper = objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
          objectMapper = objectMapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));

          return objectMapper;
     }

     /**
      * This bean is a message converter for the Octet Stream that UI will use to upload files.
      * There seems to be a difference in the different clients being used to upload files. With Postman, for example, you will not need
      * this, but some JavaScript libraries do need this. Believe this to be part of the content negotiation that goes on and the fallback sequence if
      * a specific format is not supported by the rest endpoint.
      *
      * @return the message converter bean.
      */
     @Bean
     public MappingJackson2HttpMessageConverter customizedJacksonMessageConverter() {
          var converter = new MappingJackson2HttpMessageConverter();
          converter.setSupportedMediaTypes(
                  Arrays.asList(
                          MediaType.APPLICATION_JSON,
                          new MediaType("application", "*+json"),
                          MediaType.APPLICATION_OCTET_STREAM));
          converter.setObjectMapper(objectMapper());
          return converter;
     }
}
