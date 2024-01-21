package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.features.CustomEntityFirework;
import io.github.divinerealms.footcube.features.ImageParticles;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.color.RegularColor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GoalExplosion {

  public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {
    // Gravity of a potion
    double gravity = 0.115;

    // Block locations
    int endGain = to.getBlockY() - from.getBlockY();
    double horizDist = Math.sqrt(distanceSquared(from, to));

    // Height gain
    double maxGain = Math.max(heightGain, (endGain + heightGain));

    // Solve quadratic equation for velocity
    double a = -horizDist * horizDist / (4 * maxGain);
    double c = -endGain;

    double slope = -horizDist / (2 * a) - Math.sqrt(horizDist * horizDist - 4 * a * c) / (2 * a);

    // Vertical velocity
    double vy = Math.sqrt(maxGain * gravity);

    // Horizontal velocity
    double vh = vy / slope;

    // Calculate horizontal direction
    int dx = to.getBlockX() - from.getBlockX();
    int dz = to.getBlockZ() - from.getBlockZ();
    double mag = Math.sqrt(dx * dx + dz * dz);
    double dirx = dx / mag;
    double dirz = dz / mag;

    // Horizontal velocity components
    double vx = vh * dirx;
    double vz = vh * dirz;

    return new Vector(vx, vy, vz);
  }

  private static double distanceSquared(Vector from, Vector to) {
    double dx = to.getBlockX() - from.getBlockX();
    double dz = to.getBlockZ() - from.getBlockZ();

    return dx * dx + dz * dz;
  }

  private Vector rotateAroundAxisY(Vector v, double angle) {
    double x, z, cos, sin;
    cos = Math.cos(angle);
    sin = Math.sin(angle);
    x = v.getX() * cos + v.getZ() * sin;
    z = v.getX() * -sin + v.getZ() * cos;
    return v.setX(x).setZ(z);
  }

  public void init(Location location, String exp, int type, Footcube pl) throws IOException {
    if (exp == null)
      exp = "";
    switch (exp) {
      case "Default": {
        regular(location, type, pl);
        break;
      }
      case "Helix": {
        helix(location);
        break;
      }
      case "Meteor": {
        meteor(location, pl);
        break;
      }
      case "Poo": {
        poo(location, pl);
        break;
      }
      case "Serbia": {
        serbia(location, pl);
        break;
      }
      case "Spain": {
        spain(location, pl);
        break;
      }
      case "kok": {
        kok(location, pl);
        break;
      }
      case "skl": {
        skl(location, pl);
        break;
      }
      case "kel": {
        kel(location, pl);
        break;
      }
      case "fls": {
        fls(location, pl);
        break;
      }
      case "fks": {
        fks(location, pl);
        break;
      }
      case "fkk": {
        fkk(location, pl);
        break;
      }
      case "mku": {
        mku(location, pl);
        break;
      }
      case "spa": {
        spa(location, pl);
        break;
      }
      default: {
        break;
      }
    }
  }

  public void renderImage(File file, Location location, Footcube pl, int w, int h) {
    double yaw = location.getYaw();
    if (yaw > 45 && yaw < 135)
      location.setZ(location.getZ() - 3);
    else if (yaw > 135 && yaw < 225)
      location.setX(location.getX() + 3);
    else if (yaw > 235 && yaw < 325)
      location.setZ(location.getZ() + 3);
    else
      location.setX(location.getX() - 3);

    location.setYaw(location.getYaw() + 180f);
    final Location loc = location.add(0, 10, 0);
    BufferedImage image = null;
    try {
      image = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }

    ImageParticles particles = new ImageParticles(image, 1);
    particles.setAnchor(w, h);
    particles.setDisplayRatio(0.1);
    new BukkitRunnable() {
      int v = 0;

      @Override
      public void run() {
        Map<Location, Color> particle = particles.getParticles(loc);
        for (Location spot : particle.keySet()) {
          new ParticleBuilder(ParticleEffect.REDSTONE, spot)
              .setParticleData(new RegularColor(particle.get(spot).getRed(), particle.get(spot).getGreen(), particle.get(spot).getBlue()))
              .display();

        }
        v++;
        if (v == 20)
          this.cancel();
      }
    }.runTaskTimerAsynchronously(pl, 1, 5);
  }

  public void meteor(Location loc, Footcube pl) {
    loc.setY(loc.getY() + 10);
    FallingBlock fallingBlock = loc.getWorld().spawnFallingBlock(loc, Material.OBSIDIAN, (byte) 0);
    fallingBlock.setVelocity(new Vector(0, 0, 0));
    fallingBlock.setDropItem(false);
    loc.setY(loc.getY() - 11);
    ArrayList<FallingBlock> fbs = new ArrayList<>();
    new BukkitRunnable() {
      @Override
      public void run() {
        new BukkitRunnable() {
          double t = Math.PI / 4;

          public void run() {
            t = t + 0.1 * Math.PI;
            for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 32) {
              double x = t * Math.sin(theta);
              double y = 2 * Math.exp(-0.1 * t) * Math.cos(t) + 1.5;
              double z = t * Math.cos(theta);
              loc.add(x, y, z);
              ParticleEffect.BLOCK_CRACK.display(loc);
              FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, Material.DIRT, (byte) 0);
              fb.setDropItem(false);
              fbs.add(fb);
              loc.subtract(x, y, z);
            }
            if (t > 20) {
              this.cancel();
            }
          }

        }.runTaskTimer(pl, 0, 1);
      }
    }.runTaskLater(pl, 25);
    new BukkitRunnable() {
      @Override
      public void run() {
        for (FallingBlock fb : fbs)
          fb.remove();
      }
    }.runTaskLater(pl, 100);
  }

  public void poo(Location location, Footcube pl) throws IOException {
    File file = new File(pl.getDataFolder(), "images/poo.png");
    renderImage(file, location, pl, 64, 61);
  }

  public void serbia(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/serbia.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void spain(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/spain.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void kok(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/kok.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void skl(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/skl.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void kel(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/kel.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void fls(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/fls.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void fks(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/fks.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void fkk(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/fkk.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void mku(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/mku.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void spa(Location location, Footcube pl) {
    File file = new File(pl.getDataFolder(), "images/spa.png");
    renderImage(file, location, pl, 64, 43);
  }

  public void helix(Location location) {
    int radius = 2;
    location.setY(location.getY() - 5);
    for (double y = 0; y <= 50; y += 0.05) {
      double x = radius * Math.cos(y);
      double z = radius * Math.sin(y);
      PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, true, (float) (location.getX() + x), (float) (location.getY() + y), (float) (location.getZ() + z), 0, 0, 0, 0, 1);
      for (Player online : Bukkit.getOnlinePlayers()) {
        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
      }
    }
  }

  public void regular(Location location, int type, Footcube pl) {
    FireworkEffect.Builder builder = FireworkEffect.builder();
    Location _loc = location.clone();
    FireworkEffect effect;
    if (type == 0)
      effect = builder.flicker(false).trail(false).with(FireworkEffect.Type.BALL).withColor(Color.BLUE).build();
    else
      effect = builder.flicker(false).trail(false).with(FireworkEffect.Type.BALL).withColor(Color.RED).build();

    new BukkitRunnable() {
      @Override
      public void run() {
        new CustomEntityFirework(effect, _loc);
        _loc.setY(_loc.getY() + 2);
      }
    }.runTaskLater(pl, 4);
    new BukkitRunnable() {
      @Override
      public void run() {
        new CustomEntityFirework(effect, _loc);
        _loc.setY(_loc.getY() + 2);
      }
    }.runTaskLater(pl, 6);
    new BukkitRunnable() {
      @Override
      public void run() {
        new CustomEntityFirework(effect, _loc);
      }
    }.runTaskLater(pl, 8);

    new BukkitRunnable() {
      @Override
      public void run() {
        Location loc = location.add(0, 2, 0);
        double angle = 0;
        for (int i = 0; i < 6; i++) {
          FallingBlock fallingBlock;
          if (type == 0)
            fallingBlock = location.getWorld().spawnFallingBlock(loc, Material.WOOL, (byte) 11);
          else
            fallingBlock = location.getWorld().spawnFallingBlock(loc, Material.WOOL, (byte) 14);
          fallingBlock.setVelocity(rotateAroundAxisY(calculateVelocity(loc.toVector(), loc.toVector().add(new Vector(8, 0, 0)), 6), angle));
          fallingBlock.setDropItem(false);
          angle += 45;
        }
      }
    }.runTaskLater(pl, 9);

  }
}
