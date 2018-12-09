package zzq.job;

import java.util.Date;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;   
import org.quartz.JobExecutionContext;  
import org.quartz.JobExecutionException;  
  
public class HelloJob implements BaseJob {  
  
    private static Logger logger = LoggerFactory.getLogger(HelloJob.class);
     
    public HelloJob() {  
          
    }  
     
    public void execute(JobExecutionContext context)  
        throws JobExecutionException {
        logger.info("Hello Job执行时间: " + new Date());
          
    }  
}  
