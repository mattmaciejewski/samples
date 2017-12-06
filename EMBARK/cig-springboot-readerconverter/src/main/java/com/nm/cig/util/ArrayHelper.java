package com.nm.cig.util;

import java.security.SecureRandom;

public class ArrayHelper {
	private static final SecureRandom random = new SecureRandom();

	public static <T> T randomElement(T[] array) {
		if (array == null)
			return null;
		if (array.length == 0)
			return null;

		int rnd = random.nextInt(array.length);
		return array[rnd];
	}
}
