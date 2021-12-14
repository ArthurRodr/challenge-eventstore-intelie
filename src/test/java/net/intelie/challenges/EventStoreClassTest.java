/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.intelie.challenges;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EventStoreClassTest {

    /**
     * Test the removeAll() function from EventStore
     * correctly assert the exclusion of all events from store
     * @throws Exception
     */
    @Test
    public void removeAll() throws Exception {
        EventStoreClass eventStore = new EventStoreClass();

        Event event;
        event = new Event("event_item", 10);
        eventStore.insert(event);
        event = new Event("event_item", 20);
        eventStore.insert(event);
        event = new Event("event_item", 30);
        eventStore.insert(event);
        event = new Event("event_item", 40);
        eventStore.insert(event);
        event = new Event("event_item", 50);
        eventStore.insert(event);
        //This call remove all items inside "event_item"
        eventStore.removeAll("event_item");

        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);

        assertEquals(false, eventIterator.moveNext());
    }
    /**
     * Test the removeAll() function from EventStore
     * correctly assert the exclusion of all events from store by type
     * @throws Exception
     */
    @Test
    public void removeAllOtherEvent() throws Exception {
        EventStoreClass eventStore = new EventStoreClass();
        Event event;

        event = new Event("event_item", 10);
        eventStore.insert(event);
        event = new Event("event_item", 20);
        eventStore.insert(event);
        event = new Event("event_item", 30);
        eventStore.insert(event);
        event = new Event("event_item", 40);
        eventStore.insert(event);
        event = new Event("event_item", 50);
        eventStore.insert(event);
        //criate a second Event Type
        event = new Event("event_item2", 10);
        eventStore.insert(event);
        event = new Event("event_item2", 20);
        eventStore.insert(event);
        event = new Event("event_item2", 30);
        eventStore.insert(event);
        event = new Event("event_item2", 40);
        eventStore.insert(event);
        event = new Event("event_item2", 50);
        eventStore.insert(event);

        //remove only "event_item2"
        eventStore.removeAll("event_item2");

        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);
        assertEquals(true, eventIterator.moveNext());
        assertEquals(10, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(20, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(30, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(40, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(50, eventIterator.current().timestamp());
        assertEquals(false, eventIterator.moveNext());
    }

    /**
     * Test the query() function from EventStore
     * correctly assert the use of the query method, considering the periods provided
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception {
        //In this test I use duplicate timestamps, the system should register two distinct events
        EventStoreClass eventStore = new EventStoreClass();
        Event event;
        event = new Event("event_item", 10L);
        eventStore.insert(event);
        event = new Event("event_item", 20L);
        eventStore.insert(event);
        event = new Event("event_item", 30L);
        eventStore.insert(event);
        event = new Event("event_item", 40L);
        eventStore.insert(event);
        event = new Event("event_item", 50L);
        eventStore.insert(event);

        //I limit timestamps between 20 and 40, 10 and 50 will remove
        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 19L, 49L);

        assertEquals(true, eventIterator.moveNext());
        assertEquals(20, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(30, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(40, eventIterator.current().timestamp());
        assertEquals(false, eventIterator.moveNext());
    }
}
