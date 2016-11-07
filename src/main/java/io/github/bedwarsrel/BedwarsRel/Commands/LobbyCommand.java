package io.github.bedwarsrel.BedwarsRel.Commands;

import io.github.bedwarsrel.BedwarsRel.Game.Game;
import io.github.bedwarsrel.BedwarsRel.Game.GameState;
import io.github.bedwarsrel.BedwarsRel.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by wddqing on 2016/11/7.
 */
public class LobbyCommand extends BaseCommand {

    public LobbyCommand(Main plugin) {
        super(plugin);
    }

    @Override
    public String getCommand() {
        return "lobby";
    }

    @Override
    public String getName() {
        return Main._l("commands.lobby.name");
    }

    @Override
    public String getDescription() {
        return Main._l("commands.lobby.desc");
    }

    @Override
    public String[] getArguments() {
        return new String[]{};
    }

    @Override
    public boolean execute(CommandSender sender, ArrayList<String> args) {

        getPlugin().getLogger().info(ChatColor.BLUE + "run lobby command");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Game game = getPlugin().getGameManager().getGameOfPlayer(player);
            if (game.getState() == GameState.RUNNING && !game.isStopping()) {
                game.playerLeave(player, false);
            }
        }

        return true;
    }

    @Override
    public String getPermission() {
        return "base";
    }
}
