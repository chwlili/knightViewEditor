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
		WS=14, BR=15, OTHER=16;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"PROCESS", "CDATA", "COMMENT", "DTD", "ENTITY_REF", "CHAR_REF", "NAME", 
		"'<'", "'>'", "'/'", "'='", "'\"'", "'''", "WS", "BR", "OTHER"
	};
	public static final String[] ruleNames = {
		"PROCESS", "CDATA", "COMMENT", "DTD", "ENTITY_REF", "CHAR_REF", "NAME", 
		"BRACKET_L", "BRACKET_R", "SLASH", "EQUALS", "DOUBLE_QUOTE", "SINGLE_QUOTE", 
		"WS", "BR", "OTHER", "NAME_CHAR_1", "NAME_CHAR_2"
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\22\u00be\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\3\2\3\2\3\2\3\2\7\2,\n\2\f\2\16\2/\13\2\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3?\n\3\f\3\16\3B\13\3\3\3"+
		"\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4N\n\4\f\4\16\4Q\13\4\3\4\3\4\3"+
		"\4\3\4\3\5\3\5\3\5\3\5\7\5[\n\5\f\5\16\5^\13\5\3\5\3\5\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\5\6j\n\6\3\7\3\7\3\7\3\7\6\7p\n\7\r\7\16\7q\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\6\7z\n\7\r\7\16\7{\3\7\5\7\177\n\7\3\b\6\b\u0082\n\b\r"+
		"\b\16\b\u0083\3\b\3\b\7\b\u0088\n\b\f\b\16\b\u008b\13\b\3\t\3\t\3\n\3"+
		"\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\6\17\u009a\n\17\r\17\16\17"+
		"\u009b\3\20\6\20\u009f\n\20\r\20\16\20\u00a0\3\21\3\21\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22"+
		"\u00b5\n\22\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u00bd\n\23\6-@O\\\24\3"+
		"\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r"+
		"\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\2\1%\2\1\3\2\b\3\2\62;\5\2\62"+
		";CHch\5\2\13\13\"\"~~\5\2\f\f\17\17~~\6\3<<C\\aac|\4\2/\60\62;\u00cc\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"+
		"\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"+
		"\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\3\'\3\2"+
		"\2\2\5\63\3\2\2\2\7G\3\2\2\2\tV\3\2\2\2\13i\3\2\2\2\r~\3\2\2\2\17\u0081"+
		"\3\2\2\2\21\u008c\3\2\2\2\23\u008e\3\2\2\2\25\u0090\3\2\2\2\27\u0092\3"+
		"\2\2\2\31\u0094\3\2\2\2\33\u0096\3\2\2\2\35\u0099\3\2\2\2\37\u009e\3\2"+
		"\2\2!\u00a2\3\2\2\2#\u00b4\3\2\2\2%\u00bc\3\2\2\2\'(\7>\2\2()\7A\2\2)"+
		"-\3\2\2\2*,\13\2\2\2+*\3\2\2\2,/\3\2\2\2-.\3\2\2\2-+\3\2\2\2.\60\3\2\2"+
		"\2/-\3\2\2\2\60\61\7A\2\2\61\62\7@\2\2\62\4\3\2\2\2\63\64\7>\2\2\64\65"+
		"\7#\2\2\65\66\7]\2\2\66\67\7E\2\2\678\7F\2\289\7C\2\29:\7V\2\2:;\7C\2"+
		"\2;<\7]\2\2<@\3\2\2\2=?\13\2\2\2>=\3\2\2\2?B\3\2\2\2@A\3\2\2\2@>\3\2\2"+
		"\2AC\3\2\2\2B@\3\2\2\2CD\7_\2\2DE\7_\2\2EF\7@\2\2F\6\3\2\2\2GH\7>\2\2"+
		"HI\7#\2\2IJ\7/\2\2JK\7/\2\2KO\3\2\2\2LN\13\2\2\2ML\3\2\2\2NQ\3\2\2\2O"+
		"P\3\2\2\2OM\3\2\2\2PR\3\2\2\2QO\3\2\2\2RS\7/\2\2ST\7/\2\2TU\7@\2\2U\b"+
		"\3\2\2\2VW\7>\2\2WX\7#\2\2X\\\3\2\2\2Y[\13\2\2\2ZY\3\2\2\2[^\3\2\2\2\\"+
		"]\3\2\2\2\\Z\3\2\2\2]_\3\2\2\2^\\\3\2\2\2_`\7@\2\2`\n\3\2\2\2ab\7(\2\2"+
		"bc\5\17\b\2cd\7=\2\2dj\3\2\2\2ef\7\'\2\2fg\5\17\b\2gh\7=\2\2hj\3\2\2\2"+
		"ia\3\2\2\2ie\3\2\2\2j\f\3\2\2\2kl\7(\2\2lm\7%\2\2mo\3\2\2\2np\t\2\2\2"+
		"on\3\2\2\2pq\3\2\2\2qo\3\2\2\2qr\3\2\2\2rs\3\2\2\2s\177\7=\2\2tu\7(\2"+
		"\2uv\7%\2\2vw\7z\2\2wy\3\2\2\2xz\t\3\2\2yx\3\2\2\2z{\3\2\2\2{y\3\2\2\2"+
		"{|\3\2\2\2|}\3\2\2\2}\177\7=\2\2~k\3\2\2\2~t\3\2\2\2\177\16\3\2\2\2\u0080"+
		"\u0082\5#\22\2\u0081\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0081\3\2"+
		"\2\2\u0083\u0084\3\2\2\2\u0084\u0089\3\2\2\2\u0085\u0088\5#\22\2\u0086"+
		"\u0088\5%\23\2\u0087\u0085\3\2\2\2\u0087\u0086\3\2\2\2\u0088\u008b\3\2"+
		"\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a\20\3\2\2\2\u008b\u0089"+
		"\3\2\2\2\u008c\u008d\7>\2\2\u008d\22\3\2\2\2\u008e\u008f\7@\2\2\u008f"+
		"\24\3\2\2\2\u0090\u0091\7\61\2\2\u0091\26\3\2\2\2\u0092\u0093\7?\2\2\u0093"+
		"\30\3\2\2\2\u0094\u0095\7$\2\2\u0095\32\3\2\2\2\u0096\u0097\7)\2\2\u0097"+
		"\34\3\2\2\2\u0098\u009a\t\4\2\2\u0099\u0098\3\2\2\2\u009a\u009b\3\2\2"+
		"\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\36\3\2\2\2\u009d\u009f"+
		"\t\5\2\2\u009e\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0"+
		"\u00a1\3\2\2\2\u00a1 \3\2\2\2\u00a2\u00a3\13\2\2\2\u00a3\"\3\2\2\2\u00a4"+
		"\u00b5\t\6\2\2\u00a5\u00a6\7^\2\2\u00a6\u00a7\7w\2\2\u00a7\u00a8\7H\2"+
		"\2\u00a8\u00a9\7;\2\2\u00a9\u00aa\7\62\2\2\u00aa\u00ab\7\62\2\2\u00ab"+
		"\u00ac\7\60\2\2\u00ac\u00ad\7\60\2\2\u00ad\u00ae\7^\2\2\u00ae\u00af\7"+
		"w\2\2\u00af\u00b0\7H\2\2\u00b0\u00b1\7F\2\2\u00b1\u00b2\7E\2\2\u00b2\u00b5"+
		"\7H\2\2\u00b3\u00b5\7\2\2\2\u00b4\u00a4\3\2\2\2\u00b4\u00a5\3\2\2\2\u00b4"+
		"\u00b3\3\2\2\2\u00b5$\3\2\2\2\u00b6\u00bd\t\7\2\2\u00b7\u00b8\7^\2\2\u00b8"+
		"\u00b9\7w\2\2\u00b9\u00ba\7D\2\2\u00ba\u00bd\79\2\2\u00bb\u00bd\7\2\2"+
		"\2\u00bc\u00b6\3\2\2\2\u00bc\u00b7\3\2\2\2\u00bc\u00bb\3\2\2\2\u00bd&"+
		"\3\2\2\2\22\2-@O\\iq{~\u0083\u0087\u0089\u009b\u00a0\u00b4\u00bc";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}