package fr.xam74er1.text2schem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {
    static String IP = "";

    static void sendErrorMessage(Player p , String message){
        p.sendMessage(ChatColor.RED+"[ERROR]"+message);
    }

}
