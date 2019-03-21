package zserio.ast;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import antlr.CommonHiddenStreamToken;
import zserio.antlr.DocCommentLexer;
import zserio.antlr.DocCommentParser;
import zserio.antlr.util.ParserException;
import zserio.ast.doc.DocCommentListener;
import zserio.ast.doc.DocCommentToken;

/**
 * The representation of AST node type DOC_COMMENT.
 *
 * The AST node DOC_COMMENT represents documentation comment as a single string. The documentation comment
 * string is parsed using special grammar file DocComment.g.
 *
 * This AST node is not created automatically by ANTRL but explicitly by TokenAST class during scanning
 * of all hidden tokens.
 */
public class DocCommentString extends TokenAST
{
    /**
     * Constructor from lexer token and owner.
     *
     * @param lexerToken Lexer token to construct from.
     * @param owner      Zserio type to which belongs this documentation comment.
     */
    public DocCommentString(CommonHiddenStreamToken lexerToken, ZserioType owner)
    {
        super(lexerToken);
        this.owner = owner;
    }

    /**
     * Gets documentation comment token parsed from documentation comment string.
     *
     * @return Documentation comment token parsed from documentation comment string.
     */
    public DocCommentToken getDocCommentToken()
    {
        return docCommentToken;
    }

    @Override
    protected void evaluate() throws ParserException
    {
        final CharStream inputStream = CharStreams.fromString(getText());
        final DocCommentLexer docLexer = new DocCommentLexer(inputStream);
        final DocCommentParser docParser = new DocCommentParser(new CommonTokenStream(docLexer));
        final DocCommentListener docListener = new DocCommentListener();
        final ParseTree docTree = docParser.docComment();
        ParseTreeWalker.DEFAULT.walk(docListener, docTree);

        docCommentToken = docListener.getDocComment();

        /*final int commentLexerTokenLine = getLine();
        final int baseLineNumber = (commentLexerTokenLine > 0) ? commentLexerTokenLine - 1 : 0;
        final StringReader inputString = new StringReader(getText());
        final DocCommentLexer docLexer = new DocCommentLexerWithFileNameSupport(inputString, baseLineNumber);
        docLexer.setFilename(getFileName());
        docLexer.setTokenObjectClass(FileNameLexerToken.class.getCanonicalName());
        final DocCommentParser docParser = new DocCommentParser(docLexer);
        docParser.setASTNodeClass(DocTokenAST.class.getCanonicalName());
        try
        {
            docParser.docCommentDeclaration();
        }
        catch (RecognitionException exception)
        {
            throw new ParserException(getFileName(), exception.getLine(), exception.getColumn(),
                    exception.getMessage());
        }
        catch (TokenStreamRecognitionException exception)
        {
            if (exception.recog != null)
                throw new ParserException(getFileName(), exception.recog.getLine(), exception.recog.getColumn(),
                        exception.recog.getMessage());
            else
                throw new ParserException(getFileName(), exception.toString());
        }
        catch (TokenStreamException exception)
        {
            throw new ParserException(getFileName(), exception.toString());
        }

        docCommentToken = (DocCommentToken)docParser.getAST();
        docCommentToken.evaluateAll();*/
    }

    @Override
    protected void check() throws ParserException
    {
        docCommentToken.check(owner);
    }

    /*private static class DocCommentLexerWithFileNameSupport extends DocCommentLexer
    {
        DocCommentLexerWithFileNameSupport(StringReader reader, int baseLineNumber)
        {
            super(reader);
            this.baseLineNumber = baseLineNumber;
        }

        @Override
        public int getLine()
        {
            return baseLineNumber + super.getLine();
        }

        @Override
        protected Token makeToken(int t)
        {
            final Token token = super.makeToken(t);
            token.setFilename(getFilename());
            token.setLine(baseLineNumber + token.getLine());

            return token;
        }

        private final int baseLineNumber;
    }*/

    private static final long serialVersionUID = -1L;

    private final ZserioType owner;
    private DocCommentToken docCommentToken = null;
}
