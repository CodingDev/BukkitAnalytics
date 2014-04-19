package de.CodingDev.BukkitAnalytics;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BukkitAnalyticsSendReport extends Thread{
	private boolean b;
	private BukkitAnalyticsReport analyticsReport;
	private BukkitAnalytics bukkitAnalytics;
	private String USER_AGENT = "BukkitAnalytics/Unknow";

	public BukkitAnalyticsSendReport(BukkitAnalytics bukkitAnalytics, BukkitAnalyticsReport analyticsReport) {
		this.bukkitAnalytics = bukkitAnalytics;
		this.analyticsReport = analyticsReport;
		USER_AGENT = "BukkitAnalytics/" + bukkitAnalytics.getDescription().getVersion();
	}

	public void setRunning(boolean b) {
		this.b = b;
	}
	
	private String getTrackingAPIServer(){
		if(bukkitAnalytics.isDebugMode()){
			return "http://127.0.0.1/api/v1/report.php";
		}else{
			return "http://BukkitAnalytics.net/api/v1/report.php";
		}
	}
	
	public void run(){
		while(b){
			try{
				sendReport();
				if(bukkitAnalytics.isDebugMode()){
					Thread.sleep(1*1000);
				}else{
					Thread.sleep(60*1000);
				}
			}catch(Exception threadError){
				try{
					Thread.sleep(1000);
				}catch(Exception sleepError){}
				threadError.getStackTrace();
			}
		}
	}

	public void sendReport() {
		bukkitAnalytics.sendDebugMessage("Sending BukkitAnalytics Report...");
		String jsonReport = analyticsReport.getJsonReportData();
		bukkitAnalytics.sendDebugMessage("Report Data: " + jsonReport);
		
		try{
			URL obj = new URL(getTrackingAPIServer());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	 
			String urlParameters = "jsonReport=" + URLEncoder.encode(jsonReport);
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
			bukkitAnalytics.sendDebugMessage("Sending 'POST' request to URL : " + getTrackingAPIServer());
			bukkitAnalytics.sendDebugMessage("Post parameters : " + urlParameters);
			bukkitAnalytics.sendDebugMessage("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result		
			bukkitAnalytics.sendDebugMessage("Response Data: " + response.toString());
			analyticsReport.clearReport();
		}catch(Exception sendError){
			
		}
		bukkitAnalytics.sendDebugMessage("Done!");
	}
	
}
