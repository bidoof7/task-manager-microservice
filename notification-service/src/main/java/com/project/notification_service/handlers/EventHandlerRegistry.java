package com.project.notification_service.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EventHandlerRegistry {
    private final Map<String, TaskEventHandler> handlers;

    public EventHandlerRegistry(List<TaskEventHandler> handlerList) {
        this.handlers = handlerList.stream().collect(Collectors.toMap(TaskEventHandler::eventType, Function.identity()));
    }

    public TaskEventHandler getHandler(String eventType) {
        TaskEventHandler handler = handlers.get(eventType);

        if (handler == null) {
            throw new IllegalArgumentException(
                    "Unsupported event type: " + eventType
            );
        }

        return handler;
    }
}
