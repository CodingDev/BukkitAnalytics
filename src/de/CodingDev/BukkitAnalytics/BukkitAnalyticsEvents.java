package de.CodingDev.BukkitAnalytics;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class BukkitAnalyticsEvents implements Listener {
	private BukkitAnalytics bukkitAnalytics;

	public BukkitAnalyticsEvents(BukkitAnalytics bukkitAnalytics){
		this.bukkitAnalytics = bukkitAnalytics;
	}
	
	@EventHandler
	public void onEvent(PlayerJoinEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_JOIN);
		if(bukkitAnalytics.newVersion){
			e.getPlayer().sendMessage(bukkitAnalytics.prefix + String.format(bukkitAnalytics.getMessage("newVersion"), bukkitAnalytics.versionNumber));
		}
	}
	
	@EventHandler
	public void onEvent(PlayerQuitEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_QUIT);
	}
	
	@EventHandler
	public void onEvent(BlockPlaceEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.BLOCK_PLACE);
	}
	
	@EventHandler
	public void onEvent(BlockBreakEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.BLOCK_BREAK);
	}
	
	@EventHandler
	public void onEvent(AsyncPlayerChatEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.ASYNC_PLAYER_CHAT);
	}
	
	@EventHandler
	public void onEvent(PlayerMoveEvent e){
		double distance = e.getFrom().distance(e.getTo());
		if(e.getPlayer().isInsideVehicle()){
			if(e.getPlayer().getVehicle().getType() == EntityType.MINECART){
				bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.DISTANCE_MOVED_IN_MINECART, distance);
			}else if(e.getPlayer().getVehicle().getType() == EntityType.BOAT){
				bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.DISTANCE_MOVED_IN_BOAT, distance);
			}else{
				bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.DISTANCE_MOVED_IN_UNKNOW_VEHICLE, distance);
			}
		}
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.DISTANCE_MOVED_TOTAL, distance);
	}
	
	@EventHandler
	public void onEvent(EntityDeathEvent e){
		if(e.getEntity() instanceof Player){
			bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.ENTITY_DEATH);
		}else{
			bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_DEATH);
		}
	}
	
	@EventHandler
	public void onEvent(EntityTeleportEvent e){
		if(e.getEntity() instanceof Player){
			bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_TELEPORT);
		}else{
			bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.ENTITY_TELEPORT);
		}
	}
	
	@EventHandler
	public void onEvent(PotionSplashEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.POTION_SPLASH);
	}
	
	@EventHandler
	public void onEvent(ProjectileHitEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PROJECTIL_EHIT);
	}
	
	@EventHandler
	public void onEvent(EntityRegainHealthEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.ENTITY_REGAIN_HEALTH);
	}
	
	@EventHandler
	public void onEvent(EntityExplodeEvent e){
		if(e.getEntity() instanceof Player){
			bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_EXPLODE);
		}else{
			bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.ENTITY_EXPLODE);
		}
	}
	
	@EventHandler
	public void onEvent(NotePlayEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.NOTE_PLAY);
	}
	
	@EventHandler
	public void onEvent(ServerListPingEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.SERVER_LIST_PING);
	}
	
	@EventHandler
	public void onEvent(PlayerBedEnterEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_BED_ENTER);
	}
	
	@EventHandler
	public void onEvent(PlayerBucketEmptyEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_BUCKET_EMPTY);
	}
	
	@EventHandler
	public void onEvent(PlayerBucketFillEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_BUCKET_FILL);
	}
	
	@EventHandler
	public void onEvent(PlayerEggThrowEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_EGG_TRROW);
	}
	
	@EventHandler
	public void onEvent(PlayerFishEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_FISH);
	}
	
	@EventHandler
	public void onEvent(PlayerKickEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_KICK);
	}
	
	@EventHandler
	public void onEvent(PlayerPickupItemEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_PICKUP_ITEM);
	}
	
	@EventHandler
	public void onEvent(PlayerRespawnEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_RESPAWN);
	}
	
	@EventHandler
	public void onEvent(PlayerDropItemEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_DROP_IREM);
	}
	
	@EventHandler
	public void onEvent(PlayerCommandPreprocessEvent e){
		bukkitAnalytics.getReport().countEvent(BukkitAnalyticsEventType.PLAYER_COMMAND);
	}
}
