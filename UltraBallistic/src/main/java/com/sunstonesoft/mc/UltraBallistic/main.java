package com.sunstonesoft.mc.UltraBallistic;



import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;



public final class main extends JavaPlugin {

    private static Economy econ = null;
	
	
	@Override
    public void onEnable() {
		if (!setupEconomy() ) {
			getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Ballistic missiles are ready!");
    }
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rockets")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;
				int rockets = (int) getConfig().get(String.format("rockets.%s", player.getUniqueId().toString()), 0);
				
				sender.sendMessage(String.format("You have %s rockets!", rockets));
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("buy_rocket")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;
				
				EconomyResponse r = econ.withdrawPlayer(player, (double) getConfig().get("cost",100000.00));
						
				if(r.transactionSuccess()) {
					
					int rockets = (int) getConfig().get(String.format("rockets.%s", player.getUniqueId().toString()), 0);
					
					rockets = rockets + 1;
					
					getConfig().set(String.format("rockets.%s", player.getUniqueId().toString()), rockets);
					saveConfig();
					player.sendMessage(String.format("You succesfully bought 1 rocket. Now you have %s rockets and your balance is %s!", rockets, econ.format(r.balance)));
				}
				else
				{
						sender.sendMessage(String.format("An error occured: %s", r.errorMessage));
				}
				
				
			}
			return true;
		} //If this has happened the function will return true. 
	        // If this hasn't happened the value of false will be returned.
		return false; 
	}
}
