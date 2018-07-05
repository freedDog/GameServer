package com.jbm.game.engine.struct;

import java.util.HashMap;
import java.util.Map;

import com.jbm.game.engine.cache.cooldown.Cooldown;

public abstract class Person {
	
	protected transient Map<String, Cooldown> cooldowns=new HashMap<>();

	public Map<String, Cooldown> getCooldowns() {
		return cooldowns;
	}

	public void setCooldowns(Map<String, Cooldown> cooldowns) {
		this.cooldowns = cooldowns;
	}
	
	
}
