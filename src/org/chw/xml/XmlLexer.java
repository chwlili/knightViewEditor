// Generated from Xml.g4 by ANTLR 4.1
package org.chw.xml;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class XmlLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PROCESS=1, CDATA=2, COMMENT=3, DTD=4, ENTITY_REF=5, CHAR_REF=6, NAME=7, 
		BRACKET_L=8, BRACKET_R=9, SLASH=10, EQUALS=11, DOUBLE_QUOTE=12, SINGLE_QUOTE=13, 
		WS=14, OTHER=15;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"PROCESS", "CDATA", "COMMENT", "DTD", "ENTITY_REF", "CHAR_REF", "NAME", 
		"'<'", "'>'", "'/'", "'='", "'\"'", "'''", "WS", "OTHER"
	};
	public static final String[] ruleNames = {
		"PROCESS", "CDATA", "COMMENT", "DTD", "ENTITY_REF", "CHAR_REF", "NAME", 
		"BRACKET_L", "BRACKET_R", "SLASH", "EQUALS", "DOUBLE_QUOTE", "SINGLE_QUOTE", 
		"WS", "OTHER", "NAME_CHAR_1", "NAME_CHAR_2"
	};


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


	public XmlLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Xml.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\21\u00b7\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\2\3\2\7\2*\n\2\f\2\16\2-\13\2\3\2\3\2\3\2\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3=\n\3\f\3\16\3@\13\3\3\3\3\3\3\3\3"+
		"\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4L\n\4\f\4\16\4O\13\4\3\4\3\4\3\4\3\4\3\5"+
		"\3\5\3\5\3\5\7\5Y\n\5\f\5\16\5\\\13\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\5\6h\n\6\3\7\3\7\3\7\3\7\6\7n\n\7\r\7\16\7o\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\6\7x\n\7\r\7\16\7y\3\7\5\7}\n\7\3\b\6\b\u0080\n\b\r\b\16\b\u0081"+
		"\3\b\3\b\7\b\u0086\n\b\f\b\16\b\u0089\13\b\3\t\3\t\3\n\3\n\3\13\3\13\3"+
		"\f\3\f\3\r\3\r\3\16\3\16\3\17\6\17\u0098\n\17\r\17\16\17\u0099\3\20\3"+
		"\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\5\21\u00ae\n\21\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u00b6"+
		"\n\22\6+>MZ\23\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13"+
		"\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\2\1#\2\1\3\2\7\3\2\62"+
		";\5\2\62;CHch\6\2\13\f\17\17\"\"~~\6\3<<C\\aac|\4\2/\60\62;\u00c4\2\3"+
		"\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2"+
		"\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31"+
		"\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\3%\3\2\2\2\5\61\3\2\2"+
		"\2\7E\3\2\2\2\tT\3\2\2\2\13g\3\2\2\2\r|\3\2\2\2\17\177\3\2\2\2\21\u008a"+
		"\3\2\2\2\23\u008c\3\2\2\2\25\u008e\3\2\2\2\27\u0090\3\2\2\2\31\u0092\3"+
		"\2\2\2\33\u0094\3\2\2\2\35\u0097\3\2\2\2\37\u009b\3\2\2\2!\u00ad\3\2\2"+
		"\2#\u00b5\3\2\2\2%&\7>\2\2&\'\7A\2\2\'+\3\2\2\2(*\13\2\2\2)(\3\2\2\2*"+
		"-\3\2\2\2+,\3\2\2\2+)\3\2\2\2,.\3\2\2\2-+\3\2\2\2./\7A\2\2/\60\7@\2\2"+
		"\60\4\3\2\2\2\61\62\7>\2\2\62\63\7#\2\2\63\64\7]\2\2\64\65\7E\2\2\65\66"+
		"\7F\2\2\66\67\7C\2\2\678\7V\2\289\7C\2\29:\7]\2\2:>\3\2\2\2;=\13\2\2\2"+
		"<;\3\2\2\2=@\3\2\2\2>?\3\2\2\2><\3\2\2\2?A\3\2\2\2@>\3\2\2\2AB\7_\2\2"+
		"BC\7_\2\2CD\7@\2\2D\6\3\2\2\2EF\7>\2\2FG\7#\2\2GH\7/\2\2HI\7/\2\2IM\3"+
		"\2\2\2JL\13\2\2\2KJ\3\2\2\2LO\3\2\2\2MN\3\2\2\2MK\3\2\2\2NP\3\2\2\2OM"+
		"\3\2\2\2PQ\7/\2\2QR\7/\2\2RS\7@\2\2S\b\3\2\2\2TU\7>\2\2UV\7#\2\2VZ\3\2"+
		"\2\2WY\13\2\2\2XW\3\2\2\2Y\\\3\2\2\2Z[\3\2\2\2ZX\3\2\2\2[]\3\2\2\2\\Z"+
		"\3\2\2\2]^\7@\2\2^\n\3\2\2\2_`\7(\2\2`a\5\17\b\2ab\7=\2\2bh\3\2\2\2cd"+
		"\7\'\2\2de\5\17\b\2ef\7=\2\2fh\3\2\2\2g_\3\2\2\2gc\3\2\2\2h\f\3\2\2\2"+
		"ij\7(\2\2jk\7%\2\2km\3\2\2\2ln\t\2\2\2ml\3\2\2\2no\3\2\2\2om\3\2\2\2o"+
		"p\3\2\2\2pq\3\2\2\2q}\7=\2\2rs\7(\2\2st\7%\2\2tu\7z\2\2uw\3\2\2\2vx\t"+
		"\3\2\2wv\3\2\2\2xy\3\2\2\2yw\3\2\2\2yz\3\2\2\2z{\3\2\2\2{}\7=\2\2|i\3"+
		"\2\2\2|r\3\2\2\2}\16\3\2\2\2~\u0080\5!\21\2\177~\3\2\2\2\u0080\u0081\3"+
		"\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0087\3\2\2\2\u0083"+
		"\u0086\5!\21\2\u0084\u0086\5#\22\2\u0085\u0083\3\2\2\2\u0085\u0084\3\2"+
		"\2\2\u0086\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088"+
		"\20\3\2\2\2\u0089\u0087\3\2\2\2\u008a\u008b\7>\2\2\u008b\22\3\2\2\2\u008c"+
		"\u008d\7@\2\2\u008d\24\3\2\2\2\u008e\u008f\7\61\2\2\u008f\26\3\2\2\2\u0090"+
		"\u0091\7?\2\2\u0091\30\3\2\2\2\u0092\u0093\7$\2\2\u0093\32\3\2\2\2\u0094"+
		"\u0095\7)\2\2\u0095\34\3\2\2\2\u0096\u0098\t\4\2\2\u0097\u0096\3\2\2\2"+
		"\u0098\u0099\3\2\2\2\u0099\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a\36"+
		"\3\2\2\2\u009b\u009c\13\2\2\2\u009c \3\2\2\2\u009d\u00ae\t\5\2\2\u009e"+
		"\u009f\7^\2\2\u009f\u00a0\7w\2\2\u00a0\u00a1\7H\2\2\u00a1\u00a2\7;\2\2"+
		"\u00a2\u00a3\7\62\2\2\u00a3\u00a4\7\62\2\2\u00a4\u00a5\7\60\2\2\u00a5"+
		"\u00a6\7\60\2\2\u00a6\u00a7\7^\2\2\u00a7\u00a8\7w\2\2\u00a8\u00a9\7H\2"+
		"\2\u00a9\u00aa\7F\2\2\u00aa\u00ab\7E\2\2\u00ab\u00ae\7H\2\2\u00ac\u00ae"+
		"\7\2\2\2\u00ad\u009d\3\2\2\2\u00ad\u009e\3\2\2\2\u00ad\u00ac\3\2\2\2\u00ae"+
		"\"\3\2\2\2\u00af\u00b6\t\6\2\2\u00b0\u00b1\7^\2\2\u00b1\u00b2\7w\2\2\u00b2"+
		"\u00b3\7D\2\2\u00b3\u00b6\79\2\2\u00b4\u00b6\7\2\2\2\u00b5\u00af\3\2\2"+
		"\2\u00b5\u00b0\3\2\2\2\u00b5\u00b4\3\2\2\2\u00b6$\3\2\2\2\21\2+>MZgoy"+
		"|\u0081\u0085\u0087\u0099\u00ad\u00b5";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}