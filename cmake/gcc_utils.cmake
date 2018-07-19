# A function to set pthread library correctly.
function(gcc_set_pthread)
    if (UNIX AND CMAKE_COMPILER_IS_GNUCXX)
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -pthread" PARENT_SCOPE)
    endif ()
endfunction()

# Prepares warnings setup for current target
function(gcc_get_warnings_setup VARNAME)
    set(WARNINGS_SETUP "-Wall -Wextra -pedantic -Wno-long-long")
    set(TESTED_VERSIONS "4.4.3" "4.5.4" "5.4.0")
    list(FIND TESTED_VERSIONS "${CMAKE_CXX_COMPILER_VERSION}" TESTED_VERSION_INDEX)
    if (NOT (TESTED_VERSION_INDEX EQUAL -1))
        set(WARNINGS_SETUP "${WARNINGS_SETUP} -Werror")
        if (CMAKE_CXX_COMPILER_VERSION VERSION_EQUAL "4.4.3")
            set(WARNINGS_SETUP "${WARNINGS_SETUP} -Wno-error=strict-aliasing")
        endif ()
    endif ()
    set(${VARNAME} "${WARNINGS_SETUP}" PARENT_SCOPE)
endfunction()

# A function to set warnings.
function(gcc_set_warnings)
    if (CMAKE_COMPILER_IS_GNUCXX)
        gcc_get_warnings_setup(WARNINGS_SETUP)
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${WARNINGS_SETUP}" PARENT_SCOPE)
    endif ()
endfunction()

# A function to reset warnings.
function(gcc_reset_warnings)
    if (CMAKE_COMPILER_IS_GNUCXX)
        gcc_get_warnings_setup(WARNINGS_SETUP)
        string(REGEX REPLACE " ${WARNINGS_SETUP}" "" NEW_CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
        set(CMAKE_CXX_FLAGS "${NEW_CMAKE_CXX_FLAGS}" PARENT_SCOPE)
    endif ()
endfunction()

# A function to set static c libraries.
function(gcc_set_static_clibs)
    if (CMAKE_COMPILER_IS_GNUCXX)
        set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -static-libgcc -static-libstdc++" PARENT_SCOPE)
    endif ()
endfunction()
