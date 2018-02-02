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
public class SendCommand extends SubCommand {

	public SendCommand(CommandSender sender, String[] args) {
		super(sender, args);
		if (args.length < 2) {
			Marry.getLanguage().send(sender, "command.send.empty");
			return;
		}
		
		/**
		 * ��ȡ����Ŀ��
		 */
		Player player = Bukkit.getPlayerExact(args[1]);
		if (player == null) {
			sender.sendMessage(Marry.getLanguage().get("command.send.offline").replace("$", args[1]));;
			return;
		}
		
		/**
		 * ��������
		 */
		InviteManager.invite((Player) sender, player);
	}
}
