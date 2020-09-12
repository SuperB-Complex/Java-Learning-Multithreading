package tta.basics.dayfour.one;

public class Calculator {
	private static final char PLUS = '+';
	private static final char MINUS = '-';

	public static int compute(String equation) {
		int result = 0, signal = 1, index = 0, length = equation.length();
		while (index < length) {
			char current = equation.charAt(index++);
			if (Character.isDigit(current)) {
				result += signal * (current - '0');
			} else {
				if (current == MINUS) {
					signal = -1;
					continue;
				} else if (current == PLUS) {
					signal = 1;
					continue;
				} else {
					continue;
				}
			}
		}
		return result;
	}
}
