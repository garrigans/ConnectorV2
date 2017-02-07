/**
 * @author haxor on 4/11/16.
 */

package org.strongswan.android.connector.enums;

// Used as a state machine to determine the status of the route test.

public enum StateMachine {
    INITIATE,
    RUNNING,
    EXECUTE,
    FINISH,
    RESET,
    PAUSED,
    CANCELLED,
    ERROR,
    SUCCESS,
    FAIL;
}

