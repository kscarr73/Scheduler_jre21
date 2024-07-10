package com.progbits.scheduler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 *
 * @author scarr
 */
public interface Trigger {
    String getJobName();
    
    Trigger setJobName(String name);
    
    String getTriggerName();
    
    Trigger setTriggerName(String triggerName);
    
    Trigger setZone(ZoneId zone);
    
    Optional<ZonedDateTime> getNextFireTime();
    void setNextFireTime();
    
    ZonedDateTime getPreviousFireTime();
    void setPreviousFireTime(ZonedDateTime time);
    
    TriggerState getState();
    
    void setState(TriggerState newState);
}
