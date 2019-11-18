package com.example.app.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.app.springboot.app.models.entity.Cliente;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{
	
	
}
