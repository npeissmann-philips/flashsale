package com.self.study.flashsale.flashsale.adapters.gateway;

import java.util.List;
import java.util.UUID;

import com.self.study.flashsale.flashsale.domain.models.Events;

public interface EventsGateway {
    Events save(Events event);

    Events findById(UUID id);

    void delete(UUID id);

    List<Events> findAll();
}