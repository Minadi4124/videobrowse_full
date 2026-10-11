package com.nsoft.sliit.videobrowse.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/files/videos")
public class FilesController {

    private final Path storage = Paths.get("data/videos");

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> serve(@PathVariable String filename) throws Exception {
        Path file = storage.resolve(filename).normalize();
        if(!Files.exists(file)){
            return ResponseEntity.notFound().build();
        }
        MediaType mt = MediaType.APPLICATION_OCTET_STREAM;
        try {
            String probe = Files.probeContentType(file);
            if (probe != null) mt = MediaType.parseMediaType(probe);
        } catch (Exception ignored){}
        ContentDisposition cd = ContentDisposition.inline().filename(file.getFileName().toString()).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, cd.toString())
                .contentType(mt)
                .body(new FileSystemResource(file.toFile()));
    }
}
