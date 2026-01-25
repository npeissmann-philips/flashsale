package com.self.study.flashsale.flashsale.adapters.gateway;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.drivers.db.entities.EventsEntity;
import com.self.study.flashsale.flashsale.drivers.db.repository.EventsRepository;

@Component
public class EventsGatewayImpl implements EventsGateway {

    @Autowired
    private EventsRepository eventsRepository;

    @Override
    public Events save(Events event) {
        return eventsRepository.save(event.toEntity()).toDomain();
    }

    @Override
    public Events findById(UUID id) {
        return eventsRepository.findById(id).map(EventsEntity::toDomain).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        eventsRepository.deleteById(id);
    }

    @Override
    public List<Events> findAll() {
        return eventsRepository.findAll().stream().map(EventsEntity::toDomain).toList();
    }

}
