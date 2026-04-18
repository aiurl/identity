package com.nerosoft.linkyou.seedwork;

import org.springframework.beans.factory.annotation.Autowired;

import an.awesome.pipelinr.Pipeline;

public abstract class BaseApplicationService implements ApplicationService {
    
    @Autowired
    protected Pipeline pipeline;
}
