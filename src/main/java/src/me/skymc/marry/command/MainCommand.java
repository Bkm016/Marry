package me.skymc.marry.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import me.skymc.marry.Loader;
import me.skymc.marry.Marry;
import me.skymc.marry.command.sub.AcceptCommand;
import me.skymc.marry.command.sub.DenyCommand;
import me.skymc.marry.command.sub.DivorceCommand;
import me.skymc.marry.command.sub.SendCommand;
import me.skymc.marry.command.sub.InfoCommand;
import me.skymc.marry.command.sub.ListCommand;

public class MainCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			Marry.getInst().reloadConfig();
			sender.sendMessage("reload ok!");
			return false;
		}
		
		if (args.length == 0) {
			for (String message : Marry.getLanguage().getList("command.help")) {
				sender.sendMessage(message);
			}
			return false;
		}
		else if (args[0].equals("send")) {
			new SendCommand(sender, args);
		}
		else if (args[0].equals("yes")) {
			new AcceptCommand(sender, args);
		}
		else if (args[0].equals("no")) {
			new DenyCommand(sender, args);
		}
		else if (args[0].equals("divorce")) {
			new DivorceCommand(sender, args);
		}
		else if (args[0].equals("info")) {
			new InfoCommand(sender, args);
		}
		else if (args[0].equals("list")) {
			new ListCommand(sender, args);
		}
		else {
			Marry.getLanguage().send(sender, "command.error");
		}
		return false;
	}
}

