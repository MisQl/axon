package com.example.giftcard.command.api;

import lombok.Value;

import java.util.UUID;

@Value
public class RedeemedEvent {

    UUID id;
    int amount;
}
