package io.github.bedwarsrel.BedwarsRel.Shop.Specials;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.bedwarsrel.BedwarsRel.Main;
import io.github.bedwarsrel.BedwarsRel.SoundMachine;
import io.github.bedwarsrel.BedwarsRel.Game.Game;
import io.github.bedwarsrel.BedwarsRel.Game.Team;

public class Trap extends SpecialItem {

  private List<PotionEffect> effects = null;
  private Game game = null;
  private Team team = null;
  private int maxDuration = 5;
  private boolean playSound = true;
  private Location location = null;

  public Trap() {
    this.effects = new ArrayList<PotionEffect>();
  }

  @Override
  public Material getItemMaterial() {
    return Material.TRIPWIRE;
  }

  @Override
  public Material getActivatedMaterial() {
    return null;
  }

  public void activate(final Player player) {
    try {
      ConfigurationSection section =
          Main.getInstance().getConfig().getConfigurationSection("specials.trap");

      if (section.contains("play-sound")) {
        this.playSound = section.getBoolean("play-sound");
      }

      for (Object effect : section.getList("effects")) {
        effects.add((PotionEffect) effect);

        if (((PotionEffect) effect).getDuration() / 20 > this.maxDuration) {
          this.maxDuration = ((PotionEffect) effect).getDuration() / 20;
        }
      }

      this.game.addRunningTask(new BukkitRunnable() {

        private int counter = 0;

        @Override
        public void run() {
          if (this.counter >= Trap.this.maxDuration) {
            Trap.this.game.removeRunningTask(this);
            this.cancel();
            return;
          }
          this.counter++;
        }
      }.runTaskTimer(Main.getInstance(), 0L, 20L));

      if (effects.size() > 0) {
        for (PotionEffect effect : effects) {
          if (player.hasPotionEffect(effect.getType())) {
            player.removePotionEffect(effect.getType());
          }

          player.addPotionEffect(effect);
        }
      }

      player.playSound(player.getLocation(), SoundMachine.get("FUSE", "ENTITY_TNT_PRIMED"),
          Float.valueOf("1.0"), Float.valueOf("1.0"));

      this.game.broadcast(Main._l("ingame.specials.trap.trapped"),
          new ArrayList<Player>(this.team.getPlayers()));
      if (this.playSound) {
        this.game.broadcastSound(SoundMachine.get("SHEEP_IDLE", "ENTITY_SHEEP_AMBIENT"),
            Float.valueOf("1.0"), Float.valueOf("1.0"), this.team.getPlayers());
      }

      this.game.getRegion().removePlacedUnbreakableBlock(this.location.getBlock());
      this.location.getBlock().setType(Material.AIR);
      this.game.removeSpecialItem(this);
    } catch (Exception ex) {
      Main.getInstance().getBugsnag().notify(ex);
      ex.printStackTrace();
    }
  }

  public void create(Game game, Team team, Location location) {
    this.game = game;
    this.team = team;
    this.location = location;

    this.game.addSpecialItem(this);
  }

  public Game getGame() {
    return this.game;
  }

  public Location getLocation() {
    return this.location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Team getPlacedTeam() {
    return this.team;
  }
}
