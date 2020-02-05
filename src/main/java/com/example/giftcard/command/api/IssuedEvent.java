package com.example.giftcard.command.api;

import lombok.Value;

import java.util.UUID;

@Value
public class IssuedEvent {

    UUID id;
    int amount;
}
