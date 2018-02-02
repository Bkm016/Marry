package me.skymc.marry;

import org.bukkit.Bukkit;

import me.skymc.marry.command.MainCommand;
import me.skymc.marry.listener.ListenerPlayer;

/**
 * ���������
 * 
 * @author sky
 * @since 2018��2��1��18:24:45
 */
public class Loader{
	
	public static void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new ListenerPlayer(), Marry.getInst());
	}
	
	public static void registerCommnad() {
		Marry.getInst().getServer().getPluginCommand("marry").setExecutor(new MainCommand());
	}
}
