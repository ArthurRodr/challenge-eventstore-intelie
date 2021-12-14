package net.intelie.challenges;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class EventStoreClass implements EventStore {

    /**
     * HashMap has a better performance in relation to the use of the ArrayList, besides, being able to categorize the keys by type,
     the content inside the event list becomes more condensed, when we use the "query" method.
     *
     *To store the Events, I'm using ConcurrentSkipListMap<Long,Event>. We needed a framework that could work with
     * simultaneous puts and gets, and that kind of operation is hampered by common Lists and Maps, which work synchronously.
     * ConcurrentSkipListMap in addition to providing good navigability features, allows us to create Thread-Safe structures.
     * Below, I created a structure that stores the types in HashMap and a <key mapping: timestamp / value:Event>
     **/
    private final Map<String, ConcurrentSkipListMap<Long,Event>> events;

    public EventStoreClass() {
        this.events = new HashMap();
    }

    /**
     * Using Hashmap, I created this method to help search by key(type)
     * using syncronized to thread-safe
     * @param type
     * @return ConcurrentSkipListMap<Long,Event>
     */
    public synchronized ConcurrentSkipListMap<Long,Event> getByType(String type) {
        return events.get(type);
    }

    /**
     * In the insert method, an event is provided, from that event, by its type, is filtered inside the HashMap by key.
     * After that, the internal map is updated, passing the timestamp as a key and the event itself as a value.
     * using syncronized to thread-safe
     * @param event
     */
    @Override
    public synchronized void insert(Event event) {
        if (!events.containsKey(event.type())) {
            events.put(event.type(), new ConcurrentSkipListMap());
        }
        getByType(event.type()).put(event.timestamp(),event);
    }

    /**
     * remove the HashMap by key (type)
     * using syncronized to thread-safe
     * @param type
     */
    @Override
    public synchronized void removeAll(String type) {
        //remove the HashMap by key (type)
        events.remove(type);
    }

    /**
     * In the query method, after obtaining the Map <timestamp, Event>,
     * it will be necessary for the system to be able to filter the start and end range of timestamps contained as key in a simplified way.
     * To solve this demand I used the subMap method, which allows this filtering, in addition to having the subMap(start(inclusive), end(exclusive)) structure requested in the challenge.
     * After this extraction, I convert to LinkedList and provide the Iterator to work with this data.
     * @param type      The type we are querying for.
     * @param start
     * @param end
     * @return EventIterator
     */
    @Override
    public synchronized EventIterator query(String type, long start, long end) {
        if (!events.containsKey(type)) {
            events.put(type, new ConcurrentSkipListMap());
        }
        ConcurrentSkipListMap<Long,Event> eventList = getByType(type);

        LinkedList<Event> eventsFiltered = new LinkedList(eventList.subMap(start, end).values());
        EventIteratorClass eventIterator = new EventIteratorClass(this,eventsFiltered);

        return eventIterator;

    }

    /**
     * A helper to remove the event from store.
     * @param timestamp
     * @param event
     */
    public synchronized void removeEvent(long timestamp,Event event) {
        //method to remove specific event, the Iterator Class call this method to update the HashMap.
        ConcurrentSkipListMap<Long,Event> eventList = getByType(event.type());
        eventList.remove(timestamp,event);
        events.put(event.type(), eventList);
    }

}