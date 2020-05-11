package internal;

import java.time.Instant;
import java.time.ZoneOffset;

public class InternalClock {
    public static final int SECOND = 1;
    public static final int MINUTE = 60;
    public static final int HOUR = 3600;

    private static float acceleration = 1;
    private static Instant clock;
    private static String localTime;

    public static void setDefaultClock() {
        clock = Instant.parse("2020-05-01T09:57:00.00Z");
    }

    public static void setTime(String time) {
        clock = Instant.parse("2020-05-01T" + time + ".00Z");
    }

    public static void defaultAccelerationLevel() {
        acceleration = 1;
        System.out.println("TEST SceneController.speedIncrClick: " + acceleration);
    }

    public static void increaseAccelerationLevel() {
        if (acceleration < 64) {
            acceleration *= 2;
        }
        System.out.println("TEST SceneController.speedIncrClick: " + acceleration);
    }

    public static void decreaseAccelerationLevel() {
        if (acceleration > 0.0625) {
            acceleration *= 0.5;
        }
        System.out.println("TEST SceneController.speedDecrClick: " + acceleration);
    }

    public static float getAccelerationLevel() {
        return acceleration;
    }

    private static String getLocalTime(int accuracy){
        String localTime = clock.atZone(ZoneOffset.UTC).toLocalTime().toString();

        switch (accuracy){
            case SECOND:
                localTime = localTime.substring(0, Math.min(8, localTime.length()));
                break;
            case MINUTE:
                localTime = localTime.substring(0, Math.min(5, localTime.length()));
                break;
            case HOUR:
                localTime = localTime.substring(0, Math.min(2, localTime.length()));
                break;
            default:
                break;
        }

        return localTime;
    }

    public static String getStopTime(){
        clock = clock.plusSeconds(30);
        String stopTime = getLocalTime(SECOND);
        clock = clock.minusSeconds(30);

        return stopTime;
    }

    public static boolean isTime(String time, int accuracy){
        String localTime = getLocalTime(accuracy);

        return localTime.equals(time);
    }

    public static String updateClock() {
        long millis = (long) (acceleration * 100);
        clock = clock.plusMillis(millis);

        localTime = getLocalTime(SECOND);
        if (acceleration != 1) {
            localTime += " (x" + acceleration + ")";
        }

        return localTime;
    }
}