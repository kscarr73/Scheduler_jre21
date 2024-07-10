package com.progbits.scheduler;

import com.progbits.api.exception.ApiException;
import com.progbits.scheduler.triggers.CronTrigger;
import com.progbits.scheduler.triggers.SimpleTrigger;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Scheduler using Simple and Cron schedules
 *
 * @author scarr
 */
public class Scheduler {

    private final Map<String, Job> jobs = new ConcurrentHashMap<>();
    private final Map<String, JobWrapper> runnableJobs = new ConcurrentHashMap<>();

    private ZoneId processZone = ZoneId.systemDefault();

    ThreadPoolExecutor scheduleExec;
    Thread processThread;

    public void start() {
        scheduleExec = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

        processThread = Thread.ofPlatform().name("Api Scheduler").daemon(true).start(this::processTriggers);
    }

    public void processTriggers() {
        while (!processThread.interrupted()) {
            try {
                ZonedDateTime currTime = ZonedDateTime.now(processZone);

                for (var job : runnableJobs.values()) {
                    if (job.getJobTrigger().getState() == TriggerState.ACTIVE) {
                        if (currTime.isBefore(job.getJobTrigger().getNextFireTime().get())) {
                            job.getJobTrigger().setState(TriggerState.QUEUED);
                            scheduleExec.execute(job);
                        }
                    }
                }
                TimeUnit.MILLISECONDS.wait(100);
            } catch (InterruptedException ie) {
                processThread.interrupt();
            }
        }
    }

    public void registerJob(Job job) {
        jobs.put(job.getName(), job);
    }

    public void registerSimpleTrigger(String jobName, String triggerName, long milliseconds) throws ApiException {
        if (jobs.containsKey(jobName)) {
            JobWrapper wrapper = new JobWrapper(new SimpleTrigger(triggerName, jobName, milliseconds).setZone(processZone), jobs.get(jobName));

            runnableJobs.put(triggerName, wrapper);
        } else {
            throw new ApiException(400, "Job Name: " + jobName + " Does Not Exist");
        }
    }

    public void registerCronTrigger(String jobName, String triggerName, String cron) throws ApiException {
        if (jobs.containsKey(jobName)) {
            try {
                JobWrapper wrapper = new JobWrapper(new CronTrigger(triggerName, jobName, cron).setZone(processZone), jobs.get(jobName));

                runnableJobs.put(triggerName, wrapper);
            } catch (IllegalArgumentException iae) {
                throw new ApiException(400, iae.getMessage(), iae);
            }
        } else {
            throw new ApiException(400, "Job Name: " + jobName + " Does Not Exist");
        }
    }
}
