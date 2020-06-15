package com.winway.comic.util;

import java.text.DecimalFormat;
import java.util.Random;

public class T {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getScore());
	}

	public static float getScore() {
		float min = 9f;
		float max = 9.8f;
		DecimalFormat decimalFormat=new DecimalFormat(".0");
		float floatBounded = min + new Random().nextFloat() * (max - min);
		float result= Float.parseFloat(decimalFormat.format(floatBounded)) ;
		 return result;
	}
}
