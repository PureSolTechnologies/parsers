package com.puresoltechnologies.parsers.ust;

public class NameTranslator {

	public static String getProductionClassName(String productionName) {
		StringBuffer buffer = new StringBuffer(productionName);
		removeLeadingUnderlines(buffer);
		makeFirstLetterUpperCase(buffer);
		replaceDashWithUpperCase(buffer);
		removeTrailingUnderlines(buffer);
		return buffer.toString();
	}

	private static void removeLeadingUnderlines(StringBuffer buffer) {
		while (buffer.toString().startsWith("_")) {
			buffer.delete(0, 1);
		}
	}

	private static void replaceDashWithUpperCase(StringBuffer buffer) {
		char chars[] = new char[1];
		int index = buffer.indexOf("-");
		while (index >= 0) {
			buffer.getChars(index + 1, index + 2, chars, 0);
			buffer.delete(index, index + 1);
			if (Character.isLowerCase(chars[0])) {
				chars[0] = Character.toUpperCase(chars[0]);
				buffer.replace(index, index + 1, new String(chars));
			}
			index = buffer.indexOf("-");
		}
	}

	private static void makeFirstLetterUpperCase(StringBuffer buffer) {
		char chars[] = new char[1];
		buffer.getChars(0, 1, chars, 0);
		if (Character.isLowerCase(chars[0])) {
			chars[0] = Character.toUpperCase(chars[0]);
			buffer.replace(0, 1, new String(chars));
		}
	}

	private static void removeTrailingUnderlines(StringBuffer buffer) {
		while (buffer.toString().endsWith("_")) {
			int bufferLength = buffer.length();
			buffer.delete(bufferLength - 1, bufferLength);
		}
	}
}
