package zserio.runtime.io;

import java.io.IOException;

import zserio.runtime.ZserioError;

/**
 * Interface for writing to bit stream for classes generated by Zserio.
 */
public interface Writer
{
    /**
     * Writes this objects to the given bit stream.
     *
     * @param out Bit stream writer to use.
     *
     * @throws IOException     Throws if the writing failed.
     * @throws ZserioError Throws if some Zserio error occured.
     */
    public void write(zserio.runtime.io.BitStreamWriter out) throws IOException, ZserioError;
}