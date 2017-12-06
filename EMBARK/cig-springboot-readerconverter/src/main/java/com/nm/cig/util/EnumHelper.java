package com.nm.cig.util;

import java.security.SecureRandom;

public class EnumHelper {
	private static final SecureRandom random = new SecureRandom();

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {

		// http://stackoverflow.com/a/14257525
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}
}
