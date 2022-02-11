package io.github.divinerealms.footcube.utils;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
  private static long CUBE_SPAWN_COOLDOWN;
  private static long CUBE_KICK_COOLDOWN;
  private final HashMap<UUID, Long> cooldowns;
  private final String onCooldown;
  private final boolean isCubeKickCooldownEnabled;

  public Cooldown(final Manager manager, final Configuration configuration) {
    CUBE_SPAWN_COOLDOWN = manager.getPlugin().getConfig().getInt("Cube.Spawn_Cooldown");
    CUBE_KICK_COOLDOWN = manager.getPlugin().getConfig().getInt("Cube.Kick_Cooldown.Cooldown");
    this.cooldowns = new HashMap<>();
    this.onCooldown = configuration.get().getString("ON_COOLDOWN");
    this.isCubeKickCooldownEnabled = manager.getPlugin().getConfig().getBoolean("Cube.Kick_Cooldown.Enabled");
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

  public long getCubeKickCooldown() {
    return CUBE_KICK_COOLDOWN;
  }

  public long getTimeleft(UUID player, Long cooldown) {
    return this.cooldowns.containsKey(player) ? (this.getCooldown(player) + cooldown * 1000) - System.currentTimeMillis() : 0;
  }

  public long getTimeleftMillis(UUID player, Long cooldown) {
    return this.cooldowns.containsKey(player) ? (this.getCooldown(player) + cooldown) - System.currentTimeMillis() : 0;
  }

  public String onCooldown(Long timeleft) {
    return this.onCooldown.replace("%timeleft%", String.valueOf(timeleft));
  }

  public boolean isCubeKickCooldownEnabled() {
    return this.isCubeKickCooldownEnabled;
  }
}