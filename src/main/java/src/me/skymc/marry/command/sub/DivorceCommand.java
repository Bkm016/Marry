package me.skymc.marry.command.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skymc.marry.Marry;
import me.skymc.marry.api.MarryAPI;
import me.skymc.marry.invite.InviteManager;
import me.skymc.taboolib.commands.SubCommand;

/**
 * 子命令
 * 
 * @author sky
 * @since 2018年2月2日16:30:38
 */
public class DivorceCommand extends SubCommand {

	public DivorceCommand(CommandSender sender, String[] args) {
		super(sender, args);
		/**
		 * 断绝关系
		 */
		InviteManager.divorce((Player) sender);
	}
}
