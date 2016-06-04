package com.example.jackson.gameuebung_3.math;

public class MathHelper {
	
	public static float clamp(float value, float min, float max) {
		value = Math.min(value, max);
		value = Math.max(value, min);
		return value;
	}
	
}
