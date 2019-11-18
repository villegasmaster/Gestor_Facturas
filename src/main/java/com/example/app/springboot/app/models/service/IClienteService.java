package com.example.app.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.app.springboot.app.models.entity.Cliente;
import com.example.app.springboot.app.models.entity.Factura;
import com.example.app.springboot.app.models.entity.Producto;

public interface IClienteService {

	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);

	public void save(Cliente cliente);

	public Cliente findOne(Long id);

	public void remove(Long id);
	
	public List<Producto> findByName(String term);
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(Long id);
}
