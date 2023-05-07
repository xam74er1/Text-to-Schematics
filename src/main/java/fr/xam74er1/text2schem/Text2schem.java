package fr.xam74er1.text2schem;

import org.bukkit.plugin.java.JavaPlugin;

public final class Text2schem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("sharpe").setExecutor(new SharpeCommand());
        this.getCommand("seturl").setExecutor(new SetURLCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
