package me.skymc.marry.data;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import me.skymc.marry.Marry;

public class MarryDataManager {
	
	/**
	 * 数据集合
	 */
	@Getter
	private LinkedList<MarryData> marrydata = new LinkedList<>();
	
	/**
	 * 构造方法
	 */
	public MarryDataManager() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				LinkedList<HashMap<String, Object>> data = Marry.getConnection().getValues(Marry.getTable(), "point", -1, true, "sender", "target", "point", "date");
				
				marrydata.clear();
				for (HashMap<String, Object> map : data) {
					marrydata.add(new MarryData(map.get("sender").toString(), map.get("target").toString(), Integer.valueOf(map.get("point").toString()), Long.valueOf(map.get("date").toString())));
				}
			}
		}.runTaskTimerAsynchronously(Marry.getInst(), 0, 20 * Marry.getInst().getConfig().getInt("Settings.update"));
	}
	
	/**
	 * 向数据库插入新的结婚数据
	 * 
	 * @param data 结婚数据
	 */
	public void into(MarryData data) {
		Marry.getConnection().intoValue(Marry.getTable(), data.getSender(), data.getTarget(), data.getPoint(), data.getDate());
	}
}
