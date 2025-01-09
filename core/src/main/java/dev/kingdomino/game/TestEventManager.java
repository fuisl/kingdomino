package dev.kingdomino.game;

public class TestEventManager {
    public static void main(String[] args) {
        GameTimer timer = GameTimer.getInstance();
        timer.realTime = 12f;
        timer.totalTime = 12f;

        EventManager eventManager = EventManager.getInstance();

        Event event = new Event(
                Event.TriggerType.AFTER,
                true, true,
                0.5f,
                () -> {
                    System.out.println("Hello, world!");
                },
                GameTimer.getInstance(),
                null,
                null);

        Event event2 = new Event(
                Event.TriggerType.BEFORE,
                false, false,
                10f,
                () -> {
                    System.out.println("Hello, world! 2");
                },
                GameTimer.getInstance(),
                null,
                null);

        Event event3 = new Event(
                Event.TriggerType.IMMEDIATE,
                false, false,
                0f,
                () -> {
                    System.out.println("Hello, world! 3");
                },
                GameTimer.getInstance(),
                null,
                null);

        eventManager.addEvent(event, "base", false);
        eventManager.addEvent(event2, "base", false);
        eventManager.addEvent(event3, "base", false);

        float currentTimeMillis = System.currentTimeMillis();
        float dt = 1f / 60f;

        while (timer.realTime < 15f) {
            eventManager.update(dt, false);
            timer.realTime += dt;
        }
    }
}