/*
 * Use https://jsfiddle.net/r2kw9v6b/29/
 */
package org.ice1000.gradle

import org.gradle.api.tasks.TaskAction

abstract class GenFlagTask(className: String, private vararg val list: Pair<String, String>)
	: GenJavaTask(className, packageName = "org.ice1000.jimgui.flag"), Runnable {
	@TaskAction
	override fun run() = buildString {
		GenGenTask.checkParserInitialized(project)
		append(prefixInterfacedJava)
		list.forEach { (name, value) ->
			val keyName = "${className.replace("JIm", "imGui")}_$name"
			val comment = GenGenTask.parser.map[keyName]
			if (comment != null) append("\t/**").append(comment).appendln("*/")
			append('\t')
			genStatement(name, value)
			appendln(";")
		}
		appendln("\tenum Type implements Flag {")
		preType()
		list.forEach { (name, _) ->
			append("\t\t").append(name).append("(").append(className).append(".").append(name).appendln("),")
		}
		appendln("\t\t;\n\t\tpublic final int flag;")
				.appendln("\t\tType(int flag) { this.flag = flag; }")
				.appendln("\t\t@Override public int get() { return flag; }")
		appendln("\t}").appendln('}')
	}.let { targetJavaFile.writeText(it) }

	open fun StringBuilder.preType() {
	}

	private fun StringBuilder.genStatement(name: String, value: String) {
		append("int ")
		append(name)
		append(" = ")
		append(value)
	}
}

open class GenTabBarFlags : GenFlagTask(
		"JImTabBarFlags",
		"None" to "0",
		"Reorderable" to "1",
		"AutoSelectNewTabs" to "1 << 1",
		"TabListPopupButton" to "1 << 2",
		"NoCloseWithMiddleMouseButton" to "1 << 3",
		"NoTabListScrollingButtons" to "1 << 4",
		"NoTooltip" to "1 << 5",
		"FittingPolicyResizeDown" to "1 << 6",
		"FittingPolicyScroll" to "1 << 7",
		"FittingPolicyMask" to "FittingPolicyResizeDown | FittingPolicyScroll",
		"FittingPolicyDefault" to "FittingPolicyResizeDown",
)

open class GenColorEditFlags : GenFlagTask(
		"JImColorEditFlags",
		"Nothing" to "0", // special -- the C++ version is `None`
		"NoAlpha" to "1 << 1",
		"NoPicker" to "1 << 2",
		"NoOptions" to "1 << 3",
		"NoSmallPreview" to "1 << 4",
		"NoInputs" to "1 << 5",
		"NoTooltip" to "1 << 6",
		"NoLabel" to "1 << 7",
		"NoSidePreview" to "1 << 8",
		"NoDragDrop" to "1 << 9",
		"NoBorder" to "1 << 10",
		"AlphaBar" to "1 << 16",
		"AlphaPreview" to "1 << 17",
		"AlphaPreviewHalf" to "1 << 18",
		"HDR" to "1 << 19",
		"DisplayRGB" to "1 << 20",
		"DisplayHSV" to "1 << 21",
		"DisplayHex" to "1 << 22",
		"Uint8" to "1 << 23",
		"Float" to "1 << 24",
		"PickerHueBar" to "1 << 25",
		"PickerHueWheel" to "1 << 26",
		"InputRGB" to "1 << 27",
		"InputHSV" to "1 << 28",
		"OptionsDefault" to "Uint8 | InputRGB | PickerHueBar",
)

open class GenBackendFlags : GenFlagTask(
		"JImBackendFlags",
		"None" to "0",
		"HasGamepad" to "1 << 0",
		"HasMouseCursors" to "1 << 1",
		"HasSetMousePos" to "1 << 2",
		"RendererHasVtxOffset" to "1 << 3",
)

open class GenSliderFlags : GenFlagTask(
		"JImSliderFlags",
		"None" to "0",
		"AlwaysClamp" to "1 << 4",
		"Logarithmic" to "1 << 5",
		"NoRoundToFormat" to "1 << 6",
		"NoInput" to "1 << 7",
		"InvalidMask_" to "0x7000000F",
)

open class GenComboFlags : GenFlagTask(
		"JImComboFlags",
		"None" to "0",
		"PopupAlignLeft" to "1 << 0",
		"HeightSmall" to "1 << 1",
		"HeightRegular" to "1 << 2",
		"HeightLarge" to "1 << 3",
		"HeightLargest" to "1 << 4",
		"NoArrowButton" to "1 << 5",
		"NoPreview" to "1 << 6",
)

open class GenFontAtlasFlags : GenFlagTask(
		"JImFontAtlasFlags",
		"None" to "0",
		"NoPowerOfTwoHeight" to "1 << 0",
		"NoMouseCursors" to "1 << 1",
		"NoBakedLines" to "1 << 2",
)

open class GenTreeNodeFlags : GenFlagTask(
		"JImTreeNodeFlags",
		"Nothing" to "0",
		"Selected" to "1 << 0",
		"Framed" to "1 << 1",
		"AllowItemOverlap" to "1 << 2",
		"NoTreePushOnOpen" to "1 << 3",
		"NoAutoOpenOnLog" to "1 << 4",
		"DefaultOpen" to "1 << 5",
		"OpenOnDoubleClick" to "1 << 6",
		"OpenOnArrow" to "1 << 7",
		"Leaf" to "1 << 8",
		"Bullet" to "1 << 9",
		"FramePadding" to "1 << 10",
		"SpanAvailWidth" to "1 << 11",
		"SpanFullWidth" to "1 << 12",
		"NavLeftJumpsBackHere" to "1 << 13",
		"CollapsingHeader" to "Framed | NoTreePushOnOpen | NoAutoOpenOnLog",
)

open class GenInputTextFlags : GenFlagTask(
		"JImInputTextFlags",
		"Nothing" to "0",
		"CharsDecimal" to "1 << 0",
		"CharsHexadecimal" to "1 << 1",
		"CharsUppercase" to "1 << 2",
		"CharsNoBlank" to "1 << 3",
		"AutoSelectAll" to "1 << 4",
		"EnterReturnsTrue" to "1 << 5",
		"CallbackCompletion" to "1 << 6",
		"CallbackHistory" to "1 << 7",
		"CallbackAlways" to "1 << 8",
		"CallbackCharFilter" to "1 << 9",
		"AllowTabInput" to "1 << 10",
		"CtrlEnterForNewLine" to "1 << 11",
		"NoHorizontalScroll" to "1 << 12",
		"AlwaysInsertMode" to "1 << 13",
		"ReadOnly" to "1 << 14",
		"Password" to "1 << 15",
		"NoUndoRedo" to "1 << 16",
		"CharsScientific" to "1 << 17",
		"CallbackResize" to "1 << 18",
		"CallbackEdit" to "1 << 19",
		"Multiline" to "1 << 20",
		"NoMarkEdited" to "1 << 21",
)

open class GenWindowFlags : GenFlagTask(
		"JImWindowFlags",
		"Nothing" to "0",
		"NoTitleBar" to "1 << 0",
		"NoResize" to "1 << 1",
		"NoMove" to "1 << 2",
		"NoScrollbar" to "1 << 3",
		"NoScrollWithMouse" to "1 << 4",
		"NoCollapse" to "1 << 5",
		"AlwaysAutoResize" to "1 << 6",
		"NoBackground" to "1 << 7",
		"NoSavedSettings" to "1 << 8",
		"NoMouseInputs" to "1 << 9",
		"MenuBar" to "1 << 10",
		"HorizontalScrollbar" to "1 << 11",
		"NoFocusOnAppearing" to "1 << 12",
		"NoBringToFrontOnFocus" to "1 << 13",
		"AlwaysVerticalScrollbar" to "1 << 14",
		"AlwaysHorizontalScrollbar" to "1<< 15",
		"AlwaysUseWindowPadding" to "1 << 16",
		"NoNavInputs" to "1 << 18",
		"NoNavFocus" to "1 << 19",
		"UnsavedDocument" to "1 << 20",
		"NoNav" to "NoNavInputs | NoNavFocus",
		"NoDecoration" to "NoTitleBar | NoResize | NoScrollbar | NoCollapse",
		"NoInputs" to "NoMouseInputs | NoNavInputs | NoNavFocus",
		"NavFlattened" to "1 << 23",
		"ChildWindow" to "1 << 24",
		"Tooltip" to "1 << 25",
		"Popup" to "1 << 26",
		"Modal" to "1 << 27",
		"ChildMenu" to "1 << 28",
)

open class GenMouseButton : GenFlagTask(
		"JImMouseButton",
		"Left" to "0",
		"Right" to "1",
		"Middle" to "2",
		"ExtraA" to "3",
		"ExtraB" to "4",
) {
	override fun StringBuilder.preType() {
		appendln("""		/**
		 * Used for reverse lookup results and enum comparison.
		 * Return the Nothing or Default flag to prevent errors.
		 */
		NoSuchFlag(-1),""")
	}
}