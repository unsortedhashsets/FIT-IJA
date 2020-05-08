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

    public static void setDefaultClock(){
        clock = Clock.fixed(Instant.parse("2020-05-01T10:00:00.00Z"), ZoneId.of("Europe/Prague"));
    }

    public static String updateClock(){
        long millis = (long) (acceleration * 100);
        clock = Clock.offset(clock, Duration.ofMillis(millis));

        localTime = clock.instant().atZone(ZoneOffset.UTC).toLocalTime().toString();
        return localTime; // get time without millis
    }

    public static void defaultAccelerationLevel(){
        acceleration = 1;
        System.out.println("TEST SceneController.speedIncrClick: "+ acceleration);
    }

    public static void increaseAccelerationLevel(){
        if (acceleration < 8){
            acceleration *= 2;
        }
        System.out.println("TEST SceneController.speedIncrClick: "+ acceleration);
    }

    public static void decreaseAccelerationLevel(){
        if (acceleration > 0.125){
            acceleration *= 0.5;
        }
        System.out.println("TEST SceneController.speedDecrClick: "+ acceleration);
    }

    public static float getAccelerationLevel(){
        return acceleration;
    }
}