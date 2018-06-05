package com.example.app.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.model.dao.IClienteDao;
import com.example.app.model.entity.Cliente;

@Service
public class ClienteService implements IClienteService {

	@Autowired
	private IClienteDao iClienteDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Cliente> findAll() {
		return (List<Cliente>) iClienteDao.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		iClienteDao.save(cliente);
	}

	@Override
	@Transactional(readOnly=true)
	public Cliente findOne(Long id) {		
		return iClienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		iClienteDao.deleteById(id);
	}

	@Override
	public Page<Cliente> findAll(Pageable pageable) {
		return iClienteDao.findAll(pageable);
	}

}
