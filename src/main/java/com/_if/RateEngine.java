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
	private final String NIGHT_RATE = "NightRate";
	private final String STANDARD_RATE = "StandardRate";
	
	public Rate calculate(Calendar entry, Calendar exit){
		Rate rate = null;
		if (isEarlyBird(entry, exit)){
			rate = new Rate(EARLY_BIRD, 15d);
		}
		else if (isWeekendRate(entry, exit)) {
			rate = new Rate(WEEKEND_RATE, 13d);
		}
		else if (isNightRate(entry, exit)) {
			rate = new Rate(NIGHT_RATE, 18d);
		}
		else 
			rate = getStandardRate(entry, exit);
		
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
	
	private boolean isNightRate(Calendar entry, Calendar exit){
		int entryDay = entry.get(Calendar.DAY_OF_WEEK);
		int entryHour = entry.get(Calendar.HOUR_OF_DAY);
		int exitHour = exit.get(Calendar.HOUR_OF_DAY);
		
		//entry on a weekday
		if (entryDay != Calendar.SATURDAY && entryDay != Calendar.SUNDAY) {
			//entry after 6pm
			if (entryHour >= 18) {
				//entry and exit on the same day
				if (entry.get(Calendar.YEAR) == exit.get(Calendar.YEAR) && 
						exit.get(Calendar.DAY_OF_YEAR) == entry.get(Calendar.DAY_OF_YEAR))
					return true;
				//exit on the following day before 6am
				if (entry.get(Calendar.YEAR) == exit.get(Calendar.YEAR) && 
						exit.get(Calendar.DAY_OF_YEAR) - entry.get(Calendar.DAY_OF_YEAR) == 1 &&
						exitHour < 6)
					return true;
			}
		}
		return false;
	}
	
	private Rate getStandardRate(Calendar entry, Calendar exit) {
		//same day entry and exit
		if (entry.get(Calendar.YEAR) == exit.get(Calendar.YEAR) && 
				exit.get(Calendar.DAY_OF_YEAR) == entry.get(Calendar.DAY_OF_YEAR)){
			int hours = exit.get(Calendar.HOUR_OF_DAY) - entry.get(Calendar.HOUR_OF_DAY);
			double price = 0;
			if (hours == 0) price = 5; //less than 1 hour
			else if (hours >= 1 && hours < 2) price = 10; //between 1 and 2 hours
			else if (hours >= 2 && hours < 3) price = 15; //between 2 and 3 hours
			else price = 20; // more than 3 hours
			return new Rate(STANDARD_RATE, price);
		}
			
		int fullDays = fullDaysInBetween(entry, exit);
		int entryDayHours = 24 - entry.get(Calendar.HOUR_OF_DAY);
		int exitDayHours = exit.get(Calendar.HOUR_OF_DAY);
		double price = fullDays * 20; //full days price
		//entry day price
		if (entryDayHours == 0) price += 5; //less than 1 hour
		else if (entryDayHours >= 1 && entryDayHours < 2) price += 10; //between 1 and 2 hours
		else if (entryDayHours >= 2 && entryDayHours < 3) price += 15; //between 2 and 3 hours
		else price += 20; // more than 3 hours
		//exit day price
		if (exitDayHours == 0) price += 5; //less than 1 hour
		else if (exitDayHours >= 1 && exitDayHours < 2) price += 10; //between 1 and 2 hours
		else if (exitDayHours >= 2 && exitDayHours < 3) price += 15; //between 2 and 3 hours
		else price += 20; // more than 3 hours
		return new Rate(STANDARD_RATE, price);
	}
	
	private int fullDaysInBetween(Calendar entry, Calendar exit){
		int entryYear = entry.get(Calendar.YEAR);
		int entryMonth = entry.get(Calendar.MONTH);
		int entryDay = entry.get(Calendar.DAY_OF_MONTH);
		int entryYYYYMMDD = (entryYear * 10000) + (entryMonth * 100) + entryDay;
		
		int exitYear = exit.get(Calendar.YEAR);
		int exitMonth = exit.get(Calendar.MONTH);
		int exitDay = exit.get(Calendar.DAY_OF_MONTH);
		int exitYYYYMMDD = (exitYear * 10000) + (exitMonth * 100) + exitDay;
		
		int diff = exitYYYYMMDD - entryYYYYMMDD;
		
		if (diff <= 1) return 0;
		return --diff;
	}
}
