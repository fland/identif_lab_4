package ua.pp.fland.labs.identif.lab4.model.tools;

/**
 * @author Maxim Bondarenko
 * @version 1.0 10/22/11
 */

public final class ArgumentGuard {

    private ArgumentGuard() {
    }

    public static void checkIntLessThan(int value, String valueName, int lessThan) {
        if (value >= lessThan) {
            String msg = "Value " + valueName + " must be less than" + lessThan;
            throw new IllegalArgumentException(msg);
        }
    }

    public static void checkIntLessOrEqualThan(int value, String valueName, int lessOrEqualThan) {
        if (value > lessOrEqualThan) {
            String msg = "Value " + valueName + " must be less or equal to" + lessOrEqualThan;
            throw new IllegalArgumentException(msg);
        }
    }
}
