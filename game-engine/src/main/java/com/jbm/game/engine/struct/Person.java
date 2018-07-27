package com.jbm.game.engine.struct;

import java.util.HashMap;
import java.util.Map;

import com.jbm.game.engine.cache.cooldown.Cooldown;

public abstract class Person {
	
	protected long id;
	
	protected transient Map<String, Cooldown> cooldowns=new HashMap<>();

	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Map<String, Cooldown> getCooldowns() {
		return cooldowns;
	}

	public void setCooldowns(Map<String, Cooldown> cooldowns) {
		this.cooldowns = cooldowns;
	}
	
	
}
