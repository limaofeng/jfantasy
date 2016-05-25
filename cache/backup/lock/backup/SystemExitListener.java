package org.jfantasy.framework.util.lock.backup;


public class SystemExitListener {
    private static boolean over;

    public static void addTerminateListener(ExitHandler exitHandler) {

    }

    public static boolean isOver() {
        return over;
    }
}
