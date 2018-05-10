@file:Suppress("unused")

package org.ice1000.gradle

import org.intellij.lang.annotations.Language

/**
 * @author ice1000
 */
open class GenIOTask : GenTask("JImGuiIOGen", "imgui_io") {
	init {
		description = "Generate binding for ImGui::GetIO"
	}

	@Language("JAVA")
	override val userCode = "@Contract(pure = true) public static @NotNull $className getInstance(@NotNull JImGui owner) { return owner.getIO(); }"

	override fun java(javaCode: StringBuilder) {
		functions.forEach { genFun(javaCode, it) }
		primitiveMembers.forEach { (type, name, annotation) ->
			javaCode
					.javadoc(name)
					.append('\t')
					.appendln(javaPrimitiveGetter(type, name, annotation))
					.javadoc(name)
					.appendln("\tpublic native void set$name($annotation$type newValue);")
		}
		booleanMembers.forEach {
			javaCode
					.javadoc(it)
					.append("\tpublic native boolean is").append(it).appendln("();")
					.javadoc(it)
					.append("\tpublic native void set").append(it).appendln("(boolean newValue);")
		}
		stringMembers.forEach {
			javaCode
					.append("\tprivate static native void set")
					.append(it)
					.appendln("(byte[]newValue);")
					.javadoc(it)
					.append("\tpublic void set")
					.append(it)
					.append("(@NotNull String newValue){set")
					.append(it)
					.appendln("(getBytes(newValue));}")
		}
	}

	override fun `c++`(cppCode: StringBuilder) {
		primitiveMembers.joinLinesTo(cppCode) { (type, name) -> `c++PrimitiveAccessor`(type, name, `c++Expr`(name)) }
		booleanMembers.joinLinesTo(cppCode) { `c++BooleanGetter`(it, `c++Expr`(it)) }
		booleanMembers.joinLinesTo(cppCode) { `c++BooleanSetter`(it, `c++Expr`(it)) }
		functions.forEach { (name, type, params) -> `genFunC++`(params, name, type, cppCode) }
		stringMembers.joinLinesTo(cppCode) {
			val param = string(name = it.decapitalize(), default = "null")
			val (init, deinit) = param.surrounding()
			`c++StringedFunction`(
					name = "set$it",
					params = listOf(param),
					type = null,
					`c++Expr` = `c++Expr`(it) + " = ${param.`c++Expr`()}",
					init = "$JNI_FUNCTION_INIT $init",
					deinit = "$deinit $JNI_FUNCTION_CLEAN")
		}
	}

	override val `c++Prefix`: String get() = "ImGui::GetIO()."
	private fun `c++Expr`(it: String) = "ImGui::GetIO().$it"

	private val functions = listOf(Fun("addInputCharacter", p("character", "short")))
	private val stringMembers = listOf(
			"IniFilename",
			"LogFilename"
	)

	private val booleanMembers = listOf(
			"FontAllowUserScaling",
			"OptMacOSXBehaviors",
			"OptCursorBlink",
			"MouseDrawCursor",
			"KeyCtrl",
			"KeyShift",
			"KeyAlt",
			"KeySuper",
			"WantCaptureMouse",
			"WantCaptureKeyboard",
			"WantTextInput",
			"WantSetMousePos",
			"WantSaveIniSettings",
			"NavActive",
			"NavVisible")

	private val primitiveMembers = listOf(
			PPT("int", "MetricsRenderVertices"),
			PPT("int", "MetricsRenderIndices"),
			PPT("int", "MetricsActiveWindows"),
			PPT("int", "ConfigFlags", "@MagicConstant(flagsFromClass = JImConfigFlags.class)"),
			PPT("int", "BackendFlags", "@MagicConstant(flagsFromClass = JImBackendFlags.class)"),
			PPT("float", "MouseDoubleClickTime"),
			PPT("float", "MouseDoubleClickMaxDist"),
			PPT("float", "KeyRepeatDelay"),
			PPT("float", "KeyRepeatRate"),
			PPT("float", "FontGlobalScale"),
			PPT("float", "MouseWheel"),
			PPT("float", "MouseWheelH"),
			PPT("float", "Framerate"),
			PPT("float", "IniSavingRate"))
}
