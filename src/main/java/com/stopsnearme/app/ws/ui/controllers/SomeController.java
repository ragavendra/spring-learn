package com.stopsnearme.app.ws.ui.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// package org.eclipse.jakarta.rest;

import java.lang.invoke.MethodHandles;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/some")
public class SomeController {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @GetMapping
    public String dowloadStatics(){
        logger.log(Level.WARNING, "In some now");
		return "In some get call";
	}
}
