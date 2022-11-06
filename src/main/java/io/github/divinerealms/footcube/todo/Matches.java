package io.github.divinerealms.footcube.todo;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.UUID;

public class Matches {
  private ItemStack createColoredArmor(final Material material, final Color color) {
    final ItemStack itemStack = new ItemStack(material);
    if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
      final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
      leatherArmorMeta.setColor(color);
      itemStack.setItemMeta(leatherArmorMeta);
    }
    return itemStack;
  }

  public void join(final UUID uuid, final boolean bool) {

  }

  public void leave(final UUID uuid) {

  }

  public void takePlace(final UUID uuid) {

  }

  public void kick(final UUID uuid) {
//    if () {
//
//    } else {
//
//    }
  }
}
