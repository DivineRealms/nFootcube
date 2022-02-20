package io.github.divinerealms.footcube.statistics;

import io.github.divinerealms.footcube.Footcube;
import org.bukkit.Location;

public class Match {
  public int matchID;
  public int type;
  public int phase;
  private int countdown;
  private int tickToSec;
  private int teams;
  private int count;
  private long startTime;
  private Location blue;
  private Location red;
  private Location center;
  private Footcube plugin;

  public Match(final Footcube plugin) {
    this.plugin = plugin;
  }
}
