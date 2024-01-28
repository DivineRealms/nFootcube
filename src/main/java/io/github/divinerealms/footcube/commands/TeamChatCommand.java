package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.core.Match;
import io.github.divinerealms.footcube.core.Organization;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public class TeamChatCommand implements CommandExecutor {
  private final Footcube instance;
  private final UtilManager utilManager;
  private final Logger logger;
  private final Organization organization;

  public TeamChatCommand(final Footcube instance, final UtilManager utilManager) {
    this.instance = instance;
    this.utilManager = utilManager;
    this.logger = utilManager.getLogger();
    this.organization = utilManager.getOrganization();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    // Check if the sender is a player
    if (!(sender instanceof Player)) {
      // Command can only be used by players
      getLogger().send(sender, Messages.INGAME_ONLY.getMessage(null));
      return true;
    }

    final Player player = (Player) sender;

    // Check if the player is banned
    if (player.hasPermission("footcube.banned")) {
      getLogger().send(player, Messages.BANNED.getMessage(null));
      return false;
    }

    // Check if the player is in a game
    if (!isPlayerInGame(player)) {
      getLogger().send(player, Messages.NOT_IN_GAME.getMessage(null));
      return false;
    }

    // Check if the command has the minimum required arguments
    if (args.length < 1) {
      getLogger().send(player, Messages.TC_USAGE.getMessage(null));
    } else {
      // Concatenate the arguments to form the team chat message
      final String message = String.join(" ", args);
      final int arena = getOrganization().findArena(player);
      final Match match = getMatch(arena, player);

      // Check if the player is in a match
      if (match == null) {
        getLogger().send(player, Messages.NOT_IN_GAME.getMessage(null));
      } else {
        // Send the team chat message in the match
        match.teamchat(player, message);
      }
    }
    return true;
  }

  // Helper method to check if the player is in a game
  private boolean isPlayerInGame(final Player player) {
    return getOrganization().getPlayingPlayers().contains(player.getName());
  }

  // Helper method to get the match based on the arena and player
  private Match getMatch(final int arena, final Player player) {
    if (getOrganization().getMatches2v2()[arena].getIsRed().containsKey(player))
      return getOrganization().getMatches2v2()[arena];
    else if (getOrganization().getMatches3v3()[arena].getIsRed().containsKey(player))
      return getOrganization().getMatches3v3()[arena];
    else if (getOrganization().getMatches4v4()[arena].getIsRed().containsKey(player))
      return getOrganization().getMatches4v4()[arena];
    else if (getOrganization().getMatches5v5()[arena].getIsRed().containsKey(player))
      return getOrganization().getMatches5v5()[arena];
    return null;
  }
}
