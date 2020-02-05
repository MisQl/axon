package com.example.giftcard.query;

import com.example.giftcard.command.api.IssuedEvent;
import com.example.giftcard.command.api.RedeemedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class SummaryProjector {

    private final EntityManager entityManager;

    @EventHandler
    public void on(IssuedEvent event) {
        log.debug("Handle event: {}", event);
        entityManager.persist(new GiftcardSummary(event.getId(), event.getAmount(), event.getAmount()));
    }

    @EventHandler
    public void on(RedeemedEvent event) {
        log.debug("Handle event: {}", event);
        entityManager.find(GiftcardSummary.class, event.getId()).remainingValue -= event.getAmount();
    }

    @QueryHandler
    public GiftcardSummary handle(GiftcardSummaryQuery query) {
        return entityManager.find(GiftcardSummary.class, query.getId());
    }
}
