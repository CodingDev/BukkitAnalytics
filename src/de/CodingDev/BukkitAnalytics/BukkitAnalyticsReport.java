package de.CodingDev.BukkitAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;

public class BukkitAnalyticsReport {
	private BukkitAnalytics bukkitAnalytics;
	
	public BukkitAnalyticsReport(BukkitAnalytics bukkitAnalytics) {
		this.bukkitAnalytics = bukkitAnalytics;
	}
	
	private Map<BukkitAnalyticsEventType, Double> countEvent = new HashMap<BukkitAnalyticsEventType, Double>();

	public void countEvent(BukkitAnalyticsEventType eventType) {
		countEvent(eventType, 1);
	}
	
	public void countEvent(BukkitAnalyticsEventType eventType, double d) {
		if(countEvent.containsKey(eventType)){
			countEvent.put(eventType, countEvent.get(eventType) + d);
		}else{
			countEvent.put(eventType, d);
		}
	}
	
	public boolean hasTrackPermission(BukkitAnalyticsEventType eventType){
		return hasTrackPermission("Track.Events." + eventType.name());
	}
	
	public boolean hasTrackPermission(String permission){
		return bukkitAnalytics.trackPermissions.get(permission);
	}
	
	public double getServerOnlineBalance(){
		double balance = 0;
		if(bukkitAnalytics.hasVault()){
			for(Player p : bukkitAnalytics.getServer().getOnlinePlayers()){
				try{
					balance += bukkitAnalytics.econ.getBalance(p.getName());
				}catch(Exception e){}
			}
			return balance;
		}else{
			return balance;
		}
	}
	
	public double getServerTotalBalance(){
		double balance = 0;
		if(bukkitAnalytics.hasVault()){
			for(OfflinePlayer p : bukkitAnalytics.getServer().getOfflinePlayers()){
				try{
					balance += bukkitAnalytics.econ.getBalance(p.getName());
				}catch(Exception e){}
			}
			return balance;
		}else{
			return balance;
		}
	}

	public String getJsonReportData() {
		JSONObject obj = new JSONObject();
		List<JSONObject> countList = new ArrayList<JSONObject>();
		for(Entry<BukkitAnalyticsEventType, Double> d : countEvent.entrySet()){
			if(hasTrackPermission(d.getKey())){
				JSONObject pObj = new JSONObject();
				pObj.put("EventName", d.getKey().name());
				pObj.put("Count", d.getValue());
				countList.add(pObj);
			}
		}
		obj.put("EventCountList", countList);
		if(hasTrackPermission("Track.Balance.Total")){
			obj.put("VaultTotalBalance", getServerTotalBalance());
		}
		if(hasTrackPermission("Track.Balance.Online")){
			obj.put("VaultOnlineBalance", getServerOnlineBalance());
		}
		
		//Server Stats & Infos
		if(hasTrackPermission("Track.Players.Online")){
			List<JSONObject> serverPlayers = new ArrayList<JSONObject>();
			for(Player p : bukkitAnalytics.getServer().getOnlinePlayers()){
				JSONObject pObj = new JSONObject();
				pObj.put("Username", p.getName());
				pObj.put("World", p.getWorld().getName());
				serverPlayers.add(pObj);
			}
			obj.put("PlayersOnlineList", serverPlayers);
		}
		if(hasTrackPermission("Track.Plugins")){
			List<String> serverPlugins = new ArrayList<String>();
			for(Plugin p : bukkitAnalytics.getServer().getPluginManager().getPlugins()){
				serverPlugins.add(p.getName());
			}
			obj.put("ServerPlugins", serverPlugins);
		}
		obj.put("PlayersOnline", bukkitAnalytics.getServer().getOnlinePlayers().length);
		obj.put("PlayersMax", bukkitAnalytics.getServer().getMaxPlayers());
		obj.put("ServerVersion", bukkitAnalytics.getServer().getVersion());
		obj.put("BukkitVersion", bukkitAnalytics.getServer().getBukkitVersion());
		
		if(hasTrackPermission("Track.Players.Whitelisted")){
			List<String> whitelistedPlayers = new ArrayList<String>();
			for(OfflinePlayer p : bukkitAnalytics.getServer().getWhitelistedPlayers()){
				whitelistedPlayers.add(p.getName());
			}
			obj.put("WhitelistedPlayers", whitelistedPlayers);
		}
		
		if(hasTrackPermission("Track.Players.Operators")){
			List<String> operators = new ArrayList<String>();
			for(OfflinePlayer p : bukkitAnalytics.getServer().getOperators()){
				operators.add(p.getName());
			}
			obj.put("ServerOperators", operators);
		}
		
		obj.put("HasWhitelist", bukkitAnalytics.getServer().hasWhitelist());
		
		obj.put("ServerUptime", bukkitAnalytics.getUptime());
		obj.put("ServerMotd", bukkitAnalytics.getServer().getMotd());
		List<String> worlds = new ArrayList<String>();
		for(World w : bukkitAnalytics.getServer().getWorlds()){
			if(hasTrackPermission("Track.Worlds." + w.getName())){
				worlds.add(w.getName());
			}
		}
		obj.put("ServerWorlds", worlds);
		if(hasTrackPermission("Track.Ban.Bans")){
			List<String> bans = new ArrayList<String>();
			for(OfflinePlayer p : bukkitAnalytics.getServer().getBannedPlayers()){
				bans.add(p.getName());
			}
			obj.put("BannedPlayers", bans);
		}
		
		if(hasTrackPermission("Track.Ban.IPBans")){
			List<String> IPbans = new ArrayList<String>();
			for(String p : bukkitAnalytics.getServer().getIPBans()){
				IPbans.add(p);
			}
			obj.put("IPBans", IPbans);
		}
		return obj.toJSONString();
	}

	public void clearReport() {
		countEvent.clear();
	}
	
}
