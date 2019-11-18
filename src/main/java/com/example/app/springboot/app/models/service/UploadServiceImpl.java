package com.example.app.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements IUploadService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final static String UPLOADS_PATH = "uploads";

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = getPath(filename);
		log.info("pathFoto: " + pathFoto);

		Resource recurso = null;
		recurso = new UrlResource(pathFoto.toUri());
		if (!recurso.exists() && recurso.isReadable()) {
			throw new RuntimeException("Error al cargar la imagen : " + pathFoto.toString());
		}

		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		// Vamos a asignar un nombre random al archivo unico para no tener ficheros
		// repetidos en el servidor
		String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

		// Este codigo es para agregar directorio absoluto y externo en la raiz del
		// proyecto

		Path rootPath = getPath(uniqueFileName);
		// Hay que añadir la ruta completa de nuestro proyecto, se elimina ya que el
		// getPath devuelve un absolutePath
		// Path absolutPath = rootPath.toAbsolutePath();

		log.info("rootPath: " + rootPath);

		// Este es el metodo antiguo, vamos a utilizar otro
//			byte[] bytes = foto.getBytes();
//			Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
//			// Escribe en el directorio, guarda la foto
//			Files.write(rutaCompleta, bytes);

		// Metodo nuevo de escribir ficheros
		Files.copy(file.getInputStream(), rootPath);

		return uniqueFileName;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		
		File file = rootPath.toFile();
		
		if(file.exists() && file.canRead()) {
			if(file.delete()) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_PATH).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_PATH).toFile());
		
	}

	@Override
	public void init() throws IOException{
	Files.createDirectory(Paths.get(UPLOADS_PATH));
		
	}

}
