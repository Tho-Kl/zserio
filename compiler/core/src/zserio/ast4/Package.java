package zserio.ast4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.v4.runtime.Token;

import zserio.ast.PackageName;
import zserio.tools.HashUtil;
import zserio.tools.ZserioToolPrinter;

public class Package extends AstNodeBase
{
    public Package(Token token, PackageName packageName, List<Import> imports)
    {
        super(token);
        this.packageName = packageName;
        this.imports = imports;
    }

    @Override
    public void walk(ZserioListener listener)
    {
        listener.enterPackage(this);
    }

    public PackageName getPackageName()
    {
        return packageName;
    }

    /**
     * Gets Zserio type for given type name with its package if it's visible for this package.
     *
     * This method does not throw exception in case of ambiguous type. It just returns null in this case.
     *
     * @param typePackageName Package name of the type to resolve.
     * @param typeName        Type name to resolve.
     *
     * @return Zserio type if given type name is visible for this package or null if given type name is unknown.
     */
    public ZserioType getVisibleType(PackageName typePackageName, String typeName)
    {
        final List<ZserioType> foundTypes = getAllVisibleTypes(typePackageName, typeName);

        return (foundTypes.size() == 1) ? foundTypes.get(0) : null;
    }

    /**
     * Gets list of all local types stored in the packages.
     *
     * This is called from doc emitter only. Doc emitter should be redesigned not to require such method. TODO
     *
     * @return List of all local types stored in the packages.
     */
    public Iterable<ZserioType> getLocalTypes()
    {
        return localTypes.values();
    }

    /**
     * Resolves this package.
     *
     * This method
     *
     * - resolves all imports which belong to this package
     * - resolves all type references which belong to this package
     * - resolves all subtypes which belong to this package
     *
     * @param packageNameMap   Map of all available package name to the package object.
     *
     * @throws ParserException In case of wrong import or wrong type reference or if cyclic subtype definition
     *                         is detected.
     */
    protected void resolve(Map<PackageName, Package> packageNameMap)
    {
        // TODO: resolve
        // because subtypes can used type references, type references must be resolved before subtypes
        resolveImports(packageNameMap);
        //resolveTypeReferences();
        //resolveSubtypes();
    }

    private void resolveImports(Map<PackageName, Package> packageNameMap)
    {
        for (Import importedNode : imports)
        {
            final PackageName importedPackageName = importedNode.getImportedPackageName();
            final Package importedPackage = packageNameMap.get(importedPackageName);
            if (importedPackage == null)
            {
                // imported package has not been found => this could happen only for default packages
                throw new ParserException(this, "Default package cannot be imported!");
            }

            final String importedTypeName = importedNode.getImportedTypeName();
            if (importedTypeName == null)
            {
                // this is package import
                if (importedPackages.contains(importedPackage))
                    ZserioToolPrinter.printWarning(this, "Duplicated import of package '" +
                            importedPackageName.toString() + "'.");

                // check redundant single type imports
                final List<SingleTypeName> redundantSingleTypeImports = new ArrayList<SingleTypeName>();
                for (SingleTypeName importedSingleType : importedSingleTypes)
                {
                    if (importedSingleType.getPackageType().getPackageName().equals(importedPackageName))
                    {
                        ZserioToolPrinter.printWarning(this, "Import of package '" +
                                importedPackageName.toString() + "' overwrites single type import '" +
                                importedSingleType.getTypeName() + "'.");
                        redundantSingleTypeImports.add(importedSingleType);
                    }
                }

                // remove all redundant single type imports to avoid ambiguous error
                for (SingleTypeName redundantSingleTypeImport : redundantSingleTypeImports)
                    importedSingleTypes.remove(redundantSingleTypeImport);

                importedPackages.add(importedPackage);
            }
            else
            {
                // this is single type import
                if (importedPackages.contains(importedPackage))
                {
                    ZserioToolPrinter.printWarning(this, "Single type '" + importedTypeName +
                            "' imported already by package import.");
                    // don't add it to imported single types because this type would become ambiguous
                }
                else
                {
                    final SingleTypeName importedSingleType = new SingleTypeName(importedPackage,
                            importedTypeName);
                    if (importedSingleTypes.contains(importedSingleType))
                        ZserioToolPrinter.printWarning(this, "Duplicated import of type '" +
                                importedTypeName + "'.");

                    final ZserioType importedZserioType = importedPackage.getLocalType(importedPackageName,
                            importedTypeName);
                    if (importedZserioType == null)
                        throw new ParserException(this, "Unknown type '" + importedTypeName +
                                "' in imported package '" + importedPackageName + "'!");

                    importedSingleTypes.add(importedSingleType);
                }
            }
        }
    }

    private List<ZserioType> getAllVisibleTypes(PackageName typePackageName, String typeName)
    {
        final List<ZserioType> foundTypes = new ArrayList<ZserioType>();
        final ZserioType foundLocalType = getLocalType(typePackageName, typeName);
        if (foundLocalType != null)
        {
            foundTypes.add(foundLocalType);
        }
        else
        {
            // because we must check for ambiguous types, we must collect all found types
            foundTypes.addAll(getTypesFromImportedPackages(typePackageName, typeName));
            foundTypes.addAll(getTypesFromImportedSingleTypes(typePackageName, typeName));
        }

        return foundTypes;
    }

    private ZserioType getLocalType(PackageName typePackageName, String typeName)
    {
        if (!typePackageName.isEmpty() && !typePackageName.equals(getPackageName()))
            return null;

        return localTypes.get(typeName);
    }

    private List<ZserioType> getTypesFromImportedPackages(PackageName typePackageName, String typeName)
    {
        final List<ZserioType> foundTypes = new ArrayList<ZserioType>();
        for (Package importedPackage : importedPackages)
        {
            // don't exit the loop if something has been found, we need to check for ambiguities
            final ZserioType importedType = importedPackage.getLocalType(typePackageName, typeName);
            if (importedType != null)
                foundTypes.add(importedType);
        }

        return foundTypes;
    }

    private List<ZserioType> getTypesFromImportedSingleTypes(PackageName typePackageName, String typeName)
    {
        final List<ZserioType> foundTypes = new ArrayList<ZserioType>();
        for (SingleTypeName importedSingleType : importedSingleTypes)
        {
            // don't exit the loop if something has been found, we need to check for ambiguities
            if (typeName.equals(importedSingleType.getTypeName()))
            {
                final Package importedPackage = importedSingleType.getPackageType();
                final ZserioType importedType = importedPackage.getLocalType(typePackageName, typeName);
                if (importedType != null)
                    foundTypes.add(importedType);
            }
        }

        return foundTypes;
    }

    private static class SingleTypeName implements Comparable<SingleTypeName>
    {
        public SingleTypeName(Package packageType, String typeName)
        {
            this.packageType = packageType;
            this.typeName = typeName;
        }

        @Override
        public int compareTo(SingleTypeName other)
        {
            final int result = typeName.compareTo(other.typeName);
            if (result != 0)
                return result;

            return packageType.getPackageName().compareTo(other.packageType.getPackageName());
        }

        @Override
        public boolean equals(Object other)
        {
            if (this == other)
                return true;

            if (other instanceof SingleTypeName)
                return compareTo((SingleTypeName)other) == 0;

            return false;
        }

        @Override
        public int hashCode()
        {
            int hash = HashUtil.HASH_SEED;
            hash = HashUtil.hash(hash, typeName);
            hash = HashUtil.hash(hash, packageType.getPackageName());

            return hash;
        }

        public Package getPackageType()
        {
            return packageType;
        }

        public String getTypeName()
        {
            return typeName;
        }

        private final Package packageType;
        private final String typeName;
    }

    private PackageName packageName;
    private List<Import> imports;

    // this must be a LinkedHashMap because of 'Cyclic dependency' error checked in resolveSubtypes()
    private final Map<String, ZserioType> localTypes = new LinkedHashMap<String, ZserioType>();
    private final Set<Package> importedPackages = new HashSet<Package>();
    // this must be a TreeSet because of 'Ambiguous type reference' error checked in getVisibleType()
    private final Set<SingleTypeName> importedSingleTypes = new TreeSet<SingleTypeName>();
    //private final List<TypeReference> typeReferencesToResolve = new ArrayList<TypeReference>(); // TODO:
}
