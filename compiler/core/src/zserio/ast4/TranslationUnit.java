package zserio.ast4;

import java.util.List;

import org.antlr.v4.runtime.Token;

public class TranslationUnit extends AstNodeBase
{
    public TranslationUnit(Token token, Package unitPackage, List<Import> imports,
            List<ZserioType> types)
    {
        super(token);
        this.unitPackage = unitPackage;
        this.imports = imports;
        this.types = types;
    }

    @Override
    public void walk(ZserioListener listener)
    {
        listener.beginTranslationUnit(this);
        unitPackage.walk(listener);
        for (Import unitImport : imports)
            unitImport.walk(listener);
        for (ZserioType type : types)
            type.walk(listener);
        listener.endTranslationUnit(this);
    }

    /**
     * Gets the package which is defined in this translation unit.
     *
     * @return Package defined in this translation unit or null for empty input file.
     */
    public Package getPackage()
    {
        return unitPackage;
    }

    /**
     * Gets imports which is defined in this translation unit.
     *
     * @return List of all imports defined in this translation unit.
     */
    public List<Import> getImports()
    {
        return imports;
    }

    /**
     * Gets all types which is defined in this translation unit.
     *
     * @return List of all zserio types defined in this translation unit.
     */
    public List<ZserioType> getTypes()
    {
        return types;
    }

    private final Package unitPackage;
    private final List<Import> imports;
    private final List<ZserioType> types;
}
