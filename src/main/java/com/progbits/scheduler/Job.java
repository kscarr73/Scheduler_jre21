package com.progbits.scheduler;

import com.progbits.api.model.ApiObject;

/**
 *
 * @author scarr
 */
public interface Job {
    /**
     * The name for this Job
     * 
     * @return The String Representation for this Job
     */
    String getName();
    
    ApiObject getConfig();
    
    void execute() throws Exception;
}
