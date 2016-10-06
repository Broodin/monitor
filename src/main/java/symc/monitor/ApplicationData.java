package symc.monitor;

import java.util.concurrent.TimeUnit;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.TaskReport;


/**
 * @author Arshpreet_Singh
 * This class contains get and set methods for application data
 */
public class ApplicationData {

	public boolean completed;											// Is the job Complete
	private String jobId;												// Job ID
	private String name;												// Job Name
	private String state;												// Job State
	private long startTime;												// Start Time of the Job
	private long finishTime;											// Finish Time
	private long elapsedTime;											// Total time taken by the job
	private String user;												// User name of the client
	private String queue;												// Queue containing the job
	private String priority;											// Priority of the Job
	private int usedSlots;												// Number of Slots used by the Job
	private int rsvdSlots;												// Number of Slots reserved for the Job
	private int usedMem;												// Amount of Memory used by the Job
	private int rsvdMem;												// Amount of Memory reserved for the Job
	private int neededMem;												// Amount of Memory needed by the Job

	private Configuration jobConfig;									// Configurations for a job
	private TaskReport[] mapTasks;										// Map Task Counters of the Job
	private TaskReport[] reduceTasks;									// Reduce Task Counters of the Job
	private String url;													// Tracking url of the job
	private String type;												//Type of the job (MapReduce/Tez)
	private float progress;												//Progress of the job
	

	/**
	 * Constructor for the class to create a new field for the particular Job
	 * @param jobId The Job ID of the job whose data is to be stored
	 */
	public ApplicationData(String jobId) {
		this.jobId = jobId;
	}	
	
	
	/**
	 * @param type
	 * set type of the job
	 */
	public void setType(String type){
		this.type = type;
	}
	/**
	 * @return type of the job
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * @param progress
	 * set progress of the job
	 */
	public void setProgress(float progress){
		this.progress = progress;
	}
	/**
	 * @return progress of the job
	 */
	public float getProgress(){
		return this.progress;
	}
	
	
	/**
	 * @param url
	 * set url for job history
	 */
	public void seturl(String url){
		this.url=url;
	}
	
	/**
	 * @return tracking url of the job
	 */
	public String geturl(){
		return this.url;
	}
	
	/**
	 * Get the Job ID
	 * @return Job ID
	 */
	public String getJobId() {
		return jobId;
	}
	/**
	 * Get the name of the Job
	 * @return Name of the Job
	 */
	public String getName() {
		return name;
	}
	/**
	 * Add the name of the Job
	 * @param name The name of the Job
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Get the state of the Job
	 * @return The state of the Job
	 */
	public String getState() {
		return state;
	}
	/**
	 * Add the state of the Job
	 * @param state The state of the Job
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * Get the Start Time of the Job
	 * @return Start Time of the Job
	 */
	public long getFinishTime() {
		return finishTime;
	}
	/**
	 * Add the Start Time of the Job
	 * @param startTime Start Time of the Job
	 */
	public void setFinishTime(long startTime) {
		this.finishTime = startTime;
	}
	
	/**
	 * Get the Start Time of the Job
	 * @return Start Time of the Job
	 */
	public long getStartTime() {
		return startTime;
	}
	/**
	 * Add the Start Time of the Job
	 * @param startTime Start Time of the Job
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Get the name of the user if the Job
	 * @return The name of the User of the Job
	 */
	public String getUser() {
		return user;
	}
	/**
	 * Add the name of the user of the Job
	 * @param user The name of the user of the Job
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * Get the Queue in which the Job is running
	 * @return The Queue in which the Job is running
	 */
	public String getQueue() {
		return queue;
	}
	/**
	 * Add the name of the Queue in which the Job is running
	 * @param queue The name of the Queue in which the Job is running
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}
	/**
	 * Get the priority of the Job
	 * @return The priority of the job
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * Add the priority of the Job
	 * @param priority The priority of the Job
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * Get the number of slots used by the Job
	 * @return The number of slots used by the Job
	 */
	public int getNumUsedSlots() {
		return usedSlots;
	}
	/**
	 * Add the number of slots used by the Job
	 * @param usedSlots The number of slots used by the Job
	 */
	public void setNumUsedSlots(int usedSlots) {
		this.usedSlots = usedSlots;
	}
	/**
	 * Get the number of slots reserved for the Job
	 * @return The number of slots reserved for the Job
	 */
	public int getNumRsvdSlots() {
		return rsvdSlots;
	}
	/**
	 * Add the number of slots reserved for the Job
	 * @param rsvdSlots The number of slots reserved for the Job
	 */
	public void setNumRsvdSlots(int rsvdSlots) {
		this.rsvdSlots = rsvdSlots;
	}
	/**
	 * Get the amount of memory used by the Job
	 * @return The amount of memory used by the Job
	 */
	public int getUsedMem() {
		return usedMem;
	}
	/**
	 * Add the amount of the memory used by the Job
	 * @param rsvdSlots The amount of memory used by the Job
	 */
	public void setUsedMem(int usedMem) {
		this.usedMem = usedMem;
	}
	/**
	 * Get the amount of memory reserved for the Job
	 * @return The amount of memory reserved for the Job
	 */
	public int getRsvdMem() {
		return rsvdMem;
	}
	/**
	 * Add the amount of memory reserved for the Job
	 * @param rsvdSlots The amount of memory reserved for the Job
	 */
	public void setRsvdMem(int rsvdMem) {
		this.rsvdMem = rsvdMem;
	}
	/**
	 * Get the amount of memory needed by the Job
	 * @return The amount of memory needed by the Job
	 */
	public int getNeededMem() {
		return neededMem;
	}
	/**
	 * Add the amount of memory needed by the Job
	 * @param rsvdSlots The amount of memory needed by the Job
	 */
	public void setNeededMem(int neededMem) {
		this.neededMem = neededMem;
	}
	/**
	 * Add all the map tasks run by the job
	 * @param mapTasks All the map tasks run by the job
	 */
	public void setMapTasks(TaskReport[] mapTasks) {
		this.mapTasks = mapTasks;		
	}
	/**
	 * Add all the reduce tasks run by the job
	 * @param mapTasks All the reduce tasks run by the job
	 */
	public void setReduceTasks(TaskReport[] reduceTasks) {
		this.reduceTasks = reduceTasks;		
	}
	/**
	 * Get the Task Reports about all the map tasks run by the job
	 * @return The Task Reports about all the map tasks run by the job
	 */
	public TaskReport[] getMapTasks() {
		return mapTasks;
	}
	/**
	 * Get the Task Reports about all the map tasks run by the job
	 * @return The Task Reports about all the map tasks run by the job
	 */
	public TaskReport[] getReduceTasks() {
		return reduceTasks;
	}
	/**
	 * Add the configurations the job
	 * @param job The configurations usd by the job
	 */
	public void setJobConfiguration(Configuration jobConfig) {
		this.jobConfig = jobConfig;		
	}
	/**
	 * Get the configurations used by the job
	 * @return HTe COnfigurations used by the job
	 */
	public Configuration getJobConfiguration() {
		return jobConfig;	
	}	
	/**
	 * Get the amount of time taken by the job for completion 
	 * @return The amount of time taken by the job for completion
	 */
	public String getElapsedTime() {
		
		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(elapsedTime),
	            TimeUnit.MILLISECONDS.toMinutes(elapsedTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime)),
	            TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)));

		return hms;
	}
	/**
	 * Set the amount of time taken by the job for completion
	 */
	public void setElapsedTime() {
		
		this.elapsedTime = finishTime - startTime;
		
	}
	
	@Override
	public String toString() {
		int l = jobId.length();
		String dash = "";
		for (int i = 0; i < l; i++) dash = dash + "-";
				
		return jobId + "\n" + dash + "\nName: " + name + "\nState: " + state + "\nStart Time: " + startTime
				+ "\nUser Name: " + user + "\nQueue: " + queue + "\nPriority: " + priority
				+ "\nNumber of Slots Used: " + usedSlots + "\nNumber of Slots Reserved: "
				+ rsvdSlots + "\nUsed Memory: " + usedMem + "\nReserved Memory: " + rsvdMem + "\nNeeded Memory: "
				+ neededMem + "\n";
	}
	
}
