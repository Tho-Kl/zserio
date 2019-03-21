package zserio.ast.doc;

import java.util.ArrayList;
import java.util.List;

import zserio.antlr.util.ParserException;
import zserio.ast.ZserioType;
import zserio.ast.doc.DocParagraphToken.DocParagraphTokenText;

/**
 * Implements the root AST token for documentation comment.
 */
public class DocCommentToken
{
    /**
     * Gets list of documentation paragraph tokens.
     *
     * @return List of documentation paragraph tokens.
     */
    public List<DocParagraphToken> getParagraphList()
    {
        return paragraphList;
    }

    /**
     * Gets deprecated flag.
     *
     * @return Returns true if documentation comment deprecated tag has been specified.
     */
    public boolean isDeprecated()
    {
        return isDeprecated;
    }

    public void check(ZserioType owner) throws ParserException
    {
        for (DocParagraphToken paragraph : paragraphList)
        {
            for (DocTagSeeToken seeTag : paragraph.getTagSeeList())
            {
                seeTag.check(owner);
            }

            for (DocParagraphTokenText paragraphText : paragraph.getParagraphTextList())
            {
                for (DocTagSeeToken seeTag : paragraphText.getTagSeeList())
                {
                    seeTag.check(owner);
                }
            }
        }
    }

    protected void addParagraph(DocParagraphToken docParagraph)
    {
        paragraphList.add(docParagraph);
    }

    private final List<DocParagraphToken> paragraphList = new ArrayList<DocParagraphToken>();
    private boolean isDeprecated;
}
