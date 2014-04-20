package de.CodingDev.BukkitAnalytics;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.sun.net.ssl.HttpsURLConnection;

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
		if(bukkitAnalytics.isDebugMode() && bukkitAnalytics.useDebugServer()){
			return "https://api.bukkitanalytics.net/1.0/?query=Tracking&debug=true";
		}else{
			return "https://api.bukkitanalytics.net/1.0/?query=Tracking";
		}
	}
	
	private String getAuthAPIServer(){
		if(bukkitAnalytics.isDebugMode() && bukkitAnalytics.useDebugServer()){
			return "https://api.bukkitanalytics.net/1.0/?query=Auth&debug=true";
		}else{
			return "https://api.bukkitanalytics.net/1.0/?query=Auth";
		}
	}
	
	public void run(){
		while(b){
			try{
				sendReport();
				if(bukkitAnalytics.isDebugMode()){
					Thread.sleep(5*1000);
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
			bukkitAnalytics.getLogger().warning("Can not connect to the API Server. Error: " + sendError);
		}
		bukkitAnalytics.sendDebugMessage("Done!");
	}

	public boolean validateKey(String authKey) {
		bukkitAnalytics.sendDebugMessage("Validate BukkitAnalytics Auth-Key...");		
		try{
			URL obj = new URL(getAuthAPIServer());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
	 
			String urlParameters = "authKey=" + bukkitAnalytics.getAuthKey();
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
			bukkitAnalytics.sendDebugMessage("Sending 'POST' request to URL : " + getAuthAPIServer());
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
			bukkitAnalytics.sendDebugMessage("Done!");
			if(responseCode == 200){
				return true;
			}
		}catch(Exception sendError){
			bukkitAnalytics.getLogger().warning("Can not connect to the API Server. Error: " + sendError);
		}
		return false;
	}
	
}
