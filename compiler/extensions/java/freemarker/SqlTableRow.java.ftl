<#include "FileHeader.inc.ftl">
<#include "GeneratePkgPrefix.inc.ftl">
<@standard_header generatorDescription, packageName, javaMajorVersion, []/>

<@class_header generatorDescription/>
public class ${name}
{
    <#list fields as field>
    public ${field.javaTypeName} get${field.name?cap_first}()
    {
        return ${field.name};
    }

    public void set${field.name?cap_first}(${field.javaTypeName} ${field.name})
    {
        <#if field.javaNullableTypeName != field.javaTypeName>
        is${field.name?cap_first}Null = false;
        </#if>
        this.${field.name} = ${field.name};
    }

    public void setNull${field.name?cap_first}()
    {
        <#if field.javaNullableTypeName != field.javaTypeName>
        is${field.name?cap_first}Null = true;
        ${field.name} = <#if field.isBool>false<#else>(${field.javaTypeName})0</#if>;
        <#else>
        ${field.name} = null;
        </#if>
    }

    public boolean isNull${field.name?cap_first}()
    {
        <#if field.javaNullableTypeName != field.javaTypeName>
        return is${field.name?cap_first}Null;
        <#else>
        return ${field.name} == null;
        </#if>
    }

    </#list>
    <#list fields as field>
        <#if field.javaNullableTypeName != field.javaTypeName>
    private boolean is${field.name?cap_first}Null = true;
        </#if>
    private ${field.javaTypeName} ${field.name};
    </#list>
}
