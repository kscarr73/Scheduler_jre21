package com.progbits.scheduler.triggers;

import com.progbits.scheduler.Trigger;
import com.progbits.scheduler.TriggerState;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 *
 * @author scarr
 */
public class SimpleTrigger implements Trigger {

    private String triggerName;
    private String jobName;
    private long runEvery;
    private TriggerState currState = TriggerState.RUNNING;
    private Optional<ZonedDateTime> nextFireTime = Optional.empty();
    private ZonedDateTime previousFireTime;
    private ZoneId zone;

    public SimpleTrigger(String triggerName, String jobName, long runEvery) {
        this.triggerName = triggerName;
        this.runEvery = runEvery;
    }

    @Override
    public Trigger setZone(ZoneId zone) {
        this.zone = zone;
        return this;
    }

    @Override
    public String getJobName() {
        return jobName;
    }

    @Override
    public Trigger setJobName(String name) {
        this.jobName = jobName;
        return this;
    }

    @Override
    public String getTriggerName() {
        return triggerName;
    }

    @Override
    public Trigger setTriggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    @Override
    public Optional<ZonedDateTime> getNextFireTime() {
        if (nextFireTime.isEmpty()) {
            setNextFireTime();
        }
        
        return nextFireTime;
    }

    @Override
    public void setNextFireTime() {
        this.nextFireTime = Optional.of(ZonedDateTime.now(zone).plus(runEvery, ChronoUnit.MILLIS));
    }

    @Override
    public ZonedDateTime getPreviousFireTime() {
        return previousFireTime;
    }

    @Override
    public void setPreviousFireTime(ZonedDateTime time) {
        this.previousFireTime = time;
    }

    @Override
    public TriggerState getState() {
        return currState;
    }

    @Override
    public void setState(TriggerState newState) {
        currState = newState;
    }
    
}
