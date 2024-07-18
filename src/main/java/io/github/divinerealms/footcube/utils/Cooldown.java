package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.configs.Config;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class Cooldown {
  private final HashMap<UUID, Long> cooldowns = new HashMap<>();
  @Getter private static final YamlConfiguration config = Config.getConfig("config.yml");
  private long cubeSpawnCooldown, cubeKickCooldown;
  private boolean isCubeKickCooldownEnabled;

  public Cooldown() {
    reload();
  }

  public void reload() {
    this.cubeSpawnCooldown = getConfig().getInt("cube.spawn-cooldown");
    this.cubeKickCooldown = getConfig().getInt("cube.kick-cooldown.cooldown");
    this.isCubeKickCooldownEnabled = getConfig().getBoolean("cube.kick-cooldown.enabled");
  }

  public void setCooldown(UUID playerID, Long time) {
    if (time == null) getCooldowns().remove(playerID);
    else getCooldowns().put(playerID, time);
  }

  public long getCooldown(UUID playerID) {
    return (getCooldowns().get(playerID) == null ? 0L : getCooldowns().get(playerID));
  }

  public long getTimeleft(UUID playerID, Long cooldown) {
    return getCooldowns().containsKey(playerID) ? (getCooldown(playerID) + cooldown * 1000) - System.currentTimeMillis() : 0;
  }

  public long getTimeleftMillis(UUID playerID, Long cooldown) {
    return getCooldowns().containsKey(playerID) ? (getCooldown(playerID) + cooldown) - System.currentTimeMillis() : 0;
  }
}