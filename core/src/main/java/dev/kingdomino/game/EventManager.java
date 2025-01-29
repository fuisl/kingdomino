package dev.kingdomino.game;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventManager {
    private static EventManager instance;
    // Each queue is a ConcurrentLinkedQueue of Events under a specific category/key
    private Map<String, ConcurrentLinkedQueue<Event>> queues;

    // For controlling how often to process event queues
    private float queueTimer;
    private float queueDt; // e.g., process queue every 1/60th of a second

    // A reference to some timer system that your game uses
    @SuppressWarnings("unused")
    private GameTimer timer;

    private EventManager() {
        this.timer = GameTimer.getInstance();
        this.queues = new ConcurrentHashMap<>();

        // Initialize default queues
        this.queues.put("base", new ConcurrentLinkedQueue<>());
        this.queues.put("background", new ConcurrentLinkedQueue<>());
        this.queues.put("achievement", new ConcurrentLinkedQueue<>());
        this.queues.put("other", new ConcurrentLinkedQueue<>());
        this.queues.put("input", new ConcurrentLinkedQueue<>());

        this.queueTimer = 0f;
        this.queueDt = 1f / 60f;
    }

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    /**
     * Add an event to a queue.
     * 
     * @param event The event object
     * @param queue Name of the queue (e.g. "base")
     * @param front If true, add to front; otherwise to the back
     */
    public void addEvent(Event event, String queue, boolean front) {
        if (!queues.containsKey(queue)) {
            queues.put(queue, new ConcurrentLinkedQueue<>());
        }

        ConcurrentLinkedQueue<Event> queueList = queues.get(queue);
        if (front) {
            // ConcurrentLinkedQueue does not support adding to the front directly,
            // so we need to create a new queue with the event at the front.
            ConcurrentLinkedQueue<Event> newQueue = new ConcurrentLinkedQueue<>();
            newQueue.add(event);
            newQueue.addAll(queueList);
            queues.put(queue, newQueue);
        } else {
            queueList.add(event);
        }
    }

    /**
     * Clears all events in a specific queue or all queues if queue is null.
     * 
     * @param queue        Name of the queue to clear, or null to clear all.
     * @param keepNoDelete keep "no_delete" events,
     *                     you can skip removing (TODO: implement this)
     */
    public void clearQueue(String queue, boolean keepNoDelete) {
        if (queue == null) {
            // Clear all queues
            for (String key : queues.keySet()) {
                ConcurrentLinkedQueue<Event> queueList = queues.get(key);
                queueList.clear();
            }
        } else {
            // Clear only the specified queue
            if (queues.containsKey(queue)) {
                ConcurrentLinkedQueue<Event> eventQueue = queues.get(queue);
                eventQueue.clear();
            }
        }
    }

    /**
     * Main update method.
     * 
     * @param dt     The delta time since last frame (in seconds)
     * @param forced If true, process events immediately even if not enough time has
     *               passed
     */
    public void update(float dt, boolean forced) {
        // Accumulate queueTimer
        queueTimer += dt;

        // If not enough time has passed AND not forced, just return
        if (!forced && queueTimer < queueDt) {
            return;
        }

        // Otherwise, reset queueTimer.
        queueTimer -= queueDt;

        // For each queue, handle events in order
        for (ConcurrentHashMap.Entry<String, ConcurrentLinkedQueue<Event>> entry : queues.entrySet()) {
            ConcurrentLinkedQueue<Event> eventQueue = entry.getValue();

            boolean blocked = false;
            Iterator<Event> iterator = eventQueue.iterator();

            while (iterator.hasNext()) {
                Event event = iterator.next();

                // If the queue is blocked, skip the next events that are blockable
                if (blocked && event.isBlockable()) {
                    continue;
                }

                // Let the event handle itself.
                event.handle();

                // If the event completed, remove it from the queue
                try {
                    if (event.isComplete()) {
                        iterator.remove();
                    } else if (event.isBlocking()) {
                        // If the event is blocking, set blocked = true
                        blocked = true;
                    }
                } catch (ConcurrentModificationException e) {
                    // Handle exception
                    System.out.println("Concurence exception: " + e.getMessage());
                    throw e;
                }
            }
        }
    }
}
