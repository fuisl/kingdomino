package dev.kingdomino.game;

public class Event {
    public enum TriggerType {
        IMMEDIATE, AFTER, BEFORE, CONDITION, EASE
    }

    private TriggerType trigger;
    private boolean blocking;
    private boolean blockable;
    private boolean complete;
    private boolean startTimer;

    private float delay; // changed to float
    private float startTime; // to track when the event started
    // private float endTime;

    // For 'ease' triggers
    private Ease ease;

    // For 'condition' triggers
    private Condition condition;

    // A functional interface for the event's action
    private Runnable action; // or use Consumer<Float> or your own
    // Alternatively, for returning something, use Supplier<Boolean> or similar.

    private GameTimer timer;

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
        this.timer = GameTimer.getInstance();

        // already
        this.condition = condition;
        this.ease = ease;
    }

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

    public boolean isBlocking() {
        return blocking;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isBlockable() {
        return blockable;
    }

    public TriggerType getTrigger() {
        return trigger;
    }

    public Runnable getAction() {
        return action;
    }
}
