package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerToggleSneakListener implements Listener {
  @Getter private final UtilManager utilManager;

  public PlayerToggleSneakListener(final UtilManager utilManager) {
    this.utilManager = utilManager;
  }

  @EventHandler
  public void onSneak(final PlayerToggleSneakEvent event) {
    final Player player = event.getPlayer();
    if (event.isSneaking()) getUtilManager().getPhysics().getCharges().put(player.getUniqueId(), 0D);
    else {
      player.setExp(0F);
      getUtilManager().getPhysics().getCharges().remove(player.getUniqueId());
    }
  }
}
