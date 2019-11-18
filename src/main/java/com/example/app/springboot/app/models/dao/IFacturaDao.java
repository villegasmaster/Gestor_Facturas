package com.example.app.springboot.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.app.springboot.app.models.entity.Factura;

public interface IFacturaDao extends PagingAndSortingRepository<Factura, Long>{

}
