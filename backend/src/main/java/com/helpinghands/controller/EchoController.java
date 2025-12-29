package com.helpinghands.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

    @GetMapping("/echo")
    String echo() {
        return "Helping Hands!";
    }

}
