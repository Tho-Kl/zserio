package zserio.ast.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements AST token for type DOC_PARAGRAPH.
 */
public class DocParagraphToken
{
    /**
     * Gets the list of paragraph texts stored in paragraph.
     *
     * @return The list of paragraph texts stored in paragraph.
     */
    public Iterable<DocParagraphTokenText> getParagraphTextList()
    {
        return docParagraphTextList;
    }

    /**
     * Gets the list of stand-alone see tags stored in paragraph.
     *
     * @return The list of stand-alone see tags stored in paragraph.
     */
    public Iterable<DocTagSeeToken> getTagSeeList()
    {
        return docTagSeeList;
    }

    /**
     * Gets the list of param tags stored in paragraph.
     *
     * @return The list of param tags stored in paragraph.
     */
    public Iterable<DocTagParamToken> getTagParamList()
    {
        return docTagParamList;
    }

    /**
     * Gets the list of todo tags stored in paragraph.
     *
     * @return The list of todo tags stored in paragraph.
     */
    public Iterable<DocTagTodoToken> getTagTodoList()
    {
        return docTagTodoList;
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

    /**
     * Helper class to store list of texts and see tags stored in the one paragraph token.
     */
    public static class DocParagraphTokenText
    {
        /**
         * Constructor.
         *
         * @param text The paragraph text to construct from.
         */
        public DocParagraphTokenText(String text)
        {
            textList = new ArrayList<String>();
            textList.add(text);
            tagSeeList = new ArrayList<DocTagSeeToken>();
        }

        /**
         * Adds the paragraph text into the list.
         *
         * @param text Paragraph text to add.
         */
        public void addText(String text)
        {
            textList.add(text);
        }

        /**
         * Adds the tag see token into the list.
         *
         * @param tagSee Tag see token to add.
         */
        public void addTagSee(DocTagSeeToken tagSee)
        {
            tagSeeList.add(tagSee);
        }

        /**
         * Checks if list of tag see tokens is empty.
         *
         * @return Returns true if list of tag see tokens is empty.
         */
        public boolean isTagSeeListEmpty()
        {
            return tagSeeList.isEmpty();
        }

        /**
         * Gets the list of the paragraph texts.
         *
         * @return The list of the paragraph texts.
         */
        public Iterable<String> getTextList()
        {
            return textList;
        }

        /**
         * Gets the list of the tag see tokens.
         *
         * @return The list of the tag see tokens.
         */
        public Iterable<DocTagSeeToken> getTagSeeList()
        {
            return tagSeeList;
        }

        private final List<String>          textList;
        private final List<DocTagSeeToken>  tagSeeList;
    }

    protected void addText(String text)
    {
        if (docParagraphTextList.isEmpty())
        {
            docParagraphTextList.add(new DocParagraphTokenText(text));
        }
        else
        {
            final DocParagraphTokenText last = docParagraphTextList.get(docParagraphTextList.size() - 1);
            if (last.isTagSeeListEmpty())
                last.addText(text);
            else
                docParagraphTextList.add(new DocParagraphTokenText(text));
        }
        wasText = true;
    }

    protected void addSeeTag(DocTagSeeToken seeTag)
    {
        if (wasText)
        {
            final DocParagraphTokenText last = docParagraphTextList.get(docParagraphTextList.size() - 1);
            last.addTagSee(seeTag);
        }
        else
        {
            docTagSeeList.add(seeTag);
        }
    }

    protected void addParamTag(DocTagParamToken paramTag)
    {
        docTagParamList.add(paramTag);
        wasText = false;
    }

    protected void addTodoTag(DocTagTodoToken todoTag)
    {
        docTagTodoList.add(todoTag);
        wasText = false;
    }

    protected void setDeprecated()
    {
        isDeprecated = true;
        wasText = false;
    }

    private final List<DocParagraphTokenText> docParagraphTextList = new ArrayList<DocParagraphTokenText>();
    private final List<DocTagSeeToken> docTagSeeList = new ArrayList<DocTagSeeToken>();
    private final List<DocTagParamToken> docTagParamList = new ArrayList<DocTagParamToken>();
    private final List<DocTagTodoToken> docTagTodoList = new ArrayList<DocTagTodoToken>();
    private boolean isDeprecated;
    private boolean wasText = false;
}
