package zserio.ast4;

import java.math.BigInteger;

import org.antlr.v4.runtime.Token;

/**
 * AST abstract node for all integer types.
 *
 * This is an abstract class for all built-in Zserio integer types (int8, uint8, bit:1, int:1, varint16,
 * varuint16, ...).
 */
public abstract class IntegerType extends BuiltInType
{
    public IntegerType(Token token)
    {
        super(token);
    }

    /**
     * Gets upper bound for this integer type.
     *
     * @return Upper bound or null if bit size of this integer type is unknown during compilation time.
     */
    public abstract BigInteger getUpperBound();

    /**
     * Gets lower bound for this integer type.
     *
     * @return Lower bound or null if bit size of this integer type is unknown during compilation time.
     */
    public abstract BigInteger getLowerBound();

    /**
     * Checks if this integer type is signed or not.
     *
     * @return true if this integer type is signed.
     */
    public abstract boolean isSigned();
}
