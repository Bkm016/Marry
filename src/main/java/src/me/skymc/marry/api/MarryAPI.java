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
 * @since 2018��2��1��18:20:40
 */
public class MarryAPI {
	
	/**
	 * ��ȡ������
	 * 
	 * @param player ���
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
	 * ��ȡ�������
	 * 
	 * @param player ���
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
