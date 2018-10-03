package zserio.ast;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import zserio.antlr.ZserioParserTokenTypes;
import zserio.antlr.util.BaseTokenAST;
import zserio.antlr.util.ParserException;
import zserio.tools.HashUtil;

/**
 * AST node for service types.
 *
 * Service types are Zserio types as well.
 */
public class ServiceType extends TokenAST implements ZserioScopedType, Comparable<ServiceType>
{
    @Override
    public int compareTo(ServiceType other)
    {
        final int result = getName().compareTo(other.getName());
        if (result != 0)
            return result;

        return getPackage().getPackageName().compareTo(other.getPackage().getPackageName());
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
            return true;

        if (other instanceof ServiceType)
            return compareTo((ServiceType)other) == 0;

        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = HashUtil.HASH_SEED;
        hash = HashUtil.hash(hash, getName());
        hash = HashUtil.hash(hash, getPackage().getPackageName());

        return hash;
    }

    @Override
    public Package getPackage()
    {
        return pkg;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Iterable<ZserioType> getUsedTypeList()
    {
        return usedTypeList;
    }

    @Override
    public void callVisitor(ZserioTypeVisitor visitor)
    {
        visitor.visitServiceType(this);
    }

    @Override
    public Scope getScope()
    {
        return scope;
    }

    /**
     * Sets package for the service type.
     *
     * This method is called by code generated by ANTLR using TypeEvaluator.g.
     *
     * @param pkg Package to set.
     */
    public void setPackage(Package pkg)
    {
        this.pkg = pkg;
    }

    /**
     * Gets rpc methods list.
     *
     * @return List of RPC methods.
     */
    public Iterable<Rpc> getRpcList()
    {
        return rpcs;
    }

    @Override
    protected boolean evaluateChild(BaseTokenAST child) throws ParserException
    {
        switch (child.getType())
        {
        case ZserioParserTokenTypes.ID:
            if (!(child instanceof IdToken))
                return false;
            name = child.getText();
            break;

        case ZserioParserTokenTypes.RPC:
            if (!(child instanceof Rpc))
                return false;
            final Rpc rpc = (Rpc)child;
            rpc.setServiceType(this);
            rpcs.add(rpc);
            break;

        default:
            return false;
        }

        return true;
    }

    @Override
    protected void evaluate() throws ParserException
    {
        evaluateHiddenDocComment(this);
    }

    @Override
    protected void check() throws ParserException
    {
        for (Rpc rpc : rpcs)
        {
            usedTypeList.addAll(rpc.getUsedTypeList());
        }
    }

    private static final long serialVersionUID = -6520580190710341443L;

    private final Scope scope = new Scope(this);
    private Package pkg;

    private String name;
    private final List<Rpc> rpcs = new ArrayList<Rpc>();
    private final Set<ZserioType> usedTypeList = new LinkedHashSet<ZserioType>();
}
