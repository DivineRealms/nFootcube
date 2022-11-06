import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.listeners.Organization;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Match {
  public int matchID;
  public int type;
  public int phase;
  private int countdown;
  private int tickToSec;
  private int teams;
  private int count;
  private long startTime;
  private Location blue;
  private Location red;
  private Location mid;
  private Organization organization;
  private Footcube plugin;
  private boolean x;
  private boolean redAboveBlue;
  private Player[] redPlayers;
  private Player[] bluePlayers;
  private ArrayList<Player> teamers;
  private ArrayList<Player> takePlace;
  public HashMap<Player, Boolean> isRed;
  private Player lastKickRed;
  private Player lastKickBlue;
  public int scoreRed;
  public int scoreBlue;
  private HashMap<Player, Integer> goals;
  private ItemStack redChestPlate;
  private ItemStack redLeggings;
  private ItemStack blueChestPlate;
  private ItemStack blueLeggings;
  public Score time;
  private Score redGoals;
  private Score blueGoals;
  private ScoreboardManager sbm;
  private Scoreboard sb;
  private Objective o;
  private Slime cube;

  public Match(final Organization org, final Footcube pl, final int t, final Location b, final Location r, final Location m, final int id) {
    this.count = 4;
    this.redPlayers = new Player[0];
    this.bluePlayers = new Player[0];
    this.teamers = new ArrayList<>();
    this.takePlace = new ArrayList<>();
    this.isRed = new HashMap<>();
    this.lastKickRed = null;
    this.lastKickBlue = null;
    this.goals = new HashMap<>();
    this.matchID = id;
    this.organization = org;
    this.plugin = pl;
    this.type = t;
    this.blue = b;
    this.red = r;
    this.mid = m;
    this.phase = 1;
    this.scoreRed = 0;
    this.scoreBlue = 0;
    this.startTime = 0L;
    this.redChestPlate = this.createColoredArmour(Material.LEATHER_CHESTPLATE, Color.RED);
    this.redLeggings = this.createColoredArmour(Material.LEATHER_LEGGINGS, Color.RED);
    this.blueChestPlate = this.createColoredArmour(Material.LEATHER_CHESTPLATE, Color.BLUE);
    this.blueLeggings = this.createColoredArmour(Material.LEATHER_LEGGINGS, Color.BLUE);
    this.sbm = Bukkit.getScoreboardManager();
    this.sb = this.sbm.getNewScoreboard();
    boolean objectiveExists = false;
    for (final Objective ob : this.sb.getObjectives()) {
      if (ob.getName().equalsIgnoreCase("Match")) {
        objectiveExists = true;
        break;
      }
    }
    if (objectiveExists) {
      (this.o = this.sb.getObjective("Utakmica")).setDisplayName(ChatColor.DARK_GRAY + " " + ChatColor.GOLD + ChatColor.BOLD + "MC" + ChatColor.YELLOW + ChatColor.BOLD + "F " + ChatColor.DARK_GRAY);
    }
    else {
      (this.o = this.sb.registerNewObjective("Utakmica", "dummy")).setDisplaySlot(DisplaySlot.SIDEBAR);
      this.o.setDisplayName(" " + ChatColor.YELLOW + ChatColor.GOLD + ChatColor.BOLD + "MC " + ChatColor.BOLD + "F" + ChatColor.DARK_GRAY + "- " + ChatColor.WHITE + "UTAKMICA" + ChatColor.DARK_GRAY);
    }
    (this.time = this.o.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Vreme"))).setScore(300);
    (this.redGoals = this.o.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Crveni"))).setScore(0);
    (this.blueGoals = this.o.getScore(Bukkit.getOfflinePlayer(ChatColor.BLUE + "Plavi"))).setScore(0);
    this.x = (Math.abs(b.getX() - r.getX()) > Math.abs(b.getZ() - r.getZ()));
    if (this.x) {
      if (r.getX() > b.getX()) {
        this.redAboveBlue = true;
      }
      else {
        this.redAboveBlue = false;
      }
    }
    else if (r.getZ() > b.getZ()) {
      this.redAboveBlue = true;
    }
    else {
      this.redAboveBlue = false;
    }
    this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent e) {
    final Player p = e.getPlayer();
    if (this.isRed.containsKey(p)) {
      this.organization.clearInventory(p);
      if (this.phase != 1) {
        this.organization.playerLeaves(this, this.isRed.get(p));
      }
      if (this.isRed.get(p)) {
        this.redPlayers = this.reduceArray(this.redPlayers, p);
      }
      else {
        this.bluePlayers = this.reduceArray(this.bluePlayers, p);
      }
      this.isRed.remove(p);
    }
  }

  public boolean equals(final Match m) {
    return m.matchID == this.matchID;
  }

  private ItemStack createColoredArmour(final Material material, final Color color) {
    final ItemStack is = new ItemStack(material);
    if (is.getItemMeta() instanceof LeatherArmorMeta) {
      final LeatherArmorMeta meta = (LeatherArmorMeta)is.getItemMeta();
      meta.setColor(color);
      is.setItemMeta((ItemMeta)meta);
    }
    return is;
  }

  private Player[] extendArray(final Player[] oldL, final Player add) {
    final Player[] newL = new Player[oldL.length + 1];
    for (int i = 0; i < oldL.length; ++i) {
      newL[i] = oldL[i];
    }
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

  public void join(final Player p, final boolean b) {
    if (!this.organization.matches.has(p.getUniqueId().toString())) {
      this.organization.matches.put(p.getUniqueId().toString(), 0);
      this.organization.wins.put(p.getUniqueId().toString(), 0);
      this.organization.ties.put(p.getUniqueId().toString(), 0);
      this.organization.goals.put(p.getUniqueId().toString(), 0);
    }
    if (this.redPlayers.length < this.type && !b) {
      this.redPlayers = this.extendArray(this.redPlayers, p);
      this.isRed.put(p, true);
      p.teleport(this.red);
      p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("joinRedTeam")));
    }
    else {
      this.bluePlayers = this.extendArray(this.bluePlayers, p);
      this.isRed.put(p, false);
      p.teleport(this.blue);
      p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("joinBlueTeam")));
    }
    if (this.bluePlayers.length >= this.type && this.redPlayers.length >= this.type) {
      this.phase = 2;
      this.countdown = 15;
      this.tickToSec = 20;
      this.organization.matchStart(this.type);
      for (final Player player : this.isRed.keySet()) {
        player.setLevel(15);
        if (this.type != 1) {
          this.organization.matches.rise(player.getUniqueId().toString());
        }
        if (this.isRed.get(player)) {
          player.getInventory().setChestplate(this.redChestPlate);
          player.getInventory().setLeggings(this.redLeggings);
        }
        else {
          player.getInventory().setChestplate(this.blueChestPlate);
          player.getInventory().setLeggings(this.blueLeggings);
        }
        player.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("matchStart")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("tip")));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamchat")));
      }
    }
    else {
      p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("leaveMatch")));
    }
  }

  public void leave(final Player p) {
    if (this.isRed.get(p)) {
      this.redPlayers = this.reduceArray(this.redPlayers, p);
    }
    else {
      this.bluePlayers = this.reduceArray(this.bluePlayers, p);
    }
    this.isRed.remove(p);
    p.teleport(p.getWorld().getSpawnLocation());
  }

  public void takePlace(final Player p) {
    this.takePlace.add(p);
    if (this.redPlayers.length < this.type) {
      this.redPlayers = this.extendArray(this.redPlayers, p);
      this.isRed.put(p, true);
      p.teleport(this.red);
      p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("joinRedTeam")));
    }
    else {
      this.bluePlayers = this.extendArray(this.bluePlayers, p);
      this.isRed.put(p, false);
      p.teleport(this.blue);
      p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("joinBlueTeam")));
    }
    if (this.isRed.get(p)) {
      p.getInventory().setChestplate(this.redChestPlate);
      p.getInventory().setLeggings(this.redLeggings);
    }
    else {
      p.getInventory().setChestplate(this.blueChestPlate);
      p.getInventory().setLeggings(this.blueLeggings);
    }
    if (this.phase > 2) {
      p.setScoreboard(this.sb);
    }
  }

  public void kick(final Player p) {
    if (this.isRed.containsKey(p)) {
      if (this.isRed.get(p)) {
        this.lastKickRed = p;
      }
      else {
        this.lastKickBlue = p;
      }
    }
  }

  public void teamchat(final Player p, final String message) {
    if (this.isRed.containsKey(p)) {
      if (this.isRed.get(p)) {
        Player[] redPlayers;
        for (int length = (redPlayers = this.redPlayers).length, i = 0; i < length; ++i) {
          final Player player = redPlayers[i];
          player.sendMessage(ChatColor.RED + "TC " + ChatColor.AQUA + p.getName() + ChatColor.RED + " " + ChatColor.DARK_AQUA + message);
        }
      }
      else {
        Player[] bluePlayers;
        for (int length2 = (bluePlayers = this.bluePlayers).length, j = 0; j < length2; ++j) {
          final Player player = bluePlayers[j];
          player.sendMessage(ChatColor.BLUE + "TC " + ChatColor.AQUA + p.getName() + ChatColor.BLUE + " " + ChatColor.DARK_AQUA + message);
        }
      }
    }
  }

  public boolean team(final Player p0, final Player p1) {
    if (this.redPlayers.length + this.bluePlayers.length > 2 * this.type - 2 || (this.teams >= 2 && this.type == 3)) {
      return false;
    }
    p0.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamWith") + p1.getName()));
    p1.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamWith") + p0.getName()));
    this.teamers.add(p0);
    this.teamers.add(p1);
    ++this.teams;
    if (this.type - this.redPlayers.length >= 2) {
      this.join(p0, false);
      this.join(p1, false);
    }
    else if (this.type - this.bluePlayers.length >= 2) {
      this.join(p0, true);
      this.join(p1, true);
    }
    else {
      boolean rare = true;
      Player[] bluePlayers;
      for (int length = (bluePlayers = this.bluePlayers).length, i = 0; i < length; ++i) {
        final Player p2 = bluePlayers[i];
        if (!this.teamers.contains(p2)) {
          this.bluePlayers = this.reduceArray(this.bluePlayers, p2);
          this.redPlayers = this.extendArray(this.redPlayers, p2);
          this.isRed.put(p2, true);
          p2.teleport(this.red);
          p2.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("changeTeamWith") + p0.getName() + messagesConfig.vuci().getString("changeTeamWith1") + p1.getName() + messagesConfig.vuci().getString("changeTeamWith2")));
          this.join(p0, true);
          this.join(p1, true);
          rare = false;
          break;
        }
      }
      if (rare) {
        Player[] redPlayers;
        for (int length2 = (redPlayers = this.redPlayers).length, j = 0; j < length2; ++j) {
          final Player p3 = redPlayers[j];
          if (!this.teamers.contains(p3)) {
            this.redPlayers = this.reduceArray(this.redPlayers, p3);
            this.bluePlayers = this.extendArray(this.bluePlayers, p3);
            this.isRed.put(p3, true);
            p3.teleport(this.blue);
            p3.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("changeTeamWith") + p0.getName() + messagesConfig.vuci().getString("changeTeamWith1") + p1.getName() + messagesConfig.vuci().getString("changeTeamWith2")));
            this.join(p0, false);
            this.join(p1, false);
            break;
          }
        }
      }
    }
    return true;
  }

  public void update() {
    --this.tickToSec;
    if (this.phase == 3) {
      final Location l = this.cube.getLocation();
      if (this.x) {
        if (((this.redAboveBlue && l.getBlockX() >= this.red.getBlockX()) || (!this.redAboveBlue && this.red.getBlockX() >= l.getBlockX())) && l.getY() < this.red.getY() + 3.0 && l.getZ() < this.red.getZ() + 4.0 && l.getZ() > this.red.getZ() - 4.0) {
          this.score(false);
        }
        else if (((this.redAboveBlue && l.getBlockX() <= this.blue.getBlockX()) || (!this.redAboveBlue && this.blue.getBlockX() <= l.getBlockX())) && l.getY() < this.blue.getY() + 3.0 && l.getZ() < this.blue.getZ() + 4.0 && l.getZ() > this.blue.getZ() - 4.0) {
          this.score(true);
        }
      }
      else if (((this.redAboveBlue && l.getBlockZ() >= this.red.getBlockZ()) || (!this.redAboveBlue && this.red.getBlockZ() >= l.getBlockZ())) && l.getY() < this.red.getY() + 3.0 && l.getX() < this.red.getX() + 4.0 && l.getX() > this.red.getX() - 4.0) {
        this.score(false);
      }
      else if (((this.redAboveBlue && l.getBlockZ() <= this.blue.getBlockZ()) || (!this.redAboveBlue && this.blue.getBlockZ() <= l.getBlockZ())) && l.getY() < this.blue.getY() + 3.0 && l.getX() < this.blue.getX() + 4.0 && l.getX() > this.blue.getX() - 4.0) {
        this.score(true);
      }
    }
    if ((this.phase == 2 || this.phase == 4) && this.tickToSec == 0) {
      --this.countdown;
      this.tickToSec = 20;
      for (final Player p : this.isRed.keySet()) {
        p.setLevel(this.countdown);
      }
      if (this.countdown <= 0) {
        String message;
        if (this.phase == 2) {
          message = this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("startingMatch"));
          this.startTime = System.currentTimeMillis();
          this.redGoals.setScore(0);
          this.blueGoals.setScore(0);
          for (final Player p2 : this.isRed.keySet()) {
            this.organization.playerStarts(p2);
            p2.setScoreboard(this.sb);
          }
        }
        else {
          message = this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("continueMatch"));
        }
        this.phase = 3;
        this.cube = this.plugin.spawnCube(this.mid);
        final Random random = new Random();
        final double vertical = 0.3 * random.nextDouble() + 0.2;
        double horizontal = 0.3 * random.nextDouble() + 0.3;
        if (random.nextBoolean()) {
          horizontal *= -1.0;
        }
        if (this.x) {
          this.cube.setVelocity(new Vector(0.0, vertical, horizontal));
        }
        else {
          this.cube.setVelocity(new Vector(horizontal, vertical, 0.0));
        }
        for (final Player p3 : this.isRed.keySet()) {
          p3.sendMessage(message);
          if (this.isRed.get(p3)) {
            p3.teleport(this.red);
          }
          else {
            p3.teleport(this.blue);
          }
          p3.playSound(p3.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
        }
      }
      else if (this.countdown <= 3) {
        for (final Player p : this.isRed.keySet()) {
          p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
        }
      }
    }
    this.time.setScore(300 - (int)(System.currentTimeMillis() - this.startTime) / 1000);
    if (this.time.getScore() <= 0 && this.phase > 2) {
      for (final Player p : this.isRed.keySet()) {
        final String uuid = p.getUniqueId().toString();
        this.organization.endMatch(p);
        p.setScoreboard(this.sbm.getNewScoreboard());
        p.teleport(p.getWorld().getSpawnLocation());
        this.organization.clearInventory(p);
        if (this.scoreRed > this.scoreBlue) {
          p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("timesUpRed") + this.scoreRed + messagesConfig.vuci().getString("timesUpRed1") + this.scoreBlue));
          if (this.type != 1) {
            if (this.isRed.get(p) && !this.takePlace.contains(p)) {
              this.organization.wins.rise(uuid);
              this.organization.winStreak.rise(uuid);
              if (this.organization.winStreak.get(uuid) > this.organization.bestWinStreak.get(uuid)) {
                this.organization.bestWinStreak.put(uuid, this.organization.winStreak.get(uuid));
              }
              this.organization.economy.depositPlayer(p.getName(), 200.0);
              p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("reward1")));
              if (this.organization.winStreak.get(uuid) % 5 != 0) {
                continue;
              }
              this.organization.economy.depositPlayer(p.getName(), 100.0);
              p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("bonus") + this.organization.winStreak.get(uuid) + messagesConfig.vuci().getString("bonus1")));
            }
            else {
              this.organization.winStreak.put(uuid.toString(), 0);
            }
          }
          else {
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("1v1noStats")));
          }
        }
        else if (this.scoreRed < this.scoreBlue) {
          p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("timesUpBlue") + this.scoreBlue + messagesConfig.vuci().getString("timesUpBlue1") + this.scoreRed));
          if (this.type != 1) {
            if (!this.isRed.get(p) && !this.takePlace.contains(p)) {
              this.organization.wins.rise(uuid.toString());
              this.organization.winStreak.rise(uuid.toString());
              if (this.organization.winStreak.get(uuid) > this.organization.bestWinStreak.get(uuid)) {
                this.organization.bestWinStreak.put(uuid, this.organization.winStreak.get(uuid));
              }
              this.organization.economy.depositPlayer(p.getName(), 15.0);
              p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("reward2")));
              if (this.organization.winStreak.get(uuid) % 5 != 0) {
                continue;
              }
              this.organization.economy.depositPlayer(p.getName(), 100.0);
              p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("bonus") + this.organization.winStreak.get(uuid) + messagesConfig.vuci().getString("bonus1")));
            }
            else {
              this.organization.winStreak.put(uuid, 0);
            }
          }
          else {
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("1v1noStats")));
          }
        }
        else {
          p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("tied")));
          if (this.takePlace.contains(p)) {
            continue;
          }
          if (this.type == 1) {
            continue;
          }
          this.organization.ties.rise(uuid);
          this.organization.winStreak.put(uuid, 0);
          this.organization.economy.depositPlayer(p.getName(), 100.0);
          p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("reward3")));
        }
      }
      this.phase = 1;
      this.cube.setHealth(0.0);
      this.organization.undoTakePlace(this);
      this.scoreRed = 0;
      this.scoreBlue = 0;
      this.teams = 0;
      this.redPlayers = new Player[0];
      this.bluePlayers = new Player[0];
      this.teamers = new ArrayList<Player>();
      this.isRed = new HashMap<Player, Boolean>();
      this.takePlace.clear();
      this.goals.clear();
    }
  }

  private void score(final boolean red) {
    this.phase = 4;
    this.tickToSec = 20;
    this.countdown = 5;
    this.cube.setHealth(0.0);
    this.plugin.cubes.remove(this.cube);
    Player scorer = null;
    String team = null;
    if (red) {
      scorer = this.lastKickRed;
      team = "red";
      ++this.scoreRed;
      this.redGoals.setScore(this.redGoals.getScore() + 1);
    }
    else {
      scorer = this.lastKickBlue;
      team = "blue";
      ++this.scoreBlue;
      this.blueGoals.setScore(this.blueGoals.getScore() + 1);
    }
    if (!this.takePlace.contains(scorer) && this.type != 1) {
      this.organization.goals.rise(scorer.getUniqueId().toString());
      if (scorer.hasPermission("2v2")) {
        this.organization.economy.depositPlayer(scorer.getName(), 100.0);
      }
      if (this.goals.containsKey(scorer)) {
        this.goals.put(scorer, this.goals.get(scorer) + 1);
      }
      else {
        this.goals.put(scorer, 1);
      }
      scorer.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("reward4")));
      if (this.goals.get(scorer) == 3) {
        scorer.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("reward5")));
        for (final Player p : this.isRed.keySet()) {
          p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("hatty") + scorer.getName() + messagesConfig.vuci().getString("hatty1")));
        }
        this.organization.economy.depositPlayer(scorer.getName(), 200.0);
      }
    }
    for (final Player p : this.isRed.keySet()) {
      final String uuid = p.getUniqueId().toString();
      p.sendTitle(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("scoreTitle")), ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("scoreGoal") + scorer.getName() + messagesConfig.vuci().getString("scoreGoal1")));
      final double number = this.cube.getLocation().distance(scorer.getLocation());
      Math.round(number);
      if (number > 20.0) {
        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("scoredInc") + scorer.getName() + messagesConfig.vuci().getString("scoredInc1") + Math.round(number) + messagesConfig.vuci().getString("scoredInc2")));
      }
      else {
        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("scoredNorm") + scorer.getName() + messagesConfig.vuci().getString("scoredNorm1") + Math.round(number) + messagesConfig.vuci().getString("scoredNorm2")));
      }
      if (this.time.getScore() <= 0) {
        this.organization.endMatch(p);
        p.setScoreboard(this.sbm.getNewScoreboard());
        if (this.isRed.get(p) == red && !this.takePlace.contains(p) && this.type != 1) {
          this.organization.wins.rise(uuid);
          this.organization.winStreak.rise(uuid);
          if (this.organization.winStreak.get(uuid) > this.organization.bestWinStreak.get(uuid)) {
            this.organization.bestWinStreak.put(uuid, this.organization.winStreak.get(uuid));
          }
          this.organization.economy.depositPlayer(p.getName(), 100.0);
          p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("reward2")));
          if (this.organization.winStreak.get(uuid) % 5 == 0) {
            this.organization.economy.depositPlayer(p.getName(), 100.0);
            p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("bonus") + this.organization.winStreak.get(uuid) + messagesConfig.vuci().getString("bonus1")));
          }
        }
        else if (!this.takePlace.contains(p) && this.type != 1) {
          this.organization.winStreak.put(uuid, 0);
        }
        p.teleport(p.getWorld().getSpawnLocation());
        this.organization.clearInventory(p);
      }
      else {
        p.setLevel(10);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("result") + this.scoreRed + messagesConfig.vuci().getString("result1") + this.scoreBlue + messagesConfig.vuci().getString("result2")));
        p.sendMessage(this.organization.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("continue5sec")));
      }
    }
  }
}
