package com.example.app.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.springboot.app.models.dao.IClienteDao;
import com.example.app.springboot.app.models.dao.IFacturaDao;
import com.example.app.springboot.app.models.dao.IProductoDao;
import com.example.app.springboot.app.models.entity.Cliente;
import com.example.app.springboot.app.models.entity.Factura;
import com.example.app.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	IClienteDao clienteDao;
	
	@Autowired
	IProductoDao productoDao;
	
	@Autowired
	IFacturaDao facturaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteDao.save(cliente);

	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void remove(Long id) {
		clienteDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

//	@Override
//	@Transactional(readOnly = true)
//	public List<Producto> findByName(String term) {
//		return productoDao.findByName(term);
//	}
	

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByName(String term) {
		return productoDao.findByNameLikeIgnoreCase("%" + term + "%");
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
	return productoDao.findById(id).orElse(null);
	
			
		
		
	}
	
	
	

}
