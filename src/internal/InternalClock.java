package internal;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Class that represents clocks in program's system.
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class InternalClock {
    private static double acceleration = 1;
    private static Instant clock;
    private static LocalTime setTime;
    private static String localTime;

    private InternalClock(){}

    /**
     * Sets default value for the clock.
     */
    public static void setDefaultClock() {
        clock = Instant.parse("2020-05-21T00:00:00.00Z");
        setTime = null;
    }

    /**
     * Sets time for the clock.
     * @param time A given time.
     */
    public static void setTime(String time) {
        setTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * Sets default acceleration's value.
     */
    public static void defaultAccelerationLevel() {
        acceleration = 1;
    }

    /**
     * Increases acceleration level by 2 (maximum is 2048).
     */
    public static void increaseAccelerationLevel() {
        if (acceleration < 2048) {
            acceleration *= 2;
        }
    }

    /**
     * Decreases acceleration level by 2 (minimum is 0.0625).
     */
    public static void decreaseAccelerationLevel() {
        if (acceleration > 0.0625) {
            acceleration *= 0.5;
        }
    }

    /**
     * Returns current acceleration level.
     * @return Current acceleration level.
     */
    public static double getAccelerationLevel() {
        return acceleration;
    }

    /**
     * Returns current local time.
     * @return Current local time.
     */
    private static LocalTime getLocalTime(){
        return clock.atZone(ZoneOffset.UTC).toLocalTime();
    }

    /**
     * Returns current local time increased by 10 seconds (a stop's delay).
     * @return Current local time with delay on a stop.
     */
    public static LocalTime getStopTime(){
        clock = clock.plusSeconds(10);
        LocalTime stopTime = getLocalTime();
        clock = clock.minusSeconds(10);

        return stopTime;
    }

    /**
     * Checks, if current time is after given time.
     * @param time a given time
     * @return true, if it is after, otherwise false.
     */
    public static boolean afterTime(LocalTime time){
        return getLocalTime().isAfter(time);
    }

    /**
     * Checks, if current time is before given time.
     * @param time a given time
     * @return true, if it is before, otherwise false.
     */
    public static boolean beforeTime(LocalTime time){
        return getLocalTime().isBefore(time);
    }

    /**
     * Updates the clock according to its acceleration.
     * Updating is continuous and makes every 1 ms. 
     * @return A String representation of this clock(cut to format hh:mm:ss).
     */
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