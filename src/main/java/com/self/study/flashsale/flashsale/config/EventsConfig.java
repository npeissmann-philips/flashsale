package com.self.study.flashsale.flashsale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.self.study.flashsale.flashsale.adapters.gateway.EventsGateway;
import com.self.study.flashsale.flashsale.application.useCase.events.DeleteEvent;
import com.self.study.flashsale.flashsale.application.useCase.events.DeleteEventImpl;
import com.self.study.flashsale.flashsale.application.useCase.events.FindAllEvents;
import com.self.study.flashsale.flashsale.application.useCase.events.FindAllEventsImpl;
import com.self.study.flashsale.flashsale.application.useCase.events.FindEventById;
import com.self.study.flashsale.flashsale.application.useCase.events.FindEventByIdImpl;
import com.self.study.flashsale.flashsale.application.useCase.events.SaveEvent;
import com.self.study.flashsale.flashsale.application.useCase.events.SaveEventImpl;

@Configuration
public class EventsConfig {

    @Bean
    public SaveEvent saveEvent(EventsGateway eventsGateway) {
        return new SaveEventImpl(eventsGateway);
    }

    @Bean
    public FindEventById findEventById(EventsGateway eventsGateway) {
        return new FindEventByIdImpl(eventsGateway);
    }

    @Bean
    public FindAllEvents findAllEvents(EventsGateway eventsGateway) {
        return new FindAllEventsImpl(eventsGateway);
    }

    @Bean
    public DeleteEvent deleteEvent(EventsGateway eventsGateway) {
        return new DeleteEventImpl(eventsGateway);
    }
}
