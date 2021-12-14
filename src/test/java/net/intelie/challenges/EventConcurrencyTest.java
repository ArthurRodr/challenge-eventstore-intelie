package net.intelie.challenges;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventConcurrencyTest {

    /**
     * Insert Multiple Items with concurrency.
     * correctly assert the amount of items added to a type
     * @throws Exception
     */
    @Test
    public void MultipleInserts() throws Exception {
        EventStoreClass eventStore = new EventStoreClass();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 0; i<1000;i++){
                        Event event = new Event("event_item", i);
                        eventStore.insert(event);
                    }


                } catch (Exception ex) {
                }

            }
        });
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 0; i<1000;i++){
                        Event event = new Event("event_item2", i);
                        eventStore.insert(event);
                    }
                } catch (Exception e) {
                }

            }
        });
        Thread t3 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 1001; i<=2000;i++){
                        Event event = new Event("event_item2", i);
                        eventStore.insert(event);
                    }
                } catch (Exception e) {
                }

            }
        });
        Thread t4 = new Thread( new Runnable() {

            public void run() {
                try {
                    Event event = new Event("event_item", 40);
                    eventStore.insert(event);
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();

        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 5000);
        assertEquals(1000, getIteratorSize(eventIterator));
        eventIterator = (EventIteratorClass) eventStore.query("event_item2", 0, 5000);
        assertEquals(2000, getIteratorSize(eventIterator));
    }
    /**
     * Remove Multiple Items with concurrency.
     * correctly assert the amount of items removed from a type
     * @throws Exception
     */
    @Test
    public void removingMultipleEvents() throws Exception {
        EventStoreClass eventStore = new EventStoreClass();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 0; i<1000;i++){
                        Event event = new Event("event_item", i);
                        eventStore.insert(event);
                    }

                } catch (Exception ex) {
                }

            }
        });
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 0; i<1000;i++){
                        Event event = new Event("event_item2", i);
                        eventStore.insert(event);
                    }
                } catch (Exception e) {
                }

            }
        });
        Thread t3 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 1001; i<=2000;i++){
                        Event event = new Event("event_item2", i);
                        eventStore.insert(event);
                    }
                } catch (Exception e) {
                }

            }
        });
        Thread t4 = new Thread( new Runnable() {

            public void run() {
                try {
                    EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 1001);
                    eventIterator.moveNext();
                    for(int i = 0; i<600;i++){
                        eventIterator.remove();
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        });
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(50);
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 1001);
        assertEquals(400, getIteratorSize(eventIterator));

    }

    /**
     * Remove all Items from a type with concurrency.
     * correctly assert if the type is empty
     * @throws Exception
     */
    @Test
    public void removingAllEventItems() throws Exception {
        EventStoreClass eventStore = new EventStoreClass();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 0; i<1000;i++){
                        Event event = new Event("event_item", i);
                        eventStore.insert(event);
                    }

                } catch (Exception ex) {
                }

            }
        });
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 0; i<1000;i++){
                        Event event = new Event("event_item2", i);
                        eventStore.insert(event);
                    }
                } catch (Exception e) {
                }

            }
        });
        Thread t3 = new Thread(new Runnable() {
            public void run() {
                try {
                    for(int i = 1001; i<=2000;i++){
                        Event event = new Event("event_item2", i);
                        eventStore.insert(event);
                    }
                } catch (Exception e) {
                }

            }
        });
        Thread t4 = new Thread( new Runnable() {

            public void run() {
                try {
                    eventStore.removeAll("event_item");

                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        });
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(50);
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        EventIteratorClass eventIterator = (EventIteratorClass) eventStore.query("event_item", 0, 1001);
        assertEquals(0, getIteratorSize(eventIterator));

    }

    /**
     * Helper to count the items inside the iterator
     * @param iterator
     * @return
     */
    private int getIteratorSize(EventIteratorClass iterator) {
        int count = 0;
        if (iterator != null) {
            while (iterator.moveNext()) {
                count++;
            }
        }
        return count;
    }

}
