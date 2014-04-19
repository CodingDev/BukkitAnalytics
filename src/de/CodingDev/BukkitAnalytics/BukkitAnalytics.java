package de.CodingDev.BukkitAnalytics;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.CodingDev.BukkitAnalytics.UpdateChecker.Updater;
import de.CodingDev.BukkitAnalytics.UpdateChecker.Updater.UpdateResult;
import de.CodingDev.BukkitAnalytics.UpdateChecker.Updater.UpdateType;

public class BukkitAnalytics extends JavaPlugin{
	private BukkitAnalyticsReport analyticsReport;
	private BukkitAnalyticsSendReport sendReport;
	private BukkitAnalyticsEvents analyticsEvents;
	public Economy econ = null;
	public boolean newVersion = false;
	public String versionNumber = "";
	public String prefix = "§8[§9BukkitAnalytics§8] §6";
	
	public void onEnable(){
		initConfig();
		analyticsReport = new BukkitAnalyticsReport(this);
		analyticsEvents = new BukkitAnalyticsEvents(this);
		getServer().getPluginManager().registerEvents(analyticsEvents, this);
		setupEconomy();
		sendReport = new BukkitAnalyticsSendReport(this, analyticsReport);
		sendReport.setRunning(true);
		sendReport.start();
		
		Updater updater = new Updater(this, 78526, this.getFile(), UpdateType.NO_DOWNLOAD, true);
		if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
		    getLogger().info("New version available! (" + updater.getLatestName() + ")");
		    newVersion = true;
		    versionNumber = updater.getLatestName();
		}else if (updater.getResult() == UpdateResult.NO_UPDATE) {
		    getLogger().info("No new version available.");
		}else{
		    getLogger().info("Updater: " + updater.getResult());
		}
		
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
		getConfig().addDefault("Config.Language", "enUS");
		
		//Messages - enUS
		getConfig().addDefault("Messages.enUS.newVersion", "We have released the version §c%s§6!");
		
		//Messages - deDE
		getConfig().addDefault("Messages.deDE.newVersion", "Wir haben die Version §c%s§6 veröffentlicht!");
		
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

	public String getMessage(String key) {
		return getConfig().getString("Messages." + getConfig().getString("Config.Language") + "." + key);
	}
}
