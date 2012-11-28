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

_ATC_DECLARE_PROC(1,2)
_ATC_DECLARE_BLOC(1,1)
_ATC_DECLARE(1,0x20417C17UL,0x525B28E7UL,2,_ATC_TAB_PROC(1),0,0,1,_ATC_TAB_BLOC(1),0,0)

#line 1 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_getInstance.c"
 
 
 
 
 
 
 
 
 
 
 
 
 
 

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


#line 17 "R:\\DGWP-SW-A350\\DGWP_A350_UT\\DGWP_ANF\\Class_AirportManager\\Sources\\AirportManager_static_getInstance.c"




 
 


 
 
 
 
 
AirportManager *AirportManager_static_getInstance(void) 
{_ATC_LINK(1);{_ATC_PROC(1,1);{_ATC_BLOC(1,0);{
     
	{_ATC_PROC(1,0);return &(CAirportManager->_instance);}
     
}}}}
