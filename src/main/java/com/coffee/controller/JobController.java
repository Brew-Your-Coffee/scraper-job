package com.coffee.controller;

import com.coffee.exception.ElasticSearchException;
import com.coffee.exception.ScraperException;
import com.coffee.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping("/run")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void run() throws ScraperException, ElasticSearchException {
        jobService.run();
    }
}
