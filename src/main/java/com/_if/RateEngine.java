package com._if;

import java.util.Calendar;
import com._if.model.Rate;

public class RateEngine {
	public Rate calculate(Calendar entry, Calendar exit){
		Rate rate = null;
		if (isEarlyBird(entry, exit)){
			rate = new Rate("EarlyBird", 15d);
		}
			
		return rate;
	}
	
	private boolean isEarlyBird(Calendar entry, Calendar exit){
		return false;
	}
}
