package io.github.divinerealms.footcube.core;

import com.connorlinfoot.titleapi.TitleAPI;
import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.managers.PlayerDataManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.GoalExplosion;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class Match {
  private final Footcube plugin;
  private final Logger logger;
  private final Physics physics;
  public int matchID, type, phase, tickToSec, ticksLived, scoreRed, scoreBlue, time;
  private int countdown, _countdown, teams, arena, redGoals, blueGoals;
  private final boolean x, redAboveBlue;
  private long startTime;
  private boolean prematchSidebarSet;
  private final Location blueLocation, redLocation, middleLocation;
  private final Organization organization;
  private Player[] redPlayers, bluePlayers;
  public ArrayList<Player> redAssist, blueAssist;
  private ArrayList<Player> teamers;
  private final ArrayList<Player> takePlace;
  public HashMap<Player, Boolean> isRed;
  private Player lastKick, lastKickRed, lastKickBlue;
  private final HashMap<Player, Integer> goals, assists;
  private final ItemStack redChestPlate, redLeggings, blueChestPlate, blueLeggings;
  private final ScoreboardManager sbm;
  private Slime cube;
  private final Sidebar sidebar;
  private final boolean isEconomyEnabled;
  private FileConfiguration cfg;

  public Match(final UtilManager utilManager, final Organization org, final Footcube plugin, final int type, final Location blueLocation, final Location redLocation, final Location middleLocation, final int id) {
    this.redPlayers = new Player[0];
    this.bluePlayers = new Player[0];
    this.redAssist = new ArrayList<>();
    this.blueAssist = new ArrayList<>();
    this.assists = new HashMap<>();
    this.teamers = new ArrayList<>();
    this.takePlace = new ArrayList<>();
    this.isRed = new HashMap<>();
    this.lastKick = null;
    this.lastKickRed = null;
    this.lastKickBlue = null;
    this.goals = new HashMap<>();
    this.matchID = id;
    this.organization = org;
    this._countdown = 0;
    this.plugin = plugin;
    this.logger = utilManager.getLogger();
    this.physics = utilManager.getPhysics();
    this.prematchSidebarSet = false;
    this.type = type;
    this.sidebar = getPlugin().getScoreboardLibrary().createSidebar();
    this.blueLocation = blueLocation;
    this.redLocation = redLocation;
    this.middleLocation = middleLocation;
    this.ticksLived = 0;
    this.phase = 1;
    this.scoreRed = 0;
    this.scoreBlue = 0;
    this.startTime = 0L;
    this.redChestPlate = createColoredArmour(Material.LEATHER_CHESTPLATE, Color.RED);
    this.redLeggings = createColoredArmour(Material.LEATHER_LEGGINGS, Color.RED);
    this.blueChestPlate = createColoredArmour(Material.LEATHER_CHESTPLATE, Color.BLUE);
    this.blueLeggings = createColoredArmour(Material.LEATHER_LEGGINGS, Color.BLUE);
    this.sbm = Bukkit.getScoreboardManager();
    this.arena = 0;
    this.time = 0;
    this.redGoals = 0;
    this.blueGoals = 0;
    this.x = (Math.abs(getBlueLocation().getX() - getRedLocation().getX()) > Math.abs(getBlueLocation().getZ() - getRedLocation().getZ()));
    if (this.x) this.redAboveBlue = getRedLocation().getX() > getBlueLocation().getX();
    else this.redAboveBlue = getRedLocation().getZ() > getBlueLocation().getZ();
    this.isEconomyEnabled = getPlugin().setupEconomy();
  }

  private void enableSidebar(final String type) {
    if (type.equalsIgnoreCase("ingame"))
      ingameLayout().apply(getSidebar());
    else if (type.equalsIgnoreCase("prematch"))
      prematchLayout().apply(getSidebar());

    for (final Player player : getIsRed().keySet())
      if (!getSidebar().players().contains(player))
        getSidebar().addPlayer(player);
  }

  private ComponentSidebarLayout prematchLayout() {
    cfg = getPlugin().getConfig();
    Component title = Component.text("FOOTBALL").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true);
    String name = getCfg().getString("arenas." + getType() + "v" + getType() + "." + (getArena() + 1) + ".name");
    Component blue = Component.text("  ")
        .color(NamedTextColor.DARK_GRAY)
        .append(
            Component.text("Blue Team:")
                .color(NamedTextColor.WHITE)
        );

    Component red = Component.text("  ")
        .color(NamedTextColor.DARK_GRAY)
        .append(
            Component.text("Red Team:")
                .color(NamedTextColor.WHITE)
        );

    Component arena = Component.text(" ▪ ")
        .color(NamedTextColor.DARK_GRAY)
        .append(
            Component.text("Arena: ")
                .color(NamedTextColor.WHITE)
        ).append(
            Component.text(name + " [" + (getArena() + 1) + "]")
                .color(NamedTextColor.GREEN)
        );

    SidebarComponent.Builder lines = SidebarComponent.builder()
        .addComponent(SidebarComponent.staticLine(Component.empty()))
        .addComponent(SidebarComponent.staticLine(arena))
        .addComponent(SidebarComponent.staticLine(Component.empty()));

    if (getType() != 5) {
      lines.addComponent(SidebarComponent.staticLine(blue));
      for (Player p : getIsRed().keySet()) {
        if (!getIsRed().get(p)) {
          lines.addComponent(SidebarComponent.staticLine(
              Component.text(" ▪ ")
                  .color(NamedTextColor.DARK_GRAY)
                  .append(
                      Component.text(p.getName())
                          .color(NamedTextColor.AQUA)
                  )
          ));
        }
      }
      lines.addComponent(SidebarComponent.staticLine(Component.empty()));
      lines.addComponent(SidebarComponent.staticLine(red));
      for (Player p : getIsRed().keySet()) {
        if (getIsRed().get(p)) {
          lines.addComponent(SidebarComponent.staticLine(
              Component.text(" ▪ ")
                  .color(NamedTextColor.DARK_GRAY)
                  .append(
                      Component.text(p.getName())
                          .color(NamedTextColor.RED)
                  )
          ));
        }
      }
      lines.addComponent(SidebarComponent.staticLine(Component.empty()));
    }
    SidebarComponent finalLines = lines.build();
    return new ComponentSidebarLayout(SidebarComponent.staticLine(title), finalLines);
  }

  private ComponentSidebarLayout ingameLayout() {
    String name = getCfg().getString("arenas." + getType() + "v" + getType() + "." + (getArena() + 1) + ".name");
    Component title = Component.text("FOOTBALL").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true);
    Component arena = Component.text(" ▪ ")
        .color(NamedTextColor.DARK_GRAY)
        .append(
            Component.text("Arena: ")
                .color(NamedTextColor.WHITE)
        ).append(
            Component.text(name + " [" + (getArena() + 1) + "]")
                .color(NamedTextColor.GREEN)
        );

    SidebarComponent.Builder lines = SidebarComponent.builder()
        .addComponent(SidebarComponent.staticLine(Component.empty()))
        .addStaticLine(arena)
        .addComponent(SidebarComponent.staticLine(Component.empty()))
        .addDynamicLine(() -> Component.text("   " + getBlueGoals() + " Blue")
            .color(NamedTextColor.BLUE)
            .append(
                Component.text(" - ")
                    .color(NamedTextColor.GRAY)
            ).append(
                Component.text("Red " + getRedGoals())
                    .color(NamedTextColor.RED)
            ))
        .addDynamicLine(Component::empty)
        .addDynamicLine(() -> Component.text(" ▪ ")
            .color(NamedTextColor.DARK_GRAY)
            .append(
                Component.text("Time left: ")
                    .color(NamedTextColor.WHITE)
            ).append(
                Component.text(getTime() + "")
                    .color(NamedTextColor.GREEN)
            ))
        .addDynamicLine(Component::empty);

    SidebarComponent finalLines = lines.build();
    return new ComponentSidebarLayout(SidebarComponent.staticLine(title), finalLines);
  }
  private void removeSidebar() {
    if (!getSidebar().players().isEmpty()) {
      getSidebar().removePlayers(getIsRed().keySet());
      getSidebar().removePlayers(getTakePlace());
      getSidebar().clearLines();
    }
  }

  public void spawnFirework(Location location) {
    Firework f = location.getWorld().spawn(location, Firework.class);
    FireworkMeta fm = f.getFireworkMeta();
    fm.addEffect(FireworkEffect.builder()
        .flicker(false)
        .trail(true)
        .withColor(Color.ORANGE)
        .withColor(Color.BLUE)
        .withFade(Color.BLUE)
        .build());
    fm.setPower(0);
    f.setFireworkMeta(fm);
    f.detonate();
  }

  public Vector explosionVector(Player player, Location source, double power) {
    double exposure = 3;

    double x1 = source.getX();
    double x2 = player.getEyeLocation().getX();

    double y1 = source.getY();
    double y2 = player.getEyeLocation().getY();

    double z1 = source.getZ();
    double z2 = player.getEyeLocation().getZ();

    double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));

    double multiplier = (1 - (distance / (power * 2))) * exposure;

    Vector vector = player.getLocation().toVector().subtract(source.toVector()).normalize().multiply(multiplier);

    return player.getVelocity().add(vector);
  }

  public boolean equals(final Match m) {
    return m.matchID == getMatchID();
  }

  private ItemStack createColoredArmour(final Material material, final Color color) {
    final ItemStack itemStack = new ItemStack(material);
    if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
      final LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
      meta.setColor(color);
      itemStack.setItemMeta(meta);
    }
    return itemStack;
  }

  private Player[] extendArray(final Player[] oldL, final Player add) {
    final Player[] newL = new Player[oldL.length + 1];
    System.arraycopy(oldL, 0, newL, 0, oldL.length);
    newL[oldL.length] = add;
    return newL;
  }

  private Player[] reduceArray(final Player[] oldL, final Player remove) {
    final Player[] newL = new Player[oldL.length - 1];
    int i = 0;
    int j = 0;
    while (i < newL.length) {
      if (oldL[i] != remove) {
        newL[j] = oldL[i];
        ++j;
      }
      ++i;
    }
    return newL;
  }


  public void join(final Player p) {
    PlayerDataManager pm;
    if (this.redPlayers.length < this.type) {
      this.redPlayers = this.extendArray(this.redPlayers, p);
      this.isRed.put(p, true);
      p.teleport(this.redLocation);
      getLogger().send(p, Messages.JOIN_RED_TEAM.getMessage(null));
    } else if (this.bluePlayers.length < this.type) {
      this.bluePlayers = this.extendArray(this.bluePlayers, p);
      this.isRed.put(p, false);
      p.teleport(this.blueLocation);
      getLogger().send(p, Messages.JOIN_BLUE_TEAM.getMessage(null));
    }
    if (this.bluePlayers.length >= this.type && this.redPlayers.length >= this.type) {
      this.phase = 2;
      this.countdown = 10;
      this.tickToSec = 20;
      this.organization.matchStart(this.type);
      for (final Player player : this.isRed.keySet()) {
        player.setLevel(10);
        if (this.type != 1) {
          pm=new PlayerDataManager(this.plugin, p.getUniqueId());
          pm.riseInt("matches");
          pm.savePlayerData(p.getUniqueId());
        }
        if (this.isRed.get(player)) {
          player.getInventory().setChestplate(this.redChestPlate);
          player.getInventory().setLeggings(this.redLeggings);
        } else {
          player.getInventory().setChestplate(this.blueChestPlate);
          player.getInventory().setLeggings(this.blueLeggings);
        }
        getLogger().send(p, Messages.MATCH_START.getMessage(null));
      }
    } else getLogger().send(p, Messages.LEAVE_MATCH.getMessage(null));
  }

  public void leave(final Player p) {
    if (getIsRed().get(p)) this.redPlayers = reduceArray(getRedPlayers(), p);
    else this.bluePlayers = reduceArray(getBluePlayers(), p);
    getIsRed().remove(p);
    p.teleport(p.getWorld().getSpawnLocation());
  }

  public void takePlace(final Player p) {
    getTakePlace().add(p);

    if (getRedPlayers().length < getType()) {
      this.redPlayers = this.extendArray(getRedPlayers(), p);
      getIsRed().put(p, true);
      p.teleport(getRedLocation());
      getLogger().send(p, Messages.JOIN_RED_TEAM.getMessage(null));
    } else if (getBluePlayers().length < getType()) {
      this.bluePlayers = this.extendArray(getBluePlayers(), p);
      getIsRed().put(p, false);
      p.teleport(getBlueLocation());
      getLogger().send(p, Messages.JOIN_BLUE_TEAM.getMessage(null));
    }

    if (getIsRed().get(p)) {
      p.getInventory().setChestplate(getRedChestPlate());
      p.getInventory().setLeggings(getRedLeggings());
    } else {
      p.getInventory().setChestplate(getBlueChestPlate());
      p.getInventory().setLeggings(getBlueLeggings());
    }

    if (getPhase() > 2) getSidebar().addPlayer(p);
  }

  public void kick(final Player p) {
    if (getIsRed().containsKey(p)) {
      if (getIsRed().get(p)) this.lastKickRed = p;
      else this.lastKickBlue = p;
      this.lastKick = p;
    }
  }

  public void tackle(final Player p) {
    if (getIsRed().containsKey(p)) {
      if (getIsRed().get(p)) getRedAssist().add(Bukkit.getPlayer("nobody"));
      else getBlueAssist().add(Bukkit.getPlayer("nobody"));
    }
  }

  public void assist(final Player p) {
    if (getIsRed().containsKey(p)) {
      if (getIsRed().get(p)) getRedAssist().add(p);
      else getBlueAssist().add(p);
    }
  }

  public void teamchat(final Player p, final String message) {
    if (getIsRed().containsKey(p)) {
      for (final Player redPlayers : getIsRed().keySet()) {
        if (getIsRed().get(redPlayers) && getIsRed().get(p))
          getLogger().send(redPlayers, Messages.TC_RED.getMessage(new String[]{p.getName(), message}));
        if (!getIsRed().get(redPlayers) && !getIsRed().get(p))
          getLogger().send(redPlayers, Messages.TC_BLUE.getMessage(new String[]{p.getName(), message}));
      }
    }
  }

  public boolean team(final Player p0, final Player p1) {
    if (getRedPlayers().length + getBluePlayers().length > 2 * getType() - 2 || (getTeams() >= 2 && getType() == 3)) return false;
    getLogger().send(p0, Messages.TEAM_WITH.getMessage(new String[]{p1.getName()}));
    getLogger().send(p1, Messages.TEAM_WITH.getMessage(new String[]{p0.getName()}));
    getTeamers().add(p0);
    getTeamers().add(p1);
    ++this.teams;
    if (getType() - getRedPlayers().length >= 2) {
      join(p0);
      join(p1);
    } else if (getType() - getBluePlayers().length >= 2) {
      join(p0);
      join(p1);
    } else {
      boolean rare = true;
      Player[] bluePlayers;
      for (int length = (bluePlayers = getBluePlayers()).length, i = 0; i < length; ++i) {
        final Player p2 = bluePlayers[i];
        if (!getTeamers().contains(p2)) {
          this.bluePlayers = this.reduceArray(getBluePlayers(), p2);
          this.redPlayers = this.extendArray(getRedPlayers(), p2);
          getIsRed().put(p2, true);
          p2.teleport(getRedLocation());
          getLogger().send(p2, Messages.CHANGED_TEAM_WITH.getMessage(new String[]{p0.getName(), p1.getName()}));
          join(p0);
          join(p1);
          rare = false;
          break;
        }
      }
      if (rare) {
        Player[] redPlayers;
        for (int length2 = (redPlayers = getRedPlayers()).length, j = 0; j < length2; ++j) {
          final Player p3 = redPlayers[j];
          if (!getTeamers().contains(p3)) {
            this.redPlayers = this.reduceArray(getRedPlayers(), p3);
            this.bluePlayers = this.extendArray(getBluePlayers(), p3);
            getIsRed().put(p3, true);
            p3.teleport(getBlueLocation());
            getLogger().send(p3, Messages.CHANGED_TEAM_WITH.getMessage(new String[]{p0.getName(), p1.getName()}));
            join(p0);
            join(p1);
            break;
          }
        }
      }
    }
    return true;
  }

  private void score(final boolean red) {
    this.phase = 4;
    this.tickToSec = 20;
    this.countdown = 5;
    this._countdown += 5;
    this.cube.setHealth(0.0);
    getPhysics().getCubes().remove(this.cube);
    Player scorer, assister = null;
    String team;

    if (red) {
      if (getLastKickRed() != null) scorer = getLastKickRed();
      else scorer = getLastKickBlue();
      if (getType() != 1) assister = getRedAssist().get(0);
      team = "red";
      ++this.scoreRed;
      ++this.redGoals;
    } else {
      if (getLastKickBlue() != null) scorer = getLastKickBlue();
      else scorer = getLastKickRed();
      if (getType() != 1) assister = getBlueAssist().get(1);
      team = "blue";
      ++this.scoreBlue;
      ++this.blueGoals;
    }
    if (getType() != 1) {
      getBlueAssist().clear();
      getRedAssist().clear();
      getBlueAssist().add(Bukkit.getPlayer("nobody"));
      getRedAssist().add(Bukkit.getPlayer("nobody"));
    }

    PlayerDataManager scorerData = new PlayerDataManager(getPlugin(), scorer.getUniqueId());

    if (!getTakePlace().contains(scorer) && getType() != 1) {
      final double scoreReward = getPlugin().getConfig().getDouble("scoreReward");
      final double hattyReward = getPlugin().getConfig().getDouble("hattyReward");
      scorerData.riseInt("goals");
      if (getGoals().containsKey(scorer)) getGoals().put(scorer, getGoals().get(scorer) + 1);
      else getGoals().put(scorer, 1);
      if (isEconomyEnabled) {
        getPlugin().getEconomy().depositPlayer(scorer, scoreReward);
        getLogger().send(scorer, Messages.SCORE_REWARD.getMessage(new String[]{String.valueOf(scoreReward)}));
        }
      if (getGoals().get(scorer) == 3) {
        getLogger().send(scorer, Messages.HATTY_REWARD.getMessage(new String[]{String.valueOf(scoreReward)}));
        for (final Player players : getIsRed().keySet())
          getLogger().send(players, Messages.HATTY.getMessage(new String[]{scorer.getName()}));
        if (isEconomyEnabled)
          getPlugin().getEconomy().depositPlayer(scorer, hattyReward);
      }
    }
    scorerData.savePlayerData(scorer.getUniqueId());
    if (!getTakePlace().contains(assister) && scorer != assister && assister != null) {
      PlayerDataManager assisterData = new PlayerDataManager(getPlugin(), assister.getUniqueId());
      final double assistReward = getPlugin().getConfig().getDouble("assistReward");
      assisterData.riseInt("assists");
      assisterData.savePlayerData(assister.getUniqueId());
      if (isEconomyEnabled) {
        getPlugin().getEconomy().depositPlayer(assister, assistReward);
        getLogger().send(assister, Messages.ASSIST_REWARD.getMessage(new String[]{String.valueOf(assistReward)}));
      }
      if (getAssists().containsKey(assister)) getAssists().put(assister, getAssists().get(assister) + 1);
      else getAssists().put(assister, 1);
    }

    if (team.equalsIgnoreCase("red")) {
      Location blueish = getBlueLocation().clone();
      double yaw = blueish.getYaw();
      if (yaw > 45 && yaw < 135)
        blueish.setX(blueish.getX() - 3);
      else if (yaw > 135 && yaw < 225)
        blueish.setZ(blueish.getZ() - 3);
      else if (yaw > 235 && yaw < 325)
        blueish.setX(blueish.getX() + 3);
      else
        blueish.setZ(blueish.getZ() + 3);
      try {
        (new GoalExplosion()).init(blueish, scorerData.getString("goal_explosion"), 1, getPlugin());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      Location redish = getRedLocation().clone();
      double yaw = redish.getYaw();
      if (yaw > 45 && yaw < 135)
        redish.setX(redish.getX() - 3);
      else if (yaw > 135 && yaw < 225)
        redish.setZ(redish.getZ() - 3);
      else if (yaw > 235 && yaw < 325)
        redish.setX(redish.getX() + 3);
      else
        redish.setZ(redish.getZ() + 3);
      try {
        (new GoalExplosion()).init(redish, scorerData.getString("goal_explosion"), 0, getPlugin());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    for (final Player p : getIsRed().keySet()) {
      PlayerDataManager playerData = new PlayerDataManager(getPlugin(), p.getUniqueId());
      double number = getCube().getLocation().distance(scorer.getLocation());
      number = Math.round(number);
      String message = ChatColor.translateAlternateColorCodes('&', scorerData.getString("custom_score_message"));
      if (!message.isEmpty()) {
        TitleAPI.sendTitle(p, 5, 40, 5, ChatColor.translateAlternateColorCodes('&',"&lGOOOOAL!"), ChatColor.WHITE + message);
        getLogger().send(p, Messages.CUSTOM_SCORE_MESSAGE.getMessage(new String[]{message, scorer.getName()}));
      } else {
        TitleAPI.sendTitle(p, 5, 40, 5, ChatColor.translateAlternateColorCodes('&', "&lGOOOOAL!"), ChatColor.translateAlternateColorCodes('&', Messages.SCORE_GOAL.getMessage(new String[]{scorer.getName()})));
        if (assister != scorer && assister != null) {
          if (number > 20.0) getLogger().send(p, Messages.SCORE_INCREDIBLE_ASSIST.getMessage(new String[]{scorer.getName(), assister.getName(), String.valueOf(Math.round(number))}));
          else getLogger().send(p, Messages.SCORE_NORMAL_ASSIST.getMessage(new String[]{scorer.getName(), assister.getName(), String.valueOf(Math.round(number))}));
        } else {
          if (number > 20.0) getLogger().send(p, Messages.SCORE_INCREDIBLE.getMessage(new String[]{scorer.getName(), String.valueOf(Math.round(number))}));
          else getLogger().send(p, Messages.SCORE_NORMAL.getMessage(new String[]{scorer.getName(), String.valueOf(Math.round(number))}));
        }
      }
      Location goalLocation;
      World world = getBlueLocation().getWorld();
      if (red) goalLocation = getRedLocation();
      else goalLocation = getBlueLocation();
      if (getPlugin().getConfig().getBoolean("goalThrow"))
        p.setVelocity(explosionVector(p, goalLocation, 3));
      for (Entity nearby : world.getNearbyEntities(goalLocation, 6, 2, 6)) {
        if (nearby instanceof LivingEntity) {
          LivingEntity entity = (LivingEntity) nearby;
          entity.damage(0);
        }
      }
      if (getTime() <= 0) {
        this.organization.endMatch(p);
        double x = getCfg().getDouble("afterMatchRespawn.1");
        double y = getCfg().getDouble("afterMatchRespawn.2");
        double z = getCfg().getDouble("afterMatchRespawn.3");
        final double winReward = getCfg().getDouble("winReward");
        this.removeSidebar();
        if (getIsRed().get(p) == red && !getTakePlace().contains(p) && getType() != 1) {
          playerData.riseInt("wins");
          playerData.riseInt("win_streak");
          int ws = playerData.getInt("win_streak");
          int bws = playerData.getInt("best_win_streak");
          if (ws > bws) playerData.setInt("best_win_streak", ws);
          if (isEconomyEnabled) {
            getPlugin().getEconomy().depositPlayer(p, winReward);
            getLogger().send(p, Messages.WIN_REWARD.getMessage(new String[]{String.valueOf(winReward)}));
          }
        } else if (!getTakePlace().contains(p) && getType() != 1) playerData.setInt("win_streak", 0);
        if (x == 0.0 && y == 0.0 && z == 0.0) p.teleport(p.getWorld().getSpawnLocation());
        else {
          Location loc = new Location(p.getWorld(), x, y, z);
          p.teleport(loc);
        }
        this.organization.clearInventory(p);
      } else {
        p.setLevel(10);
        getLogger().send(p, Messages.CURRENT_RESULT.getMessage(new String[]{String.valueOf(getScoreRed()), String.valueOf(getScoreBlue())}));
        getLogger().send(p, Messages.CONTINUE_5_SEC.getMessage(null));
      }
      playerData.savePlayerData(p.getUniqueId());
    }
  }

  public void update() {
    --this.tickToSec;
    if (getPhase() == 1) return;

    if (getPhase() == 3) {
      final Location l = getCube().getLocation();
      if (this.x) {
        if (((this.redAboveBlue && l.getBlockX() >= getRedLocation().getBlockX()) || (!this.redAboveBlue && getRedLocation().getBlockX() >= l.getBlockX())) && l.getY() < getRedLocation().getY() + 3.0 && l.getZ() < getRedLocation().getZ() + 4.0 && l.getZ() > getRedLocation().getZ() - 4.0) {
          this.score(false);
        } else if (((this.redAboveBlue && l.getBlockX() <= getBlueLocation().getBlockX()) || (!this.redAboveBlue && getBlueLocation().getBlockX() <= l.getBlockX())) && l.getY() < getBlueLocation().getY() + 3.0 && l.getZ() < getBlueLocation().getZ() + 4.0 && l.getZ() > getBlueLocation().getZ() - 4.0) {
          this.score(true);
        }
      } else if (((this.redAboveBlue && l.getBlockZ() >= getRedLocation().getBlockZ()) || (!this.redAboveBlue && getRedLocation().getBlockZ() >= l.getBlockZ())) && l.getY() < getRedLocation().getY() + 3.0 && l.getX() < getRedLocation().getX() + 4.0 && l.getX() > getRedLocation().getX() - 4.0) {
        this.score(false);
      } else if (((this.redAboveBlue && l.getBlockZ() <= getBlueLocation().getBlockZ()) || (!this.redAboveBlue && getBlueLocation().getBlockZ() <= l.getBlockZ())) && l.getY() < getBlueLocation().getY() + 3.0 && l.getX() < getBlueLocation().getX() + 4.0 && l.getX() > getBlueLocation().getX() - 4.0) {
        this.score(true);
      }
    }

    if ((getPhase() == 2 || getPhase() == 4) && getTickToSec() == 0) {
      --this.countdown;
      this.tickToSec = 20;
      for (final Player p : getIsRed().keySet()) p.setLevel(getCountdown());
      if (!prematchSidebarSet) {
        this.enableSidebar("prematch");
        this.prematchSidebarSet = true;
      }
      if (getCountdown() <= 0) {
        String message;
        if (getPhase() == 2) {
          message = Messages.MATCH_START.getMessage(null);
          this.startTime = System.currentTimeMillis();
          this.redGoals = 0;
          this.blueGoals = 0;
          getBlueAssist().add(Bukkit.getPlayer("nobody"));
          getRedAssist().add(Bukkit.getPlayer("nobody"));
          this.enableSidebar("ingame");
          for (final Player p : getIsRed().keySet()) {
            this.arena = this.organization.findArena(p);
            this.organization.playerStarts(p);
          }
        } else
          message = Messages.MATCH_CONTINUING.getMessage(null);
        this.phase = 3;
        this.cube = getPhysics().spawnCube(getMiddleLocation());
        for (final Player p : getIsRed().keySet()) {
          getLogger().send(p, message);
          if (getIsRed().get(p)) p.teleport(getRedLocation());
          else p.teleport(getBlueLocation());
          p.playSound(p.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
        }
      } else if (getCountdown() <= 3)
        for (final Player p : getIsRed().keySet())
          p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
    }

    if (getPhase() != 2 && getPhase() != 4) {
      if (getType() == 2 || getType() == 1)
        this.time = 180 + get_countdown() - (int) (System.currentTimeMillis() - getStartTime()) / 1000;
      else if (getType() == 3)
        this.time = 240 + get_countdown() - (int) (System.currentTimeMillis() - getStartTime()) / 1000;
      else if (this.type == 4)
        this.time = 300 + get_countdown() - (int) (System.currentTimeMillis() - getStartTime()) / 1000;
      else if (this.type == 5)
        this.time = 300 + get_countdown() - (int) (System.currentTimeMillis() - getStartTime()) / 1000;

      enableSidebar("ingame");

      if (getTicksLived() == getCube().getTicksLived() && getTicksLived() != 0 && getTime() > 0) {
        getCube().setHealth(0.0);
        this.cube = getPhysics().spawnCube(getMiddleLocation());
      }

      this.ticksLived = getCube().getTicksLived();

      if (getTime() <= 0 && getPhase() > 2) {
        for (final Player p : getIsRed().keySet()) {
          PlayerDataManager playerData = new PlayerDataManager(getPlugin(), p.getUniqueId());
          getOrganization().endMatch(p);
          final double winReward = getCfg().getDouble("winReward");
          final double tiedReward = getCfg().getDouble("tiedReward");
          double x = getCfg().getDouble("afterMatchRespawn.1");
          double y = getCfg().getDouble("afterMatchRespawn.2");
          double z = getCfg().getDouble("afterMatchRespawn.3");
          if (x == 0.0 && y == 0.0 && z == 0.0) p.teleport(p.getWorld().getSpawnLocation());
          else {
            Location loc = new Location(p.getWorld(), x, y, z);
            p.teleport(loc);
          }
          getOrganization().clearInventory(p);
          if (getScoreRed() > getScoreBlue()) {
            getLogger().send(p, Messages.TIMES_UP_RED.getMessage(new String[]{String.valueOf(getScoreRed()), String.valueOf(getScoreBlue())}));
            if (getType() != 1) {
              if (getIsRed().get(p) && !getTakePlace().contains(p)) {
                playerData.riseInt("wins");
                playerData.riseInt("win_streak");
                int ws = playerData.getInt("win_streak");
                int bws = playerData.getInt("best_win_streak");
                if (ws > bws) playerData.setInt("best_win_streak", ws);
                if (isEconomyEnabled) {
                  getPlugin().getEconomy().depositPlayer(p, winReward);
                  getLogger().send(p, Messages.WIN_REWARD.getMessage(new String[]{String.valueOf(winReward)}));
                }
              } else playerData.setInt("win_streak", 0);
            } else getLogger().send(p, Messages.NO_STATS_FOR_THIS.getMessage(null));
          } else if (getScoreRed() < getScoreBlue()) {
            getLogger().send(p, Messages.TIMES_UP_BLUE.getMessage(new String[]{String.valueOf(getScoreBlue()), String.valueOf(getScoreRed())}));
            if (getType() != 1) {
              if (!getIsRed().get(p) && !getTakePlace().contains(p)) {
                playerData.riseInt("wins");
                playerData.riseInt("win_streak");
                int ws = playerData.getInt("win_streak");
                int bws = playerData.getInt("best_win_streak");
                if (ws > bws) playerData.setInt("best_win_streak", ws);
                if (isEconomyEnabled) {
                  getPlugin().getEconomy().depositPlayer(p, winReward);
                  getLogger().send(p, Messages.WIN_REWARD.getMessage(new String[]{String.valueOf(winReward)}));
                }
              } else playerData.setInt("win_streak", 0);
            } else getLogger().send(p, Messages.NO_STATS_FOR_THIS.getMessage(null));
          } else {
            getLogger().send(p, Messages.TIED.getMessage(null));
            if (getTakePlace().contains(p)) continue;
            if (getType() == 1) continue;
            playerData.riseInt("ties");
            playerData.setInt("win_streak", 0);
            if (isEconomyEnabled) {
              getPlugin().getEconomy().depositPlayer(p, tiedReward);
              getLogger().send(p, Messages.TIED_REWARD.getMessage(new String[]{String.valueOf(tiedReward)}));
            }
          }
          playerData.savePlayerData(p.getUniqueId());
        }
        this.removeSidebar();
        this.phase = 1;
        getCube().setHealth(0.0);
        this.prematchSidebarSet = false;
        this.organization.undoTakePlace(this);
        this.scoreRed = 0;
        this.scoreBlue = 0;
        this.teams = 0;
        this.redPlayers = new Player[0];
        this.bluePlayers = new Player[0];
        this.teamers = new ArrayList<>();
        this.isRed = new HashMap<>();
        getTakePlace().clear();
        getRedAssist().clear();
        getBlueAssist().clear();
        getGoals().clear();
        getAssists().clear();
        this._countdown = 0;
      }
    }
  }
}
