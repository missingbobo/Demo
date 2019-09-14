package com.example.calbirthdaylib;

import java.io.Serializable;

public class BooleanHolder implements Serializable {
	private boolean value;
	private boolean interupt;
	private boolean canInter = false;

	public boolean isInterupt() {
		return interupt;
	}

	public void setInterupt(boolean interupt) {
		this.interupt = interupt;
	}

	public boolean isCanInter() {
		return canInter;
	}

	public void setCanInter(boolean canInter) {
		this.canInter = canInter;
	}
	public BooleanHolder(){
		this(false);
	}
	

	public BooleanHolder(boolean val){
		this.value = val;
	}
	
	public void setValue(boolean val){
		this.value = val;
	}
	
	public boolean getValue(){
		return this.value;
	}
}
