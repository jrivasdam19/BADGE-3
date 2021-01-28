package com.example.controller;

import com.example.data.Student;
import com.example.service.FileUtils;

import javax.ws.rs.*;

@Path("/Resources")
public class QResources {

    @GET
    public  Iterable<Student>  getAll() {
        return  FileUtils.getData();
    }

}
