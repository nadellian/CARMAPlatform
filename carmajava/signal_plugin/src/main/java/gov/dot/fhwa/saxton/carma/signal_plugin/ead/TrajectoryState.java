package gov.dot.fhwa.saxton.carma.signal_plugin.ead;

/**
 * Defines the states to be used by the Trajectory class.
 */
public enum TrajectoryState {
    DISENGAGED,
    SPEED_FILE,
    CRUISING,
    EAD_IN_MOTION,
    EAD_STOPPED
}