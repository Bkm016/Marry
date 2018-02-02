package me.skymc.marry.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skymc.marry.Marry;
import me.skymc.marry.api.MarryAPI;
import me.skymc.marry.invite.InviteManager;
import me.skymc.taboolib.commands.SubCommand;

/**
 * ������
 * 
 * @author sky
 * @since 2018��2��2��16:30:38
 */
public class DenyCommand extends SubCommand {

	public DenyCommand(CommandSender sender, String[] args) {
		super(sender, args);
		if (args.length < 2) {
			Marry.getLanguage().send(sender, "command.deny.empty");
			return;
		}
		
		/**
		 * �ܾ�����
		 */
		InviteManager.deny((Player) sender, args[1]);
	}
}
