package internal;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class InternalClock {
    private static float acceleration = 1;
    private static Clock clock;
    private static String localTime;

    public static void setDefaultClock() {
        clock = Clock.fixed(Instant.parse("2020-05-01T10:00:00.00Z"), ZoneId.of("Europe/Prague"));
    }

    public static String updateClock() {
        long millis = (long) (acceleration * 100);
        clock = Clock.offset(clock, Duration.ofMillis(millis));

        localTime = clock.instant().atZone(ZoneOffset.UTC).toLocalTime().toString();
        if (acceleration == 1) {
            return localTime.substring(0, Math.min(8, localTime.length())); // get time without millis
        } else {
            return localTime.substring(0, Math.min(8, localTime.length())) + " (x" + acceleration + ")";
        }
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

    public static String getStopTime(){
        clock = Clock.offset(clock, Duration.ofMinutes(1));
        String stopTime = clock.instant().atZone(ZoneOffset.UTC).toLocalTime().toString();
        clock = Clock.offset(clock, Duration.ofMinutes(-1));

        return stopTime;
    }

    public static boolean isTime(String time){
        String localTime = clock.instant().atZone(ZoneOffset.UTC).toLocalTime()
                          .toString().substring(0, 5); // get HH:MM
        return localTime.equals(time);
    }

    public static float getAccelerationLevel() {
        return acceleration;
    }
}