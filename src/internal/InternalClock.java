package internal;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class InternalClock {
    public static final int SECOND = 1;
    public static final int MINUTE = 60;
    public static final int HOUR = 3600;

    private static double acceleration = 1;
    private static Instant clock;
    private static LocalTime setTime;
    private static String localTime;

    public static void setDefaultClock() {
        clock = Instant.parse("2020-05-01T09:57:00.00Z");
        setTime = null;
    }

    public static void setTime(String time) {
        setTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static void defaultAccelerationLevel() {
        acceleration = 1;
    }

    public static void increaseAccelerationLevel() {
        if (acceleration < 2048) {
            acceleration *= 2;
        }
    }

    public static void decreaseAccelerationLevel() {
        if (acceleration > 0.0625) {
            acceleration *= 0.5;
        }
    }

    public static double getAccelerationLevel() {
        return acceleration;
    }

    private static LocalTime getLocalTime(){
        return clock.atZone(ZoneOffset.UTC).toLocalTime();
    }

    public static LocalTime getStopTime(){
        clock = clock.plusSeconds(10);
        LocalTime stopTime = getLocalTime();
        clock = clock.minusSeconds(10);

        return stopTime;
    }

    public static boolean afterTime(LocalTime time){
        return getLocalTime().isAfter(time);
    }

    public static boolean beforeTime(LocalTime time){
        return getLocalTime().isBefore(time);
    }

    public static String updateClock() {
        if (setTime != null){
            LocalTime localSetTime = setTime.minusHours(getLocalTime().getHour())
                                            .minusMinutes(getLocalTime().getMinute())
                                            .minusSeconds(getLocalTime().getSecond());
            if (localSetTime.getHour() != 0){
                acceleration = 16384;
            } else if (localSetTime.getMinute() != 0){
                acceleration = 2048;
            } else if (localSetTime.getSecond() != 0){
                acceleration = 32;
            } else {
                acceleration = 1;
                setTime = null;
            }
        }

        long nanos = (long) (acceleration * 1000000);
        clock = clock.plusNanos(nanos);

        localTime = getLocalTime().toString();
        localTime = localTime.substring(0, Math.min(8, localTime.length()));
        if (acceleration != 1) {
            localTime += " (x" + acceleration + ")";
        }

        return localTime;
    }
}