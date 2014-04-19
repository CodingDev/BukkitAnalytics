package de.CodingDev.BukkitAnalytics;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitAnalytics extends JavaPlugin{
	private BukkitAnalyticsReport analyticsReport;
	private BukkitAnalyticsSendReport sendReport;
	private BukkitAnalyticsEvents analyticsEvents;
	public Economy econ = null;
	
	public void onEnable(){
		initConfig();
		analyticsReport = new BukkitAnalyticsReport(this);
		analyticsEvents = new BukkitAnalyticsEvents(this);
		getServer().getPluginManager().registerEvents(analyticsEvents, this);
		setupEconomy();
		sendReport = new BukkitAnalyticsSendReport(this, analyticsReport);
		sendReport.setRunning(true);
		sendReport.start();
		getLogger().info("BukkitAnalytics was enabled!");
	}
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
	}
	
	public boolean hasVault(){
		return econ != null;
	}

	public void onDisable(){
		sendReport.setRunning(false);
		sendReport.sendReport();
		getLogger().info("BukkitAnalytics was disabled!");
	}
	
	public void initConfig(){
		//Basic Options
		getConfig().addDefault("Config.TrackingKey", "");
		getConfig().addDefault("Config.DebugMode", false);
		
		//Tracking Options
		getConfig().addDefault("Track.Plugins", true);
		getConfig().addDefault("Track.Players.Online", true);
		getConfig().addDefault("Track.Balance.Online", true);
		getConfig().addDefault("Track.Balance.Offline", true);
		for(BukkitAnalyticsEventType eventType : BukkitAnalyticsEventType.values()){
			getConfig().addDefault("Track.Events." + eventType.name(), true);
		}
		
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public BukkitAnalyticsReport getReport() {
		return analyticsReport;
	}
	
	public boolean isDebugMode(){
		return getConfig().getBoolean("Config.DebugMode");
	}
	
	public void sendDebugMessage(String message) {
		if(isDebugMode()){
			getLogger().info("[DEBUG] " + message);
		}
	}
}
