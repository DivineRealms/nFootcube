package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

@Getter
public class UtilManager {
  private final Plugin plugin;
  private final Cooldown cooldown;
  private final Physics physics;
  private Logger logger;

  public UtilManager(Plugin plugin) {
    this.plugin = plugin;
    this.cooldown = new Cooldown();
    this.logger = new Logger(plugin);
    this.physics = new Physics(plugin, this);
  }

  public void reload() {
    this.logger = new Logger(plugin);
    getPhysics().reload();
    getCooldown().reload();
  }

  public String color(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }
}
