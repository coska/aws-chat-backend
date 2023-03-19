package com.coska.aws.controller;

import com.coska.aws.dto.*;
import com.coska.aws.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService service;

    public UserController(final UserService us) {
        this.service = us;
    }

    @PostMapping("/create")
    public @ResponseBody UserDto create(final HttpServletRequest request, @RequestBody final UserDto dto) {
        final String str = dto.validate();
        if (StringUtils.isNotEmpty(str)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, str);
        }

        return service.create(dto);
    }

    @PutMapping("/update")
    public @ResponseBody UserDto update(final HttpServletRequest request, final Principal principal, @RequestBody final UserDto dto) {
        final String str = dto.validate();
        if (StringUtils.isNotEmpty(str)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, str);
        }

        return service.update(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(final HttpServletRequest request, @PathVariable("id") final String id) {
        if (!org.springframework.util.StringUtils.hasLength(id))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id Not Found");
        }

        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(final HttpServletRequest request) {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody void delete(final HttpServletRequest request, @PathVariable("id") final String id)    {
        if (!org.springframework.util.StringUtils.hasLength(id))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id Not Found");
        }

        service.delete(id);
    }
}