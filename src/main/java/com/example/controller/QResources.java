package com.example.controller;

import com.example.service.FileUtils;

import javax.ws.rs.*;

@Path("/Resources")
public class QResources {

    @GET
    public String hello() {
        FileUtils.getDataScraping();
        return "Hello RESTEasy";
    }



}
