package me.skymc.marry.data;

import lombok.Getter;
import lombok.Setter;

public class MarryData {
	
	@Getter
	private String sender;
	
	@Getter
	private String target;
	
	@Getter
	@Setter
	private int point;
	
	@Getter
	private long date;
	
	public MarryData(String sender, String target, int point, long date) {
		this.sender = sender;
		this.target = target;
		this.point = point;
		this.date = date;
	}
}
