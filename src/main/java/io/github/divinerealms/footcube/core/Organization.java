package io.github.divinerealms.footcube.core;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.features.DisableCommands;
import io.github.divinerealms.footcube.features.Statistics;
import io.github.divinerealms.footcube.managers.PlayerDataManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

@Getter
public class Organization {
  private final UtilManager utilManager;
  private final Logger logger;
  private final Footcube plugin;
  public DisableCommands disableCommands;
  public String or;
  public String setupGuy;
  public int setupType;
  public Location setupLoc;
  public Match[] matches1v1;
  public Match[] matches2v2;
  public Match[] matches3v3;
  public Match[] matches4v4;
  public Match[] matches5v5;
  public int matchesInProgress1v1;
  public int matchesInProgress2v2;
  public int matchesInProgress3v3;
  public int matchesInProgress4v4;
  public int matchesInProgress5v5;
  public int lobby1v1;
  public int lobby2v2;
  public int lobby3v3;
  public int lobby4v4;
  public int lobby5v5;
  public ArrayList<Slime> practiceBalls;
  public HashMap<String, Integer> waitingPlayers;
  public ArrayList<String> playingPlayers;
  public HashMap<Player, Player> team;
  public HashMap<Player, Player> teamReverse;
  public HashMap<Player, Integer> teamType;
  public Player[][] waitingTeams;
  public ArrayList<Player> waitingTeamPlayers;
  public Match[] leftMatches;
  public boolean[] leftPlayerIsRed;
  public long announcementTime;
  public Statistics stats;
  private FileConfiguration cfg;

  @SuppressWarnings("ALL")
  public Organization(final Footcube pl, final UtilManager utilManager) {
    this.utilManager = utilManager;
    this.logger = utilManager.getLogger();
    this.setupGuy = null;
    this.setupType = 0;
    this.setupLoc = null;
    this.matches1v1 = new Match[0];
    this.matches2v2 = new Match[0];
    this.matches3v3 = new Match[0];
    this.matches4v4 = new Match[0];
    this.matches5v5 = new Match[0];
    this.matchesInProgress1v1 = 0;
    this.matchesInProgress2v2 = 0;
    this.matchesInProgress3v3 = 0;
    this.matchesInProgress4v4 = 0;
    this.matchesInProgress5v5 = 0;
    this.lobby1v1 = 0;
    this.lobby2v2 = 0;
    this.lobby3v3 = 0;
    this.lobby4v4 = 0;
    this.lobby5v5 = 0;
    this.practiceBalls = new ArrayList<Slime>();
    this.waitingPlayers = new HashMap<String, Integer>();
    this.playingPlayers = new ArrayList<String>();
    this.team = new HashMap<Player, Player>();
    this.teamReverse = new HashMap<Player, Player>();
    this.teamType = new HashMap<Player, Integer>();
    this.waitingTeams = new Player[0][0];
    this.waitingTeamPlayers = new ArrayList<Player>();
    this.leftMatches = new Match[0];
    this.leftPlayerIsRed = new boolean[0];
    this.plugin = pl;
    this.disableCommands = new DisableCommands(this.plugin, this, getUtilManager());
    this.cfg = getPlugin().getConfig();
    this.loadArenas(cfg);
    this.stats=new Statistics(getPlugin());
    Collection<? extends Player> onlinePlayers;
//    this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this.plugin, (Runnable)new Runnable() {
//      @Override
//      public void run() {
//        Organization.this.update();
//      }
//    }, 1L, 1L);
//    this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this.plugin, (Runnable)new Runnable() {
//      @Override
//      public void run() {
//        Organization.this.refreshCache();
//      }
//    }, 50L, 50L);
    if (cfg.contains("arenas.world")) {
      for (final Entity e : this.plugin.getServer().getWorld(cfg.getString("arenas.world")).getEntities()) {
        if (e instanceof Slime) {
          ((Slime)e).setHealth(0.0);
        }
      }
    }
  }

  public void refreshCache() {
    int i=0;
    for(Match m : matches1v1) {
      if(m.phase>1)
        i++;
    }
    this.matchesInProgress1v1=i;
    i=0;
    for(Match m : matches2v2) {
      if(m.phase>1)
        i++;
    }
    this.matchesInProgress2v2=i;
    i=0;
    for(Match m : matches3v3) {
      if(m.phase>1)
        i++;
    }
    this.matchesInProgress3v3=i;
    i=0;
    for(Match m : matches4v4) {
      if(m.phase>1)
        i++;
    }
    this.matchesInProgress4v4=i;
    i=0;
    for(Match m : matches5v5) {
      if(m.phase>1)
        i++;
    }
    this.matchesInProgress5v5=i;
    i=0;
  }

  public int findArena(final Player p) {
    for (int i = 0; i < this.matches1v1.length; ++i) {
      if (this.matches1v1[i].isRed.containsKey(p)) {
        return i;
      }
    }
    for (int i = 0; i < this.matches2v2.length; ++i) {
      if (this.matches2v2[i].isRed.containsKey(p)) {
        return i;
      }
    }
    for (int i = 0; i < this.matches3v3.length; ++i) {
      if (this.matches3v3[i].isRed.containsKey(p)) {
        return i;
      }
    }
    for (int i = 0; i < this.matches4v4.length; ++i) {
      if (this.matches4v4[i].isRed.containsKey(p)) {
        return i;
      }
    }
    for (int i = 0; i < this.matches5v5.length; ++i) {
      if (this.matches5v5[i].isRed.containsKey(p)) {
        return i;
      }
    }
    return 69;
  }
  public void matchStart(final int type) {
    if (type == 1) {
      for (int i = 0; i < this.matches1v1.length; ++i) {
        if (this.matches1v1[i].phase == 1) {
          this.lobby1v1 = i;
          break;
        }
      }
      for (int i = 0; i < this.waitingTeams.length; ++i) {
        if (this.waitingTeams[i].length > 0 && this.matches1v1[this.lobby1v1].team(this.waitingTeams[i][0], this.waitingTeams[i][1])) {
          this.waitingTeamPlayers.remove(this.waitingTeams[i][0]);
          this.waitingTeamPlayers.remove(this.waitingTeams[i][1]);
          this.waitingTeams = this.reduceArray(this.waitingTeams, this.waitingTeams[i][0]);
        }
      }
    }
    else if (type == 2) {
      for (int i = 0; i < this.matches2v2.length; ++i) {
        if (this.matches2v2[i].phase == 1) {
          this.lobby2v2 = i;
          break;
        }
      }
      for (int i = 0; i < this.waitingTeams.length; ++i) {
        if (this.waitingTeams[i].length > 1 && this.matches2v2[this.lobby2v2].team(this.waitingTeams[i][0], this.waitingTeams[i][1])) {
          this.waitingTeamPlayers.remove(this.waitingTeams[i][0]);
          this.waitingTeamPlayers.remove(this.waitingTeams[i][1]);
          this.waitingTeams = this.reduceArray(this.waitingTeams, this.waitingTeams[i][0]);
        }
      }
    }
    else if (type == 3) {
      for (int i = 0; i < this.matches3v3.length; ++i) {
        if (this.matches3v3[i].phase == 1) {
          this.lobby3v3 = i;
          break;
        }
      }
      for (int i = 0; i < this.waitingTeams.length; ++i) {
        if (this.waitingTeams[i].length > 2 && this.matches3v3[this.lobby3v3].team(this.waitingTeams[i][0], this.waitingTeams[i][1])) {
          this.waitingTeamPlayers.remove(this.waitingTeams[i][0]);
          this.waitingTeamPlayers.remove(this.waitingTeams[i][1]);
          this.waitingTeams = this.reduceArray(this.waitingTeams, this.waitingTeams[i][0]);
        }
      }
    }
    else if (type == 4){
      for (int i = 0; i < this.matches4v4.length; ++i) {
        if (this.matches4v4[i].phase == 1) {
          this.lobby4v4 = i;
          break;
        }
      }
      for (int i = 0; i < this.waitingTeams.length; ++i) {
        if (this.waitingTeams[i].length < 3 && this.matches4v4[this.lobby4v4].team(this.waitingTeams[i][0], this.waitingTeams[i][1])) {
          this.waitingTeamPlayers.remove(this.waitingTeams[i][0]);
          this.waitingTeamPlayers.remove(this.waitingTeams[i][1]);
          this.waitingTeams = this.reduceArray(this.waitingTeams, this.waitingTeams[i][0]);
        }
      }
    }
    else {
      for (int i = 0; i < this.matches5v5.length; ++i) {
        if (this.matches5v5[i].phase == 1) {
          this.lobby5v5 = i;
          break;
        }
      }
      for (int i = 0; i < this.waitingTeams.length; ++i) {
        if (this.waitingTeams[i].length < 3 && this.matches5v5[this.lobby5v5].team(this.waitingTeams[i][0], this.waitingTeams[i][1])) {
          this.waitingTeamPlayers.remove(this.waitingTeams[i][0]);
          this.waitingTeamPlayers.remove(this.waitingTeams[i][1]);
          this.waitingTeams = this.reduceArray(this.waitingTeams, this.waitingTeams[i][0]);
        }
      }
    }
  }

  public void playerLeaves(final Match m, final boolean red) {
    this.leftMatches = this.extendArray(this.leftMatches, m);
    this.leftPlayerIsRed = this.extendArray(this.leftPlayerIsRed, red);
    if (this.leftMatches.length < 2) {
      this.announcementTime = System.currentTimeMillis();
      Collection<? extends Player> onlinePlayers;
      for (int length = (onlinePlayers = this.plugin.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
        final Player p = (Player)onlinePlayers.toArray()[i];
        if (!this.playingPlayers.contains(p.getName()) && !this.waitingPlayers.containsKey(p.getName())) {
          if (m.time < 0) getLogger().send(p, Messages.PLAYER_LEFT.getMessage(new String[]{m.type + "v" + m.type}));
          else getLogger().send(p, Messages.PLAYER_LEFT.getMessage(new String[]{m.type + "v" + m.type}) + Messages.PLAYER_LEFT_ACTIVE.getMessage(new String[]{String.valueOf(m.time)}));
          getLogger().send(p, Messages.TAKEPLACE.getMessage(null));
        }
      }
    }
  }

  public void undoTakePlace(final Match m) {
    int matches = 0;
    Match[] leftMatches;
    for (int length = (leftMatches = this.leftMatches).length, k = 0; k < length; ++k) {
      final Match match = leftMatches[k];
      if (m.equals(match)) ++matches;
    }
    final Match[] newL = new Match[this.leftMatches.length - matches];
    final boolean[] newB = new boolean[this.leftMatches.length - matches];
    int i = 0;
    int j = 0;
    while (i < this.leftMatches.length) {
      if (!this.leftMatches[i].equals(m)) {
        newL[j] = this.leftMatches[i];
        newB[j] = this.leftPlayerIsRed[i];
        ++j;
      }
      ++i;
    }
    this.leftMatches = newL;
    this.leftPlayerIsRed = newB;
  }

  public void endMatch(final Player p) {
    this.playingPlayers.remove(p.getName());
  }

  public void playerStarts(final Player p) {
    this.waitingPlayers.remove(p.getName());
    this.playingPlayers.add(p.getName());
  }

  public void ballTouch(final Player p) {
    Match[] matches1v1;
    for (int length = (matches1v1 = this.matches1v1).length, i = 0; i < length; ++i) {
      final Match match = matches1v1[i];
      match.kick(p);
    }
    Match[] matches2v2;
    for (int k = (matches2v2 = this.matches2v2).length, n = 0; n < k; ++n) {
      final Match match2 = matches2v2[n];
      match2.kick(p);
    }
    Match[] matches3v3;
    for (int m = (matches3v3 = this.matches3v3).length, i2 = 0; i2 < m; ++i2) {
      final Match match3 = matches3v3[i2];
      match3.kick(p);
    }
    Match[] matches4v4;
    for (int length2 = (matches4v4 = this.matches4v4).length, j = 0; j < length2; ++j) {
      final Match match4 = matches4v4[j];
      match4.kick(p);
    }
    Match[] matches5v5;
    for (int length2 = (matches5v5 = this.matches5v5).length, j = 0; j < length2; ++j) {
      final Match match5 = matches5v5[j];
      match5.kick(p);
    }
  }

  public void tackle(final Player p) {
    Match[] matches2v2;
    for (int length = (matches2v2 = this.matches2v2).length, i = 0; i < length; ++i) {
      final Match m = matches2v2[i];
      m.tackle(p);
    }
    Match[] matches3v3;
    for (int length2 = (matches3v3 = this.matches3v3).length, j = 0; j < length2; ++j) {
      final Match k = matches3v3[j];
      k.tackle(p);
    }
    Match[] matches4v4;
    for (int length3 = (matches4v4 = this.matches4v4).length, l = 0; l < length3; ++l) {
      final Match m2 = matches4v4[l];
      m2.tackle(p);
    }
    Match[] matches5v5;
    for (int length4 = (matches5v5 = this.matches5v5).length, l = 0; l < length4; ++l) {
      final Match m2 = matches5v5[l];
      m2.tackle(p);
    }
  }

  public void assisttouch(final Player p) {
    Match[] matches2v2;
    for (int length = (matches2v2 = this.matches2v2).length, i = 0; i < length; ++i) {
      final Match m = matches2v2[i];
      m.assist(p);
    }
    Match[] matches3v3;
    for (int length2 = (matches3v3 = this.matches3v3).length, j = 0; j < length2; ++j) {
      final Match k = matches3v3[j];
      k.assist(p);
    }
    Match[] matches4v4;
    for (int length3 = (matches4v4 = this.matches4v4).length, l = 0; l < length3; ++l) {
      final Match m2 = matches4v4[l];
      m2.assist(p);
    }
    Match[] matches5v5;
    for (int length3 = (matches5v5 = this.matches5v5).length, l = 0; l < length3; ++l) {
      final Match m2 = matches5v5[l];
      m2.assist(p);
    }
  }

  public ItemStack createComplexItem(final Material material, final String name, final String[] lore) {
    final ItemStack item = new ItemStack(material);
    final ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    final int i = lore.length;
    final ArrayList<String> loreArray = new ArrayList<>(Arrays.asList(lore).subList(0, i));
    meta.setLore(loreArray);
    item.setItemMeta(meta);
    return item;
  }

  public void clearInventory(final Player p) {
    final PlayerInventory inv = p.getInventory();
    inv.setChestplate(new ItemStack(Material.AIR));
    inv.setLeggings(new ItemStack(Material.AIR));
    for (int i = 0; i < inv.getContents().length; ++i) {
      final ItemStack is = inv.getContents()[i];
      if (is != null && is.getType() != Material.DIAMOND)
        inv.setItem(i, new ItemStack(Material.AIR));
    }
  }

  public void removeTeam(final Player p) {
    if (this.team.containsKey(p)) {
      final Player player = this.team.get(p);
      getLogger().send(player, Messages.REQUEST_DENY.getMessage(new String[]{p.getName()}));
      getLogger().send(p, Messages.REQUEST_DENY_JOIN.getMessage(null));
      this.teamType.remove(player);
      this.teamReverse.remove(player);
      this.team.remove(p);
    }
    if (this.teamReverse.containsKey(p)) {
      final Player player = this.teamReverse.get(p);
      getLogger().send(player, Messages.REQUEST_DENY.getMessage(new String[]{p.getName()}));
      getLogger().send(p, Messages.REQUEST_DENY_JOIN.getMessage(null));
      this.teamType.remove(p);
      this.teamReverse.remove(p);
      this.team.remove(player);
    }
  }

  public Player[][] extendArray(final Player[][] oldL, final Player[] add) {
    final Player[][] newL = new Player[0][oldL.length + 1];
    System.arraycopy(oldL, 0, newL, 0, oldL.length);
    newL[oldL.length] = add;
    return newL;
  }

  public Player[][] reduceArray(final Player[][] oldL, final Player remove) {
    final Player[][] newL = new Player[0][oldL.length - 1];
    int i = 0;
    int j = 0;
    while (i < newL.length) {
      if (oldL[i][0] != remove && oldL[i][1] != remove) {
        newL[j] = oldL[i];
        ++j;
      }
      ++i;
    }
    return newL;
  }

  public Match[] extendArray(final Match[] oldL, final Match add) {
    final Match[] newL = new Match[oldL.length + 1];
    System.arraycopy(oldL, 0, newL, 0, oldL.length);
    newL[oldL.length] = add;
    return newL;
  }

  public boolean[] extendArray(final boolean[] oldL, final boolean add) {
    final boolean[] newL = new boolean[oldL.length + 1];
    System.arraycopy(oldL, 0, newL, 0, oldL.length);
    newL[oldL.length] = add;
    return newL;
  }

  public boolean isOnlinePlayer(final String s) {
    Collection<? extends Player> onlinePlayers;
    for (int length = (onlinePlayers = this.plugin.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
      final Player p = (Player)onlinePlayers.toArray()[i];
      if (p.getName().equalsIgnoreCase(s)) {
        return true;
      }
    }
    return false;
  }

  public void addArena(final int type, final Location b, final Location r) {
    final Location m = new Location(b.getWorld(), (b.getX() + r.getX()) / 2.0, (b.getY() + r.getY()) / 2.0 + 2.0, (b.getZ() + r.getZ()) / 2.0);
    if (type == 1) {
      this.matches1v1 = this.extendArray(this.matches1v1, new Match(getUtilManager(), this, this.plugin, 1, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length + this.matches5v5.length));
    }
    else if (type == 2) {
      this.matches2v2 = this.extendArray(this.matches2v2, new Match(getUtilManager(), this, this.plugin, 2, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length + this.matches5v5.length));
    }
    else if (type == 3) {
      this.matches3v3 = this.extendArray(this.matches3v3, new Match(getUtilManager(), this, this.plugin, 3, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length + this.matches5v5.length));
    }
    else if (type == 4) {
      this.matches4v4 = this.extendArray(this.matches4v4, new Match(getUtilManager(), this, this.plugin, 4, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length + this.matches5v5.length));
    } else {
      this.matches5v5 = this.extendArray(this.matches5v5, new Match(getUtilManager(), this, this.plugin, 5, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length + this.matches5v5.length));
    }
  }

  public void loadArenas(final FileConfiguration cfg) {
    for (int i = 1; i <= cfg.getInt("arenas.1v1.amount"); ++i) {
      final World world = this.plugin.getServer().getWorld(cfg.getString("arenas.world"));
      final String blue = "arenas.1v1." + i + ".blue.";
      final String red = "arenas.1v1." + i + ".red.";
      final Location b = new Location(world, cfg.getDouble(blue + "x"), cfg.getDouble(blue + "y"), cfg.getDouble(blue + "z"));
      b.setPitch((float)cfg.getDouble(blue + "pitch"));
      b.setYaw((float)cfg.getDouble(blue + "yaw"));
      final Location r = new Location(world, cfg.getDouble(red + "x"), cfg.getDouble(red + "y"), cfg.getDouble(red + "z"));
      r.setPitch((float)cfg.getDouble(red + "pitch"));
      r.setYaw((float)cfg.getDouble(red + "yaw"));
      this.addArena(1, b, r);
    }
    for (int i = 1; i <= cfg.getInt("arenas.2v2.amount"); ++i) {
      final World world = this.plugin.getServer().getWorld(cfg.getString("arenas.world"));
      final String blue = "arenas.2v2." + i + ".blue.";
      final String red = "arenas.2v2." + i + ".red.";
      final Location b = new Location(world, cfg.getDouble(blue + "x"), cfg.getDouble(blue + "y"), cfg.getDouble(blue + "z"));
      b.setPitch((float)cfg.getDouble(blue + "pitch"));
      b.setYaw((float)cfg.getDouble(blue + "yaw"));
      final Location r = new Location(world, cfg.getDouble(red + "x"), cfg.getDouble(red + "y"), cfg.getDouble(red + "z"));
      r.setPitch((float)cfg.getDouble(red + "pitch"));
      r.setYaw((float)cfg.getDouble(red + "yaw"));
      this.addArena(2, b, r);
    }
    for (int i = 1; i <= cfg.getInt("arenas.3v3.amount"); ++i) {
      final World world = this.plugin.getServer().getWorld(cfg.getString("arenas.world"));
      final String blue = "arenas.3v3." + i + ".blue.";
      final String red = "arenas.3v3." + i + ".red.";
      final Location b = new Location(world, cfg.getDouble(blue + "x"), cfg.getDouble(blue + "y"), cfg.getDouble(blue + "z"));
      b.setPitch((float)cfg.getDouble(blue + "pitch"));
      b.setYaw((float)cfg.getDouble(blue + "yaw"));
      final Location r = new Location(world, cfg.getDouble(red + "x"), cfg.getDouble(red + "y"), cfg.getDouble(red + "z"));
      r.setPitch((float)cfg.getDouble(red + "pitch"));
      r.setYaw((float)cfg.getDouble(red + "yaw"));
      this.addArena(3, b, r);
    }
    for (int i = 1; i <= cfg.getInt("arenas.4v4.amount"); ++i) {
      final World world = this.plugin.getServer().getWorld(cfg.getString("arenas.world"));
      final String blue = "arenas.4v4." + i + ".blue.";
      final String red = "arenas.4v4." + i + ".red.";
      final Location b = new Location(world, cfg.getDouble(blue + "x"), cfg.getDouble(blue + "y"), cfg.getDouble(blue + "z"));
      b.setPitch((float)cfg.getDouble(blue + "pitch"));
      b.setYaw((float)cfg.getDouble(blue + "yaw"));
      final Location r = new Location(world, cfg.getDouble(red + "x"), cfg.getDouble(red + "y"), cfg.getDouble(red + "z"));
      r.setPitch((float)cfg.getDouble(red + "pitch"));
      r.setYaw((float)cfg.getDouble(red + "yaw"));
      this.addArena(4, b, r);
    }
    for (int i = 1; i <= cfg.getInt("arenas.5v5.amount"); ++i) {
      final World world = this.plugin.getServer().getWorld(cfg.getString("arenas.world"));
      final String blue = "arenas.5v5." + i + ".blue.";
      final String red = "arenas.5v5." + i + ".red.";
      final Location b = new Location(world, cfg.getDouble(blue + "x"), cfg.getDouble(blue + "y"), cfg.getDouble(blue + "z"));
      b.setPitch((float)cfg.getDouble(blue + "pitch"));
      b.setYaw((float)cfg.getDouble(blue + "yaw"));
      final Location r = new Location(world, cfg.getDouble(red + "x"), cfg.getDouble(red + "y"), cfg.getDouble(red + "z"));
      r.setPitch((float)cfg.getDouble(red + "pitch"));
      r.setYaw((float)cfg.getDouble(red + "yaw"));
      this.addArena(5, b, r);
    }
  }

  public void checkStats(Player p) {
    PlayerDataManager pm = new PlayerDataManager(this.plugin, p.getUniqueId());
    final int m = pm.getInt("matches");
    final int w = pm.getInt("wins");
    final int t = pm.getInt("ties");
    final int s = pm.getInt("best_win_streak");
    final int a = pm.getInt("assists");
    final int l = m - w - t;
    double mw = m;
    if (w > 0) mw = (double) (100 * m) / w / 100.0;
    final int g = pm.getInt("goals");
    double gm = 0.0;
    if (m > 0) gm = (double) (100 * g) / m / 100.0;
    final double multiplier = 1.0 - Math.pow(0.9, m);
    double goalBonus = 0.5;
    if (m > 0) goalBonus = 1.0 - multiplier * Math.pow(0.2, (double) g / m) - 0.5 / Math.pow(1.1111111111111112, m);
    double addition = 0.0;
    if (m > 0 && w + t > 0) {
      addition = 8.0 * (1.0 / (100 * m / (w + 0.5 * t) / 100.0)) - 4.0;
    } else if (m > 0) addition = -4.0;
    final double skillLevel = (int) (100.0 * (5.0 + goalBonus + addition * multiplier)) / 100.0;
    final int rank = (int) (skillLevel * 2.0 - 0.5);

    if (w < 0)
      getLogger().send(p, Messages.STATS.getMessage(new String[]{String.valueOf(m), String.valueOf(w), String.valueOf(t), String.valueOf(l), "nije učestvovao u FC", String.valueOf(s), String.valueOf(g), String.valueOf(gm), String.valueOf(a)}));
    else
      getLogger().send(p, Messages.STATS.getMessage(new String[]{String.valueOf(m), String.valueOf(w), String.valueOf(t), String.valueOf(l), String.valueOf(mw), String.valueOf(s), String.valueOf(g), String.valueOf(gm), String.valueOf(a)}));
  }

  public void update() {
    for (int i = 0; i < this.matches1v1.length; i++) {
      this.matches1v1[i].update();
    }
    for (int i = 0; i < this.matches2v2.length; i++) {
      this.matches2v2[i].update();
    }
    for (int i = 0; i < this.matches3v3.length; i++) {
      this.matches3v3[i].update();
    }
    for (int i = 0; i < this.matches4v4.length; i++) {
      this.matches4v4[i].update();
    }
    for (int i = 0; i < this.matches5v5.length; i++) {
      this.matches5v5[i].update();
    }
    if (this.leftMatches.length > 0 && System.currentTimeMillis() - this.announcementTime > 30000L) {
      final Match m = this.leftMatches[0];
      this.announcementTime = System.currentTimeMillis();
      Collection<? extends Player> onlinePlayers;
      for (int length = (onlinePlayers = this.plugin.getServer().getOnlinePlayers()).size(), j = 0; j < length; ++j) {
        final Player p = (Player) onlinePlayers.toArray()[j];
        if (!this.playingPlayers.contains(p.getName()) && !this.waitingPlayers.containsKey(p.getName())) {
          if (m.time < 0) getLogger().send(p, Messages.PLAYER_LEFT.getMessage(new String[]{m.type + "v" + m.type}));
          else getLogger().send(p, Messages.PLAYER_LEFT.getMessage(new String[]{m.type + "v" + m.type}) + Messages.PLAYER_LEFT_ACTIVE.getMessage(new String[]{String.valueOf(m.time)}));
          getLogger().send(p, Messages.TAKEPLACE.getMessage(null));
        }
      }
    }
  }
}
