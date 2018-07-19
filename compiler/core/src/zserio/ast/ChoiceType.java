package zserio.ast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import zserio.antlr.ZserioParserTokenTypes;
import zserio.antlr.util.BaseTokenAST;
import zserio.antlr.util.ParserException;
import zserio.tools.ZserioToolPrinter;

/**
 * AST node for choice types.
 *
 * Choice types are Zserio types as well.
 */
public class ChoiceType extends CompoundType
{
    /**
     * Default constructor.
     */
    public ChoiceType()
    {
        choiceCases = new ArrayList<ChoiceCase>();
    }

    @Override
    public Iterable<ZserioType> getUsedTypeList()
    {
        Set<ZserioType> usedTypeSet = new LinkedHashSet<ZserioType>();
        if (selectorExpression != null)
        {
            final ZserioType usedType = selectorExpression.getExprZserioType();
            if (!ZserioTypeUtil.isBuiltIn(usedType))
                usedTypeSet.add(usedType);
        }

        addFieldsToUsedTypeSet(usedTypeSet);

        return usedTypeSet;
    }

    @Override
    public void callVisitor(ZserioTypeVisitor visitor)
    {
        visitor.visitChoiceType(this);
    }

    @Override
    public <T extends ZserioType> Set<T> getReferencedTypes(Class<? extends T> clazz)
    {
        final Set<T> referencedTypes = super.getReferencedTypes(clazz);

        // add choice-specific expressions: selector expression
        referencedTypes.addAll(selectorExpression.getReferencedSymbolObjects(clazz));

        // add choice-specific expressions: case expressions
        for (ChoiceCase choiceCase : choiceCases)
        {
            final Iterable<ChoiceCase.CaseExpression> caseExpressions = choiceCase.getExpressions();
            for (ChoiceCase.CaseExpression caseExpression : caseExpressions)
                referencedTypes.addAll(caseExpression.getExpression().getReferencedSymbolObjects(clazz));
        }

        return referencedTypes;
    }

    /**
     * Extends scope for case expressions to support enumeration values.
     *
     * This method is called from expression evaluator generated by ANTLR.
     *
     * @param scope Scope which shall be added to case expression scopes.
     */
    public void addScopeForCaseExpressions(Scope scope)
    {
        for (ChoiceCase choiceCase : choiceCases)
        {
            final List<ChoiceCase.CaseExpression> caseExpressions = choiceCase.getExpressions();
            for (ChoiceCase.CaseExpression caseExpression : caseExpressions)
            {
                final Expression expression = caseExpression.getExpression();
                expression.setScope(Scope.createMixedScope(expression.getScope(), scope));
            }
        }
    }

    /**
     * Adds field to the compound type.
     *
     * @param field Field to add.
     */
    public void addField(Field field)
    {
        super.addField(field);
    }

    /**
     * Gets selector expression.
     *
     * Selector expression is compulsory for choice types, therefore this method cannot return null.
     *
     * @return Returns expressions which is given as choice selector.
     */
    public Expression getSelectorExpression()
    {
        return selectorExpression;
    }

    /**
     * Gets list of choice cases defined by the choice.
     *
     * @return List of choice cases.
     */
    public Iterable<ChoiceCase> getChoiceCases()
    {
        return choiceCases;
    }

    /**
     * Gets default case defined by the choice.
     *
     * @return Default case or null if default case is not defined.
     */
    public ChoiceDefault getChoiceDefault()
    {
        return choiceDefault;
    }

    /**
     * Checks if default case in choice can happen.
     *
     * Actually, only boolean choices can have default case unreachable. This can happen only if the bool
     * choice has defined both cases (true and false).
     *
     * @return Returns true if default case is unreachable.
     */
    public boolean isChoiceDefaultUnreachable()
    {
        return isChoiceDefaultUnreachable;
    }

    @Override
    protected boolean evaluateChild(BaseTokenAST child) throws ParserException
    {
        if (child instanceof Expression && selectorExpression == null)
        {
            selectorExpression = (Expression)child;
        }
        else
        {
            switch (child.getType())
            {
            case ZserioParserTokenTypes.ID:
                if (!(child instanceof IdToken))
                    return false;
                setName(child.getText());
                break;

            case ZserioParserTokenTypes.PARAM:
                if (!(child instanceof Parameter))
                    return false;
                addParameter((Parameter)child);
                break;

            case ZserioParserTokenTypes.CASE:
                if (!(child instanceof ChoiceCase))
                    return false;
                final ChoiceCase choiceCase = (ChoiceCase)child;
                choiceCase.setChoiceType(this);
                choiceCases.add(choiceCase);
                break;

            case ZserioParserTokenTypes.DEFAULT:
                if (choiceDefault != null || !(child instanceof ChoiceDefault))
                    return false;
                choiceDefault = (ChoiceDefault)child;
                choiceDefault.setChoiceType(this);
                break;

            case ZserioParserTokenTypes.FUNCTION:
                if (!(child instanceof FunctionType))
                    return false;
                addFunction((FunctionType)child);
                break;

            default:
                return false;
            }
        }

        return true;
    }

    @Override
    protected void evaluate() throws ParserException
    {
        evaluateHiddenDocComment(this);
        setDocComment(getHiddenDocComment());
    }

    @Override
    protected void check() throws ParserException
    {
        super.check();
        checkTableFields();

        checkSelectorType();
        checkCaseTypes();
        checkDuplicatedCases();
        checkEnumerationCases();
        isChoiceDefaultUnreachable = checkUnreachableDefault();
    }

    private void checkSelectorType() throws ParserException
    {
        final Expression.ExpressionType selectorExpressionType = selectorExpression.getExprType();
        if (selectorExpressionType == Expression.ExpressionType.STRING)
            throw new ParserException(this, "Choice '" + getName() + "' uses forbidden string selector!");

        if (selectorExpressionType == Expression.ExpressionType.FLOAT)
            throw new ParserException(this, "Choice '" + getName() + "' uses forbidden float selector!");
    }

    private void checkCaseTypes() throws ParserException
    {
        final Expression.ExpressionType selectorExpressionType = selectorExpression.getExprType();
        for (ChoiceCase choiceCase : choiceCases)
        {
            final List<ChoiceCase.CaseExpression> caseExpressions = choiceCase.getExpressions();
            for (ChoiceCase.CaseExpression caseExpression : caseExpressions)
            {
                final Expression expression = caseExpression.getExpression();
                if (expression.getExprType() != selectorExpressionType)
                    throw new ParserException(expression, "Choice '" + getName() +
                            "' has incompatible case type!");

                if (!expression.getReferencedSymbolObjects(Field.class).isEmpty())
                    throw new ParserException(expression, "Choice '" + getName() +
                            "' has non-constant case expression!");
            }
        }
    }

    private void checkDuplicatedCases() throws ParserException
    {
        final List<Expression> allExpressions = new ArrayList<Expression>();
        for (ChoiceCase choiceCase : choiceCases)
        {
            final List<ChoiceCase.CaseExpression> newCaseExpressions = choiceCase.getExpressions();
            for (ChoiceCase.CaseExpression newCaseExpression : newCaseExpressions)
            {
                final Expression newExpression = newCaseExpression.getExpression();
                for (Expression caseExpression : allExpressions)
                {
                    if (newExpression.equals(caseExpression))
                        throw new ParserException(newExpression, "Choice '" + getName() +
                                "' has duplicated case!");
                }
                allExpressions.add(newExpression);
            }
        }
    }

    private void checkEnumerationCases() throws ParserException
    {
        final ZserioType selectorExpressionType = selectorExpression.getExprZserioType();
        if (selectorExpressionType instanceof EnumType)
        {
            final EnumType resolvedEnumType = (EnumType)TypeReference.resolveType(selectorExpressionType);
            final List<EnumItem> availableEnumItems = new ArrayList<EnumItem>(resolvedEnumType.getItems());

            for (ChoiceCase choiceCase : choiceCases)
            {
                final List<ChoiceCase.CaseExpression> caseExpressions = choiceCase.getExpressions();
                for (ChoiceCase.CaseExpression caseExpression : caseExpressions)
                {
                    final Expression expression = caseExpression.getExpression();
                    final Set<EnumItem> referencedEnumItems =
                            expression.getReferencedSymbolObjects(EnumItem.class);
                    for (EnumItem referencedEnumItem : referencedEnumItems)
                        if (!availableEnumItems.remove(referencedEnumItem))
                            throw new ParserException(expression, "Choice '" + getName() +
                                    "' has case with different enumeration type than selector!");
                }
            }

            if (choiceDefault == null)
            {
                for (EnumItem availableEnumItem : availableEnumItems)
                {
                    ZserioToolPrinter.printWarning(this, "Enumeration value '" +
                            availableEnumItem.getName() + "' is not handled in choice '" + getName() +
                            "'.");
                }
            }
        }
    }

    private boolean checkUnreachableDefault() throws ParserException
    {
        boolean isDefaulUnreachable = false;
        if (selectorExpression.getExprType() == Expression.ExpressionType.BOOLEAN && choiceCases.size() > 1)
        {
            if (choiceDefault != null)
                throw new ParserException(choiceDefault, "Choice '" + getName() +
                        "' has unreachable default case!");

            isDefaulUnreachable = true;
        }

        return isDefaulUnreachable;
    }

    private static final long serialVersionUID = 5277638180208281212L;

    private final List<ChoiceCase>      choiceCases;

    private ChoiceDefault               choiceDefault;
    private boolean                     isChoiceDefaultUnreachable;
    private Expression                  selectorExpression;
}
