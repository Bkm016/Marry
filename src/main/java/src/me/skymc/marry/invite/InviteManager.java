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
 * 结婚申请管理层
 * 
 * @author sky
 * @since 2018年2月2日16:33:27
 */
public class InviteManager {

	private static HashMap<String, String> invites = new HashMap<>();
	private static HashMap<String, BukkitTask> invites_task = new HashMap<>();
	
	/**
	 * 发送交♂往请求
	 * 
	 * @param sender 发起者
	 * @param invite 接受者
	 */
	public static void invite(Player sender, Player target) {
		
		/**
		 * 检查是否是自己
		 */
		if (sender.getName().equals(target.getName())) {
			Marry.getLanguage().send(sender, "command.send.myself");
			return;
		}
		
		/**
		 * 请求正在处理
		 */
		if (invites.containsKey(sender.getName())) {
			Marry.getLanguage().send(sender, "command.send.aleady-send");
			return;
		}
		
		/**
		 * 检查自己和对方是否已经结婚
		 */
		if (MarryAPI.getMarryData(sender.getName()) != null || MarryAPI.getMarryData(target.getName()) != null) {
			Marry.getLanguage().send(sender, "command.send.already-marry");
			return;
		}
		
		/**
		 * 移除结婚戒指
		 */
		if (!InventoryUtil.hasItem(sender, ItemUtils.getCacheItem(Marry.getConf().getString("Settings.ringname")), 1, true)) {
			Marry.getLanguage().send(sender, "command.send.noring");
			return;
		}
		
		/**
		 * 向目标发送信息
		 */
		for (String message : Marry.getLanguage().getList("command.send.target")) {
			target.sendMessage(message.replace("$", sender.getName()));
		}
		/**
		 * 向发送者发送信息
		 */
		sender.sendMessage(Marry.getLanguage().get("command.send.sender").replace("$", target.getName()));
		
		/**
		 * 写入数据
		 */
		invites.put(sender.getName(), target.getName());
		
		/**
		 * 延迟一段时间后回收请求
		 */
		invites_task.put(sender.getName(), new BukkitRunnable() {
			
			@Override
			public void run() {
				/**
				 * 如果请求数据中还有申请者
				 */
				if (invites.containsKey(sender.getName()) && invites.get(sender.getName()).equals(target.getName())) {
					/**
					 * 移除数据
					 */
					invites.remove(sender.getName());
					invites_task.remove(sender.getName());
					
					/**
					 * 发送过期提醒
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
	 * 接手交♂往请求
	 * 
	 * @param target 接受者
	 * @param sender 申请者
	 */
	public static void accept(Player target, Player sender) {
		/**
		 * 如果没有请求
		 */
		if (!invites.containsKey(sender.getName()) || !invites.get(sender.getName()).equals(target.getName())) {
			Marry.getLanguage().send(target, "command.accept.notfound");
			return;
		}
		
		/**
		 * 检查自己和对方是否已经结婚
		 */
		if (MarryAPI.getMarryData(sender.getName()) != null || MarryAPI.getMarryData(target.getName()) != null) {
			Marry.getLanguage().send(sender, "command.accept.already-marry");
			return;
		}
		
		/**
		 * 移除结婚请求
		 */
		invites.remove(sender.getName());
		BukkitTask task = invites_task.remove(sender.getName());
		if (task != null) {
			task.cancel();
		}
		
		/**
		 * 发送信息
		 */
		target.sendMessage(Marry.getLanguage().get("command.accept.target").replace("$", sender.getName()));
		sender.sendMessage(Marry.getLanguage().get("command.accept.sender").replace("$", target.getName()));
		
		/**
		 * 播放公告
		 */
		Bukkit.broadcastMessage(Marry.getLanguage().get("command.accept.broadcast").replace("$1", sender.getName()).replace("$2", target.getName()));
		
		/**
		 * 创建结婚数据
		 */
		MarryData data = new MarryData(sender.getName(), target.getName(), 0, System.currentTimeMillis());
		
		/**
		 * 添加结婚数据到本地
		 */
		Marry.getMarryDataManager().getMarrydata().add(data);
		
		/**
		 * 添加结婚数据到数据库
		 */
		Marry.getMarryDataManager().into(data);
	}
	
	/**
	 * 拒绝结婚请求
	 * 
	 * @param target
	 * @param sender
	 */
	public static void deny(Player target, String sender) {
		/**
		 * 如果没有请求
		 */
		if (!invites.containsKey(sender) || !invites.get(sender).equals(target.getName())) {
			Marry.getLanguage().send(target, "command.deny.notfound");
			return;
		}
		
		/**
		 * 移除结婚请求
		 */
		invites.remove(sender);
		BukkitTask task = invites_task.remove(sender);
		if (task != null) {
			task.cancel();
		}
		
		/**
		 * 向 target 发送信息
		 */
		target.sendMessage(Marry.getLanguage().get("command.deny.target").replace("$", sender));
		
		Player senderPlayer = Bukkit.getPlayerExact(sender);
		/**
		 * 向 sender 发送信息
		 */
		if (senderPlayer != null) {
			senderPlayer.sendMessage(Marry.getLanguage().get("command.deny.sender").replace("$", target.getName()));
			
			/**
			 * 退换戒指
			 */
			Marry.getLanguage().send(senderPlayer, "command.send.return");
			senderPlayer.getInventory().addItem(ItemUtils.getCacheItem(Marry.getConf().getString("Settings.ringname")));
			
		}
	}
	
	/**
	 * 断绝关系！
	 * 
	 * @param player 玩家
	 */
	public static void divorce(Player player) {
		MarryData data = MarryAPI.getMarryData(player.getName());
		
		/**
		 * 检查是否结婚
		 */
		if (data == null) {
			player.sendMessage(Marry.getLanguage().get("command.divorce.empty"));
			return;
		}
		
		/**
		 * 是结婚发起者?
		 */
		boolean isSender = data.getSender().equals(player.getName()) ? true : false;
		
		/**
		 * 提示信息
		 */
		player.sendMessage(Marry.getLanguage().get("command.divorce.sender").replace("$", isSender ? data.getTarget() : data.getSender()));
		
		/**
		 * 从服务器内移除结婚数据
		 */
		Marry.getMarryDataManager().getMarrydata().remove(data);
		
		/**
		 * 从数据库内移除数据
		 */
		Marry.getConnection().deleteValue(Marry.getTable(), isSender ? "sender" : "target", player.getName());
	}
}
