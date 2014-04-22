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

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class XmlParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PROCESS=1, CDATA=2, COMMENT=3, DTD=4, ENTITY_REF=5, CHAR_REF=6, NAME=7, 
		BRACKET_L=8, BRACKET_R=9, SLASH=10, EQUALS=11, DOUBLE_QUOTE=12, SINGLE_QUOTE=13, 
		WS=14, OTHER=15;
	public static final String[] tokenNames = {
		"<INVALID>", "PROCESS", "CDATA", "COMMENT", "DTD", "ENTITY_REF", "CHAR_REF", 
		"NAME", "'<'", "'>'", "'/'", "'='", "'\"'", "'''", "WS", "OTHER"
	};
	public static final int
		RULE_root = 0, RULE_process = 1, RULE_dtd = 2, RULE_comm = 3, RULE_cdata = 4, 
		RULE_singleNode = 5, RULE_complexNode = 6, RULE_attribute = 7, RULE_attributeValue = 8, 
		RULE_text = 9;
	public static final String[] ruleNames = {
		"root", "process", "dtd", "comm", "cdata", "singleNode", "complexNode", 
		"attribute", "attributeValue", "text"
	};

	@Override
	public String getGrammarFileName() { return "Xml.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }


		private boolean checkEndTag(String name)
		{
			CommonTokenStream input=(CommonTokenStream) _input;
			if(input.LT(1).getText().equals("<"))
			{
				if(input.LT(2).getText().equals("/"))
				{
					if(input.LT(3).getText().equals(name))
					{
						return true;
					}
				}
			}
			return false;
		}

	public XmlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RootContext extends ParserRuleContext {
		public List<TerminalNode> WS() { return getTokens(XmlParser.WS); }
		public List<CommContext> comm() {
			return getRuleContexts(CommContext.class);
		}
		public DtdContext dtd(int i) {
			return getRuleContext(DtdContext.class,i);
		}
		public ProcessContext process(int i) {
			return getRuleContext(ProcessContext.class,i);
		}
		public CommContext comm(int i) {
			return getRuleContext(CommContext.class,i);
		}
		public List<ProcessContext> process() {
			return getRuleContexts(ProcessContext.class);
		}
		public ComplexNodeContext complexNode() {
			return getRuleContext(ComplexNodeContext.class,0);
		}
		public TerminalNode WS(int i) {
			return getToken(XmlParser.WS, i);
		}
		public SingleNodeContext singleNode() {
			return getRuleContext(SingleNodeContext.class,0);
		}
		public List<DtdContext> dtd() {
			return getRuleContexts(DtdContext.class);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitRoot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PROCESS) | (1L << COMMENT) | (1L << DTD) | (1L << WS))) != 0)) {
				{
				setState(24);
				switch (_input.LA(1)) {
				case PROCESS:
					{
					setState(20); process();
					}
					break;
				case DTD:
					{
					setState(21); dtd();
					}
					break;
				case COMMENT:
					{
					setState(22); comm();
					}
					break;
				case WS:
					{
					setState(23); match(WS);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(28);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(31);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(29); singleNode();
				}
				break;

			case 2:
				{
				setState(30); complexNode();
				}
				break;
			}
			setState(37);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMENT || _la==WS) {
				{
				setState(35);
				switch (_input.LA(1)) {
				case COMMENT:
					{
					setState(33); comm();
					}
					break;
				case WS:
					{
					setState(34); match(WS);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(39);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProcessContext extends ParserRuleContext {
		public TerminalNode PROCESS() { return getToken(XmlParser.PROCESS, 0); }
		public ProcessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_process; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterProcess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitProcess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitProcess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProcessContext process() throws RecognitionException {
		ProcessContext _localctx = new ProcessContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_process);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40); match(PROCESS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DtdContext extends ParserRuleContext {
		public TerminalNode DTD() { return getToken(XmlParser.DTD, 0); }
		public DtdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dtd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterDtd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitDtd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitDtd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DtdContext dtd() throws RecognitionException {
		DtdContext _localctx = new DtdContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_dtd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42); match(DTD);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommContext extends ParserRuleContext {
		public TerminalNode COMMENT() { return getToken(XmlParser.COMMENT, 0); }
		public CommContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterComm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitComm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitComm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommContext comm() throws RecognitionException {
		CommContext _localctx = new CommContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_comm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44); match(COMMENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CdataContext extends ParserRuleContext {
		public TerminalNode CDATA() { return getToken(XmlParser.CDATA, 0); }
		public CdataContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cdata; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterCdata(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitCdata(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitCdata(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CdataContext cdata() throws RecognitionException {
		CdataContext _localctx = new CdataContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_cdata);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46); match(CDATA);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SingleNodeContext extends ParserRuleContext {
		public Token begin;
		public Token tagName;
		public Token slash;
		public Token end;
		public TerminalNode WS() { return getToken(XmlParser.WS, 0); }
		public TerminalNode BRACKET_R() { return getToken(XmlParser.BRACKET_R, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public TerminalNode NAME() { return getToken(XmlParser.NAME, 0); }
		public TerminalNode SLASH() { return getToken(XmlParser.SLASH, 0); }
		public TerminalNode BRACKET_L() { return getToken(XmlParser.BRACKET_L, 0); }
		public SingleNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleNode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterSingleNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitSingleNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitSingleNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleNodeContext singleNode() throws RecognitionException {
		SingleNodeContext _localctx = new SingleNodeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_singleNode);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(48); ((SingleNodeContext)_localctx).begin = match(BRACKET_L);
			setState(49); ((SingleNodeContext)_localctx).tagName = match(NAME);
			setState(53);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(50); attribute();
					}
					} 
				}
				setState(55);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(57);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(56); match(WS);
				}
			}

			setState(59); ((SingleNodeContext)_localctx).slash = match(SLASH);
			setState(60); ((SingleNodeContext)_localctx).end = match(BRACKET_R);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComplexNodeContext extends ParserRuleContext {
		public Token beginL;
		public Token tagName;
		public Token beginR;
		public Token endL;
		public Token endSlash;
		public Token endName;
		public Token endR;
		public List<CommContext> comm() {
			return getRuleContexts(CommContext.class);
		}
		public DtdContext dtd(int i) {
			return getRuleContext(DtdContext.class,i);
		}
		public TerminalNode BRACKET_R(int i) {
			return getToken(XmlParser.BRACKET_R, i);
		}
		public TerminalNode WS() { return getToken(XmlParser.WS, 0); }
		public TextContext text(int i) {
			return getRuleContext(TextContext.class,i);
		}
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public TerminalNode NAME(int i) {
			return getToken(XmlParser.NAME, i);
		}
		public List<ProcessContext> process() {
			return getRuleContexts(ProcessContext.class);
		}
		public List<ComplexNodeContext> complexNode() {
			return getRuleContexts(ComplexNodeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public List<SingleNodeContext> singleNode() {
			return getRuleContexts(SingleNodeContext.class);
		}
		public List<DtdContext> dtd() {
			return getRuleContexts(DtdContext.class);
		}
		public List<TerminalNode> BRACKET_L() { return getTokens(XmlParser.BRACKET_L); }
		public List<TextContext> text() {
			return getRuleContexts(TextContext.class);
		}
		public List<TerminalNode> BRACKET_R() { return getTokens(XmlParser.BRACKET_R); }
		public ProcessContext process(int i) {
			return getRuleContext(ProcessContext.class,i);
		}
		public SingleNodeContext singleNode(int i) {
			return getRuleContext(SingleNodeContext.class,i);
		}
		public List<CdataContext> cdata() {
			return getRuleContexts(CdataContext.class);
		}
		public CommContext comm(int i) {
			return getRuleContext(CommContext.class,i);
		}
		public List<TerminalNode> NAME() { return getTokens(XmlParser.NAME); }
		public TerminalNode BRACKET_L(int i) {
			return getToken(XmlParser.BRACKET_L, i);
		}
		public CdataContext cdata(int i) {
			return getRuleContext(CdataContext.class,i);
		}
		public TerminalNode SLASH() { return getToken(XmlParser.SLASH, 0); }
		public ComplexNodeContext complexNode(int i) {
			return getRuleContext(ComplexNodeContext.class,i);
		}
		public ComplexNodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexNode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterComplexNode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitComplexNode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitComplexNode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComplexNodeContext complexNode() throws RecognitionException {
		ComplexNodeContext _localctx = new ComplexNodeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_complexNode);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(62); ((ComplexNodeContext)_localctx).beginL = match(BRACKET_L);
			setState(63); ((ComplexNodeContext)_localctx).tagName = match(NAME);
			setState(67);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					{
					setState(64); attribute();
					}
					} 
				}
				setState(69);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			setState(71);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(70); match(WS);
				}
			}

			setState(73); ((ComplexNodeContext)_localctx).beginR = match(BRACKET_R);
			setState(83);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					setState(81);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						setState(74); process();
						}
						break;

					case 2:
						{
						setState(75); dtd();
						}
						break;

					case 3:
						{
						setState(76); comm();
						}
						break;

					case 4:
						{
						setState(77); cdata();
						}
						break;

					case 5:
						{
						setState(78); text();
						}
						break;

					case 6:
						{
						setState(79); singleNode();
						}
						break;

					case 7:
						{
						setState(80); complexNode();
						}
						break;
					}
					} 
				}
				setState(85);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(86);
			if (!(checkEndTag((((ComplexNodeContext)_localctx).tagName!=null?((ComplexNodeContext)_localctx).tagName.getText():null)))) throw new FailedPredicateException(this, "checkEndTag($tagName.text)");
			setState(87); ((ComplexNodeContext)_localctx).endL = match(BRACKET_L);
			setState(88); ((ComplexNodeContext)_localctx).endSlash = match(SLASH);
			setState(89); ((ComplexNodeContext)_localctx).endName = match(NAME);
			setState(90); ((ComplexNodeContext)_localctx).endR = match(BRACKET_R);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeContext extends ParserRuleContext {
		public Token space;
		public Token name;
		public Token equals;
		public Token begin;
		public AttributeValueContext value;
		public Token end;
		public TerminalNode SINGLE_QUOTE(int i) {
			return getToken(XmlParser.SINGLE_QUOTE, i);
		}
		public List<TerminalNode> SINGLE_QUOTE() { return getTokens(XmlParser.SINGLE_QUOTE); }
		public List<TerminalNode> WS() { return getTokens(XmlParser.WS); }
		public AttributeValueContext attributeValue() {
			return getRuleContext(AttributeValueContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(XmlParser.EQUALS, 0); }
		public TerminalNode NAME() { return getToken(XmlParser.NAME, 0); }
		public TerminalNode WS(int i) {
			return getToken(XmlParser.WS, i);
		}
		public TerminalNode DOUBLE_QUOTE() { return getToken(XmlParser.DOUBLE_QUOTE, 0); }
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_attribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92); ((AttributeContext)_localctx).space = match(WS);
			setState(93); ((AttributeContext)_localctx).name = match(NAME);
			setState(95);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(94); match(WS);
				}
			}

			setState(97); ((AttributeContext)_localctx).equals = match(EQUALS);
			setState(99);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(98); match(WS);
				}
			}

			setState(109);
			switch (_input.LA(1)) {
			case DOUBLE_QUOTE:
				{
				setState(101); ((AttributeContext)_localctx).begin = match(DOUBLE_QUOTE);
				setState(102); ((AttributeContext)_localctx).value = attributeValue('\'');
				setState(103); ((AttributeContext)_localctx).end = match(DOUBLE_QUOTE);
				}
				break;
			case SINGLE_QUOTE:
				{
				setState(105); ((AttributeContext)_localctx).begin = match(SINGLE_QUOTE);
				setState(106); ((AttributeContext)_localctx).value = attributeValue('\"');
				setState(107); ((AttributeContext)_localctx).end = match(SINGLE_QUOTE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttributeValueContext extends ParserRuleContext {
		public int quote;
		public List<TerminalNode> OTHER() { return getTokens(XmlParser.OTHER); }
		public List<TerminalNode> WS() { return getTokens(XmlParser.WS); }
		public List<TerminalNode> CHAR_REF() { return getTokens(XmlParser.CHAR_REF); }
		public List<TerminalNode> EQUALS() { return getTokens(XmlParser.EQUALS); }
		public TerminalNode NAME(int i) {
			return getToken(XmlParser.NAME, i);
		}
		public TerminalNode ENTITY_REF(int i) {
			return getToken(XmlParser.ENTITY_REF, i);
		}
		public TerminalNode WS(int i) {
			return getToken(XmlParser.WS, i);
		}
		public List<TerminalNode> DOUBLE_QUOTE() { return getTokens(XmlParser.DOUBLE_QUOTE); }
		public TerminalNode OTHER(int i) {
			return getToken(XmlParser.OTHER, i);
		}
		public TerminalNode EQUALS(int i) {
			return getToken(XmlParser.EQUALS, i);
		}
		public TerminalNode SINGLE_QUOTE(int i) {
			return getToken(XmlParser.SINGLE_QUOTE, i);
		}
		public TerminalNode CHAR_REF(int i) {
			return getToken(XmlParser.CHAR_REF, i);
		}
		public List<TerminalNode> SINGLE_QUOTE() { return getTokens(XmlParser.SINGLE_QUOTE); }
		public TerminalNode SLASH(int i) {
			return getToken(XmlParser.SLASH, i);
		}
		public List<TerminalNode> ENTITY_REF() { return getTokens(XmlParser.ENTITY_REF); }
		public List<TerminalNode> NAME() { return getTokens(XmlParser.NAME); }
		public TerminalNode DOUBLE_QUOTE(int i) {
			return getToken(XmlParser.DOUBLE_QUOTE, i);
		}
		public List<TerminalNode> SLASH() { return getTokens(XmlParser.SLASH); }
		public AttributeValueContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public AttributeValueContext(ParserRuleContext parent, int invokingState, int quote) {
			super(parent, invokingState);
			this.quote = quote;
		}
		@Override public int getRuleIndex() { return RULE_attributeValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterAttributeValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitAttributeValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitAttributeValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeValueContext attributeValue(int quote) throws RecognitionException {
		AttributeValueContext _localctx = new AttributeValueContext(_ctx, getState(), quote);
		enterRule(_localctx, 16, RULE_attributeValue);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					{
					setState(122);
					switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
					case 1:
						{
						setState(111); match(NAME);
						}
						break;

					case 2:
						{
						setState(112); match(ENTITY_REF);
						}
						break;

					case 3:
						{
						setState(113); match(CHAR_REF);
						}
						break;

					case 4:
						{
						setState(114); match(EQUALS);
						}
						break;

					case 5:
						{
						setState(115); match(SLASH);
						}
						break;

					case 6:
						{
						setState(116); match(WS);
						}
						break;

					case 7:
						{
						setState(117); match(OTHER);
						}
						break;

					case 8:
						{
						setState(118);
						if (!(_localctx.quote=='\'')) throw new FailedPredicateException(this, "$quote=='\\''");
						setState(119); match(SINGLE_QUOTE);
						}
						break;

					case 9:
						{
						setState(120);
						if (!(_localctx.quote=='\"')) throw new FailedPredicateException(this, "$quote=='\\\"'");
						setState(121); match(DOUBLE_QUOTE);
						}
						break;
					}
					} 
				}
				setState(126);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TextContext extends ParserRuleContext {
		public TerminalNode OTHER() { return getToken(XmlParser.OTHER, 0); }
		public TerminalNode SINGLE_QUOTE() { return getToken(XmlParser.SINGLE_QUOTE, 0); }
		public TerminalNode WS() { return getToken(XmlParser.WS, 0); }
		public TerminalNode EQUALS() { return getToken(XmlParser.EQUALS, 0); }
		public TerminalNode CHAR_REF() { return getToken(XmlParser.CHAR_REF, 0); }
		public TerminalNode ENTITY_REF() { return getToken(XmlParser.ENTITY_REF, 0); }
		public TerminalNode NAME() { return getToken(XmlParser.NAME, 0); }
		public TerminalNode DOUBLE_QUOTE() { return getToken(XmlParser.DOUBLE_QUOTE, 0); }
		public TerminalNode SLASH() { return getToken(XmlParser.SLASH, 0); }
		public TextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_text; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).enterText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XmlListener ) ((XmlListener)listener).exitText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XmlVisitor ) return ((XmlVisitor<? extends T>)visitor).visitText(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextContext text() throws RecognitionException {
		TextContext _localctx = new TextContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_text);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ENTITY_REF) | (1L << CHAR_REF) | (1L << NAME) | (1L << SLASH) | (1L << EQUALS) | (1L << DOUBLE_QUOTE) | (1L << SINGLE_QUOTE) | (1L << WS) | (1L << OTHER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 6: return complexNode_sempred((ComplexNodeContext)_localctx, predIndex);

		case 8: return attributeValue_sempred((AttributeValueContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean attributeValue_sempred(AttributeValueContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1: return _localctx.quote=='\'';

		case 2: return _localctx.quote=='\"';
		}
		return true;
	}
	private boolean complexNode_sempred(ComplexNodeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return checkEndTag((((ComplexNodeContext)_localctx).tagName!=null?((ComplexNodeContext)_localctx).tagName.getText():null));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\21\u0084\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\3\2\3\2\3\2\3\2\7\2\33\n\2\f\2\16\2\36\13\2\3\2\3\2\5\2\"\n\2\3"+
		"\2\3\2\7\2&\n\2\f\2\16\2)\13\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7"+
		"\3\7\7\7\66\n\7\f\7\16\79\13\7\3\7\5\7<\n\7\3\7\3\7\3\7\3\b\3\b\3\b\7"+
		"\bD\n\b\f\b\16\bG\13\b\3\b\5\bJ\n\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\7"+
		"\bT\n\b\f\b\16\bW\13\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\5\tb\n\t\3"+
		"\t\3\t\5\tf\n\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\tp\n\t\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n}\n\n\f\n\16\n\u0080\13\n\3\13\3\13"+
		"\3\13\2\f\2\4\6\b\n\f\16\20\22\24\2\3\4\2\7\t\f\21\u0097\2\34\3\2\2\2"+
		"\4*\3\2\2\2\6,\3\2\2\2\b.\3\2\2\2\n\60\3\2\2\2\f\62\3\2\2\2\16@\3\2\2"+
		"\2\20^\3\2\2\2\22~\3\2\2\2\24\u0081\3\2\2\2\26\33\5\4\3\2\27\33\5\6\4"+
		"\2\30\33\5\b\5\2\31\33\7\20\2\2\32\26\3\2\2\2\32\27\3\2\2\2\32\30\3\2"+
		"\2\2\32\31\3\2\2\2\33\36\3\2\2\2\34\32\3\2\2\2\34\35\3\2\2\2\35!\3\2\2"+
		"\2\36\34\3\2\2\2\37\"\5\f\7\2 \"\5\16\b\2!\37\3\2\2\2! \3\2\2\2\"\'\3"+
		"\2\2\2#&\5\b\5\2$&\7\20\2\2%#\3\2\2\2%$\3\2\2\2&)\3\2\2\2\'%\3\2\2\2\'"+
		"(\3\2\2\2(\3\3\2\2\2)\'\3\2\2\2*+\7\3\2\2+\5\3\2\2\2,-\7\6\2\2-\7\3\2"+
		"\2\2./\7\5\2\2/\t\3\2\2\2\60\61\7\4\2\2\61\13\3\2\2\2\62\63\7\n\2\2\63"+
		"\67\7\t\2\2\64\66\5\20\t\2\65\64\3\2\2\2\669\3\2\2\2\67\65\3\2\2\2\67"+
		"8\3\2\2\28;\3\2\2\29\67\3\2\2\2:<\7\20\2\2;:\3\2\2\2;<\3\2\2\2<=\3\2\2"+
		"\2=>\7\f\2\2>?\7\13\2\2?\r\3\2\2\2@A\7\n\2\2AE\7\t\2\2BD\5\20\t\2CB\3"+
		"\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2FI\3\2\2\2GE\3\2\2\2HJ\7\20\2\2IH"+
		"\3\2\2\2IJ\3\2\2\2JK\3\2\2\2KU\7\13\2\2LT\5\4\3\2MT\5\6\4\2NT\5\b\5\2"+
		"OT\5\n\6\2PT\5\24\13\2QT\5\f\7\2RT\5\16\b\2SL\3\2\2\2SM\3\2\2\2SN\3\2"+
		"\2\2SO\3\2\2\2SP\3\2\2\2SQ\3\2\2\2SR\3\2\2\2TW\3\2\2\2US\3\2\2\2UV\3\2"+
		"\2\2VX\3\2\2\2WU\3\2\2\2XY\6\b\2\3YZ\7\n\2\2Z[\7\f\2\2[\\\7\t\2\2\\]\7"+
		"\13\2\2]\17\3\2\2\2^_\7\20\2\2_a\7\t\2\2`b\7\20\2\2a`\3\2\2\2ab\3\2\2"+
		"\2bc\3\2\2\2ce\7\r\2\2df\7\20\2\2ed\3\2\2\2ef\3\2\2\2fo\3\2\2\2gh\7\16"+
		"\2\2hi\5\22\n\2ij\7\16\2\2jp\3\2\2\2kl\7\17\2\2lm\5\22\n\2mn\7\17\2\2"+
		"np\3\2\2\2og\3\2\2\2ok\3\2\2\2p\21\3\2\2\2q}\7\t\2\2r}\7\7\2\2s}\7\b\2"+
		"\2t}\7\r\2\2u}\7\f\2\2v}\7\20\2\2w}\7\21\2\2xy\6\n\3\3y}\7\17\2\2z{\6"+
		"\n\4\3{}\7\16\2\2|q\3\2\2\2|r\3\2\2\2|s\3\2\2\2|t\3\2\2\2|u\3\2\2\2|v"+
		"\3\2\2\2|w\3\2\2\2|x\3\2\2\2|z\3\2\2\2}\u0080\3\2\2\2~|\3\2\2\2~\177\3"+
		"\2\2\2\177\23\3\2\2\2\u0080~\3\2\2\2\u0081\u0082\t\2\2\2\u0082\25\3\2"+
		"\2\2\22\32\34!%\'\67;EISUaeo|~";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}