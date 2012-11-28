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
/* FILE NAME: StringUtilities_static_copy.c                                                */
/* *************************************************************************************** */

#include "StringUtilities.h"

/* Start of user includes */
/* End of user includes */


/* *************************************************************************************** */
/* METHOD DESCRIPTION: [public][class method]                                              */
/* -------------------                                                                     */
/* ISO_9898_ANSI_C - like strcpy                                                           */
/* The copy operation copies the string pointed to by pSrc (including the terminating null */
/*  character) into the array pointed to by pDest. If copying takes place between objects  */
/* that overlap, the behavior is undefined.                                                */
/* *************************************************************************************** */
void StringUtilities_static_copy(T_char *pDestination, const T_char *pSource) 
{
    /* Start of user code */
    while ((*pSource) != 0x00)
    {
    	(*pDestination) = (*pSource);
    	
    	pSource++;
    	pDestination++;
    }
    (*pDestination) = 0x00;
    /* End of user code */
	return;
}
