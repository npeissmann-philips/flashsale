package com.self.study.flashsale.flashsale.application.useCase.events;

import java.util.UUID;

public interface DeleteEvent {
    void execute(UUID id);
}
