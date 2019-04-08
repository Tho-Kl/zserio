package zserio.ast4;

import org.antlr.v4.runtime.Token;

/**
 * AST node for string types.
 *
 * String types (Zserio command 'string') are Zserio types as well.
 */
public class StringType extends BuiltInType
{
    public StringType(Token token)
    {
        super(token);
    }

    /*@Override
    public void callVisitor(ZserioTypeVisitor visitor)
    {
        visitor.visitStringType(this);
    }*/

    @Override
    public void walk(ZserioListener listener)
    {
        listener.enterStringType(this);
    }
}
