package syntax_error;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import test_utils.ZserioErrors;

public class SyntaxErrorTest
{
    @BeforeClass
    public static void readZserioErrors() throws IOException
    {
        zserioErrors = new ZserioErrors();
    }

    @Test
    public void unexpectedEofInArrayLength()
    {
        final String error = "unexpected_eof_in_array_length_error.zs:6:1: Unexpected end of file: unexpected token: null";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void unexpectedEofInConstDefinition()
    {
        final String error = "unexpected_eof_in_const_definition_error.zs:4:1: Unexpected end of file: unexpected token: null";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void unexpectedEofInFieldDefinition()
    {
        final String error = "unexpected_eof_in_field_definition_error.zs:6:1: Unexpected end of file: expecting ID, found 'null'";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void unexpectedEofInParameterizedFieldDefinition()
    {
        final String error = "unexpected_eof_in_parameterized_field_definition_error.zs:11:1: Unexpected end of file: unexpected token: null";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void unexpectedEofInStructDefinition()
    {
        final String error = "unexpected_eof_in_struct_definition_error.zs:6:1: Unexpected end of file: expecting RCURLY, found 'null'";
        assertTrue(zserioErrors.isPresent(error));
    }

    @Test
    public void unexpectedEofMissingSemicolon()
    {
        final String error = "unexpected_eof_missing_semicolon_error.zs:7:1: Unexpected end of file: expecting SEMICOLON, found 'null'";
        assertTrue(zserioErrors.isPresent(error));
    }

    private static ZserioErrors zserioErrors;
}
