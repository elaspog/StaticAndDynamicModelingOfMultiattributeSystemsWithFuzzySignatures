package net.prokhyon.modularfuzzy.fuzzySet.util;

import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class ExtendedNumberStringConverter extends StringConverter<Number> {

	private static NumberStringConverter converter = new NumberStringConverter();

	@Override
	public String toString(Number object) {

		return converter.toString(object);
	}

	@Override
	public Number fromString(String string) {

		try {
			Number number = converter.fromString(string);
			if (number.floatValue() < 0.0)
				number = 0.0;
			else if (number.floatValue() > 1.0)
				number = 1.0;
			return number;
		} catch (Exception e) {
			return 0.0;
		}
	}
}
