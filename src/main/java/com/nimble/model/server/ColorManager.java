package com.nimble.model.server;

import com.nimble.exceptions.NoAvailableColorException;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorManager {

	private final Color red = new Color(0xAB3A45);
	private final Color purple = new Color(0x582642);
	private final Color green = new Color(0x4E7971);
	private final Color orange = new Color(0xDD844C);

	private final Map<Color, Boolean> colors = new HashMap<>() {
		{
			put(red, true);
			put(purple, true);
			put(green, true);
			put(orange, true);
		}
	};

	public Color getColor() throws NoAvailableColorException {
		for (Color color : colors.keySet()) {
			if (colors.get(color)) {
				colors.put(color, false);
				return color;
			}
		}
		throw new NoAvailableColorException();
	}

	public void restoreColor(Color color) {
		colors.put(color, true);
	}
}
