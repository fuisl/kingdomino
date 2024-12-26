package dev.kingdomino.game;

import java.util.*;

public class EventManager {
    // Each queue is a list of Events under a specific category/key
    private Map<String, List<Event>> queues;

    // For controlling how often to process event queues
    private float queueTimer;
    private float queueDt; // e.g., process queue every 1/60th of a second

    // private float queueLastProcessed; // tracks last time the update was processed

    // A reference to some timer system that your game uses
    @SuppressWarnings("unused")
    private GameTimer timer;

    public EventManager(GameTimer timer) {
        this.timer = timer;
        this.queues = new HashMap<>();

        // Initialize default queues
        this.queues.put("base", new LinkedList<>());
        this.queues.put("tutorial", new LinkedList<>());
        this.queues.put("achievement", new LinkedList<>());
        this.queues.put("other", new LinkedList<>());

        // Example: process events about 60 times per second
        this.queueTimer = 0f;
        this.queueDt = 1f / 60f;
        // this.queueLastProcessed = 0f;
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
            queues.put(queue, new LinkedList<>());
        }
        if (front) {
            queues.get(queue).add(0, event);
        } else {
            queues.get(queue).add(event);
        }
    }

    /**
     * Clears all events in a specific queue or all queues if queue is null.
     * 
     * @param queue        Name of the queue to clear, or null to clear all.
     * @param keepNoDelete If you have a concept of "no_delete" events,
     *                     you can skip removing them here. (Implementation
     *                     optional)
     */
    public void clearQueue(String queue, boolean keepNoDelete) {
        if (queue == null) {
            // Clear all queues
            for (String key : queues.keySet()) {
                queues.get(key).clear();
            }
        } else {
            // Clear only the specified queue
            if (queues.containsKey(queue)) {
                queues.get(queue).clear();
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
        // The original code uses "queue_last_processed + queue_dt",
        // but for simplicity we can just accumulate & subtract.
        queueTimer -= queueDt;

        // For each queue, handle events in order
        for (Map.Entry<String, List<Event>> entry : queues.entrySet()) {
            List<Event> eventQueue = entry.getValue();

            boolean blocked = false;
            Iterator<Event> iterator = eventQueue.iterator();

            while (iterator.hasNext()) {
                Event event = iterator.next();

                // If the queue is blocked, skip the next events that are blockable
                if (blocked && event.isBlockable()) {
                    continue;
                }

                // Let the event handle itself (like Lua's event:handle)
                event.handle();

                // If the event completed, remove it from the queue
                if (event.isComplete()) {
                    iterator.remove();
                }
                // If the event is blocking, set blocked = true
                else if (event.isBlocking()) {
                    blocked = true;
                }
            }
        }
    }
}
