package internal;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class InternalClock {
    private static float acceleration = 1;
    private static Clock clock;

    public static void setDefaultClock(){
        clock = Clock.fixed(Instant.parse("2020-05-01T10:00:00.00Z"), ZoneId.of("Europe/Prague"));
    }

    public static String updateClock(){
        long millis = (long) acceleration * 10;
        clock = Clock.offset(clock, Duration.ofMillis(millis));

        String localTime = clock.instant().atZone(ZoneOffset.UTC).toLocalTime().toString();
        return localTime.substring(0, 8); // get time without millis
    }

    public static void increaseAccelerationLevel(){
        if (acceleration < 2){
            acceleration += 0.25;
        }
    }

    public static void decreaseAccelerationLevel(){
        if (acceleration > 0.25){
            acceleration -= 0.25;
        }
    }

    public static float getAccelerationLevel(){
        return acceleration;
    }
}