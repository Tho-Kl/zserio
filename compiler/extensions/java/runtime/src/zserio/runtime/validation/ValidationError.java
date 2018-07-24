package zserio.runtime.validation;

import java.util.Arrays;
import java.util.List;

/**
 * Describes validation error which is reported from validation code generated by Zserio.
 */
public class ValidationError
{
    /**
     * Defines type of validation error.
     */
    public enum Type
    {
        /**
         * The column type in SQL table is different from definition in zserio.
         */
        INVALID_COLUMN_TYPE,

        /**
         * The column constraint in SQL table is different from definition in zserio.
         */
        INVALID_COLUMN_CONSTRAINT,

        /**
         * The column is defined in zserio but it is not in SQL table.
         */
        COLUMN_MISSING,

        /**
         * The column is not defined in zserio but it is in SQL table.
         */
        COLUMN_SUPERFLUOUS,

        /**
         * Value in SQL table is out of range according to the definition in zserio.
         */
        VALUE_OUT_OF_RANGE,

        /**
         * Enumeration value in SQL table is invalid according to the definition in zserio.
         */
        INVALID_ENUM_VALUE,

        /**
         * Parsing of read blob from SQL table failed.
         */
        BLOB_PARSE_FAILED,

        /**
         * Comparing of read blob from SQL table to parsed blob written in bit stream failed.
         */
        BLOB_COMPARE_FAILED
    };

    /**
     * Constructs a new validation error without primary key values.
     *
     * @param tableName Table name where validation error occured.
     * @param fieldName Name of table column where validation error occured.
     * @param type      Validation error type.
     * @param message   Validation error message.
     */
    public ValidationError(String tableName, String fieldName, Type type, String message)
    {
        this(tableName, fieldName, null, type, message);
    }

    /**
     * Constructs a new validation error with primary key values and with error message.
     *
     * @param tableName    Table name where validation error occured.
     * @param fieldName    Name of table column where validation error occured.
     * @param rowKeyValues List of primary key values to identify row where validation error occured.
     * @param type         Validation error type.
     * @param message      Validation error message.
     */
    public ValidationError(String tableName, String fieldName, List<String> rowKeyValues, Type type,
            String message)
    {
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.rowKeyValues = rowKeyValues;
        this.type = type;
        this.message = message;
        this.stackTrace = Thread.currentThread().getStackTrace();
    }

    /**
     * Constructs a new validation error with primary key values and with exception.
     *
     * @param tableName    Table name where validation error occured.
     * @param fieldName    Name of table column where validation error occured.
     * @param rowKeyValues List of primary key values to identify row where validation error occured.
     * @param type         Validation error type.
     * @param exception    Exception which occured during validation.
     */
    public ValidationError(String tableName, String fieldName, List<String> rowKeyValues, Type type,
            Throwable exception)
    {
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.rowKeyValues = rowKeyValues;
        this.type = type;
        this.message = exception.getMessage();
        this.stackTrace = exception.getStackTrace();
    }

    /**
     * Gets the table name where this validation error occured.
     *
     * @return Name of table where this validation error occured.
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * Gets the table column name where this validation error occured.
     *
     * @return Name of table column where this validation error occured.
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * Gets the primary key values which identify table row where this validation error occured.
     *
     * @return List of primary key values which identify table row or null if validation error is connected to
     * column.
     */
    public List<String> getRowKeyValues()
    {
        return rowKeyValues;
    }

    /**
     * Gets the validation error type.
     *
     * @return Type of this validation error.
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Gets the validation error message.
     *
     * @return Message of this validation error.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Gets the stack trace.
     *
     * @return Stack trace for this validation error.
     */
    public StackTraceElement[] getStackTrace()
    {
        return Arrays.copyOf(stackTrace, stackTrace.length);
    }

    private final String                tableName;
    private final String                fieldName;
    private final List<String>          rowKeyValues;
    private final Type                  type;
    private final String                message;
    private final StackTraceElement[]   stackTrace;
}