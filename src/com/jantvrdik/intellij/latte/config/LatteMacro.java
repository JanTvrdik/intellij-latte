package com.jantvrdik.intellij.latte.config;

/**
 * Represents a single registered Latte macro.
 */
public class LatteMacro {

	/** macro name, e.g. 'foreach' */
	public final String name;

	/** macro type */
	public final Type type;

	public boolean deprecated = false;

	public boolean allowedModifiers;

	public String deprecatedMessage;

	public LatteMacro(String name, Type type) {
		this(name, type, true);
	}

	public LatteMacro(String name, Type type, boolean allowedModifiers) {
		this.name = name;
		this.type = type;
		this.allowedModifiers = allowedModifiers;
	}

	public enum Type {
		/** macro is available only as attribute macro without any prefix, e.g. 'n:href' or 'n:class' */
		ATTR_ONLY,

		/** macro is available as pair classic macro or attribute macro, possible prefixed with 'inner-' or 'tag', e.g. 'foreach' */
		PAIR,

		/** macro is available only as unpaired classic macro, e.g. 'var' or 'link' */
		UNPAIRED,

		AUTO_EMPTY,
	}
}
