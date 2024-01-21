package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.configs.Config;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Getter
public class UtilManager {

  private final Plugin plugin;
  private final Config config;
  private final Cooldown cooldown;
  private final Logger logger;
  private final Physics physics;

  /**
   * Constructs a new UtilManager instance.
   *
   * @param plugin The main plugin instance.
   */
  public UtilManager(final Plugin plugin) {
    this.plugin = plugin;
    this.config = new Config(plugin);
    this.cooldown = new Cooldown(this);
    this.logger = new Logger(plugin);
    this.physics = new Physics(plugin, this);
  }

  /**
   * Reloads all utility components managed by UtilManager.
   */
  public void reloadUtils() {
    getConfig().reload();
    getCooldown().reload();
    getPhysics().reload();
  }
}
