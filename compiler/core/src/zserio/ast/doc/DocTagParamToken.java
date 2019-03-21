package zserio.ast.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements AST token for type DOC_TAG_PARAM.
 */
public class DocTagParamToken
{
    DocTagParamToken(String paramName, String text)
    {
        this.paramName = paramName;
        paramDescriptionList.add(text);
    }

    /**
     * Gets parameter name of the tag.
     *
     * @return Parameter name of the tag.
     */
    public String getParamName()
    {
        return paramName;
    }

    /**
     * Gets list of text rows which describes the parameter of the tag.
     *
     * @return The list of text rows which describes the parameter of the tag.
     */
    public Iterable<String> getParamDescriptionList()
    {
        return paramDescriptionList;
    }

    private String paramName;
    private final List<String> paramDescriptionList = new ArrayList<String>();
}
