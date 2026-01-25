package com.self.study.flashsale.flashsale.application.useCase.events;

import com.self.study.flashsale.flashsale.domain.models.Events;

public interface SaveEvent {
    Events execute(Events event);
}