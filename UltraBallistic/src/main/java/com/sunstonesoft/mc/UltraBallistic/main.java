package com.sunstonesoft.mc.UltraBallistic;



import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
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
		}
		if (cmd.getName().equalsIgnoreCase("launch_rocket")) { // If the player typed /basic then do the following, note: If you only registered this executor for one command, you don't need this
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;
				
				if (args.length > 2) {
			        sender.sendMessage("Too many arguments!");
			        return false;
			    } 
			    if (args.length < 2) {
			        sender.sendMessage("Not enough arguments!");
			        return false;
			    }
			    
			    
				int rockets = (int) getConfig().get(String.format("rockets.%s", player.getUniqueId().toString()), 0);
				
				if (rockets < 1) {
					player.sendMessage("You don't have enough rockets!");
					return true;
				}
				
				rockets = rockets - 1;
				
				getConfig().set(String.format("rockets.%s", player.getUniqueId().toString()), rockets);
				saveConfig();
				
						

				try {
					
					World world = player.getWorld();
					int x = Integer.parseInt(args[0]);
					int z = Integer.parseInt(args[1]);
			        int y = world.getHighestBlockAt(x, z).getY();
			        
			        
			        
					Location loc = new Location(world, x, y, z);
					
					int cycleY = 255;
					
					while (cycleY > loc.getY()) {
						try {
							  Thread.sleep(1000);
						} catch (InterruptedException e) {
							  Thread.currentThread().interrupt();
						
						}
						
						
						cycleY = cycleY - 1;
						Location cycleLoc = new Location(world, x, cycleY, z);
						world.spawnParticle(Particle.SMOKE_NORMAL, cycleLoc, 2);
					}
					
					if (cycleY == loc.getY()) {
						world.createExplosion(loc, 15, true);
						world.createExplosion(loc, 15, true);
						world.createExplosion(loc, 15, true);
						world.createExplosion(loc, 15, true);
						world.createExplosion(loc, 15, true);
						world.createExplosion(loc, 15, true);
						world.createExplosion(loc, 15, true);
						player.sendMessage("Rocket blow up!");
						return true;
					}
					
					
							
							
			    } catch (final NumberFormatException e) {
			        return false;
			    }
				
				
			}
			return true;
		}
		//If this has happened the function will return true. 
	        // If this hasn't happened the value of false will be returned.
		return false; 
	}
}
