package de.Aikonsukun.DeathCounter.PlayerSigns;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

@SuppressWarnings("serial")
public class PlayerSign implements Serializable{
	
	private String playerName; // has the playerName
	private int playerDeaths = 0; // has the player deaths
	private String[] signLines = {"§4[DeathCounter]", "", "", "Deaths"}; // has each line of the created sign
	private ArrayList<String> playerSigns = new ArrayList<String>(); // has the sign locations saved as String
	
	public PlayerSign(String name) {
		playerName = name;
		signLines[1] = "§a"+playerName;
	}
	
	public void addSignEvent(SignChangeEvent signEvent) {
		Location signLocation = signEvent.getBlock().getLocation();
		playerSigns.add(locationToString(signLocation));;
	}
	
	public int setPlayerDeaths() {
		playerDeaths = Bukkit.getPlayer(playerName).getStatistic(Statistic.DEATHS);
		signLines[2] = ""+playerDeaths;
		return playerDeaths; 
	}
	
	public void setSignText(int extra) {
		for (int signCount=0; signCount<getSignSize(); signCount++) {
			Sign sign = (Sign) stringToLocation(playerSigns.get(signCount)).getBlock().getState();
						
			for (int i=0; i<4; i++) {
				sign.setLine(i, signLines[i]);
			}
			sign.setLine(2, ""+(Integer.parseInt(signLines[2])+extra));
			sign.update();
		}
	}
	
	public String locationToString(Location loc) {
		return ""+loc.getWorld().getName()+":"+loc.getBlockX()+":"+loc.getBlockY()+":"+loc.getBlockZ()+":"+loc.getYaw()+":"+loc.getPitch();
	}
	
	public Location stringToLocation(String locS) {
		String[] spLocS = locS.split("\\:");
		return new Location(Bukkit.getWorld(spLocS[0]), Double.parseDouble(spLocS[1]),
						Double.parseDouble(spLocS[2]), Double.parseDouble(spLocS[3]), 
						Float.parseFloat(spLocS[4]), Float.parseFloat(spLocS[5]));
	}
	
	public Location getSignLocation(int signCount) {
		Location reSign = stringToLocation(playerSigns.get(signCount));
		return reSign;		
	}	
	
	public String getPlayerName() {
		return playerName;
	}
	
	public int getPlayerDeaths() {
		return playerDeaths;
	}
	
	public int getSignSize() {
		return playerSigns.size();
	}
	
}