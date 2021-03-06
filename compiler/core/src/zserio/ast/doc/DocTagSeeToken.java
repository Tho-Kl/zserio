package zserio.ast.doc;

import zserio.antlr.util.BaseTokenAST;
import zserio.antlr.util.ParserException;
import zserio.ast.SymbolReference;
import zserio.ast.ZserioType;

/**
 * Implements AST token for type DOC_TAG_SEE.
 */
public class DocTagSeeToken extends DocTokenAST
{
    @Override
    public void evaluate() throws ParserException
    {
        String linkName = null;
        final String tokenText = getText();
        final String[] parameterList = tokenText.split("\"");
        for (String parameter : parameterList)
        {
            if (!parameter.isEmpty())
            {
                // link alias is the same as a link name if no alias is available
                if (linkAlias == null)
                    linkAlias = parameter;
                linkName = parameter;
            }
        }

        linkSymbolReference = new SymbolReference(this, linkName);
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

    @Override
    protected boolean evaluateChild(BaseTokenAST child) throws ParserException
    {
        // no children
        return false;
    }

    @Override
    protected void check(ZserioType owner) throws ParserException
    {
        linkSymbolReference.check(owner);
    }

    private static final long serialVersionUID = 1L;

    private String linkAlias = null;
    private SymbolReference linkSymbolReference;
}
