package de.Aikonsukun.DeathCounter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.Aikonsukun.DeathCounter.PlayerSigns.PlayerSign;

@SerializableAs("MainDeathCounterNew")
public class MainDeathCounterNew extends JavaPlugin implements Listener{
	
	private HashMap<String, PlayerSign> playerSignList; // has all the players with there sings

	private SignChangeEvent eventSign;
	
	public void saveObject(Object obje, File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obje);
			oos.flush();
			oos.close();
			System.out.println("[DeathCounter 1.2] Saved file");
		} catch (Exception e) {
			System.out.println("[DeathCounter 1.2] Failed to save file");
			System.out.println(e.getMessage());
		}
	}	
	
	public Object loadObject(File file)  {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Object reObject = ois.readObject();
			ois.close();
			System.out.println("[DeathCounter 1.2] Loaded file");
			return reObject;
		} catch (Exception e) {
			System.out.println("[DeathCounter 1.2] Failed to load file");
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable(){
		System.out.println("[DeathCounter 1.2] DeathCounterNew is enabled");
		Bukkit.getPluginManager().registerEvents(this, this);
		File dir = getDataFolder();
		if (!dir.exists()) {
			if(!dir.mkdir()) {
				System.out.println("[DeathCounter 1.2] Could not create directory for plugin: "+ getDescription().getName());
			}
		}
		playerSignList = (HashMap<String, PlayerSign>) loadObject(new File(getDataFolder(), "playerSignList.dat"));
		if (playerSignList == null) {
			playerSignList = new HashMap<String, PlayerSign>(); 
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		eventSign = event;
		String playerName = eventSign.getLine(1);
		
		if (!event.getLine(0).isEmpty() && !event.getLine(1).isEmpty()) {
			if (event.getLine(0).equalsIgnoreCase("[DeathCounter]")) {				
				if ((Bukkit.getPlayer(playerName) != null && Bukkit.getOfflinePlayer(playerName) != null)) { // "&& Bukkit.getOfflinePlayer(playerName) != null" added	
					if (!(playerSignList.containsKey(playerName))) {
						playerSignList.put(playerName, new PlayerSign(playerName));
					}
					playerSignList.get(playerName).addSignEvent(eventSign);
				} else {
					eventSign.setLine(1, "§cPlayer is");
					eventSign.setLine(2, "§cnonexistent");	
				}
			}
		}
	}	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		String playerName = event.getPlayer().getDisplayName();
		
		if (playerSignList.containsKey(playerName)) {
			if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
			if (event.getClickedBlock().getState() instanceof Sign) {
				playerSignList.get(playerName).setPlayerDeaths();
				playerSignList.get(playerName).setSignText(0);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		String playerName = event.getEntity().getDisplayName();
		
		if (playerSignList.containsKey(playerName)) {
			playerSignList.get(playerName).setPlayerDeaths();
			playerSignList.get(playerName).setSignText(1);		
		}
	}
	
	@Override
	public void onDisable(){
		System.out.println("[DeathCounter 1.2] DeathCounterNew is disabled");
		saveObject(playerSignList, new File(getDataFolder(), "playerSignList.dat"));	
	}
	
}