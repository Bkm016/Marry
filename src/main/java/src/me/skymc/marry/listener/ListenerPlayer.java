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
			 * ����Ƿ���
			 */
			MarryData data = MarryAPI.getMarryData(e.getPlayer().getName());
			if (data == null) {
				e.getPlayer().sendMessage(Marry.getLanguage().get("command.item.empty"));
				return;
			}
			
			/**
			 * �Ƴ���Ʒ
			 */
			InventoryUtil.hasItem(e.getPlayer(), ItemUtils.getCacheItem(Marry.getConf().getString("Settings.marryitemname")), 1, true);
			
			/**
			 * ��ʾ��Ϣ
			 */
			if (Bukkit.getPlayerExact(data.getSender()) != null) {
				Marry.getLanguage().send(Bukkit.getPlayerExact(data.getSender()), "command.item.sender");
			}
			if (Bukkit.getPlayerExact(data.getTarget()) != null) {
				Marry.getLanguage().send(Bukkit.getPlayerExact(data.getTarget()), "command.item.target");
			}
			
			/**
			 * ���ӱ�������
			 */
			data.setPoint(data.getPoint() + Marry.getConf().getInt("Settings.marryitempoint"));
			
			/**
			 * �ǽ�鷢����?
			 */
			boolean isSender = data.getSender().equals(e.getPlayer().getName()) ? true : false;
			
			/**
			 * �������ݿ�����
			 */
			Marry.getConnection().setValue(Marry.getTable(), isSender ? "sender" : "target" , e.getPlayer().getName(), "point", Marry.getConf().getInt("Settings.marryitempoint"), true);
		}
	}

}
