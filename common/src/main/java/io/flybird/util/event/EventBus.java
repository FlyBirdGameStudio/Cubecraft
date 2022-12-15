package io.flybird.util.event;

/**
 * abstraction of event bus implementations.<br/>
 */

public interface EventBus {

    /**
     * call event
     *
     * @param event event
     */
    void callEvent(Event event);

    /**
     * register a event listener
     *
     * @param el handler
     */
    void registerEventListener(EventListener el);

    /**
     * unregister a event handler
     *
     * @param el handler
     */
    void unregisterEventListener(EventListener el);
}