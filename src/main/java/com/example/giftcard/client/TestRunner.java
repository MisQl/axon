package com.example.giftcard.client;

import com.example.giftcard.command.api.IssueCommand;
import com.example.giftcard.command.api.RedeemCommand;
import com.example.giftcard.query.GiftcardSummary;
import com.example.giftcard.query.GiftcardSummaryQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Override
    public void run(String... args) throws Exception {
        UUID id = UUID.randomUUID();
        commandGateway.sendAndWait(new IssueCommand(id, 100));
        commandGateway.sendAndWait(new RedeemCommand(id, 25));
        commandGateway.sendAndWait(new RedeemCommand(id, 30));

        TimeUnit.SECONDS.sleep(1);

        GiftcardSummary summary = queryGateway.query(new GiftcardSummaryQuery(id), ResponseTypes.instanceOf(GiftcardSummary.class)).join();

        log.debug("Summary: {}", summary);
    }
}
