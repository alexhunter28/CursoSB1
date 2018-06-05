package com.example.app.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.app.model.entity.Cliente;

public interface IClienteService {
	
	List<Cliente> findAll();
	Page<Cliente> findAll(Pageable pageable); 
	void save(Cliente cliente);
	Cliente findOne(Long id);
	void delete(Long id);

}
