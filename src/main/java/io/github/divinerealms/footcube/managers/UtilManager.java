package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.core.Organization;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

@Getter
public class UtilManager {
  private final Plugin plugin;
  private final Footcube instance;
  private final Cooldown cooldown;
  private final Logger logger;
  private final Physics physics;
  private final Organization organization;

  /**
   * Constructs a new UtilManager instance.
   *
   * @param plugin The main plugin instance.
   */
  public UtilManager(final Plugin plugin, final Footcube instance) {
    this.plugin = plugin;
    this.instance = instance;
    this.cooldown = new Cooldown(plugin);
    this.logger = new Logger(plugin);
    this.physics = new Physics(plugin, this);
    this.organization = new Organization(instance, this);
  }

  /**
   * Reloads all utility components managed by UtilManager.
   */
  public void reloadUtils() {
    getCooldown().reload();
    getPhysics().reload();
  }
}
