package com.self.study.flashsale.flashsale.adapters.gateway;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.self.study.flashsale.flashsale.domain.models.Events;
import com.self.study.flashsale.flashsale.domain.models.PagedResult;
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
    public Events findByIdForUpdate(UUID id) {
        return eventsRepository.findByIdForUpdate(id).map(EventsEntity::toDomain).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        eventsRepository.deleteById(id);
    }

    @Override
    public List<Events> findAll() {
        return eventsRepository.findAll().stream().map(EventsEntity::toDomain).toList();
    }

    @Override
    public PagedResult<Events> findAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventsEntity> pageResult = eventsRepository.findAll(pageable);
        List<Events> content = pageResult.getContent().stream()
                .map(EventsEntity::toDomain)
                .toList();
        return new PagedResult<>(content, pageResult.getTotalElements(), pageResult.getTotalPages(), page, size);
    }

}
