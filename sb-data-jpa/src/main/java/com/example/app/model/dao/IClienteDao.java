package com.example.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.app.model.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

}
