package dev.kingdomino.game;

/**
 * Represents a game event with various trigger types and actions.
 * 
 * @see TriggerType
 * @see Condition
 * @see Ease
 * 
 * @author @fuisl
 * @version 1.0
 * 
 *          Adapted from Balatro game.
 */
public class Event {
    public enum TriggerType {
        IMMEDIATE, AFTER, BEFORE, CONDITION, EASE
    }

    public String name = "unknown";

    private final TriggerType trigger;
    private final boolean blocking;
    private final boolean blockable;
    private boolean complete;
    private boolean startTimer;

    private final float delay; // changed to float
    private float startTime; // to track when the event started
    // private float endTime;

    // For 'ease' triggers
    private final Ease ease;

    // For 'condition' triggers
    private final Condition condition;

    // A functional interface for the event's action
    private final Runnable action;

    private final GameTimer timer;

    /**
     * Constructs an Event with specified parameters.
     * 
     * @param trigger   The trigger type of the event
     * @param blocking  Whether the event is blocking
     * @param blockable Whether the event is blockable
     * @param delay     The delay before the event is triggered
     * @param action    The action to be performed by the event
     * @param timer     The game timer
     * @param condition The condition for the event
     * @param ease      The ease for the event
     */
    public Event(TriggerType trigger, Boolean blocking, Boolean blockable,
            Float delay, Runnable action, GameTimer timer, Condition condition, Ease ease) { // changed to float
        // Set defaults
        this.trigger = (trigger != null) ? trigger : TriggerType.IMMEDIATE;

        // true by default
        this.blocking = (blocking != null) ? blocking : true;
        this.blockable = (blockable != null) ? blockable : true;

        // float
        this.delay = (delay != null) ? delay : 0f;

        // init other variables
        this.complete = false;
        this.startTimer = false;
        this.action = action;

        // Get global timer
        this.timer = (timer != null) ? timer : GameTimer.getInstance();

        // already
        this.condition = condition;
        this.ease = ease;
    }

    /**
     * Copy constructor for Event.
     * 
     * @param event The event to copy
     */
    public Event(Event event) {
        this.name = event.name;
        this.trigger = event.trigger;
        this.blocking = event.blocking;
        this.blockable = event.blockable;
        this.complete = event.complete;
        this.startTimer = event.startTimer;
        this.delay = event.delay;
        this.startTime = event.startTime;
        this.ease = event.ease;
        this.condition = event.condition;
        this.action = event.action;
        this.timer = event.timer;
    }

    /**
     * Creates a copy of this event.
     * 
     * @return A new Event object that is a copy of this event
     */
    public Event copy() {
        return new Event(this);
    }

    /**
     * Handles the event based on its trigger type.
     */
    public void handle() {
        // Check triggers, call action if needed.
        if (complete) {
            return;
        }

        switch (trigger) {
            case IMMEDIATE:
                action.run();
                complete = true;
                break;
            case AFTER:
                if (!startTimer) {
                    startTime = timer.realTime; // e.g. a method returning a float in ms
                    startTimer = true;
                }
                if (timer.realTime - startTime >= delay) { // changed to float
                    action.run();
                    complete = true;
                }
                break;
            case BEFORE:
                // run the action once, then wait the delay
                if (!startTimer) {
                    // run the action immediately
                    action.run();
                    startTime = timer.realTime;
                    startTimer = true;
                }
                // Check if we are done waiting
                if (timer.realTime - startTime >= delay) { // changed to float
                    complete = true;
                }
                break;
            case CONDITION:
                // e.g. if condition.isSatisfied() ...
                if (condition != null && condition.isSatisfied()) {
                    action.run();
                    complete = true;
                }
                break;
            case EASE:
                // For an 'ease', you might do something like:
                if (ease != null) {
                    ease.update(timer.realTime);

                    // If the ease is complete, run the action
                    if (ease.isComplete()) {
                        if (action != null) {
                            action.run();
                        }
                        complete = true;
                    }
                }
                break;
            default:
                // Do nothing
                break;
        }
    }

    /**
     * Checks if the event is blocking.
     * 
     * @return true if the event is blocking, false otherwise
     */
    public boolean isBlocking() {
        return blocking;
    }

    /**
     * Checks if the event is complete.
     * 
     * @return true if the event is complete, false otherwise
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Checks if the event is blockable.
     * 
     * @return true if the event is blockable, false otherwise
     */
    public boolean isBlockable() {
        return blockable;
    }

    /**
     * Gets the trigger type of the event.
     * 
     * @return The trigger type of the event
     */
    public TriggerType getTrigger() {
        return trigger;
    }

    /**
     * Gets the action of the event.
     * 
     * @return The action of the event
     */
    public Runnable getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "Event [name=" + name + ", trigger=" + trigger + ", blocking=" + blocking + ", blockable=" + blockable
                + ", complete=" + complete + ", startTimer=" + startTimer + ", delay=" + delay + ", startTime="
                + startTime + "]";
    }
}
