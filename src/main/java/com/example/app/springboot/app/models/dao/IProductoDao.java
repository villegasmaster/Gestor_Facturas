package com.example.app.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.app.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long>{
	
	@Query("Select p from Producto p where p.name like %?1%")
	public List<Producto> findByName(String term);
	
	
	/* Hace lo mismo que el de arriba pero con la convencion de metodo pones el 
	 * findBy + nombre del campo a buscar + Like para buscar por palabra + ignoreCase para ignorar mayusculas*/
	public List<Producto> findByNameLikeIgnoreCase(String term);

}
