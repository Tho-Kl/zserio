package zserio.ast4;

import org.antlr.v4.runtime.Token;

/**
 * AST abstract node for all built-in types.
 *
 * This is an abstract class for all built-in Zserio types (boolean, float16, string, ...).
 */
public abstract class BuiltInType extends AstNodeBase implements ZserioType
{
    public BuiltInType(Token token)
    {
        super(token);
        this.name = token.getText();
    }

    @Override
    public Package getPackage()
    {
        // built-in types do not have any package
        throw new InternalError("BuiltInType.getPackage() is not implemented!");
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Iterable<ZserioType> getUsedTypeList()
    {
        throw new InternalError("BuiltInType.getUsedTypeList() is not implemented!");
    }

    private final String name;
}
