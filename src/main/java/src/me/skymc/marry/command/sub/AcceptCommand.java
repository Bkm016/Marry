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
public class AcceptCommand extends SubCommand {

	public AcceptCommand(CommandSender sender, String[] args) {
		super(sender, args);
		if (args.length < 2) {
			Marry.getLanguage().send(sender, "command.accept.empty");
			return;
		}
		
		/**
		 * ��ȡ����Ŀ��
		 */
		Player player = Bukkit.getPlayerExact(args[1]);
		if (player == null) {
			sender.sendMessage(Marry.getLanguage().get("command.accept.offline").replace("$", args[1]));;
			return;
		}
		
		/**
		 * ��������
		 */
		InviteManager.accept((Player) sender, player);
	}
}
