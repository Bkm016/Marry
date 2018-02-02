package me.skymc.marry.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skymc.marry.Marry;
import me.skymc.marry.api.MarryAPI;
import me.skymc.marry.data.MarryData;
import me.skymc.marry.invite.InviteManager;
import me.skymc.taboolib.commands.SubCommand;
import me.skymc.taboolib.other.NumberUtils;

/**
 * 子命令
 * 
 * @author sky
 * @since 2018年2月2日16:30:38
 */
public class ListCommand extends SubCommand {

	public ListCommand(CommandSender sender, String[] args) {
		super(sender, args);
		
		/**
		 * 获取页数
		 */
		int page = 1;
		if (args.length > 1) {
			page = NumberUtils.getInteger(args[1]);
		}
		
		/**
		 * 检查页数
		 */
		if (page < 1) {
			Marry.getLanguage().send(sender, "command.list.error");
			return;
		}
		
		/**
		 * 发送顶部信息
		 */
		Marry.getLanguage().send(sender, "command.list.head");
		
		/**
		 * 起始点
		 */
		int start = (page - 1) * Marry.getConf().getInt("Settings.listsize");
		int end = page * Marry.getConf().getInt("Settings.listsize");
		
		/**
		 * 循环所有结婚数据
		 */
		for (int i = start; i < Marry.getMarryDataManager().getMarrydata().size() && start < end; i++) {
			MarryData data = Marry.getMarryDataManager().getMarrydata().get(i);
			sender.sendMessage(Marry.getLanguage().get("command.list.data")
					.replace("$id", String.valueOf(i + 1))
					.replace("$sender", data.getSender())
					.replace("$target", data.getTarget())
					.replace("$point", String.valueOf(data.getPoint())));
		}
		
		/**
		 * 发送底部信息
		 */
		sender.sendMessage(Marry.getLanguage().get("command.list.bottom")
				.replace("$page", String.valueOf(page))
				.replace("$all", String.valueOf((int) Math.ceil(Marry.getMarryDataManager().getMarrydata().size() / Marry.getConf().getDouble("Settings.listsize")))));
	}
}
