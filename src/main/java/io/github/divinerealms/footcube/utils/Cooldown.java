package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
  @Getter private final Messages messages;
  @Getter private final FileConfiguration config;
  @Getter private final HashMap<UUID, Long> cooldowns = new HashMap<>();
  @Getter private long cubeSpawnCooldown, cubeKickCooldown;
  @Getter private String onCooldown;
  @Getter private boolean isCubeKickCooldownEnabled;

  public Cooldown(final Plugin plugin, final UtilManager utilManager) {
    this.config = plugin.getConfig();
    this.messages = utilManager.getMessages();
  }

  public void reload() {
    this.cubeSpawnCooldown = getConfig().getInt("cube.spawn-cooldown");
    this.cubeKickCooldown = getConfig().getInt("cube.kick-cooldown.cooldown");
    this.onCooldown = getMessages().getString("on-cooldown");
    this.isCubeKickCooldownEnabled = getConfig().getBoolean("cube.kick-cooldown.enabled");
  }

  public void setCooldown(final UUID playerID, final Long time) {
    if (time == null) getCooldowns().remove(playerID);
    else getCooldowns().put(playerID, time);
  }

  public long getCooldown(final UUID playerID) {
    return (getCooldowns().get(playerID) == null ? 0L : getCooldowns().get(playerID));
  }

  public long getTimeleft(final UUID playerID, final Long cooldown) {
    return getCooldowns().containsKey(playerID) ? (getCooldown(playerID) + cooldown * 1000) - System.currentTimeMillis() : 0;
  }

  public long getTimeleftMillis(final UUID playerID, final Long cooldown) {
    return getCooldowns().containsKey(playerID) ? (getCooldown(playerID) + cooldown) - System.currentTimeMillis() : 0;
  }

  public String onCooldown(final Long timeleft) {
    return getOnCooldown().replace("%timeleft%", String.valueOf(timeleft));
  }
}