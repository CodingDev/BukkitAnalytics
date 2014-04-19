package de.CodingDev.BukkitAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;

public class BukkitAnalyticsReport {
	private BukkitAnalytics bukkitAnalytics;
	
	public BukkitAnalyticsReport(BukkitAnalytics bukkitAnalytics) {
		this.bukkitAnalytics = bukkitAnalytics;
	}
	
	private Map<BukkitAnalyticsEventType, Integer> countEvent = new HashMap<BukkitAnalyticsEventType, Integer>();

	public void countEvent(BukkitAnalyticsEventType eventType) {
		if(countEvent.containsKey(eventType)){
			countEvent.put(eventType, countEvent.get(eventType) + 1);
		}else{
			countEvent.put(eventType, 1);
		}
	}
	
	public double getServerOnlineBalance(){
		double balance = 0;
		if(bukkitAnalytics.hasVault()){
			for(Player p : bukkitAnalytics.getServer().getOnlinePlayers()){
				balance += bukkitAnalytics.econ.getBalance(p.getName());
			}
			return balance;
		}else{
			return balance;
		}
	}
	
	public double getServerTotalBalance(){
		double balance = 0;
		if(bukkitAnalytics.hasVault()){
			for(OfflinePlayer p : bukkitAnalytics.getServer().getOnlinePlayers()){
				balance += bukkitAnalytics.econ.getBalance(p.getName());
			}
			return balance;
		}else{
			return balance;
		}
	}

	public String getJsonReportData() {
		JSONObject obj=new JSONObject();
		obj.put("trackingKey", bukkitAnalytics.getConfig().getString("trackingKey"));
		List<String> countList = new ArrayList<String>();
		for(Entry<BukkitAnalyticsEventType, Integer> d : countEvent.entrySet()){
			countList.add(d.getKey().name() + ":" + d.getValue());
		}
		obj.put("eventCountList", countList);
		List<String> serverPlugins = new ArrayList<String>();
		for(Plugin p : bukkitAnalytics.getServer().getPluginManager().getPlugins()){
			serverPlugins.add(p.getName());
		}
		obj.put("serverPlugins", serverPlugins);
		List<String> serverPlayers = new ArrayList<String>();
		for(Player p : bukkitAnalytics.getServer().getOnlinePlayers()){
			serverPlayers.add(p.getName());
		}
		obj.put("serverPlayers", serverPlayers);
		obj.put("serverTotalBalance", getServerTotalBalance());
		obj.put("serverOnlineBalance", getServerOnlineBalance());
		return obj.toJSONString();
	}

	public void clearReport() {
		countEvent.clear();
	}
	
}
