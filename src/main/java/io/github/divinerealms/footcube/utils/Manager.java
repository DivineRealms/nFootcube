package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.listeners.Controller;

public class Manager {
  private final Footcube plugin;
  private final Color color;
  private final Controller controller;
  private final Cooldown cooldown;
  private final Logger logger;
  private final Message message;

  public Manager(final Footcube plugin, final Configuration configuration) {
    this.plugin = plugin;
    this.color = new Color(this, configuration);
    this.controller = new Controller(this);
    this.cooldown = new Cooldown(this, configuration);
    this.logger = new Logger(this, configuration);
    this.message = new Message(this, configuration);
  }

  public Footcube getPlugin() {
    return this.plugin;
  }

  public Color getColor() {
    return this.color;
  }

  public Controller getController() {
    return this.controller;
  }

  public Cooldown getCooldown() {
    return this.cooldown;
  }

  public Logger getLogger() {
    return this.logger;
  }

  public Message getMessage() {
    return this.message;
  }
}
