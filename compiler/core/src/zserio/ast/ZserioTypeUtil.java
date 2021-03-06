package zserio.ast;

import zserio.tools.StringJoinUtil;

/**
 * This class implements various utilities on ZserioType common for multiple emitters.
 */
public class ZserioTypeUtil
{
    /**
     * Returns the full zserio name with the package name as defined by Zserio source file.
     *
     * @param type ZserioType to get the full name.
     *
     * @return Full zserio name.
     */
    public static String getFullName(ZserioType type)
    {
        if (ZserioTypeUtil.isBuiltIn(type))
            return type.getName();

        return StringJoinUtil.joinStrings(type.getPackage().getPackageName().toString(), type.getName(),
                FULL_NAME_SEPARATOR);
    }

    /**
     * Checks if given type is build-in type (int8, int8[], int16, int16[], etc...).
     *
     * @param type Zserio type to check.
     *
     * @return true if given type is build-in type, otherwise false.
     */
    public static boolean isBuiltIn(ZserioType type)
    {
        return type instanceof BuiltInType ||
               (type instanceof ArrayType && TypeReference.resolveBaseType(
                        ((ArrayType)type).getElementType()) instanceof BuiltInType);
    }

    private static final String FULL_NAME_SEPARATOR = ".";
}
