package de.CodingDev.BukkitAnalytics;

public class BukkitAnalyticsSendReport extends Thread{

	private boolean b;
	private BukkitAnalyticsReport analyticsReport;
	private BukkitAnalytics bukkitAnalytics;

	public BukkitAnalyticsSendReport(BukkitAnalytics bukkitAnalytics, BukkitAnalyticsReport analyticsReport) {
		this.bukkitAnalytics = bukkitAnalytics;
		this.analyticsReport = analyticsReport;
	}

	public void setRunning(boolean b) {
		this.b = b;
	}
	
	private String getTrackingAPIServer(){
		if(bukkitAnalytics.isDebugMode()){
			return "http://127.0.0.1/api/v1/report.php";
		}else{
			return "http://127.0.0.1/api/v1/report.php";
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
		analyticsReport.clearReport();
		
		bukkitAnalytics.sendDebugMessage("Report Data: " + jsonReport);
		bukkitAnalytics.sendDebugMessage("Done!");
	}
	
}
