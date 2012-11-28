/* *************************************************************************************** */
/* (C) Copyright 2005 by THALES Avionics                                                   */
/* All rights reserved                                                                     */
/*                                                                                         */
/* This program is the property of THALES Avionics, LE HAILLAN-BORDEAUX FRANCE, and can    */
/* only be used and copied with the prior written authorisation of THALES Avionics.        */
/*                                                                                         */
/* Any whole or partial copy of this program in either its original form or in a modified  */
/* form must mention this copyright and its owner.                                         */
/* *************************************************************************************** */
/* PROJECT: PDS                                                                            */
/* *************************************************************************************** */
/* FILE NAME: StringUtilities_static_equals.c                                              */
/* *************************************************************************************** */

#include "StringUtilities.h"

/* Start of user includes */
/* End of user includes */


/* *************************************************************************************** */
/* METHOD DESCRIPTION: [public][class method]                                              */
/* -------------------                                                                     */
/* This method returns true if the two specified string are equal, otherwise false.        */
/* *************************************************************************************** */
T_boolean StringUtilities_static_equals(const T_char *pStr1, const T_char *pStr2) 
{
    /* Start of user code */
    T_uint16 lIndex = 0;
    T_boolean lReturn = K_BOOL_TRUE;
    
    while ((lReturn == K_BOOL_TRUE) && (pStr1[lIndex] != '\0') && (pStr2[lIndex] != '\0'))
    {
    	if (pStr1[lIndex] == pStr2[lIndex])
    	{
    		lIndex++;
    	}
    	else
    	{
    		lReturn = K_BOOL_FALSE;
    	}
    }
    
    if ((lReturn == K_BOOL_TRUE) && (pStr1[lIndex] != pStr2[lIndex]))
    {
    	lReturn = K_BOOL_FALSE;
    }
	else
	{
		;/* nothing to do */
	}
    
	return lReturn;
    /* End of user code */
}
