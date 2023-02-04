package com.sunstonesoft.mc.UltraBallistic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
	@Override
    public void onEnable() {
        getLogger().info("Ballistic missiles are ready!");
    }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rockets")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;
				sender.sendMessage(String.format("You have %s rockets!", 0));
			}
			return true;
		} //If this has happened the function will return true. 
	        // If this hasn't happened the value of false will be returned.
		return false; 
	}
}
