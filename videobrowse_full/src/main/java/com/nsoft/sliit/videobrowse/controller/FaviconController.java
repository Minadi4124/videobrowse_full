package com.nsoft.sliit.videobrowse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaviconController {

    @GetMapping({"/favicon.ico", "/favicon.png"})
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build();
    }
}


