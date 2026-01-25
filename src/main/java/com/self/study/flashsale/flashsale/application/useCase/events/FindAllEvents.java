package com.self.study.flashsale.flashsale.application.useCase.events;

import java.util.List;

import com.self.study.flashsale.flashsale.domain.models.Events;

public interface FindAllEvents {
    List<Events> execute();
}