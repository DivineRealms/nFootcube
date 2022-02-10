package io.github.divinerealms.footcube.utils;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
  private static long CUBE_SPAWN_COOLDOWN;
  private static long CUBE_KICK_COOLDOWN;
  private final HashMap<UUID, Long> cooldowns;
  private final String onCooldown;

  public Cooldown(Manager manager, Configuration configuration) {
    CUBE_SPAWN_COOLDOWN = manager.getPlugin().getConfig().getInt("Cube.Spawn_Cooldown");
    CUBE_KICK_COOLDOWN = manager.getPlugin().getConfig().getInt("Cube.Kick_Cooldown");
    this.cooldowns = new HashMap<>();
    this.onCooldown = configuration.get().getString("ON_COOLDOWN");
  }

  public static long getCubeKickCooldown() {
    return CUBE_KICK_COOLDOWN;
  }

  public void setCooldown(UUID player, Long time) {
    if (time == null) cooldowns.remove(player);
    else cooldowns.put(player, time);
  }

  public long getCooldown(UUID player) {
    return (cooldowns.get(player) == null ? 0L : cooldowns.get(player));
  }

  public long getCubeSpawnCooldown() {
    return CUBE_SPAWN_COOLDOWN;
  }

  public long getTimeleft(UUID player) {
    return this.cooldowns.containsKey(player) ? (this.getCooldown(player) + this.getCubeSpawnCooldown() * 1000) - System.currentTimeMillis() : 0;
  }

  public String onCooldown(Long timeleft) {
    return this.onCooldown.replace("%timeleft%", String.valueOf(timeleft));
  }
}
