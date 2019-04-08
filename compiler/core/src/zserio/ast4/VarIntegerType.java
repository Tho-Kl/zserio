package zserio.ast4;

import java.math.BigInteger;

import org.antlr.v4.runtime.Token;

import zserio.antlr.Zserio4Parser;

/**
 * AST node for built-in signed and unsigned variable integer types.
 *
 * Variable integer types (varint16, varuint16, ...) are Zserio types as well.
 */
public class VarIntegerType extends IntegerType
{
    public VarIntegerType(Token token)
    {
        super(token);

        switch (token.getType())
        {
        case Zserio4Parser.VARUINT16:
            isSigned = false;
            maxBitSize = 16;
            upperBound = BigInteger.ONE.shiftLeft(15).subtract(BigInteger.ONE);
            lowerBound = BigInteger.ZERO;
            break;
        case Zserio4Parser.VARINT16:
            isSigned = true;
            maxBitSize = 16;
            upperBound = BigInteger.ONE.shiftLeft(14).subtract(BigInteger.ONE);
            lowerBound = upperBound.negate();
            break;
        case Zserio4Parser.VARUINT32:
            isSigned = false;
            maxBitSize = 32;
            upperBound = BigInteger.ONE.shiftLeft(29).subtract(BigInteger.ONE);
            lowerBound = BigInteger.ZERO;
            break;
        case Zserio4Parser.VARINT32:
            isSigned = true;
            maxBitSize = 32;
            upperBound = BigInteger.ONE.shiftLeft(28).subtract(BigInteger.ONE);
            lowerBound = upperBound.negate();
            break;
        case Zserio4Parser.VARUINT64:
            isSigned = false;
            maxBitSize = 64;
            upperBound = BigInteger.ONE.shiftLeft(57).subtract(BigInteger.ONE);
            lowerBound = BigInteger.ZERO;
            break;
        case Zserio4Parser.VARINT64:
            isSigned = true;
            maxBitSize = 64;
            upperBound = BigInteger.ONE.shiftLeft(56).subtract(BigInteger.ONE);
            lowerBound = upperBound.negate();
            break;
        case Zserio4Parser.VARUINT:
            isSigned = false;
            maxBitSize = 72;
            upperBound = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
            lowerBound = BigInteger.ZERO;
            break;
        case Zserio4Parser.VARINT:
            isSigned = true;
            maxBitSize = 72;
            upperBound = BigInteger.valueOf(Long.MAX_VALUE);
            lowerBound = BigInteger.valueOf(Long.MIN_VALUE);
            break;
        default:
            throw new ParserException(token, "Unexpected AST node type in VarIntegerType!");
        }
    }

    /*@Override
    public void callVisitor(ZserioTypeVisitor visitor)
    {
        visitor.visitVarIntegerType(this);
    }*/

    @Override
    public void walk(ZserioListener listener)
    {
        listener.enterVarIntegerType(this);
    }

    @Override
    public BigInteger getUpperBound()
    {
        return upperBound;
    }

    @Override
    public BigInteger getLowerBound()
    {
        return lowerBound;
    }

    @Override
    public boolean isSigned()
    {
        return isSigned;
    }

    /**
     * Gets the maximum number of bits the variable integer can occupy.
     *
     * @return Maximum bit size of this variable integer.
     */
    public int getMaxBitSize()
    {
        return maxBitSize;
    }

    private final boolean isSigned;
    private final int maxBitSize;
    private final BigInteger upperBound;
    private final BigInteger lowerBound;
}
