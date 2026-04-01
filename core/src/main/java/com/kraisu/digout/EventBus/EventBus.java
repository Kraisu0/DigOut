package com.kraisu.digout.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A type-safe event bus for publishing and subscribing to game events.
 *
 * <p>
 * This implementation uses wrapper classes to handle Java's type erasure,
 * allowing type-safe event handling while maintaining proper subscription
 * management.
 *
 * @param <T> the type of data this event will carry
 */
public class EventBus {

    /**
     * Internal wrapper class that encapsulates a typed callback.
     * This is necessary to work around Java's type erasure and enable
     * proper unsubscribe functionality.
     */
    private static class CallbackWrapper<T> {
        private final Consumer<T> callback;

        CallbackWrapper(Consumer<T> callback) {
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        void accept(Object data) {
            callback.accept((T) data);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            CallbackWrapper<?> that = (CallbackWrapper<?>) obj;
            return callback.equals(that.callback);
        }

        @Override
        public int hashCode() {
            return callback.hashCode();
        }
    }

    private static final EventBus INSTANCE = new EventBus();

    /**
     * Returns the singleton instance of the EventBus.
     *
     * @return the global EventBus instance
     */
    public static EventBus getInstance() {
        return INSTANCE;
    }

    /**
     * Private constructor to prevent instantiation.
     * Use {@link #getInstance()} to obtain the singleton instance.
     */
    private EventBus() {
    }

    private final Map<EventType, List<CallbackWrapper<?>>> events = new HashMap<>();

    /**
     * Subscribes to an event type with a typed callback.
     *
     * <p>
     * The returned unsubscribe function can be called to remove this subscription.
     *
     * @param event    the event type to subscribe to
     * @param callback the callback to invoke when the event is emitted
     * @param <T>      the type of data expected by the callback
     * @return a Runnable that, when invoked, will unsubscribe this callback
     */
    public <T> Runnable subscribe(EventType event, Consumer<T> callback) {
        CallbackWrapper<T> wrapper = new CallbackWrapper<>(callback);
        events.computeIfAbsent(event, k -> new ArrayList<>()).add(wrapper);
        return () -> {
            List<CallbackWrapper<?>> callbacks = events.get(event);
            if (callbacks != null) {
                callbacks.remove(wrapper);
            }
        };
    }

    /**
     * Emits an event to all subscribers.
     *
     * <p>
     * All callbacks registered for this event type will be invoked
     * with the provided data. The data type should match the expected
     * type of the subscribers.
     *
     * @param event the event type to emit
     * @param data  the data to pass to all subscribers
     * @param <T>   the type of the event data
     */
    @SuppressWarnings("unchecked")
    public <T> void emit(EventType event, T data) {
        List<CallbackWrapper<?>> callbacks = events.get(event);
        if (callbacks != null) {
            // Create a copy to avoid ConcurrentModificationException if callbacks modify
            // the list
            List<CallbackWrapper<?>> callbacksCopy = new ArrayList<>(callbacks);
            for (CallbackWrapper<?> wrapper : callbacksCopy) {
                ((CallbackWrapper<T>) wrapper).accept(data);
            }
        }
    }

    /**
     * Removes all subscriptions for a given event type.
     *
     * @param event the event type to clear
     */
    public void clear(EventType event) {
        events.remove(event);
    }

    /**
     * Removes all subscriptions for all event types.
     */
    public void clearAll() {
        events.clear();
    }
}
