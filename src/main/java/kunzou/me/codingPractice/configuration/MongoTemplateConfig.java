package kunzou.me.codingPractice.configuration;

import kunzou.me.codingPractice.converter.LocalDateTimeStringConverter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Collections;


@Configuration
public class MongoTemplateConfig {

  private LocalDateTimeStringConverter localDateTimeStringConverter;

  public MongoTemplateConfig(LocalDateTimeStringConverter localDateTimeStringConverter) {
    this.localDateTimeStringConverter = localDateTimeStringConverter;
  }

  @Bean
  public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoMappingContext context) {
    MongoCustomConversions conversions = new MongoCustomConversions(Collections.singletonList(localDateTimeStringConverter));
    MappingMongoConverter converter =
      new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
    converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    converter.setCustomConversions(conversions);

    converter.afterPropertiesSet();
    return new MongoTemplate(mongoDbFactory, converter);
  }

}
