package org.ice1000.jimgui;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

/**
 * @author ice1000
 * @since v0.1
 */
public final class JImStyle extends JImGuiStyleGen {
	/**
	 * package-private by design
	 *
	 * @param nativeObjectPtr native pointer {@code ImStyle*}
	 */
	JImStyle(long nativeObjectPtr) {
		super(nativeObjectPtr);
	}

	public @NotNull JImVec4 getColor(@MagicConstant(valuesFromClass = JImStyleColors.class) int index) {
		return new JImVec4(getColor0(nativeObjectPtr, index));
	}

	static native long getColor0(
			long nativeObjectPtr,
			@MagicConstant(valuesFromClass = JImStyleColors.class) int index);
}
