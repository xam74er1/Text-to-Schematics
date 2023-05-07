package fr.xam74er1.text2schem;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetURLCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            // This command can only be executed by a player
            return true;
        }

        Player player = (Player) sender;

        if(! player.isOp()){
            Utils.sendErrorMessage(player,"need to be operator");
            return false;
        }

        if (args.length != 1) {
            // The command was not executed with the correct number of arguments
            player.sendMessage(ChatColor.RED + "Usage: /seturl <url>");
            return true;
        }

        String url = args[0].replace("http://", "").replace("https://", "");

        if (!url.contains("gradio.live")) {
            // The URL does not contain "gradio.live"
            player.sendMessage(ChatColor.RED + "The URL must contain 'gradio.live'");
            return true;
        }

        // Store the URL somewhere (e.g. a config file or database)
        // ...
        Utils.IP = url;

        player.sendMessage(ChatColor.GREEN + "URL set to: " + url);
        return true;
    }

}

