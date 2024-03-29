package com.coska.aws.controller;

import java.security.Principal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.coska.aws.dto.UserDto;
import com.coska.aws.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1/chat/users")
public class UserController {

    private final UserService service;

    public UserController(final UserService us) {
        this.service = us;
    }

    @PostMapping()
    public @ResponseBody UserDto create(final HttpServletRequest request, @RequestBody final UserDto dto) {
        final String str = dto.validate();
        if (StringUtils.isNotEmpty(str)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, str);
        }

        return service.create(dto);
    }

    @PutMapping()
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