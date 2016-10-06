package symc.monitor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.TaskReport;


/**
 * @author Arshpreet_Singh
 *	This class sends data into the database using HTTP POST method
 */
public class HttpPost {
	private final String USER_AGENT = "Mozilla/5.0";

	/**
	 * sends the application data
	 * @param data
	 * @throws Exception
	 */
	void sendApplicationPost(ApplicationData data) throws Exception {

		String http_url = "http://100.73.175.158:5000/app/application/post";
		URL obj = new URL(http_url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		long finish_time = data.getFinishTime();
		String id = data.getJobId();
		String name = data.getName();
		String user = data.getUser();
		long start_time = data.getStartTime();
		String queue = data.getQueue() ;
		String elapsed_time = data.getElapsedTime();
		String state = data.getState();
		String url = data.geturl();
		String urlParameters = "id=" + id + "&name=" + name + "&user=" + user + "&queue=" + queue + "&start_time="
				+ start_time + "&finish_time=" + finish_time + "&state=" + state + "&url=" + url + "&elapsed_time="
				+ elapsed_time;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + http_url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}


	/**
	 * sends the cluster usage data
	 * @param data
	 * @throws Exception
	 */
	void sendClusterUsagePost(ClusterData data) throws Exception {

		String http_url = "http://100.73.175.158:5000/app/cluster/post";
		URL obj = new URL(http_url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		String q_name = data.getQueueName();
		float used_capacity = data.getUsedCapacity();
		float max_capacity = data.getMaxCapacity();
		long time = data.getdate();
		String urlParameters = "q_name=" + q_name + "&used_capacity=" + used_capacity + "&max_capacity=" + max_capacity + "&time=" + time;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + http_url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}

	/**
	 * sends the mapper counters of the given job
	 * @param data
	 * @throws IOException
	 */
	void sendMapCounterPost(ApplicationData data) throws IOException{
		String http_url = "http://100.73.175.158:5000/app/mapcounter/post";
		URL obj = new URL(http_url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		String urlParameters = null;
		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		TaskReport [] map_report = data.getMapTasks();


		for(TaskReport report : map_report){
			Counter counter;
			String name;
			long value;
			String id = data.getJobId();
			String task_id = report.getTaskId();
			DataOutputStream wr;
			int responseCode;			
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.FileSystemCounter", "HDFS_BYTES_READ");
			name = counter.getName();
			value = counter.getValue();
			
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "MAP" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "GC_TIME_MILLIS");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "MAP"+ "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "CPU_MILLISECONDS");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "MAP"+ "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "PHYSICAL_MEMORY_BYTES");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "MAP" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "VIRTUAL_MEMORY_BYTES");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "MAP" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "SPILLED_RECORDS");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "MAP" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "MAP_OUTPUT_RECORDS");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "MAP" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
	
		}		
	}
	
	/**
	 * sends the reducer counters for the given job
	 * @param data
	 * @throws IOException
	 */
	void sendReduceCounterPost(ApplicationData data) throws IOException{
		String http_url = "http://100.73.175.158:5000/app/application/post";
		URL obj = new URL(http_url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		String urlParameters = null;
		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		TaskReport [] reduce_report = data.getReduceTasks();
		
		for(TaskReport report : reduce_report){
			
			
			Counter counter;
			String name;
			String id = data.getJobId();
			String task_id = report.getTaskId();
			long value;
			DataOutputStream wr;
			int responseCode;
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "REDUCE_SHUFFLE_BYTES");
			name = counter.getName();
			value = counter.getValue();
			
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "REDUCE" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "GC_TIME_MILLIS");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "REDUCE" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "CPU_MILLISECONDS");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "REDUCE" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "PHYSICAL_MEMORY_BYTES");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "REDUCE" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			
			//----------------------------------------------------------------------------------------------------------
			
			counter = report.getTaskCounters().findCounter("org.apache.hadoop.mapreduce.TaskCounter", "VIRTUAL_MEMORY_BYTES");
			name = counter.getName();
			value = counter.getValue();
			//id = data.getJobId();
			urlParameters = "app_id=" + id + "&name=" + name + "&value=" + value + "&type=" + "REDUCE" + "&task_id=" + task_id;


			// Send post request
			con.setDoOutput(true);
			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();


			responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
			
		}
		
	}
	
	/**
	 * sends all the reducer tasks of job 
	 * @param data
	 * @throws IOException
	 */
	void sendReduceTasksPost(ApplicationData data) throws IOException{
		String http_url = "http://100.73.175.158:5000/app/application/post";
		URL obj = new URL(http_url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		String urlParameters = null;
		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		TaskReport [] reduce_report = data.getReduceTasks();
		
		for(TaskReport report : reduce_report){
			String id =  data.getJobId();
			String task_id = report.getTaskId();
			long finish_time  = report.getFinishTime();
			long start_time = report.getStartTime();
			
			urlParameters = "app_id=" + id + "&task_id=" + task_id + "&start_time=" + start_time + "&finish_time=" + finish_time;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
		}
		
	}
	
	/**
	 * sends all the mapper tasks of the job
	 * @param data
	 * @throws IOException
	 */
	void sendMapTasksPost(ApplicationData data) throws IOException{
		String http_url = "http://100.73.175.158:5000/app/application/post";
		URL obj = new URL(http_url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		String urlParameters = null;
		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		TaskReport [] map_report = data.getMapTasks();
		
		for(TaskReport report : map_report){
			String id =  data.getJobId();
			String task_id = report.getTaskId();
			long finish_time  = report.getFinishTime();
			long start_time = report.getStartTime();
			
			urlParameters = "app_id=" + id + "&task_id=" + task_id + "&start_time=" + start_time + "&finish_time=" + finish_time;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + http_url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
			
		}
		
	}
}
