package dev.kingdomino.helpers;

@FunctionalInterface
public interface Slot {
    void onSignal(Object... args);
}