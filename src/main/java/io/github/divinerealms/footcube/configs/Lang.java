package io.github.divinerealms.footcube.configs;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public enum Lang {
  RELOAD_USAGE("reload.usage", "&6▎ &eUpozorenje: obrisaćete sve lopte ovom komandom! Ako se slažete kucajte &6/&efc reload confirm&c."),
  RELOAD_PLUGIN("reload.plugin", "&a▎ &fPlugin uspešno osvežen!"),
  RELOAD_CONFIG("reload.config", "&a▎ &fPoruke plugina uspešno osvežene!"),
  UNKNOWN_COMMAND("unknown-command", "&4▎ &cNepoznata komanda."),
  INSUFFICIENT_PERMISSION("insufficient-permission", "&4▎ &cNemate dozvolu za to ({0})."),
  INGAME_ONLY("ingame-only", "&4▎ &cOva komanda se može koristiti samo u igri."),
  ON_COOLDOWN("on-cooldown", "&4▎ &cProbajte ovu komandu ponovo za {0} sekundi."),
  DEBUG_BALL_HITS("debug.ball-hits", "&8▎ &b{0} &7[&ePW {1} &7* &dCH {2} &7* &cKP {3} &7= &aTOTAL KP {4}&7]"),
  INVALID_TIME("invalid-time", "&4▎ &cArgument za vreme \"&e{0}&c\" nije validan."),
  CUBE_SPAWNED("cube.spawned", "&a▎ &fLopta stvorena."),
  CUBE_CLEARED("cube.cleared", "&a▎ &fLopta obrisana."),
  CUBE_UNABLE("cube.unable", "&4▎ &cNema lopti u blizini."),
  TOO_MANY_CUBES("cube.too-many", "&4▎ &cNe možete stvoriti više od 5 lopti u radiusu od 100 blokova."),
  CREATED_PLAYER_DATA("created-playerdata", "&a▎ &fNapravljen userdata file za igrača &b{0}&f."),
  COMMAND_DISABLER_HELP("command-disabler.help", String.join(System.lineSeparator(),
      "&9&m═══════════════════════════════════&r",
      "&r &r",
      "&b &lFUT CRAFT&7 - &e&oOnesposobljivač komandi.",
      "&r &r",
      "&6 /&efc cd help&7: &fPrikazuje ovu stranicu.",
      "&6 /&efc cd add &2<&akomanda&2>",
      "&6 /&efc cd remove &2<&akomanda&2>",
      "&r &r",
      "&9&m═══════════════════════════════════&r")),
  HELP("help", String.join(System.lineSeparator(),
      "&9&m═══════════════════════════════════&r",
      "&r &r",
      "&f &lFUDBAL&7 - &e&oLista dostupnih komandi.",
      "&r &r",
      "&6 /&efc help&7: &fPrikazuje ovu stranicu.",
      "&6 /&efc reload&7: &fOsvežavanje plugina.",
      "&6 /&ecube&7: &fStvaranje lopte.",
      "&6 /&eclearcube&7: &fBrisanje lopte u radiusu.",
      "&r &r",
      "&9&m═══════════════════════════════════&r"));

  private static FileConfiguration LANG;
  private final String path, def;

  Lang(final String path, final String start) {
    this.path = path;
    this.def = start;
  }

  public static void setFile(final FileConfiguration config) {
    LANG = config;
  }

  public String getDefault() {
    return this.def;
  }

  public String getConfigValue(final String[] args) {
    String value = ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def));

    if (args == null) return value;
    else {
      if (args.length == 0) return value;
      for (int i = 0; i < args.length; i++) value = value.replace("{" + i + "}", args[i]);
      value = ChatColor.translateAlternateColorCodes('&', value);
    }

    return value;
  }
}