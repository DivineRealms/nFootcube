package io.github.divinerealms.footcube.utils;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class Cooldown {
  private final Plugin plugin;
  private final HashMap<UUID, Long> cooldowns = new HashMap<>();
  private long cubeSpawnCooldown, cubeKickCooldown;
  private boolean isCubeKickCooldownEnabled;

  /**
   * Constructor for the Cooldown class.
   *
   */
  public Cooldown(final Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Reloads the cooldown-related configuration values.
   */
  public void reload() {
    this.cubeSpawnCooldown = getPlugin().getConfig().getInt("cube.spawn-cooldown");
    this.cubeKickCooldown = getPlugin().getConfig().getInt("cube.kick-cooldown.cooldown");
    this.isCubeKickCooldownEnabled = getPlugin().getConfig().getBoolean("cube.kick-cooldown.enabled");
  }

  /**
   * Sets the cooldown for a player.
   *
   * @param playerID The UUID of the player.
   * @param time The cooldown duration in seconds.
   */
  public void setCooldown(final UUID playerID, final Long time) {
    if (time == null) {
      getCooldowns().remove(playerID);
    } else {
      getCooldowns().put(playerID, time);
    }
  }

  /**
   * Gets the time left for a player's cooldown.
   *
   * @param playerID The UUID of the player.
   * @param cooldown The cooldown duration in seconds.
   * @param inMillis If true, returns the time left in milliseconds; otherwise, in seconds.
   * @return The time left for the cooldown.
   */
  public long getTimeleft(final UUID playerID, final Long cooldown, boolean inMillis) {
    long currentTime = System.currentTimeMillis();
    long cooldownTime = getCooldowns().getOrDefault(playerID, currentTime);
    long timeLeft = cooldownTime + cooldown * (inMillis ? 1000L : 1L) - currentTime;
    return Math.max(timeLeft, 0);
  }
}
