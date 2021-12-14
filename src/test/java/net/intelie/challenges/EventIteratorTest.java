/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.intelie.challenges;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

//simple Test class, to test Iterator Functions
public class EventIteratorTest {

    /**
     * Test the moveNext() function from EventIterator
     * correctly assert if the pointer is moved and the final from List
     * @throws Exception
     */
    @Test
    public void moveToNextEvent() throws Exception {
        EventStoreClass eventStore = new EventStoreClass();
        Event event = new Event("event_item", 50);
        eventStore.insert(event);
        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);

        assertEquals(true, eventIterator.moveNext());
        assertEquals(event, eventIterator.current());
        assertEquals(false, eventIterator.moveNext());
    }

    /**
     * Test the current() function from EventIterator
     * correctly assert whether the method returns the current event, according to the pointer
     * @throws Exception
     */
    @Test
    public void currentEvent() throws Exception {
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
        event = new Event("event_item", 126);
        eventStore.insert(event);

        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);

        assertEquals(true, eventIterator.moveNext());
        assertEquals(10, eventIterator.current().timestamp());
        assertEquals(10, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(20, eventIterator.current().timestamp());
        assertEquals(20, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        //jump timestamp 30
        assertEquals(true, eventIterator.moveNext());
        assertEquals(40, eventIterator.current().timestamp());
        assertEquals(40, eventIterator.current().timestamp());
        assertEquals(false, eventIterator.moveNext());

    }

    /**
     * Test the moveNext() function from EventIterator
     * correctly assert move next in empty type
     * @throws Exception
     */
    @Test
    public void moveNextEmpty() throws Exception {
        EventStoreClass eventStore = new EventStoreClass();
        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);
        assertEquals(false, eventIterator.moveNext()); // Ok
    }

    /**
     * Test the current() function from EventIterator
     * correctly assert if IllegalStateException is fire when use current() without moveNext
     * @throws Exception
     */
    @Test
    public void moveNextPointerNull() throws Exception {
        EventStoreClass eventStore = new EventStoreClass();

        eventStore.insert(new Event("event_item", 0));

        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);
        try{
            eventIterator.current();
        }catch(Exception e) {
            assertEquals(true, e instanceof IllegalStateException);
        }
    }

    /**
     * Test the current() function from EventIterator
     * correctly assert if IllegalStateException is fire when use current() after the list reaches the end
     * @throws Exception
     */
    @Test
    public void moveToTheEnd() throws Exception {
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
        event = new Event("event_item", 126);//this item will remove in query
        eventStore.insert(event);

        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);
        assertEquals(true, eventIterator.moveNext());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(false, eventIterator.moveNext());
        try{
            eventIterator.current();
        }catch(Exception e) {
            assertEquals(true, e instanceof IllegalStateException);
        }
    }
    /**
     * Test the remove() function from EventIterator
     * correctly assert if IllegalStateException is fire when use remove() in empty list
     * @throws Exception
     */
    @Test
    public void removeEmpty() throws Exception {
        EventStoreClass eventStore = new EventStoreClass();
        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);
        //test Exception if try get current after Iterator ends
        try{
            eventIterator.remove();
        }catch(Exception e) {
            assertEquals(true, e instanceof IllegalStateException);
        }
    }

    /**
     * Test the remove() function from EventIterator
     * correctly assert the exclusion of events from iterator and store
     * @throws Exception
     */
    @Test
    public void remove() throws Exception {
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

        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);
        //remove 20 and 50 and see if the items will remove.
        assertEquals(true, eventIterator.moveNext());
        assertEquals(10, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(20, eventIterator.current().timestamp());
        eventIterator.remove();
        assertEquals(30, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(40, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(50, eventIterator.current().timestamp());
        eventIterator.remove();
        assertEquals(false, eventIterator.moveNext());
        eventIterator.close();
        eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 123L);
        assertEquals(true, eventIterator.moveNext());
        assertEquals(10, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(30, eventIterator.current().timestamp());
        assertEquals(true, eventIterator.moveNext());
        assertEquals(40, eventIterator.current().timestamp());
        assertEquals(false, eventIterator.moveNext());
    }

    /**
     * Test the remove() function from EventIterator
     * correctly assert the exclusion of events from iterator and store with loop
     * @throws Exception
     */
    @Test
    public void removeWithLooping() {
        EventStoreClass eventStore = new EventStoreClass();
        Event event;
        event = new Event("event_item", 10);
        eventStore.insert(event);
        event = new Event("event_item2", 20);
        eventStore.insert(event);
        event = new Event("event_item3", 30);
        eventStore.insert(event);
        event = new Event("event_item", 40);
        eventStore.insert(event);
        event = new Event("event_item", 50);
        eventStore.insert(event);
        event = new Event("event_item2", 60);
        eventStore.insert(event);
        event = new Event("event_item3", 70);
        eventStore.insert(event);
        event = new Event("event_item3", 80);
        eventStore.insert(event);
        event = new Event("event_item2", 90);
        eventStore.insert(event);
        event = new Event("event_item", 100);
        eventStore.insert(event);

        try (EventIteratorClass iterator = (EventIteratorClass) eventStore.query("event_item", 10L, 101L)) {
            while (iterator.moveNext()) {
                if (iterator.current().timestamp() < 20 || iterator.current().timestamp() > 50L){
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventIteratorClass iterator  = (EventIteratorClass) eventStore.query("event_item", 0, 101L);
        assertEquals(true, iterator.moveNext());
        assertEquals(40, iterator.current().timestamp());
        assertEquals(true, iterator.moveNext());
        assertEquals(50, iterator.current().timestamp());
        assertEquals(false, iterator.moveNext());

    }


}
