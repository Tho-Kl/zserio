package zserio.ast.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements AST token for type DOC_TAG_TODO.
 */
public class DocTagTodoToken
{
    public DocTagTodoToken(String todoText)
    {
        todoTextList.add(todoText);
    }

    /**
     * Gets list of text rows which describes the todo tag.
     *
     * @return List of text rows which describes the todo tag.
     */
    public Iterable<String> getTodoTextList()
    {
        return todoTextList;
    }

    private final List<String> todoTextList = new ArrayList<String>();
}
