package zserio.ast.doc;

import zserio.antlr.DocCommentParser;

public class DocCommentListener extends zserio.antlr.DocCommentBaseListener
{
    @Override
    public void enterDocComment(DocCommentParser.DocCommentContext ctx)
    {
        docComment = new DocCommentToken();
    }

    @Override
    public void enterDocParagraph(DocCommentParser.DocParagraphContext ctx)
    {
        docComment.addParagraph(new DocParagraphToken());
    }

    @Override
    public void enterTag(DocCommentParser.TagContext ctx)
    {
        inTag++;
    }

    @Override
    public void exitTag(DocCommentParser.TagContext ctx)
    {
        inTag--;
    }

    @Override
    public void enterSeeTag(DocCommentParser.SeeTagContext ctx)
    {
        final DocParagraphToken currentParagraph = getCurrentParagraph();

        currentParagraph.addSeeTag(new DocTagSeeToken(
                ctx.seeTagAlias() != null ? ctx.seeTagAlias().ID().getText() : "",
                ctx.seeTagId().getText()));
    }

    @Override
    public void enterParamTag(DocCommentParser.ParamTagContext ctx)
    {
        final DocParagraphToken currentParagraph = getCurrentParagraph();

        currentParagraph.addParamTag(new DocTagParamToken(ctx.ID().getText(), ctx.docTextLine().getText()));
    }

    @Override
    public void enterTodoTag(DocCommentParser.TodoTagContext ctx)
    {
        final DocParagraphToken currentParagraph = getCurrentParagraph();

        currentParagraph.addTodoTag(new DocTagTodoToken(ctx.docTextLine().getText()));
    }

    @Override
    public void enterDeprecatedTag(DocCommentParser.DeprecatedTagContext ctx)
    {
        final DocParagraphToken currentParagraph = getCurrentParagraph();

        currentParagraph.setDeprecated();
    }

    @Override
    public void enterText(DocCommentParser.TextContext ctx)
    {
        if (inTag > 0)
            return;

        final DocParagraphToken currentParagraph = getCurrentParagraph();

        currentParagraph.addText(ctx.getText());
    }

    public DocCommentToken getDocComment()
    {
        return docComment;
    }

    private DocParagraphToken getCurrentParagraph()
    {
        return docComment.getParagraphList().get(docComment.getParagraphList().size() - 1);
    }

    DocCommentToken docComment;
    int inTag = 0;
};
