package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.configs.Settings;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
  @Getter private final Messages messages;
  @Getter private final Settings settings;
  @Getter private final HashMap<UUID, Long> cooldowns = new HashMap<>();
  @Getter private long cubeSpawnCooldown, cubeKickCooldown;
  @Getter private String onCooldown;
  @Getter private boolean isCubeKickCooldownEnabled;

  public Cooldown(final UtilManager utilManager) {
    this.messages = utilManager.getMessages();
    this.settings = utilManager.getSettings();
  }

  public void reload() {
    this.cubeSpawnCooldown = getSettings().getInt("cube.spawn-cooldown");
    this.cubeKickCooldown = getSettings().getInt("cube.kick-cooldown.cooldown");
    this.onCooldown = getMessages().getString("on-cooldown");
    this.isCubeKickCooldownEnabled = getSettings().getBoolean("cube.kick-cooldown.enabled");
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