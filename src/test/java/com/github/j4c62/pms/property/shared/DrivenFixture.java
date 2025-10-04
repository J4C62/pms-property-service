package com.github.j4c62.pms.property.shared;

import static org.mockito.Mockito.mock;

import com.github.j4c62.pms.property.domain.aggregate.vo.PropertyEvents;
import com.github.j4c62.pms.property.domain.driven.PropertyEventPublisher;
import com.github.j4c62.pms.property.domain.driven.PropertyEventStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(AggregateFixture.class)
public class DrivenFixture {

  @Bean
  @ConditionalOnMissingBean(PropertyEventPublisher.class)
  public PropertyEventPublisher propertyEventPublisher() {
    return mock(PropertyEventPublisher.class);
  }

  @Bean
  @ConditionalOnMissingBean(PropertyEventStore.class)
  public PropertyEventStore propertyEventStore(PropertyEvents propertyEvents) {
    return id ->
        PropertyEvents.of(
            propertyEvents.events().stream()
                .filter(propertyEvent -> propertyEvent.id().equals(id))
                .toList());
  }
}
