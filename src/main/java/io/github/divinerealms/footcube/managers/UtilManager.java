package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Match;
import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.configs.Settings;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;

public class UtilManager {
  @Getter private final Messages messages;
  @Getter private final Settings settings;
  @Getter private final Match match;
  @Getter private final Cooldown cooldown;
  @Getter private final Logger logger;
  @Getter private final Physics physics;

  public UtilManager(final Footcube plugin) {
    this.messages = new Messages(plugin);
    this.settings = new Settings(plugin);
    this.match = new Match(plugin);
    this.cooldown = new Cooldown(this);
    this.logger = new Logger(plugin, this);
    this.physics = new Physics(plugin, this);
  }

  public void reloadUtils() {
    getMessages().reload();
    getSettings().reload();
    getMatch().reload();
    getLogger().reload();
    getCooldown().reload();
    getPhysics().reload();
  }
}
