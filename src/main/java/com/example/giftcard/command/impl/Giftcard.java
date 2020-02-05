package com.example.giftcard.command.impl;

import com.example.giftcard.command.api.IssueCommand;
import com.example.giftcard.command.api.IssuedEvent;
import com.example.giftcard.command.api.RedeemCommand;
import com.example.giftcard.command.api.RedeemedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
@Slf4j
public class Giftcard {

    @AggregateIdentifier
    private UUID id;
    private int remainingValue;

    @CommandHandler
    public Giftcard(IssueCommand command) {
        log.debug("command - create giftcard {}", command);
        if (command.getAmount() < 0) throw new RuntimeException("command.getAmount() < 0");
        apply(new IssuedEvent(command.getId(), command.getAmount()));
    }

    @CommandHandler
    public void handle(RedeemCommand command) {
        log.debug("command - change {}", command);
        if (command.getAmount() < 0) throw new RuntimeException("command.getAmount() < 0");
        if (command.getAmount() > remainingValue) throw new RuntimeException("command.getAmount() > remainingValue");
        apply(new RedeemedEvent(command.getId(), command.getAmount()));
    }

    @EventSourcingHandler
    public void on(IssuedEvent event) {
        log.debug("event - create giftcard {}", event);
        id = event.getId();
        remainingValue = event.getAmount();
    }

    @EventSourcingHandler
    public void on(RedeemedEvent event) {
        log.debug("event - change {}", event);
        id = event.getId();
        remainingValue -= event.getAmount();
    }
}
