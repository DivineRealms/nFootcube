package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.Footcube;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class Controller implements Listener {
  private final Footcube plugin;
  public HashSet<Slime> cubes;
  private final HashMap<UUID, Vector> velocities;
  private final HashMap<UUID, Long> kicked;
  private final HashMap<UUID, Double> speed;
  private final HashMap<UUID, Double> charges;
  private final Sound sound;
  public final ArrayList<Player> immune;
  public final HashMap<Player, BukkitTask> immuneMap;

  public Controller(Footcube plugin) {
    this.plugin = plugin;
    this.cubes = new HashSet<>();
    this.velocities = new HashMap<>();
    this.kicked = new HashMap<>();
    this.speed = new HashMap<>();
    this.charges = new HashMap<>();
    this.sound = Sound.SLIME_WALK;
    this.immune = new ArrayList<>();
    this.immuneMap = new HashMap<>();
    this.removeCubes();
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    Location to = event.getTo();
    Location from = event.getFrom();
    double x = Math.abs(to.getX() - from.getX());
    double y = Math.abs(to.getY() - from.getY()) / 2.0D;
    double z = Math.abs(to.getZ() - from.getZ());
    this.speed.put(event.getPlayer().getUniqueId(), Math.sqrt(x * x + y * y + z * z));
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    this.speed.remove(event.getPlayer().getUniqueId());
    this.charges.remove(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onUnloadChunk(ChunkUnloadEvent event) {
    Entity[] entities;
    for (int length = (entities = event.getChunk().getEntities()).length, i = 0; i < length; ++i) {
      Entity entity = entities[i];
      if (entity instanceof Slime && this.cubes.contains(entity)) {
        this.cubes.remove((Slime) entity);
        entity.remove();
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onDamage(EntityDamageEvent event) {
    if (event.getEntity() instanceof Slime && this.cubes.contains((Slime) event.getEntity())) {
      if (event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);

      event.setDamage(0.0);
    }
  }

  @EventHandler
  public void onRightClick(PlayerInteractEntityEvent event) {
    if (event.getRightClicked() instanceof Slime && this.cubes.contains((Slime) event.getRightClicked()) && !this.kicked.containsKey(event.getPlayer().getUniqueId())) {
      Slime cube = (Slime) event.getRightClicked();
      cube.setVelocity(cube.getVelocity().add(new Vector(0.0D, 0.7D, 0.0D)));
      cube.getWorld().playSound(cube.getLocation(), this.sound, 1.0F, 1.0F);
      this.kicked.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
      //TODO this.organization.ballTouch(event.getPlayer());
    }
  }

  @EventHandler
  public void onSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();
    if (event.isSneaking()) this.charges.put(player.getUniqueId(), 0.0D);
    else {
      player.setExp(0.0F);
      this.charges.remove(player.getUniqueId());
    }
  }

  @EventHandler
  public void onSlamSlime(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof Slime && this.cubes.contains((Slime) event.getEntity()) && event.getDamager() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
      Slime cube = (Slime) event.getEntity();
      Player player = (Player) event.getDamager();
      double charge = 1.0D;

      if (this.charges.containsKey(player.getUniqueId())) charge += this.charges.get(player.getUniqueId()) * 7.0D;

      double power = this.speed.get(player.getUniqueId()) * 2.0D + 0.4D;
      Vector kick = player.getLocation().getDirection().normalize().multiply(power * charge * (1.0D + 0.05D * 0.025D)).setY(0.3D);

      cube.setVelocity(cube.getVelocity().add(kick));
      cube.getWorld().playSound(cube.getLocation(), this.sound, 1.0F, 1.0F);
      //this.organization.ballTouch(player);
      event.setCancelled(true);
    }
  }

  public void spawnCube(Location location) {
    Slime cube = (Slime) location.getWorld().spawnEntity(location, EntityType.SLIME);
    cube.setRemoveWhenFarAway(false);
    cube.setSize(1);
    this.cubes.add(cube);
  }

  public double getDistance(Location locationA, Location locationB) {
    locationA.add(0.0D, -1.0D, 0.0D);
    double dx = Math.abs(locationA.getX() - locationB.getX());
    double dy = Math.abs(locationA.getY() - locationB.getY() - 0.25D) - 1.25D;
    if (dy < 0.0) dy = 0.0;
    double dz = Math.abs(locationA.getZ() - locationB.getZ());
    return Math.sqrt(dx * dx + dy * dy + dz * dz);
  }

  public void update() {
    if (this.kicked.size() > 0)
      this.kicked.entrySet().removeIf(entry -> System.currentTimeMillis() > this.kicked.get(entry.getKey()) + 1000L);

    Collection<? extends Player> onlinePlayers;
    if (this.plugin.getServer().getOnlinePlayers().size() != 0) {
      for (int length = (onlinePlayers = this.plugin.getServer().getOnlinePlayers()).size(), i = 0; i < length; ++i) {
        Player player = (Player) onlinePlayers.toArray()[i];
        player.setHealth(20.0);
        player.setSaturation(100.0F);
        player.setExhaustion(20.0F);
      }
    }

    for (UUID uuid : this.charges.keySet()) {
      Player player = this.plugin.getServer().getPlayer(uuid);
      double charge = this.charges.get(uuid);
      //final double nextCharge = 1.0 - (1.0 - charge) * (0.95 - this.organization.getStoreNumber(p2, 5) * 0.005);
      double nextCharge = 1.0D - (1.0D - charge) * (0.95D - 0.5D * 0.005D);
      this.charges.put(uuid, nextCharge);
      player.setExp((float) nextCharge);
    }

    Iterator<Slime> cubesIterator = this.cubes.iterator();

    while (cubesIterator.hasNext()) {
      Slime cube = cubesIterator.next();
      //for (Slime cube : this.cubes) {
      UUID id = cube.getUniqueId();
      Vector oldV = cube.getVelocity();
      if (this.velocities.containsKey(id)) oldV = this.velocities.get(id);

      if (!cube.isDead()) {
        boolean sound = false;
        boolean kicked = false;
        Vector newV = cube.getVelocity();

        for (int length2 = (onlinePlayers = this.plugin.getServer().getOnlinePlayers()).size(), j = 0; j < length2; ++j) {
          Player player = (Player) onlinePlayers.toArray()[j];
          if (!this.immune.contains(player) && player.getGameMode() != GameMode.SPECTATOR) {
            double delta = this.getDistance(cube.getLocation(), player.getLocation());
            if (delta < 1.2D) {
              if (delta < 0.8D && newV.length() > 0.5D) newV.multiply(0.5D / newV.length());

              if (this.speed.containsKey(player.getUniqueId())) {
                double power = this.speed.get(player.getUniqueId()) / 3.0D + oldV.length() / 6.0D;
                newV.add(player.getLocation().getDirection().setY(0).normalize().multiply(power));
                //this.organization.ballTouch(p3);
                kicked = true;
                if (power > 0.15D) sound = true;
              }
            }
          }
        }

        if (newV.getX() == 0.0D) {
          newV.setX(-oldV.getX() * 0.8D);
          if (Math.abs(oldV.getX()) > 0.3D) sound = true;
        } else if (!kicked && Math.abs(oldV.getX() - newV.getX()) < 0.1D) newV.setX(oldV.getX() * 0.98D);

        if (newV.getZ() == 0.0D) {
          newV.setZ(-oldV.getZ() * 0.8D);
          if (Math.abs(oldV.getZ()) > 0.3D) sound = true;
        } else if (!kicked && Math.abs(oldV.getZ() - newV.getZ()) < 0.1) newV.setZ(oldV.getZ() * 0.98D);

        if (newV.getY() < 0.0 && oldV.getY() < 0.0D && oldV.getY() < newV.getY() - 0.05D) {
          newV.setY(-oldV.getY() * 0.8D);
          if (Math.abs(oldV.getY()) > 0.3D) sound = true;
        }

        if (sound) cube.getWorld().playSound(cube.getLocation(), this.sound, 1.0F, 1.0F);

        Collection<? extends Player> onlinePlayers3;
        for (int length3 = (onlinePlayers3 = this.plugin.getServer().getOnlinePlayers()).size(), k = 0; k < length3; ++k) {
          Player p4 = (Player) onlinePlayers3.toArray()[k];
          double delta2 = this.getDistance(cube.getLocation(), p4.getLocation());
          if (delta2 < newV.length() * 1.3D) {
            Vector loc = cube.getLocation().toVector();
            Vector nextLoc = new Vector(loc.getX(), loc.getY(), loc.getZ()).add(newV);
            boolean rightDirection = true;
            Vector pDir = new Vector(p4.getLocation().getX() - loc.getX(), 0.0D, p4.getLocation().getZ() - loc.getZ());

            Vector cDir = new Vector(newV.getX(), 0.0D, newV.getZ()).normalize();
            int px = 1;
            if (pDir.getX() < 0.0D) px = -1;
            int pz = 1;
            if (pDir.getZ() < 0.0D) pz = -1;
            int cx = 1;
            if (cDir.getX() < 0.0D) cx = -1;
            int cz = 1;
            if (cDir.getZ() < 0.0D) cz = -1;
            if ((px != cx && pz != cz) || ((px != cx || pz != cz) && (cx * pDir.getX() <= cx * cz * px * cDir.getZ() || cz * pDir.getZ() <= cz * cx * pz * cDir.getX())))
              rightDirection = false;
            if (rightDirection && loc.getY() < p4.getLocation().getY() + 2.0D && loc.getY() > p4.getLocation().getY() - 1.0D && nextLoc.getY() < p4.getLocation().getY() + 2.0D && nextLoc.getY() > p4.getLocation().getY() - 1.0D) {
              double a = newV.getZ() / newV.getX();
              double b = loc.getZ() - a * loc.getX();
              double c = p4.getLocation().getX();
              double d = p4.getLocation().getZ();
              double D = Math.abs(a * c - d + b) / Math.sqrt(a * a + 1.0D);
              if (D < 0.8D) newV.multiply(delta2 / newV.length());
            }
          }
        }

        cube.setMaxHealth(1.0D);
        cube.setHealth(1.0D);
        cube.setVelocity(newV);
        cube.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, -3, true), true);
        cube.playEffect(EntityEffect.HURT);
        this.velocities.put(id, newV);
      } else cubesIterator.remove();
    }
  }

  public void removeCubes() {
    List<Entity> entities = this.plugin.getServer().getWorlds().get(0).getEntities();

    for (Entity entity : entities)
      if (entity instanceof Slime && this.cubes.contains((Slime) entity)) entity.remove();
  }
}
