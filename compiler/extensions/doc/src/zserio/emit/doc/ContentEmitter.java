package zserio.emit.doc;

import zserio.ast.CompoundType;
import zserio.ast.ConstType;
import zserio.ast.EnumType;
import zserio.ast.Package;
import zserio.ast.Root;
import zserio.ast.ServiceType;
import zserio.ast.Subtype;
import zserio.ast.ZserioType;
import zserio.emit.common.ZserioEmitException;

public class ContentEmitter extends DefaultHtmlEmitter
{
    private final CompoundEmitter compoundEmitter;
    private final EnumerationEmitter enumerationEmitter;
    private final SubtypeEmitter subtypeEmitter;
    private final ConstTypeEmitter constTypeEmitter;
    private final ServiceEmitter serviceEmitter;

    public ContentEmitter(String outputPath, boolean withSvgDiagrams)
    {
        super(outputPath);

        compoundEmitter = new CompoundEmitter(outputPath, withSvgDiagrams);
        enumerationEmitter = new EnumerationEmitter(outputPath, withSvgDiagrams);
        subtypeEmitter = new SubtypeEmitter(outputPath, withSvgDiagrams);
        constTypeEmitter = new ConstTypeEmitter(outputPath, withSvgDiagrams);
        serviceEmitter = new ServiceEmitter(outputPath, withSvgDiagrams);
    }

    @Override
    public void beginRoot(Root root)
    {
        setCurrentFolder(CONTENT_FOLDER);
    }

    @Override
    public void beginPackage(Package packageToken) throws ZserioEmitException
    {
        super.beginPackage(packageToken);
        emitPackage(packageToken);
    }

    private void emitPackage(Package pkg) throws ZserioEmitException
    {
        for (ZserioType type : pkg.getLocalTypes())
        {
            if (type instanceof CompoundType)
            {
                compoundEmitter.emit((CompoundType)type);
            }
            else if (type instanceof EnumType)
            {
                enumerationEmitter.emit((EnumType)type);
            }
            else if (type instanceof Subtype)
            {
                subtypeEmitter.emit((Subtype)type);
            }
            else if (type instanceof ConstType)
            {
                constTypeEmitter.emit((ConstType)type);
            }
            else if (type instanceof ServiceType)
            {
                serviceEmitter.emit((ServiceType)type);
            }
            else
            {
                throw new RuntimeException("don't know how to emit content for type " + type);
            }
        }
    }
}
