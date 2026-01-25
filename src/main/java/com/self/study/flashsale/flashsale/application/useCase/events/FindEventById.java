package com.self.study.flashsale.flashsale.application.useCase.events;

import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Events;

public interface FindEventById {
    Events execute(UUID id);
}