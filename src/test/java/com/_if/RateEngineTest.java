package com._if;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com._if.model.Rate;

public class RateEngineTest {

	RateEngine engine;
	private final String EARLY_BIRD = "EarlyBird";
	private final String WEEKEND_RATE = "WeekendRate";
	private final String NIGHT_RATE = "NightRate";
	private final String STANDARD_RATE = "StandardRate";
	
	@Before
	public void init(){
		engine = new RateEngine();
	}
	
	@Test
	public void testIsEarlyBird(){
		Calendar entry = Calendar.getInstance();
		entry.clear();
		entry.set(2018, 5, 26, 7, 0);
		Calendar exit = Calendar.getInstance();
		exit.clear();
		exit.set(2018, 5, 26, 16, 0);
		
		Rate rate = engine.calculate(entry, exit);
		Assert.assertTrue(rate.getName().equals(EARLY_BIRD));
	}
	
	@Test
	public void testIsWeekendRate(){
		Calendar entry = Calendar.getInstance();
		entry.clear();
		entry.set(2018, 5, 16); //Saturday
		Calendar exit = Calendar.getInstance();
		exit.clear();
		exit.set(2018, 5, 17); //Sunday
		
		Rate rate = engine.calculate(entry, exit);
		Assert.assertTrue(rate.getName().equals(WEEKEND_RATE));
	}
	
	@Test
	public void testIsNightRate(){
		Calendar entry = Calendar.getInstance();
		entry.clear();
		entry.set(2018, 5, 15, 19, 0); //Friday 7pm 
		Calendar exit = Calendar.getInstance();
		exit.clear();
		exit.set(2018, 5, 16, 5, 0); //Saturday 5am
		
		Rate rate = engine.calculate(entry, exit);
		Assert.assertTrue(rate.getName().equals(NIGHT_RATE));
	}
	
	@Test
	public void testIsStandardRate(){
		Calendar entry = Calendar.getInstance();
		entry.clear();
		entry.set(2018, 5, 15, 19, 0); //Friday 7pm 
		Calendar exit = Calendar.getInstance();
		exit.clear();
		exit.set(2018, 5, 18, 5, 0); //Monday 5am
		
		Rate rate = engine.calculate(entry, exit);
		Assert.assertTrue(rate.getName().equals(STANDARD_RATE));
		Assert.assertTrue(rate.getPrice() == 80);
	}
}
