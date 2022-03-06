package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.configs.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PlayerJoinListener implements Listener {
  @Getter private final Plugin plugin;
  @Getter @Setter private UUID playerID;
  @Getter @Setter private PlayerData playerData;

  public PlayerJoinListener(Plugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler public void onPlayerJoin(final PlayerJoinEvent playerJoinEvent) {
    setPlayerID(playerJoinEvent.getPlayer().getUniqueId());
    setPlayerData(new PlayerData(getPlugin(), getPlayerID()));
  }
}
