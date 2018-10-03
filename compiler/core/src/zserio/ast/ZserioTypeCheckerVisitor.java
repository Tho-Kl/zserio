package zserio.ast;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import zserio.ast.ArrayType;
import zserio.ast.UnionType;
import zserio.ast.BooleanType;
import zserio.ast.ChoiceType;
import zserio.ast.CompoundType;
import zserio.ast.ConstType;
import zserio.ast.ZserioType;
import zserio.ast.ZserioTypeUtil;
import zserio.ast.ZserioTypeVisitor;
import zserio.ast.EnumType;
import zserio.ast.FloatType;
import zserio.ast.FunctionType;
import zserio.ast.StructureType;
import zserio.ast.SignedBitFieldType;
import zserio.ast.SqlDatabaseType;
import zserio.ast.SqlTableType;
import zserio.ast.StdIntegerType;
import zserio.ast.StringType;
import zserio.ast.Subtype;
import zserio.ast.TokenAST;
import zserio.ast.TypeInstantiation;
import zserio.ast.TypeReference;
import zserio.ast.UnsignedBitFieldType;
import zserio.ast.VarIntegerType;
import zserio.tools.ZserioToolPrinter;

/**
 * The ZserioType visitor which checks:
 *
 * - the unused Zserio types
 */
public class ZserioTypeCheckerVisitor implements ZserioTypeVisitor
{
    /**
     * Constructor.
     *
     * @param printUnusedWarnings Whether to print unused warnings.
     */
    ZserioTypeCheckerVisitor(boolean printUnusedWarnings)
    {
        this.printUnusedWarnings = printUnusedWarnings;
    }

    /** {@inheritDoc} */
    @Override
    public void visitArrayType(ArrayType type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitUnionType(UnionType type)
    {
        visitCompoundType(type);
    }

    /** {@inheritDoc} */
    @Override
    public void visitUnsignedBitFieldType(UnsignedBitFieldType type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitBooleanType(BooleanType type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitChoiceType(ChoiceType type)
    {
        visitCompoundType(type);
    }

    /** {@inheritDoc} */
    @Override
    public void visitConstType(ConstType type)
    {
        addUsedType(type.getConstType());
    }

    /** {@inheritDoc} */
    @Override
    public void visitEnumType(EnumType type)
    {
        addUsedType(type.getIntegerBaseType());
        definedTypes.add(type);
    }

    /** {@inheritDoc} */
    @Override
    public void visitFloatType(FloatType type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitFunctionType(FunctionType type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitServiceType(ServiceType type)
    {
        final Iterable<Rpc> rpcList = type.getRpcList();
        for (Rpc rpc : rpcList)
        {
            addUsedType(rpc.getResponseType());
            addUsedType(rpc.getRequestType());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void visitStructureType(StructureType type)
    {
        visitCompoundType(type);
    }

    /** {@inheritDoc} */
    @Override
    public void visitSignedBitFieldType(SignedBitFieldType type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitSqlDatabaseType(SqlDatabaseType type)
    {
        visitCompoundTypeFields(type);
    }

    /** {@inheritDoc} */
    @Override
    public void visitSqlTableType(SqlTableType type)
    {
        visitCompoundType(type);
    }

    /** {@inheritDoc} */
    @Override
    public void visitStdIntegerType(StdIntegerType type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitStringType(StringType type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitSubtype(Subtype type)
    {
        addUsedType(type.getTargetType());
        definedTypes.add(type);
    }

    /** {@inheritDoc} */
    @Override
    public void visitTypeInstantiation(TypeInstantiation type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitTypeReference(TypeReference type)
    {
    }

    /** {@inheritDoc} */
    @Override
    public void visitVarIntegerType(VarIntegerType type)
    {
    }

    /**
     * Prints warnings.
     */
    public void printWarnings()
    {
        if (printUnusedWarnings)
        {
            for (ZserioType definedType : definedTypes)
            {
                final String definedTypeName = ZserioTypeUtil.getFullName(definedType);
                if (!usedTypeNames.contains(definedTypeName))
                    ZserioToolPrinter.printWarning((TokenAST)definedType, "Type " + definedTypeName +
                            " is not used.");
            }
        }
    }

    private void visitCompoundType(CompoundType type)
    {
        definedTypes.add(type);
        visitCompoundTypeFields(type);
    }

    private void visitCompoundTypeFields(CompoundType type)
    {
        final Iterable<ZserioType> usedTypeList = type.getUsedTypeList();
        for (ZserioType usedType : usedTypeList)
            addUsedType(usedType);
    }

    private void addUsedType(ZserioType usedType)
    {
        if (!ZserioTypeUtil.isBuiltIn(usedType))
            usedTypeNames.add(ZserioTypeUtil.getFullName(usedType));
    }

    private final Set<String> usedTypeNames = new HashSet<String>();
    private final List<ZserioType> definedTypes = new ArrayList<ZserioType>();
    private final boolean printUnusedWarnings;
}
