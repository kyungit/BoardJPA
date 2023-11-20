package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MasterController {
	private final BoardService service;
	
	
	@GetMapping("/master/sysManager")
	public void getSysManager() {
		
	}
}
