package me.skymc.marry.invite;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.skymc.marry.Marry;
import me.skymc.marry.api.MarryAPI;
import me.skymc.marry.data.MarryData;
import me.skymc.taboolib.economy.EcoUtils;
import me.skymc.taboolib.inventory.InventoryUtil;
import me.skymc.taboolib.inventory.ItemUtils;

/**
 * �����������
 * 
 * @author sky
 * @since 2018��2��2��16:33:27
 */
public class InviteManager {

	private static HashMap<String, String> invites = new HashMap<>();
	private static HashMap<String, BukkitTask> invites_task = new HashMap<>();
	
	/**
	 * ���ͽ���������
	 * 
	 * @param sender ������
	 * @param invite ������
	 */
	public static void invite(Player sender, Player target) {
		
		/**
		 * ����Ƿ����Լ�
		 */
		if (sender.getName().equals(target.getName())) {
			Marry.getLanguage().send(sender, "command.send.myself");
			return;
		}
		
		/**
		 * �������ڴ���
		 */
		if (invites.containsKey(sender.getName())) {
			Marry.getLanguage().send(sender, "command.send.aleady-send");
			return;
		}
		
		/**
		 * ����Լ��ͶԷ��Ƿ��Ѿ����
		 */
		if (MarryAPI.getMarryData(sender.getName()) != null || MarryAPI.getMarryData(target.getName()) != null) {
			Marry.getLanguage().send(sender, "command.send.already-marry");
			return;
		}
		
		/**
		 * �Ƴ�����ָ
		 */
		if (!InventoryUtil.hasItem(sender, ItemUtils.getCacheItem(Marry.getConf().getString("Settings.ringname")), 1, true)) {
			Marry.getLanguage().send(sender, "command.send.noring");
			return;
		}
		
		/**
		 * ��Ŀ�귢����Ϣ
		 */
		for (String message : Marry.getLanguage().getList("command.send.target")) {
			target.sendMessage(message.replace("$", sender.getName()));
		}
		/**
		 * �����߷�����Ϣ
		 */
		sender.sendMessage(Marry.getLanguage().get("command.send.sender").replace("$", target.getName()));
		
		/**
		 * д������
		 */
		invites.put(sender.getName(), target.getName());
		
		/**
		 * �ӳ�һ��ʱ����������
		 */
		invites_task.put(sender.getName(), new BukkitRunnable() {
			
			@Override
			public void run() {
				/**
				 * ������������л���������
				 */
				if (invites.containsKey(sender.getName()) && invites.get(sender.getName()).equals(target.getName())) {
					/**
					 * �Ƴ�����
					 */
					invites.remove(sender.getName());
					invites_task.remove(sender.getName());
					
					/**
					 * ���͹�������
					 */
					if (sender.isOnline()) {
						Marry.getLanguage().send(sender, "command.send.timeout");
						Marry.getLanguage().send(sender, "command.send.return");
						
						sender.getInventory().addItem(ItemUtils.getCacheItem(Marry.getConf().getString("Settings.ringname")));
					}
					if (target.isOnline()) {
						Marry.getLanguage().send(target, "command.send.timeout");
					}
				}
			}
		}.runTaskLater(Marry.getInst(), 20 * Marry.getConf().getInt("Settings.effectiveinvite")));
	}
	
	/**
	 * ���ֽ���������
	 * 
	 * @param target ������
	 * @param sender ������
	 */
	public static void accept(Player target, Player sender) {
		/**
		 * ���û������
		 */
		if (!invites.containsKey(sender.getName()) || !invites.get(sender.getName()).equals(target.getName())) {
			Marry.getLanguage().send(target, "command.accept.notfound");
			return;
		}
		
		/**
		 * ����Լ��ͶԷ��Ƿ��Ѿ����
		 */
		if (MarryAPI.getMarryData(sender.getName()) != null || MarryAPI.getMarryData(target.getName()) != null) {
			Marry.getLanguage().send(sender, "command.accept.already-marry");
			return;
		}
		
		/**
		 * �Ƴ��������
		 */
		invites.remove(sender.getName());
		BukkitTask task = invites_task.remove(sender.getName());
		if (task != null) {
			task.cancel();
		}
		
		/**
		 * ������Ϣ
		 */
		target.sendMessage(Marry.getLanguage().get("command.accept.target").replace("$", sender.getName()));
		sender.sendMessage(Marry.getLanguage().get("command.accept.sender").replace("$", target.getName()));
		
		/**
		 * ���Ź���
		 */
		Bukkit.broadcastMessage(Marry.getLanguage().get("command.accept.broadcast").replace("$1", sender.getName()).replace("$2", target.getName()));
		
		/**
		 * �����������
		 */
		MarryData data = new MarryData(sender.getName(), target.getName(), 0, System.currentTimeMillis());
		
		/**
		 * ��ӽ�����ݵ�����
		 */
		Marry.getMarryDataManager().getMarrydata().add(data);
		
		/**
		 * ��ӽ�����ݵ����ݿ�
		 */
		Marry.getMarryDataManager().into(data);
	}
	
	/**
	 * �ܾ��������
	 * 
	 * @param target
	 * @param sender
	 */
	public static void deny(Player target, String sender) {
		/**
		 * ���û������
		 */
		if (!invites.containsKey(sender) || !invites.get(sender).equals(target.getName())) {
			Marry.getLanguage().send(target, "command.deny.notfound");
			return;
		}
		
		/**
		 * �Ƴ��������
		 */
		invites.remove(sender);
		BukkitTask task = invites_task.remove(sender);
		if (task != null) {
			task.cancel();
		}
		
		/**
		 * �� target ������Ϣ
		 */
		target.sendMessage(Marry.getLanguage().get("command.deny.target").replace("$", sender));
		
		Player senderPlayer = Bukkit.getPlayerExact(sender);
		/**
		 * �� sender ������Ϣ
		 */
		if (senderPlayer != null) {
			senderPlayer.sendMessage(Marry.getLanguage().get("command.deny.sender").replace("$", target.getName()));
			
			/**
			 * �˻���ָ
			 */
			Marry.getLanguage().send(senderPlayer, "command.send.return");
			senderPlayer.getInventory().addItem(ItemUtils.getCacheItem(Marry.getConf().getString("Settings.ringname")));
			
		}
	}
	
	/**
	 * �Ͼ���ϵ��
	 * 
	 * @param player ���
	 */
	public static void divorce(Player player) {
		MarryData data = MarryAPI.getMarryData(player.getName());
		
		/**
		 * ����Ƿ���
		 */
		if (data == null) {
			player.sendMessage(Marry.getLanguage().get("command.divorce.empty"));
			return;
		}
		
		/**
		 * �ǽ�鷢����?
		 */
		boolean isSender = data.getSender().equals(player.getName()) ? true : false;
		
		/**
		 * ��ʾ��Ϣ
		 */
		player.sendMessage(Marry.getLanguage().get("command.divorce.sender").replace("$", isSender ? data.getTarget() : data.getSender()));
		
		/**
		 * �ӷ��������Ƴ��������
		 */
		Marry.getMarryDataManager().getMarrydata().remove(data);
		
		/**
		 * �����ݿ����Ƴ�����
		 */
		Marry.getConnection().deleteValue(Marry.getTable(), isSender ? "sender" : "target", player.getName());
	}
}
