package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.utils.Manager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class Controller implements Listener {
  private final double chargeLimit;
  private final double regularKickLimit;
  private final double chargedKickLimit;
  private final boolean isCubeEffectEnabled;
  private final Sound soundMove;
  private final Sound soundKick;
  private final EntityEffect cubeEffect;
  private final PotionEffect potionEffect = new PotionEffect(PotionEffectType.JUMP, 10, -3, true);
  private final double kickPower;
  private final boolean isDebugEnabled;
  private final Manager manager;
  private final HashMap<UUID, Vector> velocities = new HashMap<>();
  private final HashMap<UUID, Long> kicked = new HashMap<>();
  private final HashMap<UUID, Double> charges = new HashMap<>();
  private final Map<UUID, Deque<Location>> lastLocations = new HashMap<>();
  public HashSet<Slime> cubes = new HashSet<>();

  public Controller(final Manager manager) {
    this.manager = manager;
    this.kickPower = this.manager.getPlugin().getConfig().getDouble("cube.kick-power");
    this.chargedKickLimit = this.manager.getPlugin().getConfig().getDouble("cube.power-limit.charged-kick");
    this.regularKickLimit = this.manager.getPlugin().getConfig().getDouble("cube.power-limit.regular-kick");
    this.chargeLimit = this.manager.getPlugin().getConfig().getDouble("cube.power-limit.total-kick-power");
    this.isCubeEffectEnabled = this.manager.getPlugin().getConfig().getBoolean("cube.effect.enabled");
    this.soundMove = Sound.valueOf(this.manager.getPlugin().getConfig().getString("cube.sounds.move"));
    this.soundKick = Sound.valueOf(this.manager.getPlugin().getConfig().getString("cube.sounds.kick"));
    this.cubeEffect = EntityEffect.valueOf(this.manager.getPlugin().getConfig().getString("cube.effect.type"));
    this.isDebugEnabled = this.manager.getPlugin().getConfig().getBoolean("debug.ball-hits");
    this.removeCubes();
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    this.lastLocations.remove(event.getPlayer().getUniqueId());
    this.charges.remove(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onUnloadChunk(final ChunkUnloadEvent event) {
    final Entity[] entities;
    for (int length = (entities = event.getChunk().getEntities()).length, i = 0; i < length; ++i) {
      final Entity entity = entities[i];
      if (!(entity instanceof Slime)) return;
      if (!this.cubes.contains(entity)) return;

      this.cubes.remove((Slime) entity);
      entity.remove();
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onDamage(final EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Slime) && !this.cubes.contains((Slime) event.getEntity())) return;
    event.setCancelled(true);
  }

  @EventHandler
  public void onRightClick(final PlayerInteractEntityEvent event) {
    if (!(event.getRightClicked() instanceof Slime)) return;
    if (!this.cubes.contains((Slime) event.getRightClicked())) return;
    if (this.kicked.containsKey(event.getPlayer().getUniqueId())) return;
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

    final Slime cube = (Slime) event.getRightClicked();
    cube.setVelocity(cube.getVelocity().add(new Vector(0, 0.7, 0)));
    cube.getWorld().playSound(cube.getLocation(), this.soundMove, 0.75F, 1F);
    this.kicked.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    //TODO this.organization.ballTouch(event.getPlayer());
  }

  @EventHandler
  public void onSneak(final PlayerToggleSneakEvent event) {
    final Player player = event.getPlayer();
    if (event.isSneaking()) this.charges.put(player.getUniqueId(), 0D);
    else {
      player.setExp(0F);
      this.charges.remove(player.getUniqueId());
    }
  }

  private double format(Double value) {
    double formattedValue = (int) ((value * 100) + 0.5);
    return formattedValue / 100;
  }

  @EventHandler
  public void onMove(final PlayerMoveEvent event) {
    final UUID playerID = event.getPlayer().getUniqueId();
    final Deque<Location> locations = this.lastLocations.computeIfAbsent(playerID, key -> new ArrayDeque<>());
    if (locations.size() == 2) locations.poll();
    locations.add(event.getTo());
  }

  public Vector getLastMoveVector(final UUID playerID) {
    final Vector defaultLocation = new Vector(0, 0, 0);
    final Deque<Location> locations = this.lastLocations.get(playerID);
    if (locations == null) return defaultLocation;
    final Location last = locations.poll();
    final Location secondLast = locations.poll();
    if (last == null | secondLast == null) return defaultLocation;
    final Vector vector = last.toVector().subtract(secondLast.toVector());
    locations.add(last);
    locations.add(secondLast);
    return vector;
  }

  @EventHandler
  public void onSlamSlime(final EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Slime)) return;
    if (!this.cubes.contains((Slime) event.getEntity())) return;
    if (!(event.getDamager() instanceof Player)) return;
    if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

    final Slime cube = (Slime) event.getEntity();
    final Player player = (Player) event.getDamager();

    if (player.getGameMode() != GameMode.SURVIVAL) {
      if (!player.hasPermission("nfootcube.clearcube")) event.setCancelled(true);
      else {
        // TODO: disable removal in matches
        this.cubes.remove(cube);
        cube.remove();
        this.manager.getMessage().send(player, "CUBE_CLEARED");
      }
      return;
    }

    String message = "&6[&eDebug&6] &b" + player.getName();

    if (this.manager.getCooldown().isCubeKickCooldownEnabled()) {
      final long timeLeft = this.manager.getCooldown().getTimeleftMillis(player.getUniqueId(), this.manager.getCooldown().getCubeKickCooldown());
      if (timeLeft > 0) {
        event.setCancelled(true);
        return;
      }
    }

    double charge = 0;
    double power = 0.4;

    if (this.charges.containsKey(player.getUniqueId())) charge = this.charges.get(player.getUniqueId()) * chargeLimit;
    power += this.getLastMoveVector(player.getUniqueId()).length() * 2 + 0.4;

    double total = charge * this.kickPower;
    message += "&f made a";

    if (charge != 0) {
      if (power >= this.chargedKickLimit) {
        total *= this.chargedKickLimit;
        message += " &acharged";
      } else {
        total *= power;
        message += "n unlimited &acharged";
      }
    } else {
      if (power >= this.regularKickLimit) {
        total = this.regularKickLimit;
        message += " &aregular";
      } else {
        total = power;
        message += "n unlimited &aregular";
      }
    }

    cube.setVelocity(cube.getVelocity().add(player.getLocation().getDirection().normalize().multiply(total).setY(0.3)));
    cube.getWorld().playSound(cube.getLocation(), this.soundKick, 0.75F, 1F);
    //TODO: this.organization.ballTouch(player);

    this.manager.getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
    //message = PlaceholderAPI.setPlaceholders(player, message);
    if (this.isDebugEnabled)
      this.manager.getPlugin().getServer().broadcast(ChatColor.translateAlternateColorCodes('&', message + "&f kick &7[" + format(power) + "PW * " + format(charge) + "CH = " + format(total) + "KP]"), "nfootcube.debug");

    event.setCancelled(true);
  }

  public void spawnCube(final Location location) {
    final Slime cube = (Slime) location.getWorld().spawnEntity(location, EntityType.SLIME);
    cube.setRemoveWhenFarAway(false);
    cube.setSize(1);
    this.cubes.add(cube);
  }

  public Location getDistance(final Location firstLocation, final Location secondLocation) {
    firstLocation.add(0, -1, 0);
    firstLocation.subtract(secondLocation).add(0, -1.5, 0);
    return firstLocation;
  }

  @EventHandler
  public void onFoodLevelChange(final FoodLevelChangeEvent event) {
    event.setCancelled(true);
  }

  public void update() {
    if (this.kicked.size() > 0)
      this.kicked.entrySet().removeIf(entry -> System.currentTimeMillis() > this.kicked.get(entry.getKey()) + 1000L);

    for (UUID uuid : this.charges.keySet()) {
      final Player player = this.manager.getPlugin().getServer().getPlayer(uuid);
      final double charge = this.charges.get(uuid);
      final double nextCharge = 1 - (1 - charge) * 0.925;
      this.charges.put(uuid, nextCharge);
      player.setExp((float) nextCharge);
    }

    final Iterator<Slime> cubesIterator = this.cubes.iterator();

    while (cubesIterator.hasNext()) {
      final Slime cube = cubesIterator.next();
      final UUID id = cube.getUniqueId();
      Vector oldV = cube.getVelocity();
      if (this.velocities.containsKey(id)) oldV = this.velocities.get(id);
      if (cube.isDead()) cubesIterator.remove();

      boolean sound = false;
      boolean kicked = false;
      Vector newV = cube.getVelocity();

      final Collection<? extends Player> onlinePlayers = this.manager.getPlugin().getServer().getOnlinePlayers();

      for (final Player player : onlinePlayers) {
        if (player.getGameMode() == GameMode.SURVIVAL) {
//          double delta = cube.getLocation().distance(player.getLocation());
          final Location distance = this.getDistance(cube.getLocation(), player.getLocation());
          if (distance.getY() < 0.0) distance.add(0, 2.5, 0);
          final double delta = distance.length();
          if (delta < 1.2) {
            if (delta < 0.8 && newV.length() > 0.5) newV.multiply(0.5 / newV.length());
            double power = this.getLastMoveVector(player.getUniqueId()).length() / 3 + oldV.length() / 6;
            newV.add(player.getLocation().getDirection().setY(0).normalize().multiply(power));
            //TODO: this.organization.ballTouch(p3);
            kicked = true;
            if (power > 0.15) sound = true;
          }
        }
      }

      if (newV.getX() == 0) {
        newV.setX(-oldV.getX() * 0.8);
        if (Math.abs(oldV.getX()) > 0.3) sound = true;
      } else if (!kicked && Math.abs(oldV.getX() - newV.getX()) < 0.1) newV.setX(oldV.getX() * 0.98);

      if (newV.getZ() == 0) {
        newV.setZ(-oldV.getZ() * 0.8D);
        if (Math.abs(oldV.getZ()) > 0.3) sound = true;
      } else if (!kicked && Math.abs(oldV.getZ() - newV.getZ()) < 0.1) newV.setZ(oldV.getZ() * 0.98);

      if (newV.getY() < 0 && oldV.getY() < 0 && oldV.getY() < newV.getY() - 0.05) {
        newV.setY(-oldV.getY() * 0.8);
        if (Math.abs(oldV.getY()) > 0.3) sound = true;
      }

      if (sound) cube.getWorld().playSound(cube.getLocation(), this.soundMove, 0.5F, 1F);

      for (final Player player : onlinePlayers) {
        double delta2 = this.getDistance(cube.getLocation(), player.getLocation()).length();
        if (delta2 < newV.length() * 1.3) {
          final Vector loc = cube.getLocation().toVector();
          final Vector nextLoc = new Vector(loc.getX(), loc.getY(), loc.getZ()).add(newV);
          boolean rightDirection = true;
          final Vector pDir = new Vector(player.getLocation().getX() - loc.getX(), 0, player.getLocation().getZ() - loc.getZ());
          final Vector cDir = new Vector(newV.getX(), 0, newV.getZ()).normalize();
          int px = 1, pz = 1, cx = 1, cz = 1;
          if (pDir.getX() < 0) px = -1;
          if (pDir.getZ() < 0) pz = -1;
          if (cDir.getX() < 0) cx = -1;
          if (cDir.getZ() < 0) cz = -1;
          if ((px != cx && pz != cz) || ((px != cx || pz != cz) && (cx * pDir.getX() <= cx * cz * px * cDir.getZ() || cz * pDir.getZ() <= cz * cx * pz * cDir.getX())))
            rightDirection = false;
          if (rightDirection && loc.getY() < player.getLocation().getY() + 2 && loc.getY() > player.getLocation().getY() - 1 && nextLoc.getY() < player.getLocation().getY() + 2 && nextLoc.getY() > player.getLocation().getY() - 1) {
            double a = newV.getZ() / newV.getX();
            double b = loc.getZ() - a * loc.getX();
            double c = player.getLocation().getX();
            double d = player.getLocation().getZ();
            final double D = Math.abs(a * c - d + b) / Math.sqrt(a * a + 1);
            if (D < 0.8D) newV.multiply(delta2 / newV.length());
          }
        }
      }

      cube.setVelocity(newV);
      cube.addPotionEffect(this.potionEffect);

      if (this.isCubeEffectEnabled) cube.playEffect(this.cubeEffect);

      this.velocities.put(id, newV);
    }
  }

  public void removeCubes() {
    final List<Entity> entities = this.manager.getPlugin().getServer().getWorlds().get(0).getEntities();
    for (final Entity entity : entities) if (entity instanceof Slime) entity.remove();
  }
}