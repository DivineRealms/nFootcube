package io.github.divinerealms.footcube.features;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CustomEntityFirework {

  /**
   * Constructs a custom firework entity with the given FireworkEffect at the specified Location.
   *
   * @param fe  The FireworkEffect to apply to the firework.
   * @param loc The Location where the firework should be spawned.
   */
  public CustomEntityFirework(FireworkEffect fe, Location loc) {
    // Spawn a Firework entity at the specified Location
    Firework f = (Firework) loc.getWorld().spawn(loc, Firework.class);

    // Set the FireworkMeta with the provided FireworkEffect
    FireworkMeta fm = f.getFireworkMeta();
    fm.addEffect(fe);
    f.setFireworkMeta(fm);

    try {
      // Reflection to manipulate internal fields of the Firework entity
      Class<?> entityFireworkClass = getClass("net.minecraft.server.", "EntityFireworks");
      Class<?> craftFireworkClass = getClass("org.bukkit.craftbukkit.", "entity.CraftFirework");
      Object firework = craftFireworkClass.cast(f);
      Method handle = firework.getClass().getMethod("getHandle");
      Object entityFirework = handle.invoke(f);
      Field expectedLifespan = entityFireworkClass.getDeclaredField("expectedLifespan");
      Field ticksFlown = entityFireworkClass.getDeclaredField("ticksFlown");

      // Adjust the ticksFlown field to make the firework explode instantly
      ticksFlown.setAccessible(true);
      ticksFlown.setInt(entityFirework, expectedLifespan.getInt(entityFirework) - 1);
      ticksFlown.setAccessible(false);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Gets the Class object for the specified class name with the given prefix.
   *
   * @param prefix          The prefix for the class (e.g., "net.minecraft.server." or "org.bukkit.craftbukkit.").
   * @param nmsClassString  The name of the NMS class.
   * @return                The Class object for the specified NMS class.
   * @throws ClassNotFoundException If the class is not found.
   */
  private Class<?> getClass(String prefix, String nmsClassString) throws ClassNotFoundException {
    String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
    String name = prefix + version + nmsClassString;
    return Class.forName(name);
  }
}
