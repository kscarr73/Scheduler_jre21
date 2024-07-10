package com.progbits.scheduler.triggers;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.progbits.scheduler.Trigger;
import com.progbits.scheduler.TriggerState;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 *
 * @author scarr
 */
public class CronTrigger implements Trigger {

    private String triggerName;
    private String jobName;

    private String cron;

    private ExecutionTime parsedTime;
    
    private Optional<ZonedDateTime> nextFireTime;
    private ZonedDateTime previousFireTime;
    
    private TriggerState currState = TriggerState.RUNNING;
    
    private ZoneId zone;

    public CronTrigger(String triggerName, String jobName, String cron) {
        this.triggerName = triggerName;
        this.jobName = jobName;
        this.cron = cron;

        CronDefinition cronDef = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
        CronParser parser = new CronParser(cronDef);

        parsedTime = ExecutionTime.forCron(parser.parse(cron));
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
        this.jobName = name;
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
        this.nextFireTime = parsedTime.nextExecution(ZonedDateTime.now(zone));
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
