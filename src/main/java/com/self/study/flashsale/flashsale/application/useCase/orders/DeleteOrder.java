package com.self.study.flashsale.flashsale.application.useCase.orders;

import java.util.UUID;

public interface DeleteOrder {
    void execute(UUID id);
}
