package zserio.ast.doc;

import zserio.antlr.util.ParserException;
import zserio.ast.SymbolReference;
import zserio.ast.TokenAST;
import zserio.ast.ZserioType;

/**
 * Implements AST token for type DOC_TAG_SEE.
 */
public class DocTagSeeToken
{
    DocTagSeeToken(String alias, String name)
    {
        // link alias is the same as a link name if no alias is available
        linkAlias = alias.isEmpty() ? name : alias;
        linkSymbolReference = new SymbolReference(new TokenAST(), name); // TODO: fake token
    }

    /**
     * Gets string which represents tag see alias name.
     *
     * @return Tag see alias name or referenced symbol name if no alias name is available.
     */
    public String getLinkAlias()
    {
        return linkAlias;
    }

    /**
     * Gets symbol reference to which tag see points.
     *
     * @return Symbol reference of the tag see.
     */
    public SymbolReference getLinkSymbolReference()
    {
        return linkSymbolReference;
    }

    protected void check(ZserioType owner) throws ParserException
    {
        linkSymbolReference.check(owner);
    }

    private String linkAlias = null;
    private SymbolReference linkSymbolReference;
}
