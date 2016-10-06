package symc.monitor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Cluster;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.mapreduce.tools.CLI;
import org.apache.log4j.Logger;



/**
 * @author Arshpreet_Singh
 * This class contains methods implemented by MonitorRunner class.
 */
public class Monitor extends CLI{
	private static final Logger logger = Logger.getLogger(Monitor.class);
	private static String dirPath = "hdfs://b0c004ash2001.prod.symcpe.net:8020/user/vipul_sawant/monitor/user_data/";
	private static String uri = "hdfs://b0c004ash2001.prod.symcpe.net:8020/";
	private long fromTime = 0;
	private long toTime = Long.MAX_VALUE;
	
	/**
	 * Initializes cluster
	 */
	public Monitor() {
		// Connect to the Resource Manager
		super();

		/*try {
			// Trying out the Logger
			logger.info("Connecting to the Resource Manager...");
			// Connect to the Cluster
			//config.addResource(new Path("/home/vipul_sawant/myconf/yarn-site.xml"));
			this.cluster = new Cluster(getConfig());
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		// If code reached here, you have successfully managed to 
		// connect to the cluster's resource manager
		logger.info("Successfully connected");

	}
	
	/**
	 * @return all the jobs from job history server
	 */
	public JobStatus [] getJobs(){

		JobStatus[] jobs = null;
		try {
			
			
				//System.out.println(cluster.getStagingAreaDir());
				System.out.println(getConfig().get("fs.default.name"));
				System.out.println(getConfig().get("fs.defaultFS"));
				System.out.println(getConfig().get("mapreduce.jobhistory.address"));
				jobs = cluster.getAllJobStatuses();
				
				

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jobs;
	}
	
	
	
	/**
	 * @param job
	 * @return Get Data about Jobs running or finished in the cluster
	 */
	ApplicationData getData(JobStatus job) {

		// Skip Jobs as they can't be handled at the moment

		if (!job.isJobComplete()) return null;

		if(job.getFinishTime() < fromTime || job.getFinishTime() > toTime)
			return null;
		else if (job.getState().toString().equals("KILLED"))
			return null;
		else if (job.getState().toString().equals("FAILED"))
			return null;
		

		DataFetcher fetcher = new DataFetcher();

		ApplicationData jobData = null;
		Job jb = null;

		try {

			// Get the corresponding job object
			try{
				jb = cluster.getJob(job.getJobID());
			}
			catch(IOException e)
			{
				logger.error(e.toString());
				return null;
			}

			if(jb == null)
				return null;

			// Stop Huge Jobs as at the moment we are unable to stop
			// the memory leak in the getTaskReports method in DataFetcher class
			long totalTasks = Long.MAX_VALUE;
			try {
				Counters counters = jb.getCounters();
				totalTasks = counters.findCounter("org.apache.hadoop.mapreduce.JobCounter", "TOTAL_LAUNCHED_MAPS").getValue();
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error("Problem in fetching Counter : Total Mappers");
				return null;
			}

			if(totalTasks > 20000)
			{
				logger.warn("Job skipped due to huge number of tasks : " + totalTasks);
				return null;
			}

			//System.out.println("Fetching Data for :" + job.getJobID());

			// fetch the data about the job
			try{
				jobData = fetcher.fetch(job, jb);
				
			} catch(Exception e) {
				logger.error("GC Overhead : " + e.toString());
				return null;
			}

			// if some error occurred in getting job data
			if (jobData == null)
				logger.error("Some unforseen error occured "
						+ "in retrieving stats about the " + job.getJobID());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Either there were no jobs in the cluster or some serious error occurred 

		return jobData;
	}
	
	
	/**
	 * Set the time from which the data should be read from the job history server
	 * @param fromTime
	 */
	void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}
	/**
	 * Set the time unto which the data should be read from the job history server
	 * @param toTime
	 */
	void setToTime(long toTime)	{
		this.toTime = toTime;
	}
	
	
	
	/**
	 * @return configuration required to initialize cluster
	 * @throws MalformedURLException
	 */
	private static Configuration getConfig() throws MalformedURLException {
		Configuration conf = new Configuration();
		//hdfs://b0c004ash2001.prod.symcpe.net:8020/
		//conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		//conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
		//conf.set("fs.defaultFS", "hdfs://b0c004ash2001.prod.symcpe.net:8020");
		//conf.set("mapreduce.jobhistory.address", "b0d004ash2003.prod.symcpe.net:10020");
		conf.addResource(new Path("/tmp/myconf/core-site.xml"));
		conf.addResource(new Path("/tmp/myconf/hdfs-site.xml"));
		conf.addResource(new Path("/tmp/myconf/mapred-site.xml"));
		conf.addResource(new Path("/tmp/myconf/yarn-site.xml"));
		conf.set("fs.defaultFS", "hdfs://b0c004ash2001.prod.symcpe.net");
		//conf.reloadConfiguration();
		return conf;
	}
    
}
