package io.github.divinerealms.footcube.configs;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public enum Messages {
  // Enum constants with their default values
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
  JOIN_RED_TEAM("match.joinRedTeam", "&a▎ &fUšli ste u igru, sada ste u &cCrvenom &ftimu."),
  JOIN_BLUE_TEAM("match.joinRedTeam", "&a▎ &fUšli ste u igru, sada ste u &9Plavom &ftimu."),
  MATCH_START("match.matchStart", String.join(System.lineSeparator(),
      "&b▎ &f",
      "&b▎ &fMeč počinje za &e15 sekundi&f. Koristite &2/&atc &fza tim čet.",
      "&b▎ &fImate vremena da se dogovorite oko pozicija. Izaberite GK.",
      "&b▎ &f",
      "&b▎ &cZabranjeno je biti AFK, utakmica može trajati i do &e5 minuta&b.",
      "&b▎ &cNemojte paliti fly, menjati gamemode i uopšte abjuzati.",
      "&b▎ &f"
      )),
  LEAVE_MATCH("match.leave", "&b▎ &fUkoliko želite da napustite igru kucajte &6/&eleave&f."),
  TC_RED("match.tc-red", "&8▎ &c&lTC &8┃ &b{0}&f: &c{1}"),
  TC_BLUE("match.tc-blue", "&8▎ &9&lTC &8┃ &b{0}&f: &b{1}"),
  TEAM_WITH("match.team-with", "&8▎ &fU timu ste sa &b{0}&f."),
  CHANGED_TEAM_WITH("match.change-team-with", "&8▎ &fZamenili ste tim sa &b{0} &fi &b{1}&f."),
  CUSTOM_SCORE_MESSAGE("match.score-message", "&b▎ &f&l{0}&b {1} &fje dao gol!"),
  SCORE_GOAL("match.score-goal", "&b{0} &fje dao gol!"),
  SCORE_INCREDIBLE("match.score-incredible", "&8▎ &b{0} &fje zabio neverovatan gol sa &e{1}m&f!"),
  SCORE_NORMAL("match.score-normal", "&b▎ &b{0} &fje zabio gol sa &e{1}&fm."),
  SCORE_INCREDIBLE_ASSIST("match.score-incredible-assist", "&b▎ &b{0} &fje zabio neverovatan gol uz pomoć &b{1} &fsa &e{2}m&f!"),
  SCORE_NORMAL_ASSIST("match.score-normal-assist", "&b▎ &b{0} &fje zabio gol uz pomoć &b{1} &fsa &e{2}m&f."),
  SCORE_REWARD("match.score-reward", "&8▎ &fDobili ste &a${0} &fzbog postignutog gola!"),
  HATTY_REWARD("match.hatty-reward", "&8▎ &fDobili ste &a${0} &fzbog &6&lHat Tricka&f!"),
  HATTY("match.hatty", "&8▎ &fIgrač &b{0} &fje postigao &6&lHat Trick&f!"),
  ASSIST_REWARD("match.assist-reward", "&8▎ &fDobili ste &a${0} &fzbog uspešnog assista!"),
  WIN_REWARD("match.win-reward", "&a▎ &fDobili ste &a${0} &fjer ste pobedili!"),
  CURRENT_RESULT("match.result", "&8▎ &fRezultat: &cRed &e{0} - {1} &9Blue"),
  CONTINUE_5_SEC("match.continuing", "&b▎ &fMeč se nastavlja za &e5 sekundi&f."),
  MATCH_STARTING("match.starting", "&b▎ &fMeč počinje!"),
  MATCH_CONTINUING("match.starting", "&b▎ &fMeč se nastavlja!"),
  TIMES_UP_RED("match.times-up.red", "&b▎ &fIsteklo je vreme! &cRed &ftim je pobedio. Rezultat: &cRed &e{0} - {1} &9Blue"),
  TIMES_UP_BLUE("match.times-up.blue", "&b▎ &fIsteklo je vreme! &9Blue &ftim je pobedio. Rezultat: &9Blue &e{0} - {1} &cRed"),
  NO_STATS_FOR_THIS("match.no-stats-for-this", "&4▎ &cStatistika vam se ne povećava u ovim mečevima."),
  TIED("match.tied", "&b▎ &fIsteklo je vreme! Nerešeno je."),
  TIED_REWARD("match.tied-reward", "&a▎ &fDobili ste &a${0} &fzbog nerešenog meča."),
  ACTIVATED_EXPLOSION("perks.activated-explosion", "&8▎ &fUspešno ste izabrali &e{0} &fza goal explosion."),
  DEACTIVATED_EXPLOSION("perks-deactivated-explosion", "&8▎ &fIsključili ste goal explosion."),
  REQUEST_DENY("match.request-deny", "&4▎ &b{0} &cje odbio vaš zahtev za tim."),
  REQUEST_DENY_JOIN("match.request-deny-join", "&4▎ &cOdbili ste zahtev za tim ulaskom u meč."),
  TEAMMATE_LEFT("match.teammate-left", "&4▎ &cTvoj saigrač je napustio server."),
  PLAYER_LEFT("match.player-left", "&b▎ &fNeko je napustio &e{0} &fmeč! "),
  PLAYER_LEFT_ACTIVE("match.player-left-active", "&fOstalo je &e{0} sekundi."),
  TAKEPLACE("match.takeplace", "&b▎ &fMožete zauzeti ovo mesto komandom &6/&efc takeplace&f!"),
  DISABLED_COMMAND("match.disabled-command", "&4▎ &cNe možete koristiti tu komandu tokom igre."),
  TC_USAGE("match.tc-usage", "&4▎ &cNedovoljno argumenata. Koristite: &6/&etc poruka&c."),
  NOT_IN_GAME("match.not-in-game", "&4▎ &cNiste u igri."),
  BANNED("match.banned", "&4▎ &cNe možete učestvovati u igri jer ste banovani iz FC."),
  STATS("match.stats", String.join(System.lineSeparator(),
      "&9&m═══════════════════════════════════&r",
      "&r &r",
      "&f &lStatistika igrača {0}",
      "&r &r",
      "&f Mečeva odigrano: &a{1}",
      "&a W&f/&6T&f/&cL&f: &a{2}&f/&6{3}&f/&c{4}",
      "&f Prosek pobeda: &e{6}",
      "&f Niz pobeda: &e{7}",
      "&f Ukupno golova: &e{8}",
      "&f Prosek golova: &e{9}",
      "&f Asistencija: &e{5}",
      "&r &r",
      "&9&m═══════════════════════════════════&r")),
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

  // Constructor for enum constants
  Messages(final String path, final String start) {
    this.path = path;
    this.def = start;
  }

  // Setter for the file configuration
  public static void setFile(final FileConfiguration config) {
    LANG = config;
  }

  // Getter for the default value
  public String getDefault() {
    return this.def;
  }

  // Method to get the configuration value with optional arguments
  public String getMessage(final String[] args) {
    String value = ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def));

    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++)
        value = value.replace("{" + i + "}", args[i]);
      value = ChatColor.translateAlternateColorCodes('&', value);
    }

    return value;
  }
}