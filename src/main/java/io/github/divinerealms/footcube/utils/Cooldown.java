package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.configs.Config;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class Cooldown {
  private final Config config;
  private final HashMap<UUID, Long> cooldowns = new HashMap<>();
  private long cubeSpawnCooldown, cubeKickCooldown;
  private boolean isCubeKickCooldownEnabled;

  public Cooldown(final UtilManager utilManager) {
    this.config = utilManager.getConfig();
  }

  public void reload() {
    this.cubeSpawnCooldown = getConfig().getInt("cube.spawn-cooldown");
    this.cubeKickCooldown = getConfig().getInt("cube.kick-cooldown.cooldown");
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
}