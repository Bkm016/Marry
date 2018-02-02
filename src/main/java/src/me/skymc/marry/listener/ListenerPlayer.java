package me.skymc.marry.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.skymc.marry.Marry;
import me.skymc.marry.api.MarryAPI;
import me.skymc.marry.data.MarryData;
import me.skymc.taboolib.inventory.InventoryUtil;
import me.skymc.taboolib.inventory.ItemUtils;

public class ListenerPlayer implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void in(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			ItemStack item = e.getPlayer().getItemInHand();
			if (item == null || item.getType().equals(Material.AIR) || !item.isSimilar(ItemUtils.getCacheItem(Marry.getConf().getString("Settings.marryitemname")))) {
				return;
			}
			else {
				e.setCancelled(true);
			}
			
			/**
			 * 检查是否结婚
			 */
			MarryData data = MarryAPI.getMarryData(e.getPlayer().getName());
			if (data == null) {
				e.getPlayer().sendMessage(Marry.getLanguage().get("command.item.empty"));
				return;
			}
			
			/**
			 * 移除物品
			 */
			InventoryUtil.hasItem(e.getPlayer(), ItemUtils.getCacheItem(Marry.getConf().getString("Settings.marryitemname")), 1, true);
			
			/**
			 * 提示信息
			 */
			if (Bukkit.getPlayerExact(data.getSender()) != null) {
				Marry.getLanguage().send(Bukkit.getPlayerExact(data.getSender()), "command.item.sender");
			}
			if (Bukkit.getPlayerExact(data.getTarget()) != null) {
				Marry.getLanguage().send(Bukkit.getPlayerExact(data.getTarget()), "command.item.target");
			}
			
			/**
			 * 增加本地数据
			 */
			data.setPoint(data.getPoint() + Marry.getConf().getInt("Settings.marryitempoint"));
			
			/**
			 * 是结婚发起者?
			 */
			boolean isSender = data.getSender().equals(e.getPlayer().getName()) ? true : false;
			
			/**
			 * 增加数据库数据
			 */
			Marry.getConnection().setValue(Marry.getTable(), isSender ? "sender" : "target" , e.getPlayer().getName(), "point", Marry.getConf().getInt("Settings.marryitempoint"), true);
		}
	}

}
