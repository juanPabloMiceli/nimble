package com.nimble.utils;

import com.nimble.model.enums.ValidCardColors;
import org.jetbrains.annotations.NotNull;

public class ColorUtils {

	public static @NotNull Boolean isValidCardColor(String color) {
		for (ValidCardColors validColor : ValidCardColors.values()) {
			if (validColor.name().equals(color)) {
				return true;
			}
		}
		return false;
	}

	public static @NotNull Boolean isValidPlayerColor(String color) {
		for (ValidCardColors validColor : ValidCardColors.values()) {
			if (validColor.name().equals(color)) {
				return true;
			}
		}
		return false;
	}

}
