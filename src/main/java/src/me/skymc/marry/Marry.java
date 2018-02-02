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
 * �������
 * 
 * @author sky
 * @since 2018��2��1��18:19:52
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
		// ���������ļ�
		reloadConfig();
		// ���������ļ�
		language = new Language("zh_CN", this, true);
		// ���������ݹ����
		marryDataManager = new MarryDataManager();
		// �������ڸ�ʽ
		dateFormat = new SimpleDateFormat(getConfig().getString("Settings.dateformat"));
		
		// ע�������
		Loader.registerEvents();
		// ע������
		Loader.registerCommnad();
		
		// �������ݿ�
		connection = MysqlUtils.getMySQLConnectionFromConfiguration(getConfig(), "MySQL", 30, this);
		if (connection == null) {
			MsgUtils.warn("���ݿ���δ����, ����ѹر�!");
			setEnabled(false);
		}
		else {
			// ���Դ������ݱ�
			connection.createTable(getTable(), new Column("sender", ColumnString.TEXT), new Column("target", ColumnString.TEXT), new Column("point", ColumnInteger.INT), new Column("date", ColumnInteger.BIGINT));
		}
	}
	
	/**
	 * ��ȡ���ݿ����
	 * 
	 * @return {@link String}
	 */
	public static String getTable() {
		return inst.getConfig().getString("MySQL.table");
	}
}
