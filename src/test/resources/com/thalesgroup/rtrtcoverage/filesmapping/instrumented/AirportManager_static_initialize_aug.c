/* IBM Rational Test RealTime C Instrumentor 7.0.0.1.031.001 */
/* Build Number: 031.001 */
/* Copyright(C) 2007 Rational Software Corporation. All rights reserved. */
/*   Date : 20-Feb-07 12:00 */
/*     OS : ms-dos */

#define ATL_C_INSTRUMENTOR

#define USE_ATC 1

#define ATC_INFORMATION 0

#include "TP.h"

#define _ATC_INIT_CONST_COMPOUND_STUFF

_ATC_DECLARE_PROC(1,12)
_ATC_DECLARE_BLOC(1,10)
_ATC_DECLARE(1,0x581F6379UL,0x359235CUL,12,_ATC_TAB_PROC(1),0,0,10,_ATC_TAB_BLOC(1),0,0)

#line 1 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
 
 
 
 
 
 
 
 
 
 
 
 
 
 

#line 1 "..\Includes\AirportManager.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 1 "..\Includes\pds_basic_types.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 



















typedef unsigned T_bitfield;

typedef signed T_signed_bitfield;

typedef unsigned char T_boolean;




typedef unsigned char T_char;

typedef signed char T_int8;
typedef short T_int16;
typedef long T_int32;
 



typedef	unsigned long long T_uint64;


typedef unsigned char T_uint8;
typedef unsigned short T_uint16;
typedef unsigned long T_uint32;
 



typedef	long long T_int64;


typedef float T_float32;
typedef double T_float64;

typedef void *T_ptr;
typedef unsigned long T_addr;




#line 96 "..\Includes\AirportManager.h"
#line 1 "..\Includes\AircraftLocalizationServices.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 82 "..\Includes\AircraftLocalizationServices.h"
#line 1 "..\Includes\shared_library.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\shared_library.h"



typedef enum
{
	E_RUNWAY_TYPE = 0,
	E_TAXIWAY_TYPE,
	E_STAND_TYPE,
	E_OTHER_TYPE,
	E_ALL_TYPE
} T_AirportElementType;

typedef enum {
	E_UNKNOWN_AIRPORT,
	E_ORIGIN_AIRPORT,
	E_DESTINATION_AIRPORT,
	E_ALTERNATE_AIRPORT,
	E_SELECTED_AIRPORT,
	E_GPS_AIRPORT,
	E_DEFAULT_AIRPORT
} T_AirportType;

typedef enum {
	E_ARC_MODE = 0,
	E_NAV_MODE = 1,
	E_PLAN_MODE = 2,
	E_UNKNOWN_MODE = 3
} T_Mode;

typedef T_char *T_PtrChar;

typedef enum {
	E_METER_UNIT = 0,
	E_FEET_UNIT = 1
} T_UnitRange;







typedef T_boolean T_DeclutteringTable[ 0x05 ];


typedef struct
{
	T_uint32		startDate;
	T_uint32		endDate;
} T_DatabaseDate;
typedef enum {
	E_NOT_COMPUTED_AIRPORT_PHASE = 0,
	E_ORIGIN_AIRPORT_PHASE = 1,
	E_NO_AIRPORT_PHASE = 2,
	E_DESTINATION_AIRPORT_PHASE = 3
} T_AirportPhase;
typedef struct T_buffer
{
    T_uint32 maxSize;
    T_uint32 size;
    T_uint8 *buffer;
} T_buffer;




typedef enum {
	E_HIGHLIGHT_NULL = 0,
	E_HIGHLIGHT_THRESHOLD = 1,
	E_HIGHLIGHT_EXIT = 2,
	E_HIGHLIGHT_END = 3
} T_HighlightElementType;

typedef enum {
	E_ND  = 0,
	E_NO  = 1,
	E_FT  = 2,
	E_NCD = 3,
	E_FW  = 4,
	E_NR  = 5
} T_FunctionalStatus;
typedef enum {
	E_ARPTNAV_ST = 1,
	E_BTVWOES_ST = 2,
	E_BTVWUE_ST  = 3,
	E_BTVWSEL_ST = 4,
	E_BTVARM_ST  = 5,
	E_BTVACT_ST  = 6,
	E_EXTOVT_ST  = 7,
	E_RWYOVT_ST  = 8,
	E_RWYINVALID_ST    = 9,
	E_ROPARM_ST  = 10,
	E_ROPACT_ST  = 11,
	E_ROPRWYOVT_ST     = 12,
	E_ROPRWYINVALID_ST = 13
} T_BTVStates;
typedef enum
{
	E_RUNWAY_END = 0,
	E_RIGHT = 1,
	E_LEFT = 2
}T_BtvRwyExitSide;
typedef struct T_ReferenceStruct
{
	T_float32 RefPosX;
	T_float32 RefPosY;
	T_float32 RefBearing;
} T_ReferenceStruct;
typedef struct T_ThresholdInfoStruct
{
	T_uint32          IdNumber;
	T_char            Name[ 4 ];
	T_float32         SurfaceType;
	T_float32         Length;
	T_float32         Width;
	T_float32         Altitude;
	T_float32         MagHeading;
	T_ReferenceStruct Reference;
} T_ThresholdInfoStruct;
typedef struct T_ExitInfoStruct
{
	T_uint32          IdNumber;
	T_char            Name[ 4 ];
	T_float32         LateralDistance;
	T_float32         LongitudinalDistance;
	T_float32         DistanceToVacate;
	T_float32         PositionX;
	T_float32         PositionY;
} T_ExitInfoStruct;
typedef struct T_RunwayShiftInfo
{
	 
	 
	T_int16   airportId;
	 
	 
	T_uint32  runwayThresholdId;
	 
	 
	 
	T_boolean isShiftEditionEnabled;
	 
	 
	T_float32 startShift;
	 
	 
	T_float32 endShift;
}T_RunwayShiftInfo;

typedef enum {
	E_DUMP_BITE_MESSAGE_CMD_IN_PROGRESS,
	E_DUMP_BITE_MESSAGE_CMD_COMPLETE,
	E_DUMP_BITE_MESSAGE_CMD_ERROR
} T_DumpBiteMessageCmd;



typedef struct T_DumpBiteMessage
{
	T_DumpBiteMessageCmd dumpBiteMessageCmd;
	T_char dumpBiteMessageText[ 32 ];
} T_DumpBiteMessage;



typedef struct T_SISMessage
{
	T_int16		indexSISArea;
	T_boolean	status;
	T_char		Description[ 20 ];
	T_char		Value[ 16 ];
} T_SISMessage;



typedef struct
{
	T_float32		x;
	T_float32		y;
} T_PointGO;
typedef enum {
	E_IATA = 0,
	E_ICAO = 1,
	E_NAME = 2
} T_SearchType;


typedef enum
{
	 
	E_SERVICE_ENABLED,
	 
	E_SERVICE_VIEW_ONLY,
	 
	E_SERVICE_DISABLED
} T_runwayShiftMode;


typedef enum {
	E_AIRPORT_EMPTY = 0,
	E_AIRPORT_LOAD_IN_PROGRESS,
	E_AIRPORT_DIRTY,
	E_AIRPORT_CREATED,
	E_AIRPORT_FAILED
} T_AirportState;

 

 

 

typedef enum
{
	E_BTV_INACTIVE_MODE,
	E_BTV_CONFIGURATION_MODE,
	E_BTV_MONITORING_MODE
} T_btvMode;
typedef enum
{
	E_SERVICE_ENABLE,
	E_SERVICE_DELETE_ONLY,
	E_SERVICE_DISABLE,
} T_annotationMode;
typedef enum {
	E_RANGE_02_NM,
	E_RANGE_05_NM,
	E_RANGE_1_NM,
	E_RANGE_2_NM,
	E_RANGE_5_NM,
	E_RANGE_OTHER
} T_Range;
typedef enum
{
	E_STYLE_ACTION_ADD,
	E_STYLE_ACTION_SUPPRESS
} T_StyleAction;
typedef enum {
	E_EQUIPMENT_FO = 0,
	E_EQUIPMENT_CA = 1
} T_SideEquipment;
typedef enum {
	E_NO_AIRPORT,
	E_BTV_AIRPORT,
	E_FMS_AIRPORT
} T_AutoBTVAirport;
typedef enum {
	E_ANNOTATION_NO_ACTION,
	E_ANNOTATION_ADD,
	E_ANNOTATION_DELETE
} T_ActionMapData;

typedef struct T_FileInfo
{
	T_char name[ 256 ];
	T_addr addr;
	T_uint32 size;
	T_boolean isLoaded;
} T_FileInfo;
typedef enum
{
	E_BRAKE_TO_VACATE_FUNCTION,
	E_ANNOTATION_FUNCTION,
	E_RUNWAYSHIFT_FUNCTION,
	E_DATABASE_FUNCTION
} T_ComponentsToSynchronize;



 








#line 83 "..\Includes\AircraftLocalizationServices.h"

#line 1 "..\Includes\MapObjectServices.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 1 "..\Includes\PointerArray.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 34 "..\Includes\PointerArray.h"

typedef struct PointerArray
{	 
	T_boolean (*add)(struct PointerArray *pThis, T_ptr pItemPointer);
	T_ptr (*get)(struct PointerArray *pThis, T_uint32 pIndex);
	T_uint32 (*getCount)(struct PointerArray *pThis);
	T_uint32 (*getCapacity)(struct PointerArray *pThis);
	void (*reset)(struct PointerArray *pThis);
	T_ptr (*removeAt)(struct PointerArray *pThis, T_uint32 pIndex);
	T_ptr (*setAt)(struct PointerArray *pThis, T_ptr pItemPointer, T_uint32 pIndex);
	 
	T_ptr *_array;
	T_uint32 _count;
	T_uint32 _capacity;
} PointerArray;

typedef struct PointerArrayClass 
{
	 
	void (*initialize)(PointerArray *pInstance, T_ptr *pArray, T_uint32 pCapacity);
	 
} PointerArrayClass;

extern PointerArrayClass *CPointerArray;


#line 53 "..\Includes\MapObjectServices.h"
#line 54 "..\Includes\MapObjectServices.h"
#line 55 "..\Includes\MapObjectServices.h"

typedef struct MapObjectServices
{
	 
	PointerArray *(*getList)(struct MapObjectServices *pThis, struct MapObjectServices *pMapObject, T_int32 pData);
	T_PtrChar (*getCharData)(struct MapObjectServices *pThis, struct MapObjectServices *pMapObject, T_int32 pData);
	T_boolean (*getBooleanData)(struct MapObjectServices *pThis, struct MapObjectServices *pMapObject, T_int32 pData);
	T_float32 (*getFloatData)(struct MapObjectServices *pThis, struct MapObjectServices *pMapObject, T_int32 pData);
	T_int32 (*getInt32Data)(struct MapObjectServices *pThis, struct MapObjectServices *pMapObject, T_int32 pData);
	T_int16 (*getInt16Data)(struct MapObjectServices *pThis, struct MapObjectServices *pMapObject, T_int32 pData);
	struct MapObjectServices *(*getMapObject)(struct MapObjectServices *pThis, struct MapObjectServices *pMapObject, T_int32 pData);
	T_ptr (*getGenericPtr)(struct MapObjectServices *pThis, struct MapObjectServices *pMapObject, T_int32 pData);
	void (*setDisplayObjectInstance)(struct MapObjectServices *pThis, struct MapObjectServices *pMapObject, T_ptr pData);
	 
	void *_impl;
} MapObjectServices;


#line 85 "..\Includes\AircraftLocalizationServices.h"

 
 


 
typedef enum T_FlightPhase
{
	E_GND_PREFLIGHT = 1,
	E_GND_TAXI_OUT,
	E_GND_ACCELERATION,
	E_GND_TAKE_OFF,
	E_FLT_CLIMB,
	E_FLT_CRUISE,
	E_FLT_APPROACH,
	E_GND_LANDING,
	E_GND_BRAKING,
	E_GND_TAXI_IN,
	E_GND_ENGSTOPPED
}T_FlightPhase;

typedef enum T_OverrunElementType
{
	E_OVERRUN_DATA_NOT_AVAILABLE = 0,
	E_OVERRUN_PARKING_STAND_AREA,
	E_OVERRUN_TAXIWAY,
	E_OVERRUN_RUNWAY,
	E_OVERRUN_DISPLACED_AREA,
	E_OVERRUN_STOPWAY,
	E_OVERRUN_DEICING_AREA,
	E_OVERRUN_RUNWAY_INTERSECTION
} T_OverrunElementType;


typedef struct T_OverrunElement
{
	T_OverrunElementType type;
	T_char name[ 51 ];
	MapObjectServices* mapObjectservices;
} T_OverrunElement;

 
 
 


typedef struct AircraftLocalizationServices
{
	 
	T_float32 (*getAircraftLatitude)(struct AircraftLocalizationServices *pThis);
	T_float32 (*getAircraftLongitude)(struct AircraftLocalizationServices *pThis);
	T_boolean (*getAircraftOnGround)(struct AircraftLocalizationServices *pThis);
	T_int16 (*getNearestAirport)(struct AircraftLocalizationServices *pThis);
	T_boolean (*getAircraftLocationValidity)(struct AircraftLocalizationServices *pThis);
	T_boolean (*getFlightPhaseValidity)(struct AircraftLocalizationServices *pThis);
	T_FlightPhase (*getFlightPhase)(struct AircraftLocalizationServices *pThis);
	T_float32 (*getAircraftAltitude)(struct AircraftLocalizationServices *pThis);
	T_float32 (*getTrueHeading)(struct AircraftLocalizationServices *pThis);
	T_boolean (*getTrueHeadingValidity)(struct AircraftLocalizationServices *pThis);
	T_float32 (*getMagHeading)(struct AircraftLocalizationServices *pThis);
	T_boolean (*getMagHeadingValidity)(struct AircraftLocalizationServices *pThis);
	T_float32 (*getGroundSpeed)(struct AircraftLocalizationServices *pThis);
	T_boolean (*getGroundSpeedValidity)(struct AircraftLocalizationServices *pThis);
	T_float32 (*getTurnRate)(struct AircraftLocalizationServices *pThis);
	T_boolean (*getTurnRateValidity)(struct AircraftLocalizationServices *pThis);
	T_float32 (*getDistXFromARPInMeter)(struct AircraftLocalizationServices *pThis);
	T_float32 (*getDistYFromARPInMeter)(struct AircraftLocalizationServices *pThis);
	T_float32 (*getDistACToNose)(struct AircraftLocalizationServices *pThis);
	T_int16 (*getAirportInAircraftCone)(struct AircraftLocalizationServices *pThis);
	T_boolean (*isThrustLeverPositionAtOrAboveClimb)(struct AircraftLocalizationServices *pThis);
	void (*getOverrunElement)(struct AircraftLocalizationServices *pThis, T_OverrunElement*  pOverrunElement);
	T_uint32 (*getFlightCount)(struct AircraftLocalizationServices *pThis);
	T_boolean (*getAircraftIsInAirportBoundingBox)(struct AircraftLocalizationServices *pThis, T_int16 pAirportId);
	T_int16 (*getOverrunAirport)(struct AircraftLocalizationServices *pThis);
	T_PtrChar (*getActiveQfu)(struct AircraftLocalizationServices *pThis);
	 
	void *_impl;
} AircraftLocalizationServices;


#line 97 "..\Includes\AirportManager.h"
#line 98 "..\Includes\AirportManager.h"
#line 1 "..\Includes\AirportSelectionEvent.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 30 "..\Includes\AirportSelectionEvent.h"

typedef struct AirportSelectionEvent
{
	 
	void (*airportSelected)(struct AirportSelectionEvent *pThis, T_int16 pAirportRequest);
	 
	void *_impl;
} AirportSelectionEvent;


#line 99 "..\Includes\AirportManager.h"
#line 1 "..\Includes\Task.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 31 "..\Includes\Task.h"

 


typedef T_char T_TaskName[ 31 +1];

typedef struct Task
{
	 
	void (*execute)(struct Task *pThis);
	T_char *(*getName)(struct Task *pThis);
	 
	void *_impl;
} Task;


#line 100 "..\Includes\AirportManager.h"



 














typedef enum
{
	E_NO_AIRPORT_TO_DISPLAY,
	E_AIRPORT_TO_DISPLAY_IN_NAV,
	E_AIRPORT_TO_DISPLAY_IN_PLAN
}T_airportToDisplay;

typedef struct AirportManager
{	 
	void (*_updateAirportToBeDisplayedInPlan)(struct AirportManager *pThis);
	void (*_updateAirportPriorityList)(struct AirportManager *pThis);
	void (*_updateAirportToBeDisplayedInArcNav)(struct AirportManager *pThis);
	void (*_updateFMSAirports)(struct AirportManager *pThis);
	void (*_updateAircraftSituation)(struct AirportManager *pThis);
	void (*_updateBTVAirport)(struct AirportManager *pThis);
	void (*airportSelected)(struct AirportManager *pThis, T_int16 pAirportRequest);
	void (*execute)(struct AirportManager *pThis);
	T_char *(*getName)(struct AirportManager *pThis);
	void (*_updateAirportToDisplay)(struct AirportManager *pThis);
	void (*_updateFlightTest)(struct AirportManager *pThis);
	void (*_updateMapManagementData)(struct AirportManager *pThis);
	void (*_updateMode)(struct AirportManager *pThis);
	void (*_updateAirportServiceActivation)(struct AirportManager *pThis);
	void (*_checkDatabaseSwap)(struct AirportManager *pThis);
	 
	AirportSelectionEvent *(*getAirportSelectionEventInstance)(struct AirportManager *pThis);
	Task *(*getTaskInstance)(struct AirportManager *pThis);
	 
	AirportSelectionEvent _airportSelectionEventImpl;
	Task _taskImpl;
	 
	T_int16 _alternateAirport;
	T_int16 _BTVAirport;
	T_int16 _nearestAirport;
	T_int16 _destinationAirport;
	T_int16 _originAirport;
	T_int16 _requestedAirportForDisplay;
	T_int16 _airportToBeDisplayedInPlan;
	T_int16 _memorizedAirportInArcNav;
	T_int16 _defaultAirport;
	T_FlightPhase _flightPhase;
	T_int16 _airportToBeDisplayedInArcNav;
	T_int16 _destinationAirportOfLastFlight;
	T_float32 _aircraftLatitude;
	T_float32 _aircraftLongitude;
	T_boolean _BTVAirportChanged;
	T_boolean _onGroundCondition;
	T_int16 _airportPriorityList[ 4 ];
	T_Mode _mode;
	T_boolean _flightPhaseValidity;
	T_boolean _aircraftLocationValidity;
	T_int16 _lastDisplayedAirportInPlan;
	T_float32 _aircraftAltitude;
	T_boolean _transitionPerformed;
	T_boolean _firstEntryPlanModeAfterTransition;
	T_boolean _firstDisplayModePlanAfterTransition;
	T_boolean _airportDisplayedInPlanAfterTransition;
	T_char _airportNameToBeDisplayedInPlan[ 5 ];
	T_airportToDisplay _airportToBeDisplayedState;
	T_Mode _previousMode;
	T_BTVStates _btvStateConsolidated;
	T_boolean _databaseSwapState;
} AirportManager;

typedef struct AirportManagerClass 
{
	 
	AirportManager *(*getInstance)(void);
	void (*initialize)(AirportManager *pInstance);
	 
	AirportManager _instance;
} AirportManagerClass;

extern AirportManagerClass *CAirportManager;


#line 17 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"




 
#line 1 "..\Includes\THEMIS_BASIC_TYPES.h"
 
#line 40 "..\Includes\THEMIS_BASIC_TYPES.h"







 
#line 1 "..\Includes\COD_CMTY_CS_COMMON.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




 
typedef unsigned char					T_CHAR;
typedef unsigned char				T_UCHAR;

 
typedef enum
{
    K_FALSE = 0,
    K_TRUE  = 1 
} T_BOOL;

 
typedef unsigned char		T_INT8;
typedef short					T_INT16;
typedef long					T_INT32;
typedef long long				T_INT64;

 
typedef unsigned char				T_UINT8;
typedef unsigned short				T_UINT16;
typedef unsigned long				T_UINT32;
typedef unsigned long long			T_UINT64;

 
typedef float					T_FLOAT32;
typedef double					T_FLOAT64;

 
typedef void *					T_PTR;
typedef unsigned long				T_ADDR;

 



 









 # 49 "..\Includes\THEMIS_BASIC_TYPES.h" 1


 














 
typedef float T_FLOAT;

 
typedef double T_FLOAT_L;

 
typedef long T_INT;

 
typedef short T_INT_S;

 
typedef unsigned long T_UINT;

 
typedef unsigned short T_UINT_S;


 
 

 
typedef T_FLOAT T_POINT[2];

 
 

 


typedef struct {
  T_INT length;
  T_CHAR data[ 32 ];
} T_STRING;

 
 
 



 
 
 

typedef struct
{
   T_CHAR    port_name[32];
   T_UINT    port_mode;      
   T_UINT 	 refresh;
   T_UINT  	 size_max ;
   T_UINT    nb_msg; 		 
} T_REF_MSG ;

 
 






#line 23 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManagerComponent.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 34 "..\Includes\AirportManagerComponent.h"
#line 1 "..\Includes\ComponentHelper.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 39 "..\Includes\ComponentHelper.h"
#line 1 "..\Includes\Component.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 40 "..\Includes\Component.h"

 
typedef T_char **T_NamedPorts;


 
typedef T_ptr T_PortInterface;


typedef struct Component
{
	 
	T_boolean (*configurationCompleted)(struct Component *pThis);
	T_uint32 (*getNamedFacets)(struct Component *pThis, T_NamedPorts *pNameList);
	T_uint32 (*getNamedReceptacles)(struct Component *pThis, T_NamedPorts *pNameList);
	void (*connect)(struct Component *pThis, const T_char *pName, T_PortInterface pInterface);
	T_PortInterface (*provide)(struct Component *pThis, const T_char *pName);
	 
	void *_impl;
} Component;


#line 40 "..\Includes\ComponentHelper.h"

typedef struct ComponentHelper
{	 
	void (*newConnection)(struct ComponentHelper *pThis);
	T_int32 (*indexOfFacet)(struct ComponentHelper *pThis, const T_char *pName);
	T_int32 (*indexOfReceptacle)(struct ComponentHelper *pThis, const T_char *pName);
	T_boolean (*connectionTargetAchieved)(struct ComponentHelper *pThis);
	T_uint32 (*getNamedFacets)(struct ComponentHelper *pThis, T_NamedPorts *pNameList);
	T_uint32 (*getNamedReceptacles)(struct ComponentHelper *pThis, T_NamedPorts *pNameList);
	 
	T_uint32 _connectionTarget;
	T_NamedPorts _facetList;
	T_NamedPorts _receptacleList;
	T_uint32 _facetCount;
	T_uint32 _receptacleCount;
	T_uint32 _connectionCount;
} ComponentHelper;

typedef struct ComponentHelperClass 
{
	 
	void (*initialize)(ComponentHelper *pInstance, T_NamedPorts pFacetList, T_uint32 pFacetCount, T_NamedPorts pReceptacleList, T_uint32 pReceptacleCount, T_uint32 pConnectionTarget);
	 
} ComponentHelperClass;

extern ComponentHelperClass *CComponentHelper;


#line 35 "..\Includes\AirportManagerComponent.h"
#line 36 "..\Includes\AirportManagerComponent.h"

#line 38 "..\Includes\AirportManagerComponent.h"
#line 1 "..\Includes\DatabaseServices.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 44 "..\Includes\DatabaseServices.h"
#line 45 "..\Includes\DatabaseServices.h"
#line 46 "..\Includes\DatabaseServices.h"

typedef struct DatabaseServices
{
	 
	T_ptr (*getGenericPtr)(struct DatabaseServices *pThis, T_int32 pData);
	T_float32 (*getFloatData)(struct DatabaseServices *pThis, T_int32 pData);
	T_boolean (*getBooleanData)(struct DatabaseServices *pThis, T_int32 pData);
	T_PtrChar (*getCharData)(struct DatabaseServices *pThis, T_int32 pData);
	T_int16 (*getInt16Data)(struct DatabaseServices *pThis, T_int32 pData);
	T_int16 *(*getListAirport)(struct DatabaseServices *pThis, T_int32 pData);
	T_int32 (*getInt32Data)(struct DatabaseServices *pThis, T_int32 pData);
	T_int16 (*searchAirportByChar)(struct DatabaseServices *pThis, T_SearchType pSearchType, T_PtrChar pChar);
	MapObjectServices *(*getAirportInstance)(struct DatabaseServices *pThis, T_int16 pAirport);
	void (*setAirportState)(struct DatabaseServices *pThis, T_int16 pAirport, T_AirportState pState);
	 
	void *_impl;
} DatabaseServices;


#line 39 "..\Includes\AirportManagerComponent.h"
#line 40 "..\Includes\AirportManagerComponent.h"
#line 1 "..\Includes\FlightPlanManagementServices.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 51 "..\Includes\FlightPlanManagementServices.h"
#line 52 "..\Includes\FlightPlanManagementServices.h"
#line 53 "..\Includes\FlightPlanManagementServices.h"

typedef struct FlightPlanManagementServices
{
	 
	T_int16 (*getOriginAirport)(struct FlightPlanManagementServices *pThis);
	T_int16 (*getDestinationAirport)(struct FlightPlanManagementServices *pThis);
	T_int16 (*getAlternateAirport)(struct FlightPlanManagementServices *pThis);
	T_int16 (*getDestinationAirportOfLastFlight)(struct FlightPlanManagementServices *pThis);
	T_int32 (*getDestinationRunway)(struct FlightPlanManagementServices *pThis);
	T_PtrChar (*getOriginRunwayName)(struct FlightPlanManagementServices *pThis);
	T_PtrChar (*getDestinationRunwayName)(struct FlightPlanManagementServices *pThis);
	T_int32 (*getOriginRunway)(struct FlightPlanManagementServices *pThis);
	T_PtrChar (*getDestinationAirportICAO)(struct FlightPlanManagementServices *pThis);
	T_PtrChar (*getOriginAirportICAO)(struct FlightPlanManagementServices *pThis);
	MapObjectServices *(*getOriginRunwayMos)(struct FlightPlanManagementServices *pThis);
	MapObjectServices *(*getDestinationRunwayMos)(struct FlightPlanManagementServices *pThis);
	 
	void *_impl;
} FlightPlanManagementServices;


#line 41 "..\Includes\AirportManagerComponent.h"
#line 1 "..\Includes\ModeManagerServices.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 31 "..\Includes\ModeManagerServices.h"

typedef struct ModeManagerServices
{
	 
	T_Mode (*getMode)(struct ModeManagerServices *pThis);
	T_Range (*getRange)(struct ModeManagerServices *pThis);
	 
	void *_impl;
} ModeManagerServices;


#line 42 "..\Includes\AirportManagerComponent.h"
#line 1 "..\Includes\BTVServices.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 43 "..\Includes\BTVServices.h"
#line 44 "..\Includes\BTVServices.h"
#line 45 "..\Includes\BTVServices.h"

typedef struct BTVServices
{
	 
	T_int16 (*getBTVAirport)(struct BTVServices *pThis);
	T_ThresholdInfoStruct *(*getBTVReferenceInfo)(struct BTVServices *pThis);
	T_boolean (*getBTVSPPStatus)(struct BTVServices *pThis);
	T_BTVStates (*getBTVState)(struct BTVServices *pThis);
	MapObjectServices *(*getBTVExit)(struct BTVServices *pThis);
	 
	void *_impl;
} BTVServices;


#line 43 "..\Includes\AirportManagerComponent.h"
#line 1 "..\Includes\ErrorThrower.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 37 "..\Includes\ErrorThrower.h"

 


typedef T_uint32 T_ErrorType;

typedef struct
{     
	T_ErrorType type;
	T_char className[ 31 +1];
	T_char methodName[ 31 +1];
	T_uint32 line;
	T_char info1[ 31 +1];
	T_uint32 info2;
	T_uint32 info3;
} T_ErrorData;

typedef struct ErrorThrower
{
	 
	void (*raise)(struct ErrorThrower *pThis, const T_ErrorData *pErrorData);
	void (*reset)(struct ErrorThrower *pThis, T_ErrorData *pErrorData, T_ErrorType pErrorType);
	void (*setLocation)(struct ErrorThrower *pThis, T_ErrorData *pErrorData, const T_char *pClassName, const T_char *pMethodName, T_uint32 pLine);
	void (*setAdditionalInformations)(struct ErrorThrower *pThis, T_ErrorData *pErrorData, const T_char *pInfo1, T_uint32 pInfo2, T_uint32 pInfo3);
	 
	void *_impl;
} ErrorThrower;


#line 44 "..\Includes\AirportManagerComponent.h"
#line 1 "..\Includes\AirportLoadingEvent.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 





typedef struct AirportLoadingEvent
{
	 
	void (*airportPriorityListUpdated)(struct AirportLoadingEvent *pThis, T_int16*pAirportPriorityList);
	 
	void *_impl;
} AirportLoadingEvent;


#line 45 "..\Includes\AirportManagerComponent.h"
#line 1 "..\Includes\AirportRequestActivationEvent.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 28 "..\Includes\AirportRequestActivationEvent.h"

typedef struct AirportRequestActivationEvent
{
	 
	void (*airportRequestEnabled)(struct AirportRequestActivationEvent *pThis, T_boolean pEnabled);
	 
	void *_impl;
} AirportRequestActivationEvent;


#line 46 "..\Includes\AirportManagerComponent.h"
#line 1 "..\Includes\AirportToDisplayModificationEvent.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 30 "..\Includes\AirportToDisplayModificationEvent.h"

typedef struct AirportToDisplayModificationEvent
{
	 
	void (*airportToDisplayChanged)(struct AirportToDisplayModificationEvent *pThis, T_int16 pAirportToDisplay);
	 
	void *_impl;
} AirportToDisplayModificationEvent;


#line 47 "..\Includes\AirportManagerComponent.h"
#line 48 "..\Includes\AirportManagerComponent.h"
#line 49 "..\Includes\AirportManagerComponent.h"

 
typedef enum T_AirportManagerComponentReceptaclesIndexes
{
	E_AIRPORT_MANAGER_DATABASE_SERVICES_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_MAP_OBJECT_SERVICES_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_AIRCRAFT_LOCALIZATION_SERVICES_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_FLIGHT_PLAN_MANAGEMENT_SERVICES_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_MODE_MANAGER_SERVICES_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_BTV_SERVICES_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_ERROR_THROWER_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_AIRPORT_LOADING_EVENT_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_AIRPORT_REQUEST_ACTIVATION_EVENT_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_AIRPORT_TO_DISPLAY_MODIFICATION_EVENT_RECEPTACLE_INDEX,
	E_AIRPORT_MANAGER_COMPONENT_NUMBER_OF_RECEPTACLE_INDEXES
}T_AirportManagerComponentReceptaclesIndexes;

 
typedef enum T_AirportManagerComponentFacetsIndexes
{
	E_AIRPORT_MANAGER_TASK_FACET_INDEX,
	E_AIRPORT_MANAGER_AIRPORT_SELECTION_EVENT_FACET_INDEX,
	E_AIRPORT_MANAGER_COMPONENT_NUMBER_OF_FACET_INDEXES
}T_AirportManagerComponentFacetsIndexes;

 
typedef struct T_AirportManagerComponentReceptacles
{
	AircraftLocalizationServices *aircraftLocalizationServices;
	DatabaseServices *databaseServices;
	MapObjectServices *mapObjectServices;
	FlightPlanManagementServices *flightPlanManagementServices;
	ModeManagerServices *modeManagerServices;
	BTVServices *btvServices;
	ErrorThrower *errorThrower;
	AirportLoadingEvent *airportLoadingEventsArray[ 8 ];
	PointerArray airportLoadingEvents;
	AirportRequestActivationEvent *airportRequestActivationEventsArray[ 8 ];
	PointerArray airportRequestActivationEvents;
	AirportToDisplayModificationEvent *airportToDisplayModificationEventsArray[ 8 ];
	PointerArray airportToDisplayModificationEvents;
}T_AirportManagerComponentReceptacles;

 


 


typedef struct AirportManagerComponent
{	 
	void (*connect)(struct AirportManagerComponent *pThis, const T_char *pName, T_PortInterface pInterface);
	T_boolean (*configurationCompleted)(struct AirportManagerComponent *pThis);
	T_uint32 (*getNamedFacets)(struct AirportManagerComponent *pThis, T_NamedPorts *pNameList);
	T_uint32 (*getNamedReceptacles)(struct AirportManagerComponent *pThis, T_NamedPorts *pNameList);
	T_PortInterface (*provide)(struct AirportManagerComponent *pThis, const T_char *pName);
	T_PortInterface (*getConnectedFacet)(struct AirportManagerComponent *pThis, T_AirportManagerComponentReceptaclesIndexes pReceptacleIndex);
	 
	Component *(*getComponentInstance)(struct AirportManagerComponent *pThis);
	 
	Component _componentImpl;
	 
	T_AirportManagerComponentReceptacles _receptacles;
	T_boolean _connectionErrorDetected;
	ComponentHelper _componentHelper;
} AirportManagerComponent;

typedef struct AirportManagerComponentClass
{
	 
	void (*initialize)(AirportManagerComponent *pInstance);
	AirportManagerComponent *(*getInstance)(void);
	 
	AirportManagerComponent *_instance;
} AirportManagerComponentClass;

extern AirportManagerComponentClass *CAirportManagerComponent;


#line 24 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 25 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"

extern T_STRING	FT_ICAO_OF_THE_DEFAULT_ARPT;
extern T_STRING FT_ICAO_TO_BE_DISP_IN_PLAN;

extern T_FLOAT ARPLATITUDE;
extern T_FLOAT ARPLONGITUDE;
extern T_BOOL AC_IN_BOUNDING_BOX;
extern T_BOOL SAME_ARPT_PLAN_AS_ROSENAV;


extern T_STRING FT_ICAO_OF_PRELOADED_ARPT1;
extern T_STRING FT_ICAO_OF_PRELOADED_ARPT2;
extern T_STRING FT_ICAO_OF_PRELOADED_ARPT3;
extern T_STRING FT_ICAO_OF_PRELOADED_ARPT4;

#line 1 "..\Includes\StringUtilities.h"
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 27 "..\Includes\StringUtilities.h"

 



typedef struct StringUtilitiesClass 
{
	 
	T_boolean (*equals)(const T_char *pStr1, const T_char *pStr2);
	T_uint16 (*length)(const T_char *pStr);
	T_boolean (*nequals)(const T_char *pStr1, const T_char *pStr2, T_uint32 pSize);
	T_int32 (*copy)(T_char *pDestination, const T_char *pSource);
	T_int32 (*concat)(T_char *pDest, const T_char *pSrc);
	T_int32 (*ncopy)(T_char *pDestination, const T_char *pSource, T_uint32 pSize);
	T_int32 (*nconcat)(T_char *pDest, const T_char *pSrc, T_uint32 pSize);
	T_uint32 (*nlength)(const T_char *pStr, T_uint32 pMaxLength);
	 
} StringUtilitiesClass;

extern StringUtilitiesClass *CStringUtilities;


#line 41 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
 


#line 1 "..\Includes\AirportManager__updateAirportToBeDisplayedInPlan.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateAirportToBeDisplayedInPlan.hpp"

 
 
 
 
 
extern void AirportManager__updateAirportToBeDisplayedInPlan(AirportManager *pThis);


#line 45 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateAirportPriorityList.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateAirportPriorityList.hpp"

 
 
 
 
 
extern void AirportManager__updateAirportPriorityList(AirportManager *pThis);


#line 46 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateAirportToBeDisplayedInArcNav.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateAirportToBeDisplayedInArcNav.hpp"

 
 
 
 
 
extern void AirportManager__updateAirportToBeDisplayedInArcNav(AirportManager *pThis);


#line 47 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateFMSAirports.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateFMSAirports.hpp"

 
 
 
 
 
 
extern void AirportManager__updateFMSAirports(AirportManager *pThis);


#line 48 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateAircraftSituation.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateAircraftSituation.hpp"

 
 
 
 
 
 
extern void AirportManager__updateAircraftSituation(AirportManager *pThis);


#line 49 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateBTVAirport.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateBTVAirport.hpp"

 
 
 
 
 
extern void AirportManager__updateBTVAirport(AirportManager *pThis);


#line 50 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager_airportSelected.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager_airportSelected.hpp"

 
 
 
 
 
extern void AirportManager_airportSelected(AirportManager *pThis, T_int16 pAirportRequest);


#line 51 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager_execute.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager_execute.hpp"

 
 
 
 
 
extern void AirportManager_execute(AirportManager *pThis);


#line 52 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager_getName.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager_getName.hpp"

 
 
 
 
 
extern T_char *AirportManager_getName(AirportManager *pThis);


#line 53 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateAirportToDisplay.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateAirportToDisplay.hpp"

 
 
 
 
 
extern void AirportManager__updateAirportToDisplay(AirportManager *pThis);


#line 54 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateFlightTest.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateFlightTest.hpp"

 
 
 
 
extern void AirportManager__updateFlightTest(AirportManager *pThis);


#line 55 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateMapManagementData.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateMapManagementData.hpp"

 
 
 
 
extern void AirportManager__updateMapManagementData(AirportManager *pThis);


#line 56 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateMode.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateMode.hpp"

 
 
 
 
 
extern void AirportManager__updateMode(AirportManager *pThis);


#line 57 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__updateAirportServiceActivation.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__updateAirportServiceActivation.hpp"

 
 
 
 
 
 
extern void AirportManager__updateAirportServiceActivation(AirportManager *pThis);


#line 58 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"
#line 1 "..\Includes\AirportManager__checkDatabaseSwap.hpp"
 
 
 
 
 
 
 
 
 
 
 
 
 
 




#line 20 "..\Includes\AirportManager__checkDatabaseSwap.hpp"

 
 
 
 
 
extern void AirportManager__checkDatabaseSwap(AirportManager *pThis);


#line 59 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_initialize.c"

static AirportSelectionEvent *AirportManager_getAirportSelectionEventInstance(AirportManager *pThis);
static void AirportManager_airportSelectedAirportSelectionEventImpl(AirportSelectionEvent *pThis, T_int16 pAirportRequest);

static Task *AirportManager_getTaskInstance(AirportManager *pThis);
static void AirportManager_executeTaskImpl(Task *pThis);
static T_char *AirportManager_getNameTaskImpl(Task *pThis);

 
 
 
 
 
void AirportManager_static_initialize(AirportManager *pInstance)
{_ATC_LINK(1);{_ATC_PROC(1,1);{_ATC_BLOC(1,4);{
     
	T_uint16 lI = 0;
     

	 
	pInstance->_updateAirportToBeDisplayedInPlan = AirportManager__updateAirportToBeDisplayedInPlan;
	pInstance->_updateAirportPriorityList = AirportManager__updateAirportPriorityList;
	pInstance->_updateAirportToBeDisplayedInArcNav = AirportManager__updateAirportToBeDisplayedInArcNav;
	pInstance->_updateFMSAirports = AirportManager__updateFMSAirports;
	pInstance->_updateAircraftSituation = AirportManager__updateAircraftSituation;
	pInstance->_updateBTVAirport = AirportManager__updateBTVAirport;
	pInstance->airportSelected = AirportManager_airportSelected;
	pInstance->execute = AirportManager_execute;
	pInstance->getName = AirportManager_getName;
	pInstance->_updateAirportToDisplay = AirportManager__updateAirportToDisplay;
	pInstance->_updateFlightTest = AirportManager__updateFlightTest;
	pInstance->_updateMapManagementData = AirportManager__updateMapManagementData;
	pInstance->_updateMode = AirportManager__updateMode;
	pInstance->_updateAirportServiceActivation = AirportManager__updateAirportServiceActivation;
	pInstance->_checkDatabaseSwap = AirportManager__checkDatabaseSwap;

	 
	pInstance->getAirportSelectionEventInstance = AirportManager_getAirportSelectionEventInstance;
	pInstance->_airportSelectionEventImpl.airportSelected = AirportManager_airportSelectedAirportSelectionEventImpl;
	pInstance->_airportSelectionEventImpl._impl = (void *)pInstance;
	 
	pInstance->getTaskInstance = AirportManager_getTaskInstance;
	pInstance->_taskImpl.execute = AirportManager_executeTaskImpl;
	pInstance->_taskImpl.getName = AirportManager_getNameTaskImpl;
	pInstance->_taskImpl._impl = (void *)pInstance;

	 
	pInstance->_alternateAirport =  -1 ;
	pInstance->_BTVAirport =  -1 ;
	pInstance->_nearestAirport =  -1 ;
	pInstance->_destinationAirport =  -1 ;
	pInstance->_originAirport =  -1 ;
	pInstance->_requestedAirportForDisplay =  -1 ;
	pInstance->_airportToBeDisplayedInPlan =  -1 ;
	pInstance->_memorizedAirportInArcNav =  -1 ;
	pInstance->_defaultAirport =  -1 ;
	pInstance->_flightPhase = E_GND_PREFLIGHT;
	pInstance->_airportToBeDisplayedInArcNav =  -1 ;
	pInstance->_destinationAirportOfLastFlight =  -1 ;
	pInstance->_aircraftLatitude = 0.0f;
	pInstance->_aircraftLongitude = 0.0f;
	pInstance->_BTVAirportChanged =  1 ;
	pInstance->_onGroundCondition =  0 ;
	pInstance->_mode = E_UNKNOWN_MODE;
	pInstance->_flightPhaseValidity =  0 ;
	pInstance->_aircraftLocationValidity =  0 ;
	pInstance->_lastDisplayedAirportInPlan =  -1 ;
	pInstance->_aircraftAltitude = 0.0f;
	pInstance->_transitionPerformed =  0 ;
	pInstance->_firstEntryPlanModeAfterTransition =  0 ;
	pInstance->_firstDisplayModePlanAfterTransition =  0 ;
	pInstance->_airportDisplayedInPlanAfterTransition =  0 ;
	pInstance->_airportToBeDisplayedState = E_NO_AIRPORT_TO_DISPLAY;
	pInstance->_previousMode = E_UNKNOWN_MODE;
	pInstance->_btvStateConsolidated = E_ARPTNAV_ST;
	pInstance->_databaseSwapState =  0 ;


     
	CStringUtilities->ncopy(&(pInstance->_airportNameToBeDisplayedInPlan[0]), "",  5 );

	 
	CStringUtilities->ncopy(&FT_ICAO_OF_THE_DEFAULT_ARPT.data[0], "",  32 );
	FT_ICAO_OF_THE_DEFAULT_ARPT.length = 0;

	CStringUtilities->ncopy(&FT_ICAO_TO_BE_DISP_IN_PLAN.data[0], "",  32 );
	FT_ICAO_TO_BE_DISP_IN_PLAN.length = 0;

	ARPLATITUDE = 0.0f;
	ARPLONGITUDE = 0.0f;
	AC_IN_BOUNDING_BOX =  0 ;
	SAME_ARPT_PLAN_AS_ROSENAV =  0 ;

	CStringUtilities->ncopy(&FT_ICAO_OF_PRELOADED_ARPT1.data[0], "",  32 );
	CStringUtilities->ncopy(&FT_ICAO_OF_PRELOADED_ARPT2.data[0], "",  32 );
	CStringUtilities->ncopy(&FT_ICAO_OF_PRELOADED_ARPT3.data[0], "",  32 );
	CStringUtilities->ncopy(&FT_ICAO_OF_PRELOADED_ARPT4.data[0], "",  32 );
	FT_ICAO_OF_PRELOADED_ARPT1.length = 0;
	FT_ICAO_OF_PRELOADED_ARPT2.length = 0;
	FT_ICAO_OF_PRELOADED_ARPT3.length = 0;
	FT_ICAO_OF_PRELOADED_ARPT4.length = 0;

	{_ATC_LOOP_bef(1,1);for (lI = 0; lI <  4 ; lI++)
	{_ATC_LOOP_in(1,1);{_ATC_BLOC(1,0);{
		pInstance->_airportPriorityList[lI] =  -1 ;
	}}}}

	 
	{_ATC_PROC(1,0);return;}
}}}}

 
static AirportSelectionEvent *AirportManager_getAirportSelectionEventInstance(AirportManager *pThis)
{_ATC_LINK(1);{_ATC_PROC(1,3);{_ATC_BLOC(1,5);{
	{_ATC_PROC(1,2);return &(pThis->_airportSelectionEventImpl);}
}}}}
static void AirportManager_airportSelectedAirportSelectionEventImpl(AirportSelectionEvent *pThis, T_int16 pAirportRequest)
{_ATC_LINK(1);{_ATC_PROC(1,5);{_ATC_BLOC(1,6);{
	AirportManager *lImpl = (AirportManager *)(pThis->_impl);
	lImpl->airportSelected(lImpl, pAirportRequest);
	{_ATC_PROC(1,4);return;}
}}}}

 
static Task *AirportManager_getTaskInstance(AirportManager *pThis)
{_ATC_LINK(1);{_ATC_PROC(1,7);{_ATC_BLOC(1,7);{
	{_ATC_PROC(1,6);return &(pThis->_taskImpl);}
}}}}
static void AirportManager_executeTaskImpl(Task *pThis)
{_ATC_LINK(1);{_ATC_PROC(1,9);{_ATC_BLOC(1,8);{
	AirportManager *lImpl = (AirportManager *)(pThis->_impl);
	lImpl->execute(lImpl);
	{_ATC_PROC(1,8);return;}
}}}}
static T_char *AirportManager_getNameTaskImpl(Task *pThis)
{_ATC_LINK(1);{_ATC_PROC(1,11);{_ATC_BLOC(1,9);{
	AirportManager *lImpl = (AirportManager *)(pThis->_impl);
	{_ATC_PROC(1,10);return lImpl->getName(lImpl);}
}}}}
