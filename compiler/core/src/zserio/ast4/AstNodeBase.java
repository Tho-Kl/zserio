package zserio.ast4;

import org.antlr.v4.runtime.Token;

public abstract class AstNodeBase implements AstNode
{
    public AstNodeBase(Token token)
    {
        this.location = new AstNodeLocation(token);
    }

    @Override
    public AstNodeLocation getLocation()
    {
        return location;
    }

    private final AstNodeLocation location;
};
