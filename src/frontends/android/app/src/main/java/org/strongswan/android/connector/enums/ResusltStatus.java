/**
 * @author haxor on 4/11/16.
 */

package org.strongswan.android.connector.enums;

// Used as a state machine to determine the results of the route test.

public enum ResusltStatus {
    REACHABLE,
    UNREACHABLE,
    ERROR,
    TIMEOUT;
}
