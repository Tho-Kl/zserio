grammar DocComment;

/** Parser */

docComment : COMMENT_BEGIN whitespace* docText? whitespace* COMMENT_END ;

docText : docParagraph (NEWLINE SPACE* NEWLINE whitespace* docParagraph)* ;

docParagraph : docTextLine (NEWLINE docTextLine)* ;

docTextLine : docTextElement+ ;

docTextElement
    : tag
    | text
    ;

tag
    : seeTag
    | todoTag
    | paramTag
    | deprecatedTag
    ;

text
    : SEE
    | TODO
    | PARAM
    | ID
    | WORD
    | DOUBLE_QUOTE
    | DOT
    | SPACE
    ;

seeTag : SEE whitespace+ (seeTagAlias whitespace+)? seeTagId ;

seeTagAlias : DOUBLE_QUOTE ID DOUBLE_QUOTE ;

seeTagId : ID (DOT ID)* ;

todoTag : TODO whitespace+ docTextLine ;

paramTag : PARAM whitespace+ ID whitespace+ docTextLine ;

deprecatedTag : DEPRECATED ;

whitespace : SPACE | NEWLINE ;

/** Lexer */

fragment
STAR : '*' ;

fragment
SLASH : '/' ;

fragment
AT : '@' ;

fragment
NEWLINE_COMMENT : SPACE (STAR { _input.LA(1) != '/' }?)+ ;

COMMENT_BEGIN : '/**' STAR* SPACE* ;

COMMENT_END : SPACE* STAR* '*/' ;

NEWLINE : ('\r'? '\n' | '\r') NEWLINE_COMMENT? SPACE? ;

SPACE : ' ' | '\t' ;

DOT : '.' ;

DOUBLE_QUOTE : '"' ;

SEE : AT 'see' ;

TODO : AT 'todo' ;

PARAM : AT 'param' ;

DEPRECATED : AT 'deprecated' ;

ID : [a-zA-Z_][a-zA-Z_0-9]+ ;

WORD : ~[ .\t\r\n"]+ ;
