package com.voiddragon.client;

/**
 * GENERATED FILE - do not edit by hand.
 * Wing animation anchors, derived from tools/dragon_spec.py by tools/gen_model.py.
 *
 * <p>The model geometry is authored with the wings SPREAD (rest pose at the
 * back of this file). A grounded dragon folds them with {@link #FOLD_ROOT},
 * {@link #FOLD_FORE} and {@link #FOLD_HAND}, and the five finger fans collapse
 * to {@link #FOLDED_FINGER} / {@link #FOLDED_MEMBRANE}. In flight the fans are
 * the authored values and the arm beats by {@link #FLAP_ROOT_Z}.</p>
 */
public final class DragonPose {
    private DragonPose() {
    }
    public static final float FINGER_COUNT = 5F;

    /** xRot / yRot / zRot of the arm and forearm while the dragon is grounded. */
    public static final float FOLD_ROOT_X = -0.160F;
    public static final float FOLD_ROOT_Y = -1.020F;
    public static final float FOLD_ROOT_Z = 0.520F;
    public static final float FOLD_FORE_X = 0.000F;
    public static final float FOLD_FORE_Y = -0.620F;
    public static final float FOLD_FORE_Z = -0.340F;
    public static final float FOLD_HAND_X = 0.000F;
    public static final float FOLD_HAND_Y = 0.000F;
    public static final float FOLD_HAND_Z = -0.340F;

    /** Amplitude of the down-stroke, in radians. */
    public static final float FLAP_ROOT_Z = 0.340F;
    public static final float FLAP_FORE_Z = 0.160F;
    public static final float FLAP_FINGER_LAG = 0.100F;
    public static final float ROAR_UNFURL = 0.300F;
    public static final float FOLD_FAN = 0.500F;

    /** Absolute yRot of each finger / membrane panel while grounded. */
    public static final float[] FOLDED_FINGER = {
            0.068F, -0.072F, -0.212F, -0.362F, -0.542F, -0.002F, -0.142F, -0.287F, -0.452F, 0.343F, -0.802F};
    public static final float[] OPEN_FINGER = {
            0.360F, 0.080F, -0.200F, -0.500F, -0.860F, 0.220F, -0.060F, -0.350F, -0.680F, 0.700F, -1.160F};
}
