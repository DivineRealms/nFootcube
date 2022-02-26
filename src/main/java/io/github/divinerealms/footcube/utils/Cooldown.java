package io.github.divinerealms.footcube.utils;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
  private final long cubeSpawnCooldown;
  private final long cubeKickCooldown;
  private final HashMap<UUID, Long> cooldowns = new HashMap<>();
  private final String onCooldown;
  private final boolean isCubeKickCooldownEnabled;

  public Cooldown(final Manager manager, final Configuration configuration) {
    this.cubeSpawnCooldown = manager.getPlugin().getConfig().getInt("cube.spawn-cooldown");
    this.cubeKickCooldown = manager.getPlugin().getConfig().getInt("cube.kick-cooldown.cooldown");
    this.onCooldown = configuration.get("messages.yml").getString("ON_COOLDOWN");
    this.isCubeKickCooldownEnabled = manager.getPlugin().getConfig().getBoolean("cube.kick-cooldown.enabled");
  }

  public void setCooldown(UUID player, Long time) {
    if (time == null) cooldowns.remove(player);
    else cooldowns.put(player, time);
  }

  public long getCooldown(UUID player) {
    return (cooldowns.get(player) == null ? 0L : cooldowns.get(player));
  }

  public long getCubeSpawnCooldown() {
    return this.cubeSpawnCooldown;
  }

  public long getCubeKickCooldown() {
    return this.cubeKickCooldown;
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