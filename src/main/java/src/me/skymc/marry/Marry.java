package me.skymc.marry;

import java.io.File;
import java.text.SimpleDateFormat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.skymc.marry.data.MarryDataManager;
import me.skymc.taboolib.fileutils.ConfigUtils;
import me.skymc.taboolib.message.MsgUtils;
import me.skymc.taboolib.mysql.MysqlUtils;
import me.skymc.taboolib.mysql.protect.MySQLConnection;
import me.skymc.taboolib.mysql.protect.MySQLConnection.Column;
import me.skymc.taboolib.mysql.protect.MySQLConnection.ColumnInteger;
import me.skymc.taboolib.mysql.protect.MySQLConnection.ColumnString;
import me.skymc.taboolib.string.Language;

/**
 * 插件主类
 * 
 * @author sky
 * @since 2018年2月1日18:19:52
 */
public class Marry extends JavaPlugin implements Listener {
	
	@Getter
	private static Plugin inst;
	
	@Getter
	private static MySQLConnection connection;
	
	@Getter
	private static Language language;
	
	@Getter
	private static FileConfiguration conf;

	@Getter
	private static MarryDataManager marryDataManager;
	
	@Getter
	private static SimpleDateFormat dateFormat;
	
	@Override
	public FileConfiguration getConfig() {
		return conf;
	}
	
	@Override
	public void reloadConfig() {
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", true);
		}
		conf = ConfigUtils.load(this, file);
	}
	
	@Override
	public void onLoad() {
		inst = this;
	}
	
	@Override
	public void onEnable() {
		// 载入配置文件
		reloadConfig();
		// 载入语言文件
		language = new Language("zh_CN", this, true);
		// 载入结婚数据管理层
		marryDataManager = new MarryDataManager();
		// 载入日期格式
		dateFormat = new SimpleDateFormat(getConfig().getString("Settings.dateformat"));
		
		// 注册监听器
		Loader.registerEvents();
		// 注册命令
		Loader.registerCommnad();
		
		// 连接数据库
		connection = MysqlUtils.getMySQLConnectionFromConfiguration(getConfig(), "MySQL", 30, this);
		if (connection == null) {
			MsgUtils.warn("数据库尚未连接, 插件已关闭!");
			setEnabled(false);
		}
		else {
			// 尝试创建数据表
			connection.createTable(getTable(), new Column("sender", ColumnString.TEXT), new Column("target", ColumnString.TEXT), new Column("point", ColumnInteger.INT), new Column("date", ColumnInteger.BIGINT));
		}
	}
	
	/**
	 * 获取数据库表名
	 * 
	 * @return {@link String}
	 */
	public static String getTable() {
		return inst.getConfig().getString("MySQL.table");
	}
}
