package internal;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

public class InternalClock {
    private float acceleration;
    private Clock clock;

    public InternalClock(){
        setDefaultClock();
        this.acceleration = 1;
    }

    public void setDefaultClock(){
        this.clock = Clock.fixed(Instant.parse("2020-05-01T10:00:00.00Z"), ZoneId.of("Europe/Prague"));
    }

    public Clock updateClock(){
        long millis = (long) acceleration * 100;
        this.clock = Clock.offset(this.clock, Duration.ofMillis(millis));
        return this.clock;
    }

    public void increaseAccelerationLevel(){
        if (this.acceleration < 2){
            this.acceleration += 0.25;
        }
    }

    public void decreaseAccelerationLevel(){
        if (this.acceleration > 0.25){
            this.acceleration -= 0.25;
        }
    }

    public float getAccelerationLevel(){
        return this.acceleration;
    }
}