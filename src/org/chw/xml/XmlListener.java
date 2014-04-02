// Generated from Xml.g4 by ANTLR 4.1
package org.chw.xml;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XmlParser}.
 */
public interface XmlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link XmlParser#process}.
	 * @param ctx the parse tree
	 */
	void enterProcess(@NotNull XmlParser.ProcessContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#process}.
	 * @param ctx the parse tree
	 */
	void exitProcess(@NotNull XmlParser.ProcessContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#cdata}.
	 * @param ctx the parse tree
	 */
	void enterCdata(@NotNull XmlParser.CdataContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#cdata}.
	 * @param ctx the parse tree
	 */
	void exitCdata(@NotNull XmlParser.CdataContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#comm}.
	 * @param ctx the parse tree
	 */
	void enterComm(@NotNull XmlParser.CommContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#comm}.
	 * @param ctx the parse tree
	 */
	void exitComm(@NotNull XmlParser.CommContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#url}.
	 * @param ctx the parse tree
	 */
	void enterText(@NotNull XmlParser.TextContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#url}.
	 * @param ctx the parse tree
	 */
	void exitText(@NotNull XmlParser.TextContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void enterAttributeValue(@NotNull XmlParser.AttributeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#attributeValue}.
	 * @param ctx the parse tree
	 */
	void exitAttributeValue(@NotNull XmlParser.AttributeValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(@NotNull XmlParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(@NotNull XmlParser.RootContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#singleNode}.
	 * @param ctx the parse tree
	 */
	void enterSingleNode(@NotNull XmlParser.SingleNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#singleNode}.
	 * @param ctx the parse tree
	 */
	void exitSingleNode(@NotNull XmlParser.SingleNodeContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(@NotNull XmlParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(@NotNull XmlParser.AttributeContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#dtd}.
	 * @param ctx the parse tree
	 */
	void enterDtd(@NotNull XmlParser.DtdContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#dtd}.
	 * @param ctx the parse tree
	 */
	void exitDtd(@NotNull XmlParser.DtdContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#space}.
	 * @param ctx the parse tree
	 */
	void enterSpace(@NotNull XmlParser.SpaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#space}.
	 * @param ctx the parse tree
	 */
	void exitSpace(@NotNull XmlParser.SpaceContext ctx);

	/**
	 * Enter a parse tree produced by {@link XmlParser#complexNode}.
	 * @param ctx the parse tree
	 */
	void enterComplexNode(@NotNull XmlParser.ComplexNodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XmlParser#complexNode}.
	 * @param ctx the parse tree
	 */
	void exitComplexNode(@NotNull XmlParser.ComplexNodeContext ctx);
}