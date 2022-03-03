package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
  @Getter private final Plugin plugin;
  @Getter private final UtilManager utilManager;
  @Getter private final HashMap<UUID, Long> cooldowns = new HashMap<>();
  @Getter private long cubeSpawnCooldown, cubeKickCooldown;
  @Getter private String onCooldown;
  @Getter private boolean isCubeKickCooldownEnabled;

  public Cooldown(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.utilManager = utilManager;
  }

  public void reload() {
    this.cubeSpawnCooldown = getPlugin().getConfig().getInt("cube.spawn-cooldown");
    this.cubeKickCooldown = getPlugin().getConfig().getInt("cube.kick-cooldown.cooldown");
    this.onCooldown = getUtilManager().getMessages().getString("ON_COOLDOWN");
    this.isCubeKickCooldownEnabled = getPlugin().getConfig().getBoolean("cube.kick-cooldown.enabled");
  }

  public void setCooldown(final UUID player, final Long time) {
    if (time == null) getCooldowns().remove(player);
    else getCooldowns().put(player, time);
  }

  public long getCooldown(final UUID player) {
    return (getCooldowns().get(player) == null ? 0L : getCooldowns().get(player));
  }

  public long getTimeleft(final UUID player, final Long cooldown) {
    return getCooldowns().containsKey(player) ? (getCooldown(player) + cooldown * 1000) - System.currentTimeMillis() : 0;
  }

  public long getTimeleftMillis(final UUID player, final Long cooldown) {
    return getCooldowns().containsKey(player) ? (getCooldown(player) + cooldown) - System.currentTimeMillis() : 0;
  }

  public String onCooldown(final Long timeleft) {
    return getOnCooldown().replace("%timeleft%", String.valueOf(timeleft));
  }
}