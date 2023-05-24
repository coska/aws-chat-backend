package com.coska.aws.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.coska.aws.dto.RoomDto;
import com.coska.aws.service.CommunicationService;
import com.coska.aws.service.RoomService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1/chat/rooms")
public class RoomController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @Autowired
    private CommunicationService communicationService;
    
    @PostMapping()
    public @ResponseBody RoomDto create(final HttpServletRequest request, @RequestBody final RoomDto dto) {
        
        UUID uuid = UUID.randomUUID();

        dto.setId(uuid.toString());
        String str = dto.validate();
        if (StringUtils.isNotEmpty(str)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, str);
        }
        RoomDto rtnDto = service.create(dto);
        communicationService.addRoom(rtnDto);
        return rtnDto;
    }

    @PutMapping()
    public @ResponseBody RoomDto update(final HttpServletRequest request, final Principal principal,
            @RequestBody final RoomDto dto) {
        final String str = dto.validate();
        if (StringUtils.isNotEmpty(str)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, str);
        }

        return service.update(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> findById(final HttpServletRequest request, @PathVariable("id") final String id) {
        if (!org.springframework.util.StringUtils.hasLength(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id Not Found");
        }

        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> findAll(final HttpServletRequest request) {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody void delete(final HttpServletRequest request, @PathVariable("id") final String id) {
        if (!org.springframework.util.StringUtils.hasLength(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id Not Found");
        }

        service.delete(id);
    }

}
