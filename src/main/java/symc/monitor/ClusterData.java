package symc.monitor;

/**
 * @author Arshpreet_Singh
 * This class contains methods to store cluster information
 */
public class ClusterData {
	private String queue_name;
	private float max_capacity;
	private float used_capacity;
	private long date;
	
	/**
	 * Sets the name of queue
	 * @param queue_name
	 */
	public void setQueueName(String queue_name){
		this.queue_name = queue_name;
	}
	
	/**
	 * @return the name of queue
	 */
	public String getQueueName(){
		return this.queue_name;
	}
	
	/**
	 * Sets maximum capacity of the queue
	 * @param max_capacity
	 */
	public void setMaxCapacity(float max_capacity){
		this.max_capacity = max_capacity;
	}
	
	/**
	 * @return maximum capacity of queue
	 */
	public float getMaxCapacity(){
		return this.max_capacity;
	}
	
	/**
	 * sets the amount of queue used
	 * @param used_capacity
	 */
	public void setUsedCapacity(float used_capacity){
		this.used_capacity = used_capacity;
	}
	
	/**
	 * @return capacity of queue used
	 */
	public float getUsedCapacity(){
		return this.used_capacity;
	}
	
	/**
	 * sets the current timestamp
	 * @param date
	 */
	public void setDate(long date){
		this.date= date;
	}
	
	/**
	 * @return current timestamp
	 */
	public long getdate(){
		return this.date;
	}
	
	public String toString(){
		return queue_name + "\n" + used_capacity;
		
	}
}
