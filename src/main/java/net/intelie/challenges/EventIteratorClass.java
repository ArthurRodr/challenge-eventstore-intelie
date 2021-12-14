package net.intelie.challenges;

import java.util.*;
import java.util.function.Consumer;

public class EventIteratorClass implements EventIterator{
    private final LinkedList<Event> events;
    private final EventStoreClass eventStore;
    int pointerPosition = -1;

    /**
     * provides information from other classes to the iterator
     * */
    public EventIteratorClass(EventStoreClass eventStore,LinkedList<Event> events) {
        this.events = events;
        this.eventStore = eventStore;
    }

    /**
     * Method to control the position of iterator
     * return false if the pointer has reached the end of the vector
     * return true if the vector has next element
     * */
    @Override
    public boolean moveNext() {
        pointerPosition++;
        if(pointerPosition >= events.size()) {
            return false;
        }
        return true;
    }

    /**
     * Method to return the current event on iterator
     * throw IllegalStateException if the pointerPosition never moved or reached the end;
     * return the current event in vector
     * */
    @Override
    public  Event current() {
        if(pointerPosition == -1 || pointerPosition >= events.size()) {
            throw new IllegalStateException();
        }
        return events.get(pointerPosition);
    }

    /**
     * Method to remove a event from store
     * throw IllegalStateException if the pointerPosition never moved or reached the end;
     * call the removeEvent method from EventStoreClass to update items
     * remove event from iterator too
     * */
    @Override
    public  void remove() {
        if(pointerPosition == -1 || pointerPosition >= events.size()) {
            throw new IllegalStateException();
        }

        Event event = events.get(pointerPosition);
        eventStore.removeEvent(event.timestamp(),event);
        events.remove(pointerPosition);
    }

    /**
     * Method to reset the iterator
     * */
    @Override
    public  void close() throws Exception {
        events.removeAll(events);
    }

}