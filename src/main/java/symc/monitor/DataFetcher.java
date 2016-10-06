package symc.monitor;

import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.QueueInfo;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.ApplicationNotFoundException;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.metrics.ContextFactory;
import org.apache.hadoop.metrics.MetricsContext;
import org.apache.hadoop.metrics.spi.OutputRecord; 

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.mapreduce.TaskType;


/**
 * @author Arshpreet_Singh
 * This class fetches all the data required from the cluster
 */
public class DataFetcher {
	
	

	/**
	 * @param job
	 * @param jobstatus
	 * @return data for the job
	 * @throws IOException
	 * @throws YarnException
	 */
	public ApplicationData fetch(JobStatus job, Job j) throws IOException, YarnException {

		ApplicationData app = new ApplicationData(job.getJobID().toString());		// Job ID
		ClusterData cluster = new ClusterData();
		
		
		
		app.seturl(job.getTrackingUrl());
		app.completed = job.isJobComplete();										// Is Job completed?
		app.setName(job.getJobName());                                     			// Name
		app.setState(job.getState().toString());                                 	// Job State
		app.setStartTime(job.getStartTime());                             			// Start Time
		app.setFinishTime(job.getFinishTime());										// Finished Time
		app.setElapsedTime();														// Total Time taken by job
		app.setUser(job.getUsername());                              				// User
		app.setQueue(job.getQueue());                                 				// Queue
		app.setPriority(job.getPriority().name());                               	// Priority
		app.setNumUsedSlots(job.getNumUsedSlots());									// Number of Used Slots
		app.setNumRsvdSlots(job.getNumReservedSlots());								// Number of Reserved Slots
		app.setUsedMem(job.getUsedMem());											// Used Memory
		app.setRsvdMem(job.getReservedMem());										// Reserved Memory
		app.setNeededMem(job.getNeededMem());										// Needed Memory
		app.setJobConfiguration(j.getConfiguration());								// Job configurations
		
		 YarnClient yarnClient = YarnClient.createYarnClient();
		  yarnClient.init(j.getConfiguration());
		  yarnClient.start();
		  ApplicationReport appReport = null;
		  /*
		  QueueInfo queueInfo = yarnClient.getQueueInfo("root");
		  List<QueueInfo> list=yarnClient.getAllQueues();
		  
		  if(queueInfo !=null){
			  cluster.setQueueName(queueInfo.getQueueName());
			  cluster.setMaxCapacity(queueInfo.getMaximumCapacity());
			  cluster.setUsedCapacity(queueInfo.getCurrentCapacity());
			  cluster.setDate(System.currentTimeMillis());
			  
		  }else{
			  System.out.println("Check name of queue");
		  }*/
		  
		  	try {
		      appReport = yarnClient.getApplicationReport(ConverterUtils.toApplicationId(app.getJobId().replaceAll("job", "application")));
		      app.setType(appReport.getApplicationType());
		      app.setProgress(appReport.getProgress());
		    } catch (ApplicationNotFoundException e) {
		      System.out.println("Application with id '" + app.getJobId()
		          + "' doesn't exist in RM or Timeline Server.");

		    }
		  	
		  
		
		try {
			app.setMapTasks(j.getTaskReports(TaskType.MAP));						// Get Map Tasks
			app.setReduceTasks(j.getTaskReports(TaskType.REDUCE));					// Get Reduce Taskss
		} catch (InterruptedException e) {
			System.out.println(e.toString());
		}

		return app;
	}
	
	/**
	 * @return Cluster data: name, usage and timestamp
	 * @throws YarnException
	 * @throws IOException
	 */
	ClusterData fetch() throws YarnException, IOException{
		ClusterData data = new ClusterData();
		YarnConfiguration conf = new YarnConfiguration();
		YarnClient c = YarnClient.createYarnClient();
		c.init(conf);
		c.start();
		System.out.println(c.getConfig().get("mapreduce.map.memory.mb", "1024"));
		System.out.println(c.getConfig().get("mapreduce.reduce.memory.mb", "1024"));
		QueueInfo queueInfo = c.getQueueInfo("root");
		if(queueInfo !=null){
			  data.setQueueName(queueInfo.getQueueName());
			  data.setMaxCapacity(queueInfo.getMaximumCapacity());
			  data.setUsedCapacity(queueInfo.getCurrentCapacity());
			  data.setDate(System.currentTimeMillis());
			  
		  }else{
			  System.out.println("Check name of queue");
		  }
		return data;
	}
	
}
