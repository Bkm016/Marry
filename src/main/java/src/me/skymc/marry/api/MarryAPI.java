package me.skymc.marry.api;

import java.util.HashMap;
import java.util.Map.Entry;

import me.skymc.marry.Marry;
import me.skymc.marry.data.MarryData;
import me.skymc.marry.data.MarryDataManager;
import me.skymc.taboolib.database.GlobalDataManager;

/**
 * API
 * 
 * @author sky
 * @since 2018年2月1日18:20:40
 */
public class MarryAPI {
	
	/**
	 * 获取结婚对象
	 * 
	 * @param player 玩家
	 * @return {@link String}
	 */
	public static String getCouple(String player) {
		for (MarryData data : Marry.getMarryDataManager().getMarrydata()) {
			if (data.getSender().equals(player)) {
				return data.getTarget();
			}
			if (data.getTarget().equals(player)) {
				return data.getSender();
			}
		}
		return null;
	}
	
	/**
	 * 获取结婚数据
	 * 
	 * @param player 玩家
	 * @return {@link MarryData}
	 */
	public static MarryData getMarryData(String player) {
		for (MarryData data : Marry.getMarryDataManager().getMarrydata()) {
			if (data.getSender().equals(player) || data.getTarget().equals(player)) {
				return data;
			}
		}
		return null;
	}
}
