package symc.monitor;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.mapreduce.TaskReport;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;



/**
 * @author Arshpreet_Singh
 * This class calls the methods to get data and post in the database
 */
public class MonitorRunner extends TimerTask implements Tool {
	private static Monitor monitor;
	HttpPost post;
	private static final Logger logger = Logger.getLogger(MonitorRunner.class);
	private static long fromTime = 0;
	private static long toTime = 0;
	private static long intervalTime = 0;
	private static long currentTime = 0;
	private static String filename;
	static String [] args;
	private static String uri = "hdfs://b0c004ash2001.prod.symcpe.net:8020/";
	private static String dirPath = "hdfs://b0c004ash2001.prod.symcpe.net:8020/user/vipul_sawant/monitor/user_data/";
	
	/**
	 * @param args
	 * Constructor method to be called from main class
	 */
	public MonitorRunner(String[] args) {
		// TODO Auto-generated constructor stub
		this.args = args;
	}



	/**
	 * 
	 */
	public static void main() {
		Configuration conf = new Configuration();
		conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

		currentTime = System.currentTimeMillis();

		// Interval time is passed when the
										// monitor is scheduled to run after
			intervalTime = 60; // a specific interval. In
													// this case, it processes
													// the jobs generated during
			intervalTime = intervalTime * 1000; // that past interval.
			fromTime = currentTime - intervalTime;
			toTime = Long.MAX_VALUE;




		final MonitorRunner monitorObj = new MonitorRunner(args);

		Calendar date = Calendar.getInstance();
		date.setTime(new Date(fromTime));

		
		monitor = new Monitor();

		monitor.setFromTime(fromTime);
		monitor.setToTime(toTime);

		try {
			System.out.println("Running...at time " + currentTime);
			System.out.println("Collecting Data from " + fromTime + " to " + toTime);
			ToolRunner.run(monitorObj, args);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	public Configuration getConf() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setConf(Configuration arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.apache.hadoop.util.Tool#run(java.lang.String[])
	 */
	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		JobStatus[] jobs = monitor.getJobs();
		if(jobs==null){
			System.out.println("not finding jobs");
		}
		for (JobStatus job : jobs) {
			ApplicationData jobData = monitor.getData(job);
			//app.sendApplicationPost(jobData);

			if (jobData == null) {
				continue;
			}else{
				
				String file, dir;
				BufferedWriter bw;
				System.out.println(jobData.toString());
				dir = dirPath;
				file = dir + "/" + "data.tsv";
				FileSystem fs = FileSystem.get(new URI(uri), new Configuration());
				
				Path path = new Path(file);
				
				if (fs.exists(path))
					bw = new BufferedWriter(new OutputStreamWriter(fs.append(path)));
				else
					bw = new BufferedWriter(new OutputStreamWriter(fs.create(path)));
				
				String jobName = jobData.getName().replaceAll("\n", " ").replaceAll("\t", " ");
				
				jobName = jobName.replaceAll("[\\d]{2}", "");
				jobName = jobName.replace("job", "application");
				
				
				bw.write(jobData.getJobId() + "\t"  + jobName + "\t"  + jobData.getUser() + "\t" + jobData.getType() + "\t" + jobData.getQueue() + "\t" + jobData.getStartTime() + "\t" + jobData.getFinishTime() + "\t" + jobData.getState() + "\t" + jobData.geturl() + "\t" + jobData.getElapsedTime() + "\t" + "NO");
				bw.newLine();
				bw.close();
				//post.sendApplicationPost(jobData);
				//post.sendTaskCounterPost(jobData);
				
				//-------------------TASKS------------------------------------------------------------------------
				
				dir = dirPath;
				file = dir + "/" + "task.tsv";
				fs = FileSystem.get(new URI(uri), new Configuration());
				
				 path = new Path(file);
				
				if (fs.exists(path))
					bw = new BufferedWriter(new OutputStreamWriter(fs.append(path)));
				else
					bw = new BufferedWriter(new OutputStreamWriter(fs.create(path)));
				
				TaskReport [] maptasks = jobData.getMapTasks();
				for(TaskReport report : maptasks){
					
					bw.write(report.getTaskId() + "\t" + jobData.getJobId() + "\t" + report.getStartTime() + "\t" + report.getFinishTime() + "\t" + "MAP");
					bw.newLine();
					
				}
				TaskReport [] redtasks = jobData.getReduceTasks();
				for(TaskReport report : redtasks){
					bw.write(report.getTaskId() + "\t" + jobData.getJobId() + "\t" + report.getStartTime() + "\t" + report.getFinishTime() + "\t" + "REDUCE");
					bw.newLine();
				}
				
				bw.close();
				
				//--------------------------------------TASK COUNTERS-------------------------------------------------------------------
				
				dir = dirPath;
				file = dir + "/" + "taskcounter.tsv";
				fs = FileSystem.get(new URI(uri), new Configuration());
				
				 path = new Path(file);
				
				if (fs.exists(path))
					bw = new BufferedWriter(new OutputStreamWriter(fs.append(path)));
				else
					bw = new BufferedWriter(new OutputStreamWriter(fs.create(path)));
				
				
				for(TaskReport report : maptasks){
					
					Counter counter;
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.FileSystemCounter", "HDFS_BYTES_READ");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "MAP");
					bw.newLine();
					
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "GC_TIME_MILLIS");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "MAP");
					bw.newLine();
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "CPU_MILLISECONDS");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "MAP");
					bw.newLine();
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "PHYSICAL_MEMORY_BYTES");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "MAP");
					bw.newLine();
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "VIRTUAL_MEMORY_BYTES");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "MAP");
					bw.newLine();
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "SPILLED_RECORDS");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "MAP");
					bw.newLine();
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_OUTPUT_RECORDS");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "MAP");
					bw.newLine();
				}
				
				for(TaskReport report : redtasks){
					Counter counter;
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "REDUCE_SHUFFLE_BYTES");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "REDUCE");
					bw.newLine();
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "GC_TIME_MILLIS");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "REDUCE");
					bw.newLine();
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "CPU_MILLISECONDS");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "REDUCE");
					bw.newLine();
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "PHYSICAL_MEMORY_BYTES");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "REDUCE");
					bw.newLine();
					
					counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "VIRTUAL_MEMORY_BYTES");
					bw.write(report.getTaskId() + "\t" + counter.getName() + "\t" + counter.getValue() + "\t" + "REDUCE");
					bw.newLine();
				}
				
				bw.close();				
				
			}
		}
		DataFetcher fetcher = new DataFetcher();
		ClusterData clusterData = null;
		clusterData = fetcher.fetch();
		System.out.println(clusterData.toString());
		//post.sendClusterUsagePost(data);
		String file, dir;
		BufferedWriter bw;
		
		dir = dirPath;
		file = dir + "/" + "cluster.tsv";
		FileSystem fs = FileSystem.get(new URI(uri), new Configuration());
		
		Path path = new Path(file);
		
		if (fs.exists(path))
			bw = new BufferedWriter(new OutputStreamWriter(fs.append(path)));
		else
			bw = new BufferedWriter(new OutputStreamWriter(fs.create(path)));
		
		
		bw.write(clusterData.getQueueName() + "\t" + clusterData.getMaxCapacity() + "\t" + clusterData.getUsedCapacity() + "\t" + clusterData.getdate());
		bw.newLine();
		bw.close();
		
		
		
		return 0;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		main();
		
	}
	
}
