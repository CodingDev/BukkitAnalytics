package de.CodingDev.BukkitAnalytics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitAnalyticsEvents implements Listener {
	private BukkitAnalytics bukkitAnalytics;

	public BukkitAnalyticsEvents(BukkitAnalytics bukkitAnalytics){
		this.bukkitAnalytics = bukkitAnalytics;
	}
	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_JOIN);
		if(bukkitAnalytics.newVersion){
			e.getPlayer().sendMessage(bukkitAnalytics.prefix + String.format(bukkitAnalytics.getMessage("newVersion"), bukkitAnalytics.versionNumber));
		}
	}
	
	@EventHandler
	public void onJoinEvent(PlayerQuitEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_QUIT);
	}
	
	@EventHandler
	public void onJoinEvent(BlockPlaceEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.BLOCK_PLACE);
	}
	
	@EventHandler
	public void onJoinEvent(BlockBreakEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.BLOCK_BREAK);
	}
	
	@EventHandler
	public void onJoinEvent(AsyncPlayerChatEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.ASYNC_PLAYER_CHAT);
	}
}
