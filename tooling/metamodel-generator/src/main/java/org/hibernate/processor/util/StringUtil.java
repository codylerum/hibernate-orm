/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.processor.util;

import java.util.Locale;

import static java.lang.Character.charCount;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toUpperCase;

/**
 * @author Hardy Ferentschik
 */
public final class StringUtil {
	private static final String NAME_SEPARATOR = ".";
	private static final String PROPERTY_PREFIX_GET = "get";
	private static final String PROPERTY_PREFIX_IS = "is";
	private static final String PROPERTY_PREFIX_HAS = "has";

	private StringUtil() {
	}

	public static String determineFullyQualifiedClassName(String defaultPackage, String name) {
		if ( isFullyQualified( name ) ) {
			return name;
		}
		else {
			return defaultPackage + NAME_SEPARATOR + name;
		}
	}

	public static boolean isFullyQualified(String name) {
		return name.contains( NAME_SEPARATOR );
	}

	public static String packageNameFromFqcn(String fqcn) {
		return fqcn.substring( 0, fqcn.lastIndexOf( NAME_SEPARATOR ) );
	}

	public static String classNameFromFqcn(String fqcn) {
		return fqcn.substring( fqcn.lastIndexOf( NAME_SEPARATOR ) + 1 );
	}

	public static boolean isProperty(String methodName, String returnTypeAsString) {
		if ( methodName == null || "void".equals( returnTypeAsString ) ) {
			return false;
		}

		if ( isValidPropertyName( methodName, PROPERTY_PREFIX_GET ) ) {
			return true;
		}

		if ( isValidPropertyName( methodName, PROPERTY_PREFIX_IS )
				|| isValidPropertyName( methodName, PROPERTY_PREFIX_HAS ) ) {
			return isBooleanGetter( returnTypeAsString );
		}

		return false;
	}

	private static boolean isBooleanGetter(String type) {
		return "Boolean".equals( type ) || "java.lang.Boolean".equals( type );
	}

	private static boolean isValidPropertyName(String name, String prefix) {
		if ( !name.startsWith( prefix ) ) {
			return false;
		}

		// the name has to start with the prefix and have at least one more character
		return name.length() >= prefix.length() + 1;
	}

	public static String getPropertyName(String name) {
		String tmp = name;
		if ( name.startsWith( PROPERTY_PREFIX_GET ) ) {
			tmp = name.replaceFirst( PROPERTY_PREFIX_GET, "" );
		}
		else if ( name.startsWith( PROPERTY_PREFIX_IS ) ) {
			tmp = name.replaceFirst( PROPERTY_PREFIX_IS, "" );
		}
		else if ( name.startsWith( PROPERTY_PREFIX_HAS ) ) {
			tmp = name.replaceFirst( PROPERTY_PREFIX_HAS, "" );
		}
		return decapitalize( tmp );
	}

	public static String decapitalize(String string) {
		if ( string == null || string.isEmpty() || startsWithSeveralUpperCaseLetters( string ) ) {
			return string;
		}
		else {
			return string.substring( 0, 1 ).toLowerCase(Locale.ROOT) + string.substring( 1 );
		}
	}

	public static String nameToFieldName(String name){
		return getUpperUnderscoreCaseFromLowerCamelCase(nameToMethodName(name));
	}

	public static String nameToMethodName(String name) {
		return name.replaceAll("[\\s.\\-!@#%=+/*^&|(){}\\[\\],]", "_");
	}

	public static String getUpperUnderscoreCaseFromLowerCamelCase(String lowerCamelCaseString) {
		final StringBuilder result = new StringBuilder();
		int position = 0;
		while ( position < lowerCamelCaseString.length() ) {
			final int codePoint = lowerCamelCaseString.codePointAt( position );
			if ( position>0 && isUpperCase( codePoint ) ) {
				result.append('_');
			}
			result.appendCodePoint( toUpperCase( codePoint ) );
			position += charCount( codePoint );
		}
		return result.toString();
	}

	private static boolean startsWithSeveralUpperCaseLetters(String string) {
		return string.length() > 1 &&
				Character.isUpperCase( string.charAt( 0 ) ) &&
				Character.isUpperCase( string.charAt( 1 ) );
	}
}
