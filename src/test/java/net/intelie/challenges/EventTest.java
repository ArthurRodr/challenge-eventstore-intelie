package net.intelie.challenges;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventTest {
    @Test
    public void thisIsAWarning() throws Exception {
        Event event = new Event("some_type", 123L);

        //THIS IS A WARNING:
        //Some of us (not everyone) are coverage freaks.
        assertEquals(123L, event.timestamp());
        assertEquals("some_type", event.type());
    }
    @Test
    public void concurrency() throws Exception {
        EventConcurrencyTest evt = new EventConcurrencyTest();
        evt.MultipleInserts();
        evt.removingMultipleEvents();
        evt.removingAllEventItems();
    }

    @Test
    public void iteratorTests() throws Exception {
        EventIteratorTest evt = new EventIteratorTest();
        evt.currentEvent();
        evt.moveNextEmpty();
        evt.remove();
        evt.moveToNextEvent();
        evt.moveNextPointerNull();
        evt.moveToTheEnd();
        evt.removeWithLooping();
        evt.removeEmpty();
    }

    @Test
    public void storeTests() throws Exception {
        EventStoreClassTest evt = new EventStoreClassTest();
        evt.removeAll();
        evt.testQuery();
        evt.removeAllOtherEvent();
    }
}