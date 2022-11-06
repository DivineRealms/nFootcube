package me.ivchie.footcube;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Organization implements Listener {
    private Footcube plugin;
    private DisableCommands disableCommands;
    private Highscores highscores;
    public String pluginString;
    private String adminString;
    private String or;
    private String setupGuy;
    private int setupType;
    private Location setupLoc;
    private Match[] matches1v1;
    private Match[] matches2v2;
    private Match[] matches3v3;
    private Match[] matches4v4;
    private int lobby1v1;
    private int lobby2v2;
    private int lobby3v3;
    private int lobby4v4;
    public ArrayList<Slime> practiceBalls;
    public HashMap<String, Integer> waitingPlayers;
    public ArrayList<String> playingPlayers;
    private HashMap<Player, Player> team;
    private HashMap<Player, Player> teamReverse;
    private HashMap<Player, Integer> teamType;
    private Player[][] waitingTeams;
    private ArrayList<Player> waitingTeamPlayers;
    private Match[] leftMatches;
    private boolean[] leftPlayerIsRed;
    private long announcementTime;
    public Stats wins;
    public Stats matches;
    public Stats ties;
    public Stats goals;
    public Stats store;
    public Stats winStreak;
    public Stats bestWinStreak;
    public UUIDConverter uuidConverter;
    public Economy economy;

    public Organization(final Footcube pl) {
        this.highscores = null;
        this.pluginString = ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("prefix"));
        this.adminString = ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("adminPrefix"));
        this.or = ChatColor.YELLOW + "|" + ChatColor.AQUA;
        this.setupGuy = null;
        this.setupType = 0;
        this.setupLoc = null;
        this.matches1v1 = new Match[0];
        this.matches2v2 = new Match[0];
        this.matches3v3 = new Match[0];
        this.matches4v4 = new Match[0];
        this.lobby1v1 = 0;
        this.lobby2v2 = 0;
        this.lobby3v3 = 0;
        this.lobby4v4 = 0;
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
        this.wins = new Stats();
        this.matches = new Stats();
        this.ties = new Stats();
        this.goals = new Stats();
        this.store = new Stats();
        this.winStreak = new Stats();
        this.bestWinStreak = new Stats();
        this.uuidConverter = new UUIDConverter();
        this.economy = null;
        this.plugin = pl;
        this.disableCommands = new DisableCommands(this.plugin, this);
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        final FileConfiguration cfg = this.plugin.getConfig();
        cfg.addDefault("arenas.1v1.amount", (Object)0);
        cfg.addDefault("arenas.2v2.amount", (Object)0);
        cfg.addDefault("arenas.3v3.amount", (Object)0);
        cfg.addDefault("arenas.4v4.amount", (Object)0);
        cfg.options().copyDefaults(true);
        this.plugin.saveConfig();
        this.loadArenas(cfg);
        this.wins.setup("plugins" + File.separator + "Footcube" + File.separator + "wins.stats");
        this.matches.setup("plugins" + File.separator + "Footcube" + File.separator + "matches.stats");
        this.ties.setup("plugins" + File.separator + "Footcube" + File.separator + "ties.stats");
        this.goals.setup("plugins" + File.separator + "Footcube" + File.separator + "goals.stats");
        this.store.setup("plugins" + File.separator + "Footcube" + File.separator + "store.stats");
        this.winStreak.setup("plugins" + File.separator + "Footcube" + File.separator + "winStreak.stats");
        this.bestWinStreak.setup("plugins" + File.separator + "Footcube" + File.separator + "bestWinStreak.stats");
        this.uuidConverter.setup("plugins" + File.separator + "Footcube" + File.separator + "UUID.data");
        this.wins.load();
        this.matches.load();
        this.ties.load();
        this.goals.load();
        this.store.load();
        this.winStreak.load();
        this.bestWinStreak.load();
        this.uuidConverter.load();
        Collection<? extends Player> onlinePlayers;
        for (int length = (onlinePlayers = (Collection<? extends Player>)this.plugin.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
            final Player p = (Player)onlinePlayers.toArray()[i];
            if (!this.uuidConverter.has(p.getUniqueId().toString())) {
                this.uuidConverter.put(p.getUniqueId().toString(), p.getName());
            }
        }
        this.setupEconomy();
        if (cfg.contains("arenas.world")) {
            for (final Entity e : this.plugin.getServer().getWorld(cfg.getString("arenas.world")).getEntities()) {
                if (e instanceof Slime) {
                    ((Slime)e).setHealth(0.0);
                }
            }
        }
        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this.plugin, (Runnable)new Runnable() {
            @Override
            public void run() {
                Organization.this.update();
            }
        }, 1L, 1L);
    }

    public void command(final CommandSender sender, final Command cmd, final String c, final String[] args) {
        this.disableCommands.command(sender, cmd, c, args);
        final Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("tc")) {
            String message = "";
            for (final String s : args) {
                message = message + s + " ";
            }
            Match[] matches2v2;
            for (int length3 = (matches2v2 = this.matches2v2).length, n = 0; n < length3; ++n) {
                final Match m = matches2v2[n];
                m.teamchat(p, message);
            }
            Match[] matches3v3;
            for (int i = (matches3v3 = this.matches3v3).length, k = 0; k < i; ++k) {
                final Match l = matches3v3[k];
                l.teamchat(p, message);
            }
            Match[] matches4v4;
            for (int length4 = (matches4v4 = this.matches4v4).length, n2 = 0; n2 < length4; ++n2) {
                final Match m2 = matches4v4[n2];
                m2.teamchat(p, message);
            }
        }
        if (cmd.getName().equalsIgnoreCase("footcube")) {
            boolean success = true;
            if (args.length < 1) {
                success = false;
            }
            else if (args[0].equalsIgnoreCase("reload") && p.hasPermission("footcube.admin")) {
                messagesConfig.osvezi();
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("reload")));
            }
            else if (args[0].equalsIgnoreCase("join")) {
                final FileConfiguration cfg = this.plugin.getConfig();
                if (this.waitingPlayers.containsKey(p.getName()) || this.playingPlayers.contains(p.getName())) {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("alreadyIngame")));
                }
                else if (this.waitingTeamPlayers.contains(p)) {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("alreadyIngame")));
                }
                else if (args.length < 2) {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("specifyArena")));
                }
                else if (args[1].equalsIgnoreCase("2v2")) {
                    if (this.plugin.getConfig().getInt("arenas.2v2.amount") == 0) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("no2v2")));
                    }
                    else {
                        p.setFlying(false);
                        this.matches2v2[this.lobby2v2].join(p, false);
                        this.waitingPlayers.put(p.getName(), 2);
                        this.removeTeam(p);
                    }
                }
                else if (args[1].equalsIgnoreCase("3v3")) {
                    if (this.plugin.getConfig().getInt("arenas.3v3.amount") == 0) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("no3v3")));
                    }
                    else {
                        p.setFlying(false);
                        this.matches3v3[this.lobby3v3].join(p, false);
                        this.waitingPlayers.put(p.getName(), 3);
                        this.removeTeam(p);
                    }
                }
                else if (args[1].equalsIgnoreCase("4v4")) {
                    if (this.plugin.getConfig().getInt("arenas.4v4.amount") == 0) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("no4v4")));
                    }
                    else {
                        p.setFlying(false);
                        this.matches4v4[this.lobby4v4].join(p, false);
                        this.waitingPlayers.put(p.getName(), 4);
                        this.removeTeam(p);
                    }
                }
                else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("specifyArena")));
                }
            }
            else if (args[0].equalsIgnoreCase("best")) {
                this.updateHighscores(p);
            }
            else if (args[0].equalsIgnoreCase("team")) {
                if (args.length < 2) {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamNoArgs")));
                }
                else if (args[1].equalsIgnoreCase("2v2") || args[1].equalsIgnoreCase("3v3") || args[1].equalsIgnoreCase("4v4")) {
                    if (this.waitingPlayers.containsKey(p.getName()) || this.playingPlayers.contains(p.getName())) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("ingameTeam")));
                    }
                    else if (this.waitingTeamPlayers.contains(p)) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("alreadyIngame")));
                    }
                    else if (this.team.containsKey(p)) {
                        final String matchType = this.teamType.get(this.team.get(p)) + "v" + this.teamType.get(this.team.get(p));
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("alreadyHaveRequest") + this.team.get(p).getName() + messagesConfig.vuci().getString("alreadyHaveRequest1") + matchType + messagesConfig.vuci().getString("alreadyHaveRequest2")));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamResponse")));
                    }
                    else if (this.teamReverse.containsKey(p)) {
                        final String matchType = this.teamType.get(p) + "v" + this.teamType.get(p);
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("alreadySentRequest") + this.teamReverse.get(p).getName() + messagesConfig.vuci().getString("alreadySentRequest1") + matchType + messagesConfig.vuci().getString("alreadySentRequest2")));
                    }
                    else if (args.length < 3) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamNoArgs")));
                    }
                    else if (this.isOnlinePlayer(args[2])) {
                        final Player player = this.plugin.getServer().getPlayer(args[2]);
                        if (this.waitingTeamPlayers.contains(player)) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("inTeamDeny") + args[2] + messagesConfig.vuci().getString("inTeamDeny1")));
                        }
                        else if (this.waitingPlayers.containsKey(player.getName()) || this.playingPlayers.contains(player.getName())) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("inGameDeny") + args[2] + messagesConfig.vuci().getString("inGameDeny1")));
                        }
                        else if (this.team.containsKey(player)) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("playerAlreadyGotRequest") + args[2] + messagesConfig.vuci().getString("playerAlreadyGotRequest1")));
                        }
                        else if (this.teamReverse.containsKey(player)) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("playerAlreadySentRequest") + args[2] + messagesConfig.vuci().getString("playerAlreadySentRequest1")));
                        }
                        else if (p.getName() == player.getName()) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamYourself")));
                        }
                        else {
                            this.team.put(player, p);
                            this.teamReverse.put(p, player);
                            int matchType2 = 2;
                            if (args[1].equalsIgnoreCase("3v3")) {
                                matchType2 = 3;
                            }
                            if (args[1].equalsIgnoreCase("4v4")) {
                                matchType2 = 4;
                            }
                            this.teamType.put(p, matchType2);
                            player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("wannaBeTeam") + p.getName() + messagesConfig.vuci().getString("wannaBeTeam1") + matchType2 + "v" + matchType2 + messagesConfig.vuci().getString("wannaBeTeam2")));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamResponse")));
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamRequestSent") + player.getName() + messagesConfig.vuci().getString("teamRequestSent1") + matchType2 + "v" + matchType2 + messagesConfig.vuci().getString("teamRequestSent2")));
                        }
                    }
                    else {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("playerOffline") + args[2] + messagesConfig.vuci().getString("playerOffline1")));
                    }
                }
                else if (args[1].equalsIgnoreCase("cancel")) {
                    if (this.teamReverse.containsKey(p)) {
                        final Player player = this.teamReverse.get(p);
                        player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("requestCanceled") + p.getName() + messagesConfig.vuci().getString("requestCanceled1")));
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamCancel")));
                        this.teamType.remove(p);
                        this.teamReverse.remove(p);
                        this.team.remove(player);
                    }
                    else {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("RequestNotSent")));
                    }
                }
                else if (args[1].equalsIgnoreCase("accept")) {
                    if (this.team.containsKey(p)) {
                        final Player player = this.team.get(p);
                        if (this.teamType.get(player) == 2) {
                            this.waitingPlayers.put(p.getName(), 2);
                            this.waitingPlayers.put(player.getName(), 2);
                            if (!this.matches2v2[this.lobby2v2].team(p, player)) {
                                this.waitingPlayers.remove(p.getName());
                                this.waitingPlayers.remove(player.getName());
                                this.waitingTeams = this.extendArray(this.waitingTeams, new Player[] { p, player });
                                this.waitingTeamPlayers.add(p);
                                this.waitingTeamPlayers.add(player);
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamed") + player.getName()));
                                player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamed") + p.getName()));
                            }
                        }
                        else if (this.teamType.get(player) == 3) {
                            this.waitingPlayers.put(p.getName(), 3);
                            this.waitingPlayers.put(player.getName(), 3);
                            if (!this.matches3v3[this.lobby3v3].team(p, player)) {
                                this.waitingPlayers.remove(p.getName());
                                this.waitingPlayers.remove(player.getName());
                                this.waitingTeams = this.extendArray(this.waitingTeams, new Player[] { p, player });
                                this.waitingTeamPlayers.add(p);
                                this.waitingTeamPlayers.add(player);
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamed") + player.getName()));
                                player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamed") + p.getName()));
                            }
                        }
                        else {
                            this.waitingPlayers.put(p.getName(), 4);
                            this.waitingPlayers.put(player.getName(), 4);
                            if (!this.matches4v4[this.lobby4v4].team(p, player)) {
                                this.waitingPlayers.remove(p.getName());
                                this.waitingPlayers.remove(player.getName());
                                this.waitingTeams = this.extendArray(this.waitingTeams, new Player[] { p, player });
                                this.waitingTeamPlayers.add(p);
                                this.waitingTeamPlayers.add(player);
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamed") + player.getName()));
                                player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamed") + p.getName()));
                            }
                        }
                        this.team.remove(p);
                        this.teamReverse.remove(player);
                        this.teamType.remove(player);
                    }
                    else {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("noRequest")));
                    }
                }
                else if (args[1].equalsIgnoreCase("decline")) {
                    if (this.team.containsKey(p)) {
                        final Player player = this.team.get(p);
                        player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("requestDeny") + p.getName() + messagesConfig.vuci().getString("requestDeny1")));
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("requestDenySuccess")));
                        this.teamType.remove(player);
                        this.teamReverse.remove(player);
                        this.team.remove(p);
                    }
                    else {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("noRequest2Deny")));
                    }
                }
                else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teamNoArgs")));
                }
            }
            else if (args[0].equalsIgnoreCase("takeplace")) {
                if (this.leftMatches.length > 0) {
                    if (this.waitingPlayers.containsKey(p.getName()) || this.playingPlayers.contains(p.getName())) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("alreadyIngame")));
                    }
                    else {
                        this.leftMatches[0].takePlace(p);
                        this.playingPlayers.add(p.getName());
                        final Match[] newL = new Match[this.leftMatches.length - 1];
                        final boolean[] newB = new boolean[this.leftMatches.length - 1];
                        for (int i2 = 0; i2 < newL.length; ++i2) {
                            newL[i2] = this.leftMatches[i2 + 1];
                            newB[i2] = this.leftPlayerIsRed[i2 + 1];
                        }
                        this.leftMatches = newL;
                        this.leftPlayerIsRed = newB;
                    }
                }
                else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("noTkp")));
                }
            }
            else if (args[0].equalsIgnoreCase("stats")) {
                if (args.length > 1) {
                    if (this.uuidConverter.hasValue(args[1])) {
                        final String uuid = this.uuidConverter.getKey(args[1]);
                        this.checkStats(uuid, p);
                    }
                    else {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("playerNoStats") + args[1] + messagesConfig.vuci().getString("playerNoStats1")));
                        this.checkStats(p.getUniqueId().toString(), p);
                    }
                }
                else {
                    this.checkStats(p.getUniqueId().toString(), p);
                }
            }
            else if (args[0].equalsIgnoreCase("leave")) {
                if (this.waitingPlayers.containsKey(p.getName())) {
                    if (this.waitingPlayers.get(p.getName()) == 2) {
                        this.matches2v2[this.lobby2v2].leave(p);
                        this.waitingPlayers.remove(p.getName());
                        for (int j3 = 0; j3 < this.waitingTeams.length; ++j3) {
                            if (this.waitingTeams[j3].length > 1) {
                                break;
                            }
                        }
                    } else if (this.waitingPlayers.get(p.getName()) == 3) {
                        this.matches3v3[this.lobby3v3].leave(p);
                        this.waitingPlayers.remove(p.getName());
                        int team = -1;
                        for (int j2 = 0; j2 < this.waitingTeams.length; ++j2) {
                            if (this.waitingTeams[j2].length > 2) {
                                team = j2;
                                break;
                            }
                        }
                        if (team > -1 && this.matches3v3[this.lobby3v3].team(this.waitingTeams[team][0], this.waitingTeams[team][1])) {
                            this.waitingTeamPlayers.remove(this.waitingTeams[team][0]);
                            this.waitingTeamPlayers.remove(this.waitingTeams[team][1]);
                            this.reduceArray(this.waitingTeams, this.waitingTeams[team][0]);
                        }
                    } else {
                        if (this.lobby4v4 < this.matches4v4.length) {
                            this.matches4v4[this.lobby4v4].leave(p);
                        }
                        this.waitingPlayers.remove(p.getName());
                        int team = -1;
                        for (int j2 = 0; j2 < this.waitingTeams.length; ++j2) {
                            if (this.waitingTeams[j2].length < 3) {
                                team = j2;
                                break;
                            }
                        }
                        if (team > -1 && this.matches4v4[this.lobby4v4].team(this.waitingTeams[team][0], this.waitingTeams[team][1])) {
                            this.waitingTeamPlayers.remove(this.waitingTeams[team][0]);
                            this.waitingTeamPlayers.remove(this.waitingTeams[team][1]);
                            this.reduceArray(this.waitingTeams, this.waitingTeams[team][0]);
                        }
                    }
                }
                else if (!this.playingPlayers.contains(p.getName())) {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("notIngame")));
                }
                else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("cantLeave")));
                }
            }
            else if (args[0].equalsIgnoreCase("undo") && this.setupGuy == p.getName()) {
                this.setupGuy = null;
                this.setupType = 0;
                this.setupLoc = null;
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("undo")));
            }
            else if (args[0].equalsIgnoreCase("setuparena") && p.hasPermission("footcube.admin")) {
                if (this.setupGuy == null) {
                    if (args.length < 2) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', "Morate navesti vrstu arene."));
                        p.sendMessage("/fc setuparena (2v2, 3v3 ili 4v4)");
                    }
                    else {
                        if (args[1].equalsIgnoreCase("2v2")) {
                            this.setupType = 2;
                        }
                        if (args[1].equalsIgnoreCase("3v3")) {
                            this.setupType = 3;
                        }
                        else if (args[1].equalsIgnoreCase("4v4")) {
                            this.setupType = 4;
                        }
                        else {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("invalidArenaType")));
                        }
                        if (this.setupType > 0) {
                            this.setupGuy = p.getName();
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("arenaCreation")));
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("arenaCreation1")));
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("arenaCreation2")));
                        }
                    }
                }
                else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("alreadyInSetup")));
                }
            }
            else if (args[0].equalsIgnoreCase("cleararenas") && p.hasPermission("footcube.admin")) {
                final FileConfiguration cfg = this.plugin.getConfig();
                cfg.set("arenas", (Object)null);
                cfg.addDefault("arenas.1v1.amount", (Object)0);
                cfg.addDefault("arenas.2v2.amount", (Object)0);
                cfg.addDefault("arenas.3v3.amount", (Object)0);
                cfg.addDefault("arenas.4v4.amount", (Object)0);
                cfg.options().copyDefaults(true);
                this.plugin.saveConfig();
                this.matches1v1 = new Match[0];
                this.matches2v2 = new Match[0];
                this.matches3v3 = new Match[0];
                this.matches4v4 = new Match[0];
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("arenaClearSuccess")));
            }
            else if (args[0].equalsIgnoreCase("set") && this.setupGuy == p.getName()) {
                if (this.setupLoc == null) {
                    this.setupLoc = p.getLocation();
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("firstLocation")));
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("secondLocation")));
                }
                else {
                    final FileConfiguration cfg = this.plugin.getConfig();
                    final String v = this.setupType + "v" + this.setupType;
                    final int arena = cfg.getInt("arenas." + v + ".amount") + 1;
                    final String blue = "arenas." + v + "." + arena + ".blue.";
                    final String red = "arenas." + v + "." + arena + ".red.";
                    final Location b2 = this.setupLoc;
                    final Location r = p.getLocation();
                    cfg.set("arenas." + v + ".amount", (Object)arena);
                    cfg.set("arenas.world", (Object)p.getWorld().getName());
                    cfg.set(blue + "x", (Object)b2.getX());
                    cfg.set(blue + "y", (Object)b2.getY());
                    cfg.set(blue + "z", (Object)b2.getZ());
                    cfg.set(blue + "pitch", (Object)b2.getPitch());
                    cfg.set(blue + "yaw", (Object)b2.getYaw());
                    cfg.set(red + "x", (Object)r.getX());
                    cfg.set(red + "y", (Object)r.getY());
                    cfg.set(red + "z", (Object)r.getZ());
                    cfg.set(red + "pitch", (Object)r.getPitch());
                    cfg.set(red + "yaw", (Object)r.getYaw());
                    this.plugin.saveConfig();
                    this.addArena(this.setupType, b2, r);
                    this.setupGuy = null;
                    this.setupType = 0;
                    this.setupLoc = null;
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("arenaSetupSuccess")));
                }
            }
            else {
                success = false;
            }
            if (!success) {
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("help")));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("help1")));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("help2")));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("help3")));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("help4")));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("help5")));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("help6")));
                if (p.hasPermission("footcube.admin")) {
                    p.sendMessage(this.adminString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("adminHelp")));
                    p.sendMessage(this.adminString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("adminHelp1")));
                }
            }
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final String uuid = p.getUniqueId().toString();
        this.clearInventory(p);
        if (!this.store.has(uuid)) {
            this.store.put(uuid, 0);
        }
        if (!this.winStreak.has(uuid)) {
            this.winStreak.put(uuid, 0);
        }
        if (!this.bestWinStreak.has(uuid)) {
            this.bestWinStreak.put(uuid, 0);
        }
        this.uuidConverter.put(p.getUniqueId().toString(), p.getName());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        if (this.waitingPlayers.containsKey(p.getName())) {
            this.waitingPlayers.remove(p.getName());
        }
        if (this.playingPlayers.contains(p.getName())) {
            this.playingPlayers.remove(p.getName());
        }
        if (this.team.containsKey(p)) {
            final Player player = this.team.get(p);
            player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("requestDeny") + p.getName() + messagesConfig.vuci().getString("requestDeny1")));
            this.teamType.remove(player);
            this.teamReverse.remove(player);
            this.team.remove(p);
        }
        else if (this.teamReverse.containsKey(p)) {
            final Player player = this.teamReverse.get(p);
            this.teamType.remove(p);
            this.teamReverse.remove(p);
            this.team.remove(player);
        }
        else if (this.waitingTeamPlayers.contains(p)) {
            for (int i = 0; i < this.waitingTeams.length; ++i) {
                if (this.waitingTeams[i][0] == p) {
                    final Player player2 = this.waitingTeams[i][1];
                    player2.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teammateLeave")));
                    this.waitingTeams = this.reduceArray(this.waitingTeams, p);
                    this.waitingTeamPlayers.remove(p);
                    this.waitingTeamPlayers.remove(player2);
                }
                else if (this.waitingTeams[i][1] == p) {
                    final Player player2 = this.waitingTeams[i][0];
                    player2.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("teammateLeave")));
                    this.waitingTeams = this.reduceArray(this.waitingTeams, p);
                    this.waitingTeamPlayers.remove(p);
                    this.waitingTeamPlayers.remove(player2);
                }
            }
        }
    }

    public void matchStart(final int type) {
        if (type == 2) {
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
        else {
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
    }

    public void playerLeaves(final Match m, final boolean red) {
        this.leftMatches = this.extendArray(this.leftMatches, m);
        this.leftPlayerIsRed = this.extendArray(this.leftPlayerIsRed, red);
        if (this.leftMatches.length < 2) {
            this.announcementTime = System.currentTimeMillis();
            Collection<? extends Player> onlinePlayers;
            for (int length = (onlinePlayers = (Collection<? extends Player>)this.plugin.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
                final Player p = (Player)onlinePlayers.toArray()[i];
                if (!this.playingPlayers.contains(p.getName()) && !this.waitingPlayers.containsKey(p.getName())) {
                    if (m.time.getScore() < 0) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("playerLeft") + m.type + "v" + m.type + messagesConfig.vuci().getString("playerLeft1")));
                    }
                    else {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("playerLeft") + m.type + "v" + m.type + messagesConfig.vuci().getString("playerLeft1") + messagesConfig.vuci().getString("playerLeft2") + m.time.getScore() + messagesConfig.vuci().getString("playerLeft3")));
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("tkp")));
                }
            }
        }
    }

    public void undoTakePlace(final Match m) {
        int matches = 0;
        Match[] leftMatches;
        for (int length = (leftMatches = this.leftMatches).length, k = 0; k < length; ++k) {
            final Match match = leftMatches[k];
            if (m.equals(match)) {
                ++matches;
            }
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
        this.playingPlayers.add(p.getName());
        this.waitingPlayers.remove(p.getName());
    }

    public void ballTouch(final Player p) {
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
    }

    public ItemStack createComplexItem(final Material material, final String name, final String[] lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        final ArrayList<String> loreArray = new ArrayList<String>();
        final String[] arrayOfString = lore;
        final int i = lore.length;
        for (byte b = 0; b < i; ++b) {
            final String s = arrayOfString[b];
            loreArray.add(s);
        }
        meta.setLore((List)loreArray);
        item.setItemMeta(meta);
        return item;
    }

    public void clearInventory(final Player p) {
        final PlayerInventory inv = p.getInventory();
        inv.setChestplate(new ItemStack(Material.AIR));
        inv.setLeggings(new ItemStack(Material.AIR));
        for (int i = 0; i < inv.getContents().length; ++i) {
            final ItemStack is = inv.getContents()[i];
            if (is != null && is.getType() != Material.DIAMOND) {
                inv.setItem(i, new ItemStack(Material.AIR));
            }
        }
    }

    public int getStoreNumber(final Player p, final int digit) {
        final int storeContent = this.store.get(p.getUniqueId().toString());
        return (int)((storeContent % Math.pow(10.0, digit + 1) - storeContent % Math.pow(10.0, digit - 1)) / Math.pow(10.0, digit - 1));
    }

    private void removeTeam(final Player p) {
        if (this.team.containsKey(p)) {
            final Player player = this.team.get(p);
            player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("requestDeny") + p.getName() + messagesConfig.vuci().getString("requestDeny1")));
            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("joinRequestDeny")));
            this.teamType.remove(player);
            this.teamReverse.remove(player);
            this.team.remove(p);
        }
        if (this.teamReverse.containsKey(p)) {
            final Player player = this.teamReverse.get(p);
            player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("requestDeny") + p.getName() + messagesConfig.vuci().getString("requestDeny1")));
            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("joinRequestDeny")));
            this.teamType.remove(p);
            this.teamReverse.remove(p);
            this.team.remove(player);
        }
    }

    private Player[][] extendArray(final Player[][] oldL, final Player[] add) {
        final Player[][] newL = new Player[0][oldL.length + 1];
        for (int i = 0; i < oldL.length; ++i) {
            newL[i] = oldL[i];
        }
        newL[oldL.length] = add;
        return newL;
    }

    private Player[][] reduceArray(final Player[][] oldL, final Player remove) {
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

    private Match[] extendArray(final Match[] oldL, final Match add) {
        final Match[] newL = new Match[oldL.length + 1];
        for (int i = 0; i < oldL.length; ++i) {
            newL[i] = oldL[i];
        }
        newL[oldL.length] = add;
        return newL;
    }

    private boolean[] extendArray(final boolean[] oldL, final boolean add) {
        final boolean[] newL = new boolean[oldL.length + 1];
        for (int i = 0; i < oldL.length; ++i) {
            newL[i] = oldL[i];
        }
        newL[oldL.length] = add;
        return newL;
    }

    private boolean isOnlinePlayer(final String s) {
        Collection<? extends Player> onlinePlayers;
        for (int length = (onlinePlayers = (Collection<? extends Player>)this.plugin.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
            final Player p = (Player)onlinePlayers.toArray()[i];
            if (p.getName().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    private void addArena(final int type, final Location b, final Location r) {
        final Location m = new Location(b.getWorld(), (b.getX() + r.getX()) / 2.0, (b.getY() + r.getY()) / 2.0 + 2.0, (b.getZ() + r.getZ()) / 2.0);
        if (type == 2) {
            this.matches2v2 = this.extendArray(this.matches2v2, new Match(this, this.plugin, 2, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length));
        }
        else if (type == 3) {
            this.matches3v3 = this.extendArray(this.matches3v3, new Match(this, this.plugin, 3, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length));
        }
        else {
            this.matches4v4 = this.extendArray(this.matches4v4, new Match(this, this.plugin, 4, b, r, m, this.matches1v1.length + this.matches2v2.length + this.matches3v3.length + this.matches4v4.length));
        }
    }

    private void loadArenas(final FileConfiguration cfg) {
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
    }

    private void checkStats(final String uuid, final Player asker) {
        uuid.equals(asker.getUniqueId().toString());
        if (this.matches.has(uuid)) {
            final int m = this.matches.get(uuid);
            final int w = this.wins.get(uuid);
            final int t = this.ties.get(uuid);
            final int s = this.bestWinStreak.get(uuid);
            final int l = m - w - t;
            double mw = m;
            if (w > 0) {
                mw = 100 * m / w / 100.0;
            }
            final int g = this.goals.get(uuid);
            double gm = 0.0;
            if (m > 0) {
                gm = 100 * g / m / 100.0;
            }
            final double multiplier = 1.0 - Math.pow(0.9, m);
            double goalBonus = 0.5;
            if (m > 0) {
                goalBonus = 1.0 - multiplier * Math.pow(0.2, g / m) - 0.5 / Math.pow(1.1111111111111112, m);
            }
            double addition = 0.0;
            if (m > 0 && w + t > 0) {
                addition = 8.0 * (1.0 / (100 * m / (w + 0.5 * t) / 100.0)) - 4.0;
            }
            else if (m > 0) {
                addition = -4.0;
            }
            final double skillLevel = (int)(100.0 * (5.0 + goalBonus + addition * multiplier)) / 100.0;
            final int rank = (int)(skillLevel * 2.0 - 0.5);
            String rang = null;
            switch (rank) {
                case 1: {
                    rang = "Noob";
                    break;
                }
                case 2: {
                    rang = "Loser";
                    break;
                }
                case 3: {
                    rang = "Baby";
                    break;
                }
                case 4: {
                    rang = "Pupil";
                    break;
                }
                case 5: {
                    rang = "Bad";
                    break;
                }
                case 6: {
                    rang = "Sadface";
                    break;
                }
                case 7: {
                    rang = "Meh";
                    break;
                }
                case 8: {
                    rang = "Player";
                    break;
                }
                case 9: {
                    rang = "Ok";
                    break;
                }
                case 10: {
                    rang = "Average";
                    break;
                }
                case 11: {
                    rang = "Well";
                    break;
                }
                case 12: {
                    rang = "Good";
                    break;
                }
                case 13: {
                    rang = "King";
                    break;
                }
                case 14: {
                    rang = "Superb";
                    break;
                }
                case 15: {
                    rang = "Pro";
                    break;
                }
                case 16: {
                    rang = "Maradona";
                    break;
                }
                case 17: {
                    rang = "Superman";
                    break;
                }
                case 18: {
                    rang = "God";
                    break;
                }
                case 19: {
                    rang = "Hacker";
                    break;
                }
            }
            asker.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("stats")));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("stats1") + m));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("stats2") + w + messagesConfig.vuci().getString("stats3") + l + messagesConfig.vuci().getString("stats4") + t + messagesConfig.vuci().getString("stats5")));
            if (w > 0) {
                asker.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("stats6") + mw + messagesConfig.vuci().getString("stats7")));
            }
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("stats8") + s + messagesConfig.vuci().getString("stats9")));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("stats10") + g + messagesConfig.vuci().getString("stats11")));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("stats12") + gm + messagesConfig.vuci().getString("stats13")));
            asker.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("stats14") + skillLevel + messagesConfig.vuci().getString("stats15") + rang + messagesConfig.vuci().getString("stats16")));
        }
        else {
            asker.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("statsNoMatchs")));
        }
    }

    private void updateHighscores(final Player p) {
        if (this.highscores == null) {
            this.highscores = new Highscores(this, this.plugin, p);
        }
        else if (this.highscores.needsUpdate()) {
            this.highscores.update(p);
        }
        else if (this.highscores.isUpdating) {
            this.highscores.addWaitingPlayer(p);
        }
        else {
            this.highscores.showHighscores(p);
        }
    }

    private void update() {
        for (int i = 0; i < this.matches2v2.length; ++i) {
            this.matches2v2[i].update();
        }
        for (int i = 0; i < this.matches3v3.length; ++i) {
            this.matches3v3[i].update();
        }
        for (int i = 0; i < this.matches4v4.length; ++i) {
            this.matches4v4[i].update();
        }
        if (this.leftMatches.length > 0 && System.currentTimeMillis() - this.announcementTime > 30000L) {
            final Match m = this.leftMatches[0];
            this.announcementTime = System.currentTimeMillis();
            Collection<? extends Player> onlinePlayers;
            for (int length = (onlinePlayers = (Collection<? extends Player>)this.plugin.getServer().getOnlinePlayers()).size(), j = 0; j < length; ++j) {
                final Player p = (Player)onlinePlayers.toArray()[j];
                if (!this.playingPlayers.contains(p.getName()) && !this.waitingPlayers.containsKey(p.getName())) {
                    if (m.time.getScore() < 0) {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("playerLeft") + m.type + "v" + m.type + messagesConfig.vuci().getString("playerLeft1")));
                    }
                    else {
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("playerLeft") + m.type + "v" + m.type + messagesConfig.vuci().getString("playerLeft1") + messagesConfig.vuci().getString("playerLeft2") + m.time.getScore() + messagesConfig.vuci().getString("playerLeft3")));
                    }
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', messagesConfig.vuci().getString("tkp")));
                }
            }
        }
    }

    private boolean setupEconomy() {
        final RegisteredServiceProvider<Economy> economyProvider = (RegisteredServiceProvider<Economy>)this.plugin.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (economyProvider != null) {
            this.economy = (Economy)economyProvider.getProvider();
        }
        return this.economy != null;
    }
}
