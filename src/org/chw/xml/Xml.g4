grammar Xml;

@members {
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
}

root
:
	(
		process
		| dtd
		| comm
		| WS
	)*
	(
		singleNode
		| complexNode
	)
	(
		comm
		| WS
	)*
;

process
:
	PROCESS
;

dtd
:
	DTD
;

comm
:
	COMMENT
;

cdata
:
	CDATA
;

singleNode
:
	begin=BRACKET_L tagName = NAME attribute* WS? slash=SLASH end=BRACKET_R
;

complexNode
:
	beginL=BRACKET_L tagName = NAME attribute* WS? beginR=BRACKET_R
	(
		process
		| dtd
		| comm
		| cdata
		| text
		| singleNode
		| complexNode
	)*
	{checkEndTag($tagName.text)}?

	endL=BRACKET_L endSlash=SLASH endName=NAME endR=BRACKET_R
;

attribute
:
	space=WS name = NAME WS? equals=EQUALS WS?
	(
		begin=DOUBLE_QUOTE value = attributeValue ['\''] end=DOUBLE_QUOTE
		| begin=SINGLE_QUOTE value = attributeValue ['\"'] end=SINGLE_QUOTE
	)
;

attributeValue [int quote]
:
	(
		NAME
		| ENTITY_REF
		| CHAR_REF
		| EQUALS
		| SLASH
		| WS
		| OTHER
		|
		{$quote=='\''}?

		SINGLE_QUOTE
		|
		{$quote=='\"'}?

		DOUBLE_QUOTE
	)*
;

text
:
	NAME
	| ENTITY_REF
	| CHAR_REF
	| EQUALS
	| SLASH
	| DOUBLE_QUOTE
	| SINGLE_QUOTE
	| WS
	| OTHER
;

PROCESS
:
	'<?' .*? '?>'
;

CDATA
:
	'<![CDATA[' .*? ']]>'
;

COMMENT
:
	'<!--' .*? '-->'
;

DTD
:
	'<!' .*? '>'
;

ENTITY_REF
:
	'&' NAME ';'
	| '%' NAME ';'
;

CHAR_REF
:
	'&#' [0-9]+ ';'
	| '&#x' [0-9a-fA-F]+ ';'
;

NAME
:
	NAME_CHAR_1+
	(
		NAME_CHAR_1
		| NAME_CHAR_2
	)*
;

BRACKET_L
:
	'<'
;

BRACKET_R
:
	'>'
;

SLASH
:
	'/'
;

EQUALS
:
	'='
;

DOUBLE_QUOTE
:
	'"'
;

SINGLE_QUOTE
:
	'\''
;

WS
:
	[ |\t|\r|\n]+
;

OTHER
:
	.
;

fragment
NAME_CHAR_1
:
	':'
	| '_'
	| [a-z]
	| [A-Z]
	| '\\uC0' .. '\\uD'
	| '\\uD8' .. '\\uF6'
	| '\\uF8' .. '\\u2FF'
	| '\\u370' .. '\\u37D'
	| '\\u37F' .. '\\u1FFF'
	| '\\u200C' .. '\\u200D'
	| '\\u2070' .. '\\u218F'
	| '\\u2C00' .. '\\u2FEF'
	| '\\u3001' .. '\\uD7FF'
	| '\\uF900..\\uFDCF'
	| '\\uFDF0' .. '\\uFFFD'
	| '\\u10000' .. '\\uEFFFF'
;

fragment
NAME_CHAR_2
:
	'-'
	| '.'
	| [0-9]
	| '\\uB7'
	| '\\u0300' .. '\\u036F'
	| '\\u203F' .. '\\u2040'
;

