package com.example;

import com.example.helper.FileUtils;
import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@Path("/Resources")
public class QResources {

    @GET
    public String hello() {
        FileUtils.getDataScrapping();
        return "Hello RESTEasy";
    }



}
