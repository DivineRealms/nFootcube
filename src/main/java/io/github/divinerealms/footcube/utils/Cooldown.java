package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
  @Getter private final HashMap<UUID, Long> cooldowns = new HashMap<>();
  @Getter private long cubeSpawnCooldown, cubeKickCooldown;
  @Getter private String onCooldown;
  @Getter private boolean isCubeKickCooldownEnabled;

  public void reload(final Plugin plugin, final ConfigManager configManager) {
    this.cubeSpawnCooldown = plugin.getConfig().getInt("cube.spawn-cooldown");
    this.cubeKickCooldown = plugin.getConfig().getInt("cube.kick-cooldown.cooldown");
    this.onCooldown = configManager.getConfig("messages.yml").getString("ON_COOLDOWN");
    this.isCubeKickCooldownEnabled = plugin.getConfig().getBoolean("cube.kick-cooldown.enabled");
  }

  public void setCooldown(UUID player, Long time) {
    if (time == null) getCooldowns().remove(player);
    else getCooldowns().put(player, time);
  }

  public long getCooldown(UUID player) {
    return (getCooldowns().get(player) == null ? 0L : getCooldowns().get(player));
  }

  public long getTimeleft(UUID player, Long cooldown) {
    return getCooldowns().containsKey(player) ? (getCooldown(player) + cooldown * 1000) - System.currentTimeMillis() : 0;
  }

  public long getTimeleftMillis(UUID player, Long cooldown) {
    return getCooldowns().containsKey(player) ? (getCooldown(player) + cooldown) - System.currentTimeMillis() : 0;
  }

  public String onCooldown(Long timeleft) {
    return getOnCooldown().replace("%timeleft%", String.valueOf(timeleft));
  }
}