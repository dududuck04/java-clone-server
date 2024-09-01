package com.mzc.stc.caa.carrot.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class SampleController {
	
	@GetMapping("/hello")
	public String hello(@RequestParam String name) {
		return String.format("할로 '%s'", name);
	}

}
