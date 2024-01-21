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

  /**
   * Constructor for the Cooldown class.
   *
   * @param utilManager The UtilManager instance providing access to configurations.
   */
  public Cooldown(final UtilManager utilManager) {
    this.config = utilManager.getConfig();
  }

  /**
   * Reloads the cooldown-related configuration values.
   */
  public void reload() {
    this.cubeSpawnCooldown = getConfig().getInt("cube.spawn-cooldown");
    this.cubeKickCooldown = getConfig().getInt("cube.kick-cooldown.cooldown");
    this.isCubeKickCooldownEnabled = getConfig().getBoolean("cube.kick-cooldown.enabled");
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
