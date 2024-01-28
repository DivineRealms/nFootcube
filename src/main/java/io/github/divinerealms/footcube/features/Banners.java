package io.github.divinerealms.footcube.features;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;

public class Banners {
  public void Serbia(final Inventory inv, final int slot, final String name, final List lore) {
    ItemStack i = new ItemStack(Material.BANNER, 1);
    BannerMeta m = (BannerMeta)i.getItemMeta();

    m.setBaseColor(DyeColor.RED);

    List<Pattern> patterns = new ArrayList<>();

    patterns.add(new Pattern(DyeColor.YELLOW, PatternType.BRICKS));
    patterns.add(new Pattern(DyeColor.WHITE, PatternType.BRICKS));
    patterns.add(new Pattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM));
    patterns.add(new Pattern(DyeColor.BLUE, PatternType.TRIANGLE_TOP));
    patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT));
    patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
    m.setPatterns(patterns);

    m.setDisplayName(name);
    m.setLore(lore);

    i.setItemMeta(m);

    inv.setItem(slot, i);
  }

  public void Spain(final Inventory inv, final int slot, final String name, final List lore) {
    ItemStack i = new ItemStack(Material.BANNER, 1);
    BannerMeta m = (BannerMeta)i.getItemMeta();

    m.setBaseColor(DyeColor.YELLOW);

    List<Pattern> patterns = new ArrayList<>();

    patterns.add(new Pattern(DyeColor.BROWN, PatternType.STRIPE_MIDDLE));
    patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_BOTTOM));
    patterns.add(new Pattern(DyeColor.YELLOW, PatternType.TRIANGLE_TOP));
    patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT));
    patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_RIGHT));
    m.setPatterns(patterns);

    m.setDisplayName(name);
    m.setLore(lore);

    i.setItemMeta(m);

    inv.setItem(slot, i);
  }
}