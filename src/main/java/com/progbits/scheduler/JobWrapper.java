package com.progbits.scheduler;

/**
 *
 * @author scarr
 */
public class JobWrapper implements Runnable {

    private Job wrappedJob;
    private Trigger jobTrigger;
    
    public JobWrapper(Trigger trigger, Job job) {
        this.wrappedJob = job;
        this.jobTrigger = trigger;
    }

    public Trigger getJobTrigger() {
        return jobTrigger;
    }

    public Job getWrappedJob() {
        return wrappedJob;
    }
    
    @Override
    public void run() {
        jobTrigger.setState(TriggerState.RUNNING);
        
        try {
            wrappedJob.execute();
        } catch (Exception ex) {
            
        } finally {
            jobTrigger.setPreviousFireTime(jobTrigger.getNextFireTime().get());
            
            jobTrigger.setNextFireTime();
            
            jobTrigger.setState(TriggerState.ACTIVE);
        }
    }
    
}
