// Generated from Xml.g4 by ANTLR 4.1
package org.chw.xml;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link XmlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface XmlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link XmlParser#process}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcess(@NotNull XmlParser.ProcessContext ctx);

	/**
	 * Visit a parse tree produced by {@link XmlParser#cdata}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCdata(@NotNull XmlParser.CdataContext ctx);

	/**
	 * Visit a parse tree produced by {@link XmlParser#comm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComm(@NotNull XmlParser.CommContext ctx);

	/**
	 * Visit a parse tree produced by {@link XmlParser#text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(@NotNull XmlParser.TextContext ctx);

	/**
	 * Visit a parse tree produced by {@link XmlParser#attributeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeValue(@NotNull XmlParser.AttributeValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link XmlParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(@NotNull XmlParser.RootContext ctx);

	/**
	 * Visit a parse tree produced by {@link XmlParser#singleNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleNode(@NotNull XmlParser.SingleNodeContext ctx);

	/**
	 * Visit a parse tree produced by {@link XmlParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(@NotNull XmlParser.AttributeContext ctx);

	/**
	 * Visit a parse tree produced by {@link XmlParser#dtd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDtd(@NotNull XmlParser.DtdContext ctx);

	/**
	 * Visit a parse tree produced by {@link XmlParser#complexNode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexNode(@NotNull XmlParser.ComplexNodeContext ctx);
}