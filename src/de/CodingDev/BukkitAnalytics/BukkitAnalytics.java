package de.CodingDev.BukkitAnalytics;

import java.util.HashMap;
import java.util.Map.Entry;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.World;
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
	public HashMap<String, Boolean> trackPermissions = new HashMap<String, Boolean>();
	public HashMap<String, Boolean> basicTrackPermissions = new HashMap<String, Boolean>();
	private long uptime;
	
	public void onEnable(){
		getLogger().info("Enable BukkitAnalytics...");
		uptime = System.currentTimeMillis()/1000;
		initConfig();
		analyticsReport = new BukkitAnalyticsReport(this);
		if(new BukkitAnalyticsSendReport(this, analyticsReport).validateKey(getAuthKey())){
			analyticsEvents = new BukkitAnalyticsEvents(this);
			getServer().getPluginManager().registerEvents(analyticsEvents, this);
			setupEconomy();
			sendReport = new BukkitAnalyticsSendReport(this, analyticsReport);
			sendReport.setRunning(true);
			sendReport.start();
			getLogger().info("BukkitAnalytics was enabled!");
		}else{
			getLogger().warning("BukkitAnalytics can not started!");
			getLogger().warning("Your Auth-Key is not valid. (Your Key: " + getAuthKey() + ")");
			getServer().getPluginManager().disablePlugin(this);
		}
		
		
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
	}
	
	public void onDisable(){
		getLogger().info("Disable BukkitAnalytics...");
		if(sendReport != null){
			sendReport.setRunning(false);
			sendReport.sendReport();
		}
		getLogger().info("BukkitAnalytics was disabled!");
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
	
	public void initConfig(){
		//Basic Options
		getConfig().addDefault("Config.AuthKey", "");
		getConfig().addDefault("Config.DebugMode", false);
		getConfig().addDefault("Config.DebugServer", false);
		getConfig().addDefault("Config.Language", "enUS");
		
		//Messages - enUS
		getConfig().addDefault("Messages.enUS.newVersion", "We have released the version §c%s§6!");
		
		//Messages - deDE
		getConfig().addDefault("Messages.deDE.newVersion", "Wir haben die Version §c%s§6 veröffentlicht!");
		
		
		//Setup Permissions for Config
		initPermissions();
		for(Entry<String, Boolean> trackPermission : basicTrackPermissions.entrySet()){
			getConfig().addDefault(trackPermission.getKey(), trackPermission.getValue());
		}
		
		//Cache Permissions from Config
		int allowedPermission = 0;
		
		for(Entry<String, Boolean> trackPermission : basicTrackPermissions.entrySet()){
			boolean b = getConfig().getBoolean(trackPermission.getKey());
			if(b){
				allowedPermission++;
			}
			trackPermissions.put(trackPermission.getKey(), b);
		}
		
		if(basicTrackPermissions.size() == allowedPermission){
			getLogger().info("Good Job, Bro! We have all Track-Permissions to Analytics you server. (" + allowedPermission + " of " + basicTrackPermissions.size() +" Rules are Allowed)");
		}else{
			getLogger().warning("Hey, Bro! We dont have all Track-Permissions to Analytics you server. :/ (" + allowedPermission + " of " + basicTrackPermissions.size() +" Rules are Allowed)");
		}
		
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void initPermissions(){
		basicTrackPermissions.put("Track.Plugins", true);
		basicTrackPermissions.put("Track.Ban.Bans", true);
		basicTrackPermissions.put("Track.Ban.IPBans", true);
		basicTrackPermissions.put("Track.Players.Online", true);
		basicTrackPermissions.put("Track.Players.Operators", true);
		basicTrackPermissions.put("Track.Players.Whitelisted", true);
		basicTrackPermissions.put("Track.Balance.Online", true);
		basicTrackPermissions.put("Track.Balance.Total", true);
		for(World w : getServer().getWorlds()){
			basicTrackPermissions.put("Track.Worlds." + w.getName(), true);
		}
		for(BukkitAnalyticsEventType eventType : BukkitAnalyticsEventType.values()){
			basicTrackPermissions.put("Track.Events." + eventType.name(), true);
		}
	}
	
	public String getAuthKey(){
		return getConfig().getString("Config.AuthKey");
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

	public long getUptime() {
		return uptime;
	}

	public boolean useDebugServer() {
		return getConfig().getBoolean("Config.DebugServer");
	}
}
