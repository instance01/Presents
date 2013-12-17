package com.comze_instancelabs.presents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public ArrayList<ItemStack> presents = new ArrayList<ItemStack>();
	ArrayList<Location> toremove = new ArrayList<Location>();
	
	public String skull_owner_name = "InstanceLabs";
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		
		addAllPresents();
		
		// reset timer
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			@Override
			public void run() {
				for(Location l : toremove){
					l.getWorld().getBlockAt(l).setType(Material.AIR);
				}
				toremove.clear();
				
				for(Player p : Bukkit.getServer().getOnlinePlayers()){
					Random r = new Random();
					// 1% chance to spawn a new present
					int f = r.nextInt(100);
					if(f == 50){
						spawnPresentAroundYou(p);
					}
				}
			}
		}, 1200, 1200); // 60 seconds
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(event.getBlock().getType() == Material.SKULL){
			try{
				Skull s = (Skull)event.getBlock().getState();
				if(s.getOwner().equals(skull_owner_name)){ // X_DarkPhoenix_X
					event.getPlayer().getInventory().addItem(getRandomPresent());
					event.getPlayer().sendMessage("§6You just got a present! §cMerry Christmas =)");
					event.setCancelled(true);
					event.getBlock().setType(Material.AIR);
				}	
			}catch(Exception e){
				for(StackTraceElement f : e.getStackTrace()){
					System.out.println(f.toString());
				}
				//System.out.println(e.getCause());
			}
		}
	}
	
	public void spawnPresentAroundYou(Player p){
		//TODO: spawn presents sometimes around you in a range of 10 blocks
		// idea: search for an air block in 10 blocks range which has a solid block under it
		// and replace it with a present skull
		// add it to the remove timer like in glassparty (every 10 seconds)
		int x = p.getLocation().getBlockX();
		int y = p.getLocation().getBlockY();
		int z = p.getLocation().getBlockZ();
		
		int x_low = x - 5;
		int y_low = y - 5;
		int z_low = z - 5;
		
		int width = 10;
		int height = 10;
		int length = 10;
		
		for(x = 0; x < width; x++){
			for(y = 0; y < height; y++){
				for(z = 0; z < length; z++){
					Location temp = new Location(p.getLocation().getWorld(), x_low + x, y_low + y, z_low +z);
					Location temp_under = new Location(p.getLocation().getWorld(), x_low + x, y_low + y - 1, z_low +z);

					if(temp.getBlock().getType() == Material.AIR && temp_under.getBlock().getType() != Material.AIR){
						temp.getBlock().setType(Material.SKULL);
						Skull s = (Skull) temp.getBlock().getState();
						s.setOwner(skull_owner_name);
						s.update();
						toremove.add(temp);
						return;
					}
				}
			}
		}
	}
	
	public ItemStack getRandomPresent(){
		if(presents.size() < 1){
			addAllPresents();
		}
		Random r = new Random();
		int i = r.nextInt(presents.size() - 1);
		ItemStack s = presents.get(i);
		setItemNameAndLore(s, "§cChristmas Present", new String[]{"Drakonnas simply loves you!"});
		return presents.get(i);
	}
	
	private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }
	
	public void addAllPresents(){
		presents.add(new ItemStack(Material.DIAMOND, 1));
		presents.add(new ItemStack(Material.ARROW, 32));
		presents.add(new ItemStack(Material.STONE_PICKAXE, 1));
		presents.add(new ItemStack(Material.IRON_SWORD, 1));
		presents.add(new ItemStack(Material.APPLE, 10));
		presents.add(new ItemStack(Material.EXP_BOTTLE, 1));
		presents.add(new ItemStack(Material.TNT, 3));
		presents.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
		presents.add(new ItemStack(Material.GOLD_INGOT, 1));
		presents.add(new ItemStack(Material.IRON_INGOT, 1));
		presents.add(new ItemStack(Material.BOW, 1));
	}
}
