// Generated from Xml.g4 by ANTLR 4.1
package org.chw.xml;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class XmlParser extends Parser
{
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int PROCESS = 1, CDATA = 2, COMMENT = 3, DTD = 4,
			ENTITY_REF = 5, CHAR_REF = 6, NAME = 7, BRACKET_L = 8,
			BRACKET_R = 9, SLASH = 10, EQUALS = 11, DOUBLE_QUOTE = 12,
			SINGLE_QUOTE = 13, WS = 14, BR = 15, OTHER = 16;
	public static final String[] tokenNames = { "<INVALID>", "PROCESS", "CDATA", "COMMENT", "DTD", "ENTITY_REF", "CHAR_REF", "NAME", "'<'", "'>'", "'/'", "'='", "'\"'", "'''", "WS", "BR", "OTHER" };
	public static final int RULE_root = 0, RULE_process = 1, RULE_dtd = 2,
			RULE_comm = 3, RULE_cdata = 4, RULE_singleNode = 5,
			RULE_complexNode = 6, RULE_attribute = 7, RULE_attributeValue = 8,
			RULE_text = 9, RULE_space = 10;
	public static final String[] ruleNames = { "root", "process", "dtd", "comm", "cdata", "singleNode", "complexNode", "attribute", "attributeValue", "text", "space" };

	@Override
	public String getGrammarFileName()
	{
		return "Xml.g4";
	}

	@Override
	public String[] getTokenNames()
	{
		return tokenNames;
	}

	@Override
	public String[] getRuleNames()
	{
		return ruleNames;
	}

	@Override
	public ATN getATN()
	{
		return _ATN;
	}

	private boolean checkEndTag(String name)
	{
		CommonTokenStream input = (CommonTokenStream) _input;
		if (input.LT(1).getText().equals("<"))
		{
			if (input.LT(2).getText().equals("/"))
			{
				if (input.LT(3).getText().equals(name))
				{
					return true;
				}
			}
		}
		return false;
	}

	public XmlParser(TokenStream input)
	{
		super(input);
		_interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	public static class RootContext extends ParserRuleContext
	{
		public List<CommContext> comm()
		{
			return getRuleContexts(CommContext.class);
		}

		public DtdContext dtd(int i)
		{
			return getRuleContext(DtdContext.class, i);
		}

		public List<SpaceContext> space()
		{
			return getRuleContexts(SpaceContext.class);
		}

		public ProcessContext process(int i)
		{
			return getRuleContext(ProcessContext.class, i);
		}

		public CommContext comm(int i)
		{
			return getRuleContext(CommContext.class, i);
		}

		public List<ProcessContext> process()
		{
			return getRuleContexts(ProcessContext.class);
		}

		public SpaceContext space(int i)
		{
			return getRuleContext(SpaceContext.class, i);
		}

		public ComplexNodeContext complexNode()
		{
			return getRuleContext(ComplexNodeContext.class, 0);
		}

		public SingleNodeContext singleNode()
		{
			return getRuleContext(SingleNodeContext.class, 0);
		}

		public List<DtdContext> dtd()
		{
			return getRuleContexts(DtdContext.class);
		}

		public RootContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_root;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterRoot(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitRoot(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitRoot(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final RootContext root() throws RecognitionException
	{
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		int _la;
		try
		{
			enterOuterAlt(_localctx, 1);
			{
				setState(28);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PROCESS) | (1L << COMMENT) | (1L << DTD) | (1L << WS) | (1L << BR))) != 0))
				{
					{
						setState(26);
						switch (_input.LA(1))
						{
							case PROCESS:
								{
									setState(22);
									process();
								}
								break;
							case DTD:
								{
									setState(23);
									dtd();
								}
								break;
							case COMMENT:
								{
									setState(24);
									comm();
								}
								break;
							case WS:
							case BR:
								{
									setState(25);
									space();
								}
								break;
							default:
								throw new NoViableAltException(this);
						}
					}
					setState(30);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(33);
				switch (getInterpreter().adaptivePredict(_input, 2, _ctx))
				{
					case 1:
						{
							setState(31);
							singleNode();
						}
						break;

					case 2:
						{
							setState(32);
							complexNode();
						}
						break;
				}
				setState(39);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << COMMENT) | (1L << WS) | (1L << BR))) != 0))
				{
					{
						setState(37);
						switch (_input.LA(1))
						{
							case COMMENT:
								{
									setState(35);
									comm();
								}
								break;
							case WS:
							case BR:
								{
									setState(36);
									space();
								}
								break;
							default:
								throw new NoViableAltException(this);
						}
					}
					setState(41);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class ProcessContext extends ParserRuleContext
	{
		public TerminalNode PROCESS()
		{
			return getToken(XmlParser.PROCESS, 0);
		}

		public ProcessContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_process;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterProcess(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitProcess(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitProcess(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final ProcessContext process() throws RecognitionException
	{
		ProcessContext _localctx = new ProcessContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_process);
		try
		{
			enterOuterAlt(_localctx, 1);
			{
				setState(42);
				match(PROCESS);
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class DtdContext extends ParserRuleContext
	{
		public TerminalNode DTD()
		{
			return getToken(XmlParser.DTD, 0);
		}

		public DtdContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_dtd;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterDtd(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitDtd(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitDtd(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final DtdContext dtd() throws RecognitionException
	{
		DtdContext _localctx = new DtdContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_dtd);
		try
		{
			enterOuterAlt(_localctx, 1);
			{
				setState(44);
				match(DTD);
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class CommContext extends ParserRuleContext
	{
		public TerminalNode COMMENT()
		{
			return getToken(XmlParser.COMMENT, 0);
		}

		public CommContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_comm;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterComm(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitComm(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitComm(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final CommContext comm() throws RecognitionException
	{
		CommContext _localctx = new CommContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_comm);
		try
		{
			enterOuterAlt(_localctx, 1);
			{
				setState(46);
				match(COMMENT);
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class CdataContext extends ParserRuleContext
	{
		public TerminalNode CDATA()
		{
			return getToken(XmlParser.CDATA, 0);
		}

		public CdataContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_cdata;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterCdata(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitCdata(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitCdata(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final CdataContext cdata() throws RecognitionException
	{
		CdataContext _localctx = new CdataContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_cdata);
		try
		{
			enterOuterAlt(_localctx, 1);
			{
				setState(48);
				match(CDATA);
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class SingleNodeContext extends ParserRuleContext
	{
		public Token tagName;

		public TerminalNode BRACKET_R()
		{
			return getToken(XmlParser.BRACKET_R, 0);
		}

		public SpaceContext space()
		{
			return getRuleContext(SpaceContext.class, 0);
		}

		public List<AttributeContext> attribute()
		{
			return getRuleContexts(AttributeContext.class);
		}

		public AttributeContext attribute(int i)
		{
			return getRuleContext(AttributeContext.class, i);
		}

		public TerminalNode NAME()
		{
			return getToken(XmlParser.NAME, 0);
		}

		public TerminalNode SLASH()
		{
			return getToken(XmlParser.SLASH, 0);
		}

		public TerminalNode BRACKET_L()
		{
			return getToken(XmlParser.BRACKET_L, 0);
		}

		public SingleNodeContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_singleNode;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterSingleNode(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitSingleNode(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitSingleNode(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final SingleNodeContext singleNode() throws RecognitionException
	{
		SingleNodeContext _localctx = new SingleNodeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_singleNode);
		int _la;
		try
		{
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(50);
				match(BRACKET_L);
				setState(51);
				((SingleNodeContext) _localctx).tagName = match(NAME);
				setState(55);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
				while (_alt != 2 && _alt != -1)
				{
					if (_alt == 1)
					{
						{
							{
								setState(52);
								attribute();
							}
						}
					}
					setState(57);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
				}
				setState(59);
				_la = _input.LA(1);
				if (_la == WS || _la == BR)
				{
					{
						setState(58);
						space();
					}
				}

				setState(61);
				match(SLASH);
				setState(62);
				match(BRACKET_R);
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class ComplexNodeContext extends ParserRuleContext
	{
		public Token tagName;

		public List<CommContext> comm()
		{
			return getRuleContexts(CommContext.class);
		}

		public DtdContext dtd(int i)
		{
			return getRuleContext(DtdContext.class, i);
		}

		public TerminalNode BRACKET_R(int i)
		{
			return getToken(XmlParser.BRACKET_R, i);
		}

		public TextContext text(int i)
		{
			return getRuleContext(TextContext.class, i);
		}

		public List<AttributeContext> attribute()
		{
			return getRuleContexts(AttributeContext.class);
		}

		public TerminalNode NAME(int i)
		{
			return getToken(XmlParser.NAME, i);
		}

		public List<ProcessContext> process()
		{
			return getRuleContexts(ProcessContext.class);
		}

		public List<ComplexNodeContext> complexNode()
		{
			return getRuleContexts(ComplexNodeContext.class);
		}

		public AttributeContext attribute(int i)
		{
			return getRuleContext(AttributeContext.class, i);
		}

		public List<SingleNodeContext> singleNode()
		{
			return getRuleContexts(SingleNodeContext.class);
		}

		public List<DtdContext> dtd()
		{
			return getRuleContexts(DtdContext.class);
		}

		public List<TerminalNode> BRACKET_L()
		{
			return getTokens(XmlParser.BRACKET_L);
		}

		public List<TextContext> text()
		{
			return getRuleContexts(TextContext.class);
		}

		public List<TerminalNode> BRACKET_R()
		{
			return getTokens(XmlParser.BRACKET_R);
		}

		public SpaceContext space()
		{
			return getRuleContext(SpaceContext.class, 0);
		}

		public ProcessContext process(int i)
		{
			return getRuleContext(ProcessContext.class, i);
		}

		public SingleNodeContext singleNode(int i)
		{
			return getRuleContext(SingleNodeContext.class, i);
		}

		public List<CdataContext> cdata()
		{
			return getRuleContexts(CdataContext.class);
		}

		public CommContext comm(int i)
		{
			return getRuleContext(CommContext.class, i);
		}

		public List<TerminalNode> NAME()
		{
			return getTokens(XmlParser.NAME);
		}

		public TerminalNode BRACKET_L(int i)
		{
			return getToken(XmlParser.BRACKET_L, i);
		}

		public CdataContext cdata(int i)
		{
			return getRuleContext(CdataContext.class, i);
		}

		public TerminalNode SLASH()
		{
			return getToken(XmlParser.SLASH, 0);
		}

		public ComplexNodeContext complexNode(int i)
		{
			return getRuleContext(ComplexNodeContext.class, i);
		}

		public ComplexNodeContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_complexNode;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterComplexNode(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitComplexNode(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitComplexNode(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final ComplexNodeContext complexNode() throws RecognitionException
	{
		ComplexNodeContext _localctx = new ComplexNodeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_complexNode);
		int _la;
		try
		{
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(64);
				match(BRACKET_L);
				setState(65);
				((ComplexNodeContext) _localctx).tagName = match(NAME);
				setState(69);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 7, _ctx);
				while (_alt != 2 && _alt != -1)
				{
					if (_alt == 1)
					{
						{
							{
								setState(66);
								attribute();
							}
						}
					}
					setState(71);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 7, _ctx);
				}
				setState(73);
				_la = _input.LA(1);
				if (_la == WS || _la == BR)
				{
					{
						setState(72);
						space();
					}
				}

				setState(75);
				match(BRACKET_R);
				setState(85);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
				while (_alt != 2 && _alt != -1)
				{
					if (_alt == 1)
					{
						{
							setState(83);
							switch (getInterpreter().adaptivePredict(_input, 9, _ctx))
							{
								case 1:
									{
										setState(76);
										process();
									}
									break;

								case 2:
									{
										setState(77);
										dtd();
									}
									break;

								case 3:
									{
										setState(78);
										comm();
									}
									break;

								case 4:
									{
										setState(79);
										cdata();
									}
									break;

								case 5:
									{
										setState(80);
										text();
									}
									break;

								case 6:
									{
										setState(81);
										singleNode();
									}
									break;

								case 7:
									{
										setState(82);
										complexNode();
									}
									break;
							}
						}
					}
					setState(87);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
				}
				setState(88);
				if (!(checkEndTag((((ComplexNodeContext) _localctx).tagName != null ? ((ComplexNodeContext) _localctx).tagName.getText() : null))))
					throw new FailedPredicateException(this, "checkEndTag($tagName.text)");
				setState(89);
				match(BRACKET_L);
				setState(90);
				match(SLASH);
				setState(91);
				match(NAME);
				setState(92);
				match(BRACKET_R);
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeContext extends ParserRuleContext
	{
		public Token name;
		public AttributeValueContext value;

		public TerminalNode SINGLE_QUOTE(int i)
		{
			return getToken(XmlParser.SINGLE_QUOTE, i);
		}

		public List<TerminalNode> SINGLE_QUOTE()
		{
			return getTokens(XmlParser.SINGLE_QUOTE);
		}

		public AttributeValueContext attributeValue()
		{
			return getRuleContext(AttributeValueContext.class, 0);
		}

		public List<SpaceContext> space()
		{
			return getRuleContexts(SpaceContext.class);
		}

		public TerminalNode EQUALS()
		{
			return getToken(XmlParser.EQUALS, 0);
		}

		public SpaceContext space(int i)
		{
			return getRuleContext(SpaceContext.class, i);
		}

		public TerminalNode NAME()
		{
			return getToken(XmlParser.NAME, 0);
		}

		public TerminalNode DOUBLE_QUOTE()
		{
			return getToken(XmlParser.DOUBLE_QUOTE, 0);
		}

		public AttributeContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_attribute;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterAttribute(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitAttribute(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitAttribute(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException
	{
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_attribute);
		int _la;
		try
		{
			enterOuterAlt(_localctx, 1);
			{
				setState(95);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do
				{
					{
						{
							setState(94);
							space();
						}
					}
					setState(97);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				while (_la == WS || _la == BR);
				setState(99);
				((AttributeContext) _localctx).name = match(NAME);
				setState(101);
				_la = _input.LA(1);
				if (_la == WS || _la == BR)
				{
					{
						setState(100);
						space();
					}
				}

				setState(103);
				match(EQUALS);
				setState(105);
				_la = _input.LA(1);
				if (_la == WS || _la == BR)
				{
					{
						setState(104);
						space();
					}
				}

				setState(115);
				switch (_input.LA(1))
				{
					case DOUBLE_QUOTE:
						{
							setState(107);
							match(DOUBLE_QUOTE);
							setState(108);
							((AttributeContext) _localctx).value = attributeValue('\'');
							setState(109);
							match(DOUBLE_QUOTE);
						}
						break;
					case SINGLE_QUOTE:
						{
							setState(111);
							match(SINGLE_QUOTE);
							setState(112);
							((AttributeContext) _localctx).value = attributeValue('\"');
							setState(113);
							match(SINGLE_QUOTE);
						}
						break;
					default:
						throw new NoViableAltException(this);
				}
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeValueContext extends ParserRuleContext
	{
		public int quote;

		public List<TerminalNode> OTHER()
		{
			return getTokens(XmlParser.OTHER);
		}

		public List<TerminalNode> WS()
		{
			return getTokens(XmlParser.WS);
		}

		public List<TerminalNode> CHAR_REF()
		{
			return getTokens(XmlParser.CHAR_REF);
		}

		public List<TerminalNode> EQUALS()
		{
			return getTokens(XmlParser.EQUALS);
		}

		public TerminalNode NAME(int i)
		{
			return getToken(XmlParser.NAME, i);
		}

		public TerminalNode ENTITY_REF(int i)
		{
			return getToken(XmlParser.ENTITY_REF, i);
		}

		public TerminalNode WS(int i)
		{
			return getToken(XmlParser.WS, i);
		}

		public List<TerminalNode> DOUBLE_QUOTE()
		{
			return getTokens(XmlParser.DOUBLE_QUOTE);
		}

		public TerminalNode OTHER(int i)
		{
			return getToken(XmlParser.OTHER, i);
		}

		public TerminalNode EQUALS(int i)
		{
			return getToken(XmlParser.EQUALS, i);
		}

		public TerminalNode SINGLE_QUOTE(int i)
		{
			return getToken(XmlParser.SINGLE_QUOTE, i);
		}

		public TerminalNode CHAR_REF(int i)
		{
			return getToken(XmlParser.CHAR_REF, i);
		}

		public List<TerminalNode> SINGLE_QUOTE()
		{
			return getTokens(XmlParser.SINGLE_QUOTE);
		}

		public TerminalNode SLASH(int i)
		{
			return getToken(XmlParser.SLASH, i);
		}

		public List<TerminalNode> ENTITY_REF()
		{
			return getTokens(XmlParser.ENTITY_REF);
		}

		public List<TerminalNode> NAME()
		{
			return getTokens(XmlParser.NAME);
		}

		public TerminalNode DOUBLE_QUOTE(int i)
		{
			return getToken(XmlParser.DOUBLE_QUOTE, i);
		}

		public List<TerminalNode> SLASH()
		{
			return getTokens(XmlParser.SLASH);
		}

		public AttributeValueContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		public AttributeValueContext(ParserRuleContext parent, int invokingState, int quote)
		{
			super(parent, invokingState);
			this.quote = quote;
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_attributeValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterAttributeValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitAttributeValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitAttributeValue(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final AttributeValueContext attributeValue(int quote) throws RecognitionException
	{
		AttributeValueContext _localctx = new AttributeValueContext(_ctx, getState(), quote);
		enterRule(_localctx, 16, RULE_attributeValue);
		try
		{
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(130);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
				while (_alt != 2 && _alt != -1)
				{
					if (_alt == 1)
					{
						{
							setState(128);
							switch (getInterpreter().adaptivePredict(_input, 15, _ctx))
							{
								case 1:
									{
										setState(117);
										match(NAME);
									}
									break;

								case 2:
									{
										setState(118);
										match(ENTITY_REF);
									}
									break;

								case 3:
									{
										setState(119);
										match(CHAR_REF);
									}
									break;

								case 4:
									{
										setState(120);
										match(EQUALS);
									}
									break;

								case 5:
									{
										setState(121);
										match(SLASH);
									}
									break;

								case 6:
									{
										setState(122);
										match(WS);
									}
									break;

								case 7:
									{
										setState(123);
										match(OTHER);
									}
									break;

								case 8:
									{
										setState(124);
										if (!(_localctx.quote == '\''))
											throw new FailedPredicateException(this, "$quote=='\\''");
										setState(125);
										match(SINGLE_QUOTE);
									}
									break;

								case 9:
									{
										setState(126);
										if (!(_localctx.quote == '\"'))
											throw new FailedPredicateException(this, "$quote=='\\\"'");
										setState(127);
										match(DOUBLE_QUOTE);
									}
									break;
							}
						}
					}
					setState(132);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
				}
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class TextContext extends ParserRuleContext
	{
		public TerminalNode OTHER()
		{
			return getToken(XmlParser.OTHER, 0);
		}

		public TerminalNode SINGLE_QUOTE()
		{
			return getToken(XmlParser.SINGLE_QUOTE, 0);
		}

		public TerminalNode WS()
		{
			return getToken(XmlParser.WS, 0);
		}

		public TerminalNode EQUALS()
		{
			return getToken(XmlParser.EQUALS, 0);
		}

		public TerminalNode CHAR_REF()
		{
			return getToken(XmlParser.CHAR_REF, 0);
		}

		public TerminalNode ENTITY_REF()
		{
			return getToken(XmlParser.ENTITY_REF, 0);
		}

		public TerminalNode NAME()
		{
			return getToken(XmlParser.NAME, 0);
		}

		public TerminalNode DOUBLE_QUOTE()
		{
			return getToken(XmlParser.DOUBLE_QUOTE, 0);
		}

		public TerminalNode BR()
		{
			return getToken(XmlParser.BR, 0);
		}

		public TerminalNode SLASH()
		{
			return getToken(XmlParser.SLASH, 0);
		}

		public TextContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_text;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterText(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitText(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitText(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final TextContext text() throws RecognitionException
	{
		TextContext _localctx = new TextContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_text);
		int _la;
		try
		{
			enterOuterAlt(_localctx, 1);
			{
				setState(133);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ENTITY_REF) | (1L << CHAR_REF) | (1L << NAME) | (1L << SLASH) | (1L << EQUALS) | (1L << DOUBLE_QUOTE) | (1L << SINGLE_QUOTE) | (1L << WS) | (1L << BR) | (1L << OTHER))) != 0)))
				{
					_errHandler.recoverInline(this);
				}
				consume();
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public static class SpaceContext extends ParserRuleContext
	{
		public TerminalNode WS()
		{
			return getToken(XmlParser.WS, 0);
		}

		public TerminalNode BR()
		{
			return getToken(XmlParser.BR, 0);
		}

		public SpaceContext(ParserRuleContext parent, int invokingState)
		{
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex()
		{
			return RULE_space;
		}

		@Override
		public void enterRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).enterSpace(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener)
		{
			if (listener instanceof XmlListener)
				((XmlListener) listener).exitSpace(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor)
		{
			if (visitor instanceof XmlVisitor)
				return ((XmlVisitor<? extends T>) visitor).visitSpace(this);
			else
				return visitor.visitChildren(this);
		}
	}

	public final SpaceContext space() throws RecognitionException
	{
		SpaceContext _localctx = new SpaceContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_space);
		int _la;
		try
		{
			enterOuterAlt(_localctx, 1);
			{
				setState(135);
				_la = _input.LA(1);
				if (!(_la == WS || _la == BR))
				{
					_errHandler.recoverInline(this);
				}
				consume();
			}
		}
		catch (RecognitionException re)
		{
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally
		{
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex)
	{
		switch (ruleIndex)
		{
			case 6:
				return complexNode_sempred((ComplexNodeContext) _localctx, predIndex);

			case 8:
				return attributeValue_sempred((AttributeValueContext) _localctx, predIndex);
		}
		return true;
	}

	private boolean attributeValue_sempred(AttributeValueContext _localctx, int predIndex)
	{
		switch (predIndex)
		{
			case 1:
				return _localctx.quote == '\'';

			case 2:
				return _localctx.quote == '\"';
		}
		return true;
	}

	private boolean complexNode_sempred(ComplexNodeContext _localctx, int predIndex)
	{
		switch (predIndex)
		{
			case 0:
				return checkEndTag((((ComplexNodeContext) _localctx).tagName != null ? ((ComplexNodeContext) _localctx).tagName.getText() : null));
		}
		return true;
	}

	public static final String _serializedATN = "\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\22\u008c\4\2\t\2" + "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" + "\t\13\4\f\t\f\3\2\3\2\3\2\3\2\7\2\35\n\2\f\2\16\2 \13\2\3\2\3\2\5\2$\n" + "\2\3\2\3\2\7\2(\n\2\f\2\16\2+\13\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7" + "\3\7\3\7\7\78\n\7\f\7\16\7;\13\7\3\7\5\7>\n\7\3\7\3\7\3\7\3\b\3\b\3\b" + "\7\bF\n\b\f\b\16\bI\13\b\3\b\5\bL\n\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b" + "\7\bV\n\b\f\b\16\bY\13\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\6\tb\n\t\r\t\16\t" + "c\3\t\3\t\5\th\n\t\3\t\3\t\5\tl\n\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5" + "\tv\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u0083\n\n\f\n" + "\16\n\u0086\13\n\3\13\3\13\3\f\3\f\3\f\2\r\2\4\6\b\n\f\16\20\22\24\26" + "\2\4\4\2\7\t\f\22\3\2\20\21\u009f\2\36\3\2\2\2\4,\3\2\2\2\6.\3\2\2\2\b" + "\60\3\2\2\2\n\62\3\2\2\2\f\64\3\2\2\2\16B\3\2\2\2\20a\3\2\2\2\22\u0084" + "\3\2\2\2\24\u0087\3\2\2\2\26\u0089\3\2\2\2\30\35\5\4\3\2\31\35\5\6\4\2" + "\32\35\5\b\5\2\33\35\5\26\f\2\34\30\3\2\2\2\34\31\3\2\2\2\34\32\3\2\2" + "\2\34\33\3\2\2\2\35 \3\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37#\3\2\2\2 " + "\36\3\2\2\2!$\5\f\7\2\"$\5\16\b\2#!\3\2\2\2#\"\3\2\2\2$)\3\2\2\2%(\5\b" + "\5\2&(\5\26\f\2\'%\3\2\2\2\'&\3\2\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*" + "\3\3\2\2\2+)\3\2\2\2,-\7\3\2\2-\5\3\2\2\2./\7\6\2\2/\7\3\2\2\2\60\61\7" + "\5\2\2\61\t\3\2\2\2\62\63\7\4\2\2\63\13\3\2\2\2\64\65\7\n\2\2\659\7\t" + "\2\2\668\5\20\t\2\67\66\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:=\3\2" + "\2\2;9\3\2\2\2<>\5\26\f\2=<\3\2\2\2=>\3\2\2\2>?\3\2\2\2?@\7\f\2\2@A\7" + "\13\2\2A\r\3\2\2\2BC\7\n\2\2CG\7\t\2\2DF\5\20\t\2ED\3\2\2\2FI\3\2\2\2" + "GE\3\2\2\2GH\3\2\2\2HK\3\2\2\2IG\3\2\2\2JL\5\26\f\2KJ\3\2\2\2KL\3\2\2" + "\2LM\3\2\2\2MW\7\13\2\2NV\5\4\3\2OV\5\6\4\2PV\5\b\5\2QV\5\n\6\2RV\5\24" + "\13\2SV\5\f\7\2TV\5\16\b\2UN\3\2\2\2UO\3\2\2\2UP\3\2\2\2UQ\3\2\2\2UR\3" + "\2\2\2US\3\2\2\2UT\3\2\2\2VY\3\2\2\2WU\3\2\2\2WX\3\2\2\2XZ\3\2\2\2YW\3" + "\2\2\2Z[\6\b\2\3[\\\7\n\2\2\\]\7\f\2\2]^\7\t\2\2^_\7\13\2\2_\17\3\2\2" + "\2`b\5\26\f\2a`\3\2\2\2bc\3\2\2\2ca\3\2\2\2cd\3\2\2\2de\3\2\2\2eg\7\t" + "\2\2fh\5\26\f\2gf\3\2\2\2gh\3\2\2\2hi\3\2\2\2ik\7\r\2\2jl\5\26\f\2kj\3" + "\2\2\2kl\3\2\2\2lu\3\2\2\2mn\7\16\2\2no\5\22\n\2op\7\16\2\2pv\3\2\2\2" + "qr\7\17\2\2rs\5\22\n\2st\7\17\2\2tv\3\2\2\2um\3\2\2\2uq\3\2\2\2v\21\3" + "\2\2\2w\u0083\7\t\2\2x\u0083\7\7\2\2y\u0083\7\b\2\2z\u0083\7\r\2\2{\u0083" + "\7\f\2\2|\u0083\7\20\2\2}\u0083\7\22\2\2~\177\6\n\3\3\177\u0083\7\17\2" + "\2\u0080\u0081\6\n\4\3\u0081\u0083\7\16\2\2\u0082w\3\2\2\2\u0082x\3\2" + "\2\2\u0082y\3\2\2\2\u0082z\3\2\2\2\u0082{\3\2\2\2\u0082|\3\2\2\2\u0082" + "}\3\2\2\2\u0082~\3\2\2\2\u0082\u0080\3\2\2\2\u0083\u0086\3\2\2\2\u0084" + "\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\23\3\2\2\2\u0086\u0084\3\2\2" + "\2\u0087\u0088\t\2\2\2\u0088\25\3\2\2\2\u0089\u008a\t\3\2\2\u008a\27\3" + "\2\2\2\23\34\36#\')9=GKUWcgku\u0082\u0084";
	public static final ATN _ATN = ATNSimulator.deserialize(_serializedATN.toCharArray());
	static
	{
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++)
		{
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}