package io.github.bedwarsrel.BedwarsRel.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.inventivetalent.nicknamer.api.NickNamerAPI;
import org.inventivetalent.nicknamer.api.event.refresh.PlayerRefreshEvent;

import io.github.bedwarsrel.BedwarsRel.Main;
import io.github.bedwarsrel.BedwarsRel.Game.Game;
import io.github.bedwarsrel.BedwarsRel.Game.Team;
import net.md_5.bungee.api.ChatColor;

public class NickNamerListener extends BaseListener {

  @EventHandler
  public void onPlayerRefresh(PlayerRefreshEvent event) {
    Main.getInstance().getServer().getConsoleSender()
        .sendMessage("Event " + event.getPlayer().getName());
    Player player = event.getPlayer();

    Game game = Main.getInstance().getGameManager().getGameOfPlayer(player);
    if (game == null) {
      return;
    }

    Team team = game.getPlayerTeam(player);
    if (team == null) {
      return;
    }
    
    if (Main.getInstance().getBooleanConfig("overwrite-names", false)) {
      player.setDisplayName(team.getChatColor() + ChatColor.stripColor(team.getPlayerName(player)));
    }

    if (Main.getInstance().getBooleanConfig("overwrite-names", false)
        || NickNamerAPI.getNickManager().isNicked(player.getUniqueId())) {
      player.setPlayerListName(
          team.getChatColor() + ChatColor.stripColor(team.getPlayerName(player)));
    }

    if (Main.getInstance().getBooleanConfig("teamname-on-tab", true)) {
      player.setPlayerListName(team.getChatColor() + team.getName() + ChatColor.WHITE + " | "
          + team.getChatColor() + ChatColor.stripColor(team.getPlayerDisplayName(player)));
    }

  }

}
