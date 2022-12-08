package io.flybird.util.event;

/**
 * abstraction of event bus implementations.<br/>
 */

public interface EventBus {
    void callEvent(Event event, Object... param);

    void registerEventListener(EventListener el);

    void unregisterEventListener(EventListener el);
}