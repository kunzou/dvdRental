package kunzou.me.codingPractice.configuration;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import kunzou.me.codingPractice.converter.LocalDateTimeStringConverter;


@Configuration
public class MongoTemplateConfig {

  private LocalDateTimeStringConverter localDateTimeStringConverter;

  public MongoTemplateConfig(LocalDateTimeStringConverter localDateTimeStringConverter) {
    this.localDateTimeStringConverter = localDateTimeStringConverter;
  }

  @Bean
  public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory, MongoMappingContext context) {
    MongoCustomConversions conversions = new MongoCustomConversions(Collections.singletonList(localDateTimeStringConverter));
    MappingMongoConverter converter =
      new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
    converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    converter.setCustomConversions(conversions);

    converter.afterPropertiesSet();
    return new MongoTemplate(mongoDbFactory, converter);
  }

}
