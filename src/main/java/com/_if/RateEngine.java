package com._if;

import java.util.Calendar;
import com._if.model.Rate;

public class RateEngine {
	private final int EARLY_BIRD_ENTRY_LOWER = 6;
	private final int EARLY_BIRD_ENTRY_HIGHER = 9;
	private final int EARLY_BIRD_EXIT_LOWER_H = 15;
	private final int EARLY_BIRD_EXIT_HIGHER_H = 23;
	private final int EARLY_BIRD_EXIT_LOWER_M = 30;
	private final int EARLY_BIRD_EXIT_HIGHER_M = 30;	
	
	private final String EARLY_BIRD = "EarlyBird";
	private final String WEEKEND_RATE = "WeekendRate";
	
	public Rate calculate(Calendar entry, Calendar exit){
		Rate rate = null;
		if (isEarlyBird(entry, exit)){
			rate = new Rate(EARLY_BIRD, 15d);
		}
		else if (isWeekendRate(entry, exit)) {
			rate = new Rate(WEEKEND_RATE, 13d);
		}
			
		return rate;
	}
	
	private boolean isEarlyBird(Calendar entry, Calendar exit){
		int entryYear = entry.get(Calendar.YEAR);
		int entryMonth = entry.get(Calendar.MONTH);
		int entryDay = entry.get(Calendar.DAY_OF_MONTH);
		
		//calculate valid entry time (lower bound) based on actual entry
		Calendar validEntryLowerBound = Calendar.getInstance();
		validEntryLowerBound.clear();
		validEntryLowerBound.set(entryYear, entryMonth, entryDay, EARLY_BIRD_ENTRY_LOWER, 0);
		
		//calculate valid entry time (higher bound) based on actual entry
		Calendar validEntryHigherBound = Calendar.getInstance();
		validEntryHigherBound.clear();
		validEntryHigherBound.set(entryYear, entryMonth, entryDay, EARLY_BIRD_ENTRY_HIGHER, 0);
		
		//check entry time
		if (!entry.after(validEntryLowerBound) || !entry.before(validEntryHigherBound))
			return false;
		
		//calculate valid exit time (lower bound) based on actual entry
		Calendar validExitLowerBound = Calendar.getInstance();
		validExitLowerBound.clear();
		validExitLowerBound.set(entryYear, entryMonth, entryDay, EARLY_BIRD_EXIT_LOWER_H, EARLY_BIRD_EXIT_LOWER_M);
		
		//calculate valid exit time (higher bound) based on actual entry
		Calendar validExitHigherBound = Calendar.getInstance();
		validExitHigherBound.clear();
		validExitHigherBound.set(entryYear, entryMonth, entryDay, EARLY_BIRD_EXIT_HIGHER_H, EARLY_BIRD_EXIT_HIGHER_M);		

		//check exit time
		if (!exit.after(validExitLowerBound) || !exit.before(validExitHigherBound))
			return false;
		return true;
	}
	
	private boolean isWeekendRate(Calendar entry, Calendar exit){
		int entryDay = entry.get(Calendar.DAY_OF_WEEK);
		int exitDay = exit.get(Calendar.DAY_OF_WEEK);
		
		//entry on the weekend
		if (entryDay == Calendar.SATURDAY || entryDay == Calendar.SUNDAY)
			//exit on the weekend
			if (exitDay == Calendar.SATURDAY || exitDay == Calendar.SUNDAY)
				//entry and exit on the same weekend!
				if (entry.get(Calendar.YEAR) == exit.get(Calendar.YEAR) && 
					exit.get(Calendar.DAY_OF_YEAR) - entry.get(Calendar.DAY_OF_YEAR) < 3)
					return true;
		return false;
	}
}
