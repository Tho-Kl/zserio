package zserio.ast4;

import java.math.BigInteger;

import org.antlr.v4.runtime.Token;

/**
 * AST node for built-in signed bit field integer types.
 *
 * Signed bit field integer types (int:1, int<expr>, ...) are Zserio types as well.
 */
public class SignedBitFieldType extends BitFieldType
{
    public SignedBitFieldType(Token token)
    {
        super(token);
    }

    /*@Override
    public void callVisitor(ZserioTypeVisitor visitor)
    {
        visitor.visitSignedBitFieldType(this);
    }*/

    @Override
    public void walk(ZserioListener listener)
    {
        listener.enterSignedBitFieldType(this);
    }

    @Override
    public BigInteger getUpperBound()
    {
        final Integer bitSize = getBitSize();
        if (bitSize == null)
            return null;

        // (1 << (bitSize - 1)) - 1
        return BigInteger.ONE.shiftLeft(bitSize - 1).subtract(BigInteger.ONE);
    }

    @Override
    public BigInteger getLowerBound()
    {
        final Integer bitSize = getBitSize();
        if (bitSize == null)
            return null;

        // -(1 << (bitSize - 1))
        return BigInteger.ONE.shiftLeft(bitSize - 1).negate();
    }

    @Override
    public boolean isSigned()
    {
        return true;
    }

    @Override
    protected int getMaxBitFieldBits()
    {
        return MAX_SIGNED_BITFIELD_BITS;
    }

    private static final int MAX_SIGNED_BITFIELD_BITS = 64;
}
