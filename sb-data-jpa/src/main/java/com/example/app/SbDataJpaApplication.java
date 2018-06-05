package com.example.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.app.model.service.IFileUploadService;

@SpringBootApplication
public class SbDataJpaApplication implements CommandLineRunner {

	@Autowired
	private IFileUploadService iFileUploadService;
	
	public static void main(String[] args) {
		SpringApplication.run(SbDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		iFileUploadService.deleteAll();
		iFileUploadService.init();
		
	}
}
