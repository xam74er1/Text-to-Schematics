package fr.xam74er1.text2schem;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

public class SharpeCommand implements CommandExecutor {

    public void load_schem(String path,Player player){

        File file = new File(path);

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            Clipboard clipboard = reader.read();

            BukkitPlayer actor = BukkitAdapter.adapt(player);
            LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(actor);
            localSession.setClipboard(new ClipboardHolder(clipboard));

            player.sendMessage(ChatColor.GREEN+"Schematics loaded "+ChatColor.DARK_PURPLE+" paste with ,"+ChatColor.AQUA+"  //paste ");

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void manageMessage(JSONObject json,WebsocketClientEndpoint clientEndPoint,Player player,String text) {
        if (json.has("msg")) {
            String msg = json.getString("msg");
            if (msg.equalsIgnoreCase("send_data")) {
                clientEndPoint.sendMessage("{\"fn_index\":0,\"data\":[\""+text+"\"],\"event_data\":null,\"session_hash\":\"" + player.getName() + "\"}");
            } else if (msg.equalsIgnoreCase("process_completed")) {

                try {
                    // Get the "data" array from the "output" object
                    JSONArray data = json.getJSONObject("output").getJSONArray("data");

                    // Get the "orig_name" field from the first element of the "data" array
                    String origName = data.getJSONObject(0).getString("orig_name");
                    String path = data.getJSONObject(0).getString("name");

                    // Print the value of the "orig_name" field
                    System.out.println("Orig name: " + origName);

                    String fileUrl = "https://" + Utils.IP + "/file=" + path;
                    player.sendMessage(ChatColor.GREEN+"\n\nYou can downloqd the schematics at "+ChatColor.AQUA+fileUrl);
                    String outPut = "plugins/WorldEdit/schematics/"+origName;
                    clientEndPoint.downloadFile(fileUrl, outPut);
                    load_schem(outPut,player);

                } catch (Exception e) {
                    System.out.println("Error parsing JSON: " + e.getMessage());
                    Utils.sendErrorMessage(player, "the API server has an internal error");
                }

            } else if (msg.equalsIgnoreCase("estimation")) {
                int queuSize = json.getInt("queue_size");
                double time = json.getDouble("avg_event_process_time");
                System.out.println("prio" + queuSize + " time" + time);
                time = Math.max(time,100);
                player.sendMessage(ChatColor.DARK_PURPLE+"You are "+queuSize+" in the queue , estimated Time "+Math.floor(time)+" s");

            }

        }
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Code to be executed when the "sharpe" command is executed
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String userName = player.getName();
            String text = "";
            for (int i = 0; i < args.length; i++) {
                text += args[i] + " ";
            }
            text = text.strip();


            if (Utils.IP.length() < 3) {
                Utils.sendErrorMessage(player, "The url is not set , use /seturl <url> to set the url" + Utils.IP);
                return false;
            }


            String finalText = text;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        // open websocket
                        final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("wss://" + Utils.IP + "/queue/join"));

                        // add listener

                        clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                            public void handleMessage(String message) {
                                System.out.println(message);
                                JSONObject json = new JSONObject(message);
                                manageMessage(json, clientEndPoint, player, finalText);
                            }
                        });

                        clientEndPoint.sendMessage("{\"fn_index\":0,\"session_hash\":\"" + userName + "\"}");
                        // send message to websocket

                        while (clientEndPoint.getUserSession() != null) {
                            Thread.sleep(1000);
                        }
                        // wait 5 seconds for messages from websocket

                        player.sendMessage(ChatColor.GREEN + "\nModel generated !");
                    } catch (InterruptedException ex) {
                        System.err.println("InterruptedException exception: " + ex.getMessage());
                        Utils.sendErrorMessage(player, "the API server has an internal error");
                    } catch (Exception ex) {
                        System.err.println("URISyntaxException exception: " + ex.getMessage());
                        Utils.sendErrorMessage(player, "the URL for the API server (" + Utils.IP + ") is incorrect");
                    }
                }
            };

            new Thread(runnable).start();
        }

        return true;
    }
}

