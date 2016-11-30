/**
 * 
 */
package com.jk.log4j2;

/**
 * @author kason
 *
 */
/*******************************************************************************
 * Copyright (c) 2012-11-21 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A string helper provides a set of utility methods to process the data.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2012-11-21
 */
public final class StringHelper {

	/** the default encrypt password you should set the 'encrypt.key' to System.properties when you want to use AES encode. **/
	private static final String ENCRYPTOR_PASSWD = "@@@ENCRYPTOR_PASSWD$$$";
	/** special char in html, this is the highest(int value is bigger) char **/
	public static final int HIGHEST_SPECIAL = '>' + 1;
	/** store the special html char **/
	public static char[][] specialCharactersRepresentation = new char[HIGHEST_SPECIAL][];

	static {
		specialCharactersRepresentation['&'] = "&amp;".toCharArray();
		specialCharactersRepresentation['<'] = "&lt;".toCharArray();
		specialCharactersRepresentation['>'] = "&gt;".toCharArray();
		specialCharactersRepresentation['"'] = "&#034;".toCharArray();
		specialCharactersRepresentation['\''] = "&#039;".toCharArray();
	}

	/**
	 * this constructor should never be called
	 */
	private StringHelper() {
	}

	/**
	 * concatenate the strings. Null string convert to blank string.
	 * @param strs
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String concat(String... strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			if (str == null) {
				str = "";
			}
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * <pre>
	 * replace the xml chars:
	 * <code>&</code> to <code>&amp;amp;</code>
	 * <code><</code> to <code>&amp;lt;</code>
	 * <code>></code> to <code>&amp;gt;</code>
	 * <code>"</code> to <code>&amp;#034;</code>
	 * <code>'</code> to <code>&amp;#039;</code>
	 * </pre>
	 * @param str xml or html
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2012-7-12
	 */
	public static String replaceXmlChar(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(str.length() * 2);
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);
			if (c < HIGHEST_SPECIAL) {
				char[] escaped = specialCharactersRepresentation[c];
				if (escaped != null) {
					sb.append(escaped);
				} else {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * <pre>
	 * replace string block to the specified value.
	 * the string block should wrap with {a-zA-z0-9}, such as: {name}, {helloWorld}
	 * this kind of the string block will not parse, such as: {name a}, {name,a}
	 * examples:
	 * hello {name} + "world" => hello world
	 * </pre>
	 * @param str
	 * @param replaces
	 * @param blank
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2012-7-12
	 */
	public static String replaceBlock(String str, Map<String, Object> replaces, String blank) {
		if (str == null || str.length() == 0) {
			return "";
		}
		replaces = replaces == null ? new HashMap<String, Object>() : replaces;
		StringBuffer sb = new StringBuffer(str);
		Matcher matcher = Pattern.compile("(\\{\\w*\\})").matcher(str);
		int offset = 0;
		for (; matcher.find();) {
			String group = matcher.group();
			String substring = group.substring(1, group.length() - 1);
			String replace = replaces.containsKey(substring) ? String.valueOf(replaces.get(substring))
					: (blank == null ? group : blank);
			int start = matcher.start(), end = matcher.end();
			sb.replace(Math.min(start + offset, sb.length()), Math.min(end + offset, sb.length()), replace);
			offset += replace.length() - (end - start);
		}
		return sb.toString();
	}

	/**
	 * <pre>
	 * replace string block to the specified value.
	 * the string block should wrap with {a-zA-z0-9}, such as: {name}, {helloWorld}
	 * this kind of the string block will not parse, such as: {name a}, {name,a}
	 * examples:
	 * hello {name} + "world" => hello world
	 * </pre>
	 * @param str
	 * @param replaces
	 * @param blank
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2012-7-12
	 */
	public static String replaceBlock(String str, Object[] replaces, String blank) {
		if (str == null || str.length() == 0) {
			return "";
		}
		int i = 0, len = replaces != null ? replaces.length : 0;
		StringBuffer sb = new StringBuffer(str);
		Matcher matcher = Pattern.compile("(\\{\\w*\\})").matcher(str);
		int offset = 0;
		for (; matcher.find(); i++) {
			String replace = i < len ? String.valueOf(replaces[i]) : (blank == null ? matcher.group() : blank);
			int start = matcher.start(), end = matcher.end();
			sb.replace(Math.min(start + offset, sb.length()), Math.min(end + offset, sb.length()), replace);
			offset += replace.length() - (end - start);
		}
		return sb.toString();
	}

	/**
	 * concatenate strings with path separator. null value convert to blank string.
	 * @param paths
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String pathConcat(String... paths) {
		if (paths == null || paths.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0, j = paths.length - 1; i < paths.length; i++) {
			String path = paths[i];
			if (path == null) {
				path = "";
			}
			sb.append(path);
			if (i != j) {
				sb.append(getFileSeparator());
			}
		}
		return pathBuild(sb.toString(), getFileSeparator());
	}

	/**
	 * <pre>
	 * build the beautiful path.
	 * example:
	 * c:\a\\\\b\\\c   =>  c:\a\b\c
	 * c:\a\/b\\\c   =>  c:\a\b\c
	 * /a\/b\\\c   =>  /a/b/c
	 * </pre>
	 * @param str
	 * @param fileSeparator
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2012-7-12
	 */
	public static String pathBuild(String str, String fileSeparator) {
		if (str == null || str.length() == 0) {
			return "";
		}
		String replace = fileSeparator;
		StringBuffer sb = new StringBuffer(str);
		{
			Matcher matcher = Pattern.compile("((\\\\+|/+)+)").matcher(str);
			int offset = 0;
			for (; matcher.find();) {
				int start = matcher.start(), end = matcher.end();
				sb.replace(Math.min(start + offset, sb.length()), Math.min(end + offset, sb.length()), replace);
				offset += replace.length() - (end - start);
			}
		}
		{
			Matcher matcher = Pattern.compile("((\\\\+|/+)+)").matcher(sb.toString());
			int offset = 0;
			for (; matcher.find();) {
				int start = matcher.start(), end = matcher.end();
				sb.replace(Math.min(start + offset, sb.length()), Math.min(end + offset, sb.length()), replace);
				offset += replace.length() - (end - start);
			}
		}
		return sb.toString();
	}

	/**
	 * @return file or path separator
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String getFileSeparator() {
		return File.separator;
	}

	/**
	 * null value will return the blank string .
	 * @param value
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String getNotNullValue(Object value) {
		return value == null ? "" : value.toString();
	}

	/**
	 * reverse the string.
	 * @param str
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String reverse(String str) {
		if (str == null) {
			return null;
		}
		return new StringBuilder(str).reverse().toString();
	}

	/**
	 * return the uuid string , defualt is 19 length uuid. 
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String uuid() {
		return minUUID();
	}

	/**
	 * calculate the digits.
	 * @param val
	 * @param digits
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	private static String digits(long val, int digits) {
		long hi = 1L << (digits * 4);
		return NumberHelper.toString(hi | (val & (hi - 1)), NumberHelper.MAX_RADIX).substring(1);
	}

	/** 
	 * generate 19 length uuid
	 * @return 
	 */
	public static String minUUID() {
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder();
		{
			long mostSignificantBits = uuid.getMostSignificantBits();
			sb.append(digits(mostSignificantBits >> 32, 8));
			sb.append(digits(mostSignificantBits >> 16, 4));
			sb.append(digits(mostSignificantBits, 4));
		}
		{
			long leastSignificantBits = uuid.getLeastSignificantBits();
			sb.append(digits(leastSignificantBits >> 48, 4));
			sb.append(digits(leastSignificantBits, 12));
		}
		return sb.toString();
	}

	/**
	 * sub a unicode string, and make sure not sub a half character.
	 * @param str
	 * @param byteLength
	 * @return
	 */
	public static String subUniCodeString(String str, int length) {
		if (str == null) {
			return null;
		}
		int byteCount = 0;//字节计数
		int charCount = 0;//字符计数
		for (int i = 0; i < length; i++) {
			if (i >= str.length()) {
				break;
			}
			charCount++;
			int charIndex = (int) str.charAt(i);
			if (charIndex > 128) {
				byteCount += 2;
			} else {
				byteCount += 1;
			}
			if (byteCount > length) {
				charCount--;
				break;
			}
		}
		if (charCount < str.length()) {
			return str.substring(0, charCount);
		}
		return str;
	}

	/**
	 * safe substring, without exception
	 * @param str
	 * @param length
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-9-25
	 */
	public static String substr(String str, int length) {
		if (str == null) {
			return str;
		}
		if (length < str.length()) {
			return str.substring(0, length);
		}
		return str;
	}

	/**
	 * <pre>
	 * 1. value is String: return value
	 * 2. value is null: return null
	 * 3. value is Object: return value.toString()
	 * </pre>
	 * @param value
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2014-8-23
	 */
	public static String valueOf(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}
		return value.toString();
	}

	static final String EX = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

	/**
	 * encode uri, use UTF-8 charset.
	 * @param input
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String encodeURIComponent(String input) {
		if (input == null) {
			return "";
		}
		int l = input.length();
		StringBuilder o = new StringBuilder(l * 3);
		for (int i = 0; i < l; i++) {
			String e = input.substring(i, i + 1);
			if (EX.indexOf(e) == -1) {
				byte[] b = new byte[0];
				try {
					b = e.getBytes("UTF-8");
				} catch (Exception e1) {
				}
				o.append(getHex(b));
				continue;
			}
			o.append(e);
		}
		return o.toString();
	}

	/**
	 * hex encode the data.
	 * @param buf
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-2-6
	 */
	public static String getHex(byte buf[]) {
		StringBuilder o = new StringBuilder(buf.length * 3);
		for (int i = 0; i < buf.length; i++) {
			int n = (int) buf[i] & 0xff;
			o.append("%");
			if (n < 0x10) {
				o.append("0");
			}
			o.append(Long.toString(n, 16).toUpperCase());
		}
		return o.toString();
	}

	/**
	 * <pre>
	 * convert url to standard form
	 * input : file:///c:/windows or file:///c:\\windows
	 * output: file:///c:/windows
	 * input : file://c:/windows  or file://c:\\windows
	 * output: file:///c:windows
	 * input : file:/c:/windows   or file:/c:\\windows
	 * output: file:///c:/windows
	 * input : null or blank
	 * output: null or blank
	 * </pre> 
	 * @param urlString
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-7-16
	 */
	public static String fixUrl(String urlString) {
		if (urlString == null || urlString.length() < 1 || urlString.indexOf(':') < 0) {
			return urlString;
		}
		int index = 0;
		if (urlString.indexOf(":///") < 1) {
			index = urlString.indexOf('/');
			if (urlString.indexOf("://") > -1) {
				urlString = urlString.substring(0, index) + "/" + urlString.substring(index);
			} else if (urlString.indexOf(":/") > -1) {
				urlString = urlString.substring(0, index) + "//" + urlString.substring(index);
			}
		}
		if ((index = urlString.indexOf(":///")) > -1) {
			urlString = urlString.substring(0, index + 3) + pathBuild(urlString.substring(index + 3), "/");
		}
		return urlString;
	}

	public static String cutLeft(String source, String toCut) {
		if (source == null || source.length() < 1) {
			return source;
		}
		int index = source.indexOf(toCut);
		return index > -1 ? (source.substring(index + toCut.length())) : source;
	}

	public static String cutRight(String source, String toCut) {
		if (source == null || source.length() < 1) {
			return source;
		}
		int index = source.lastIndexOf(toCut);
		return index > -1 ? (source.substring(0, index)) : source;
	}

	public static String addStart(String source, String toAdd) {
		if (source == null || source.length() < 1) {
			return source;
		}
		return source.startsWith(toAdd) ? source : (toAdd + source);
	}

	public static String addEnd(String source, String toAdd) {
		if (source == null || source.length() < 1) {
			return source;
		}
		return source.endsWith(toAdd) ? source : (source + toAdd);
	}

	@Deprecated
	public static String cutTo(String source, String toCut) {
		return cutLeft(source, toCut);
	}

	@Deprecated
	public static String cutOff(String source, String toCut) {
		return cutRight(source, toCut);
	}

	public static String trim(String source, String toTrim) {
		if (source == null || source.length() < 1) {
			return source;
		}
		return trimRight(trimLeft(source, toTrim), toTrim);
	}

	public static String trimRight(String source, String toTrim) {
		if (source == null || source.length() < 1) {
			return source;
		}
		int index = source.length() - 1;
		boolean breakPoint = false;
		while (!breakPoint && index > -1) {
			for (int i = toTrim.length() - 1; i > -1; i--) {
				if (toTrim.charAt(i) != source.charAt(index)) {
					index = index + (toTrim.length() - 1 - i);
					breakPoint = true;
					break;
				}
				index--;
			}
		}
		return index < 1 ? source : source.substring(0, index + 1);
	}

	public static String trimLeft(String source, String toTrim) {
		if (source == null || source.length() < 1) {
			return source;
		}
		int index = 0;
		boolean breakPoint = false;
		while (!breakPoint && index < source.length()) {
			for (int i = 0; i < toTrim.length(); i++) {
				if (toTrim.charAt(i) != source.charAt(index)) {
					index = index - i;
					breakPoint = true;
					break;
				}
				index++;
			}
		}
		return index < 1 ? source : source.substring(index);
	}

	/**
	 * test the text match the wild char "*"
	 * @param text    example: helloworld
	 * @param pattern example: hell*world
	 * @return 
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-4-8
	 */
	public static boolean wildCardMatch(String text, String pattern) {
		if (pattern == null || pattern.length() < 1 || "*".equals(pattern)) {
			return true;
		}
		if (text == null || text.length() < 1) {
			return false;
		}
		String[] cards = pattern.split("\\*");
		/*
		 * is starts with "*"
		 * because "a/*".split("\\*") == "*a/".split("\\*");
		 * so we need to handle this situation.
		 */
		boolean starStart = pattern.charAt(0) == '*';
		for (int i = 0; i < cards.length; i++) {
			String card = cards[i];
			int idx = -1;
			if (i == 0) {/* if starts with "*" then test the indexOf.*/
				if (starStart) {
					continue;
				} else {/*if not starts with "*", must match string start.*/
					idx = text.startsWith(card) ? 0 : -1;
				}
			} else {
				idx = text.indexOf(card);
			}
			if (idx == -1) {// not match
				return false;
			}
			text = text.substring(idx + card.length());
		}
		return true;
	}

	

	public static void main(String[] args) {
		System.out.println(pathBuild("file:///g:/a/b/c", "/"));
		System.out.println(subUniCodeString("我ABC汉DEF", 60));
		System.out.println(subUniCodeString("我ABC汉DEF", 4));
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			minUUID();
		}
		System.out.println((System.currentTimeMillis() - start));
		System.out.println(uuid());
		for (int i = 0; i < 256; i++) {
			System.out.print(Character.isLetter(i) ? (char) i : ' ');
			System.out.print('=');
			System.out.print(i);
			System.out.print(',');
		}
		//
		System.out.println("==================");
		System.out.println(trimLeft("aaaaaaaTestaaaaa", "aa"));
		System.out.println(trimRight("aaaaaaTestaaaaaaaaaaa", "aaa"));
		System.out.println(trim("aaaaaaaTestaaaaaaaa", "aaa"));
	}

}
