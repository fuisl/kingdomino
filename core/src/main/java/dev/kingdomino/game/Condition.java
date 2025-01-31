package dev.kingdomino.game;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Represents a condition that must be satisfied for an event to trigger.
 * 
 * <p>
 * Conditions are used to check if a certain value in a reference table
 * matches a target value. This is useful for checking if a game state
 * condition is met, such as if a player has reached a certain score.
 * 
 * <p>
 * Conditions are used in conjunction with {@link Event}s to trigger
 * game events when certain conditions are met.
 * 
 * Currently not used in the game, but can be used for future expansion.
 * 
 * @see Event
 * @see EventManager
 * 
 * @author @fuisl
 * @version 1.0
 * 
 *          Adapted from Balatro Game
 */
public class Condition {

    private Map<String, Object> refTable;

    private String refValue;

    private Object stopVal;

    /**
     * Check if the condition is satisfied.
     * If none is provided, we default to checking equality (==) between
     * the refTable[refValue] and stopVal.
     */
    private final Supplier<Boolean> conditionCheck;

    /**
     * Constructor with a custom condition-checking function.
     * If 'customCheck' is null, we default to:
     * refTable.get(refValue).equals(stopVal)
     * which mirrors the Lua default:
     * self.condition.ref_table[self.condition.ref_value] == self.condition.stop_val
     *
     * @param refTable    A map representing your "ref_table".
     * @param refValue    The key inside refTable you want to check.
     * @param stopVal     The target value you want to match.
     * @param customCheck Optional function to decide whether the condition is
     *                    satisfied.
     *                    If null, we use the default equality check.
     */
    public Condition(Map<String, Object> refTable,
            String refValue,
            Object stopVal,
            Supplier<Boolean> customCheck) {

        this.refTable = refTable;
        this.refValue = refValue;
        this.stopVal = stopVal;

        // If no custom check is provided, default to "compare equals"
        if (customCheck != null) {
            this.conditionCheck = customCheck;
        } else {
            this.conditionCheck = () -> {
                Object currentVal = refTable.get(refValue);
                // Return true if currentVal is equal to stopVal
                return (currentVal != null && currentVal.equals(stopVal));
            };
        }
    }

    /**
     * Check if the condition is satisfied.
     * Equivalent to calling the Lua function in self.func().
     */
    public boolean isSatisfied() {
        return conditionCheck.get();
    }

    // Getters or additional logic as needed
    public Map<String, Object> getRefTable() {
        return refTable;
    }

    public String getRefValue() {
        return refValue;
    }

    public Object getStopVal() {
        return stopVal;
    }
}
