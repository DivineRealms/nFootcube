package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

@Getter
public class PlayerToggleSneakListener implements Listener {
  private final Physics physics;

  public PlayerToggleSneakListener(final UtilManager utilManager) {
    this.physics = utilManager.getPhysics();
  }

  @EventHandler
  public void onSneak(final PlayerToggleSneakEvent event) {
    final Player player = event.getPlayer();

    // Ignore if not in survival mode
    if (player.getGameMode() != GameMode.SURVIVAL) return;

    if (event.isSneaking()) {
      // Start charging when player starts sneaking
      getPhysics().getCharges().put(player.getUniqueId(), 0d);
    } else {
      // Reset charge and player exp when player stops sneaking
      player.setExp(0);
      getPhysics().getCharges().remove(player.getUniqueId());
    }
  }
}
