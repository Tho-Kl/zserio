<#include "FileHeader.inc.ftl">
<#include "Inspector.inc.ftl">
<@file_header generatorDescription/>

<@include_guard_begin rootPackage.path, "InspectorZserioTypeNames"/>

#include <zserio/StringHolder.h>
<@system_includes headerSystemIncludes, false/>

<@namespace_begin rootPackage.path/>

/**
 * Contains all Zserio type names neccessary for Blob Inspector Tree.
 */
class InspectorZserioTypeNames
{
public:
<#list zserioTypeNames as zserioTypeName>
    static const zserio::StringHolder <@inspector_zserio_type_name zserioTypeName/>;
</#list>
};

<@namespace_end rootPackage.path/>

<@include_guard_end rootPackage.path, "InspectorZserioTypeNames"/>
