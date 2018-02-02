package me.skymc.marry.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skymc.marry.Marry;
import me.skymc.marry.api.MarryAPI;
import me.skymc.marry.data.MarryData;
import me.skymc.marry.invite.InviteManager;
import me.skymc.taboolib.commands.SubCommand;

/**
 * ������
 * 
 * @author sky
 * @since 2018��2��2��16:30:38
 */
public class InfoCommand extends SubCommand {

	public InfoCommand(CommandSender sender, String[] args) {
		super(sender, args);
		MarryData data = MarryAPI.getMarryData(sender.getName());
		
		/**
		 * ��ѯ�Ƿ�������
		 */
		if (data == null) {
			Marry.getLanguage().send(sender, "command.info.empty");
		}
		else {
			for (String message : Marry.getLanguage().getList("command.info.message")) {
				sender.sendMessage(message
						.replace("$sender", data.getSender())
						.replace("$target", data.getTarget())
						.replace("$point", String.valueOf(data.getPoint()))
						.replace("$date", Marry.getDateFormat().format(data.getDate())));
			}
		}
	}
}
