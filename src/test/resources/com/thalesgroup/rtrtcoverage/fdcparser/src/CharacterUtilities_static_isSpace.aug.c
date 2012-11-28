
#define ATL_C_INSTRUMENTOR

#define USE_ATC 1

#define ATC_INFORMATION 0

#define USE_ATT 1

#define USE_ATP 1
#define _ATP_PARSE_GLOBALS_NEEDED 0

#include "../TEST/cpcgnu\lib\TP.h"

#define _ATC_INIT_CONST_COMPOUND_STUFF

_ATC_DECLARE_PROC(1,1)
_ATC_DECLARE_BLOC(1,3)
_ATC_DECLARE_COND(1,7)
_ATC_DECLARE(1,0x10720C04UL,0x11082755UL,1,_ATC_TAB_PROC(1),0,0,3,_ATC_TAB_BLOC(1),7,_ATC_TAB_COND(1))

_ATL_STACK_DECLARE(1,0x10720C04UL,0x6D123820UL)

#define _ATP_LINK_PARSE_GLOBALS

#line 1 "../CODE/PBL/pds/Class_CharacterUtilities/Source/CharacterUtilities_static_isSpace.c"
#line 1 "<built-in>"
#line 1 "<command line>"
#line 1 "../CODE/PBL/pds/Class_CharacterUtilities/Source/CharacterUtilities_static_isSpace.c"
#line 16 "../CODE/PBL/pds/Class_CharacterUtilities/Source/CharacterUtilities_static_isSpace.c"
#line 1 "../CODE/PBL/pds/includes/CharacterUtilities.h"
#line 26 "../CODE/PBL/pds/includes/CharacterUtilities.h"
#line 1 "../CODE/PBL/pds/includes/pds_basic_types.h"
#line 27 "../CODE/PBL/pds/includes/pds_basic_types.h"
typedef unsigned T_bitfield;

typedef signed T_signed_bitfield;

typedef unsigned char T_boolean;






typedef char T_char;




typedef signed char T_int8;
typedef short T_int16;
typedef long T_int32;





typedef unsigned long long T_uint64;




typedef unsigned char T_uint8;
typedef unsigned short T_uint16;
typedef unsigned long T_uint32;


typedef long long T_int64;




typedef float T_float32;
typedef double T_float64;

typedef void *T_ptr;
typedef unsigned long T_addr;
#line 27 "../CODE/PBL/pds/includes/CharacterUtilities.h"


typedef struct CharacterUtilitiesClass
{

        T_char (*isDigit)(T_char pSource);
        T_char (*isSpace)(T_char pSource);

} CharacterUtilitiesClass;

extern CharacterUtilitiesClass *CCharacterUtilities;
#line 17 "../CODE/PBL/pds/Class_CharacterUtilities/Source/CharacterUtilities_static_isSpace.c"
#line 32 "../CODE/PBL/pds/Class_CharacterUtilities/Source/CharacterUtilities_static_isSpace.c"
T_char CharacterUtilities_static_isSpace(T_char pSource)
{_ATC_LINK(1);{_ATL_STACK_IN(1,1)_ATP_IN(1)_ATT_IN(1){_ATC_PROC(1,0);{_ATC_BLOC(1,2);{

    T_char lReturnValue;

    {_ATC_SWITCH;switch (pSource)
    {
        case 0x09 :{_ATC_COND_CASE(1,0);
        case 0x0A :{_ATC_COND_CASE(1,1);
        case 0x0B :{_ATC_COND_CASE(1,2);
        case 0x0C :{_ATC_COND_CASE(1,3);
        case 0x0D :{_ATC_COND_CASE(1,4);
        case 0x20 :{_ATC_COND_CASE(1,5);{_ATC_BLOC(1,0);
            lReturnValue = pSource;}}}}}}}
            break;

        default :{_ATC_COND_CASE(1,6);{_ATC_BLOC(1,1);
            lReturnValue = 0L;}}
            break;
    }}

    { _ATP_OUT _ATT_OUT _ATL_STACK_OUT return lReturnValue;}

}}}}}
