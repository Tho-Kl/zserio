package zserio.ast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import zserio.antlr.util.BaseTokenAST;
import zserio.antlr.util.ParserException;

/**
 * This class represents a lexical scope which maps symbol names to objects.
 *
 * A symbol name must be unique in its scope. Each scope belongs to the one package. Scopes are used for
 * types (called owner) which contains fields (Compound Types, SQL Table, SQL Database...). Then, field name
 * is a symbol name and object represents this field.
 *
 * Scopes are filled by ANTLR2 TypeEvaluator walker.
 */
public class Scope implements Serializable
{
    /**
     * Constructs scope within given package and sets owner to the given Zserio type.
     *
     * @param owner Zserio scoped type which owns the current scope.
     */
    public Scope(ZserioScopedType owner)
    {
        this.owner = owner;
    }

    /**
     * Copy constructor.
     *
     * @param scope Scope to construct from.
     */
    public Scope(Scope scope)
    {
        owner = scope.owner;
        add(scope);
    }

    /**
     * Adds a name with its corresponding object to the current scope.
     *
     * @param name   AST node which defines name in current scope.
     * @param symbol AST node which represent an object of that name.
     *
     * @throws ParserException Throws if symbol has been already defined in the current scope.
     */
    public void setSymbol(BaseTokenAST name, Object symbol) throws ParserException
    {
        final Object symbolObject = symbolTable.put(name.getText(), symbol);
        if (symbolObject != null)
            throw new ParserException(name, "'" + name.getText() + "' is already defined in this scope!");
    }

    /**
     * Removes the symbol object for the given name.
     *
     * @param name Name of the symbol to be removed.
     *
     * @return Removed symbol object or null if no such symbol is available.
     */
    public Object removeSymbol(BaseTokenAST name)
    {
        return symbolTable.remove(name.getText());
    }

    /**
     * Adds another scope to the scope.
     *
     * @param scope Scope to be added.
     */
    public void add(Scope addedScope)
    {
        symbolTable.putAll(addedScope.symbolTable);
    }

    /**
     * Gets the Zserio type in which is defined this lexical scope.
     *
     * The scope always has an owner except of expressions defined directly in the package (like const).
     * The scope is 'null' in this case.
     *
     * @return Zserio type which owns this lexical scope or null.
     */
    public ZserioType getOwner()
    {
        return owner;
    }

    /**
     * Get the symbol object for the given name.
     *
     * @param name Name of the symbol to be looked up.
     *
     * @return Corresponding symbol object or null if no such symbol is visible.
     */
    public Object getSymbol(String name)
    {
        return symbolTable.get(name);
    }

    private static final long serialVersionUID = -5373010074297029934L;

    private final ZserioType owner;

    /**
     * Symbol table containing local symbols defined within the current scope. Each symbol is mapped to
     * an Object.
     */
    private final Map<String, Object> symbolTable = new HashMap<String, Object>();
}
