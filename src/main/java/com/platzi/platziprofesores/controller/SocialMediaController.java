package com.platzi.platziprofesores.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.platzi.platziprofesores.model.*;
import com.platzi.platziprofesores.service.SocialMediaService;
import com.platzi.platziprofesores.util.CustomErrorType;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/v1")
public class SocialMediaController {
	
	@Autowired
	private SocialMediaService _socialMediaService;
	private static final String SOCIAL_MEDIA_IMG_DIR="images/socialMedias/";
	
	@RequestMapping(value="/socialMedias",method=RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<List<SocialMedia>> getSocialMedias(@RequestParam(value="name",required=false) String name){
		List<SocialMedia> socialMedias= new ArrayList<SocialMedia>();
		
		if(name==null) {
			socialMedias=_socialMediaService.findAll();
			if(socialMedias.isEmpty()) {
				return new ResponseEntity(new CustomErrorType("No existen redes sociales"),HttpStatus.NO_CONTENT);
			}
		}else {
			if(name.isEmpty() || name.equals("")) {
				return new ResponseEntity(new CustomErrorType("Nombre invalido"),HttpStatus.BAD_REQUEST);
			}
			SocialMedia socialMedia= _socialMediaService.findByName(name);
			if(socialMedia==null) {
				return new ResponseEntity(new CustomErrorType("Debe existir al menos una red social con ese nombre"),HttpStatus.NOT_FOUND);
			}
			socialMedias.add(socialMedia);
		}
		
		return new ResponseEntity<List<SocialMedia>>(socialMedias,HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/socialMedias/{id}",method=RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<SocialMedia> getSocialMedia(@PathVariable("id") Long idSocialMedia){
		if(idSocialMedia==null || idSocialMedia<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		
		SocialMedia currentSocialMedia= _socialMediaService.findById(idSocialMedia);
		if(currentSocialMedia==null) {
			return new ResponseEntity(new CustomErrorType("La red social debe existir"),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<SocialMedia>(currentSocialMedia,HttpStatus.OK);
	}
	
	@RequestMapping(value="/socialMedias",method=RequestMethod.POST,headers="Accept=application/json")
	public ResponseEntity<?> createSocialMedia(@RequestBody SocialMedia socialMedia,UriComponentsBuilder uriComponentBuilder){
		if(socialMedia.getName().equals("") || socialMedia.getName().equals(null)) {
			return new ResponseEntity(new CustomErrorType("Nombre de red social invalido"),HttpStatus.BAD_REQUEST);
		}
		if(_socialMediaService.findByName(socialMedia.getName())!=null) {
			return new ResponseEntity(new CustomErrorType("La red social ya existe"),HttpStatus.BAD_REQUEST);
		}
		
		_socialMediaService.create(socialMedia);
		SocialMedia newSocialMedia= _socialMediaService.findByName(socialMedia.getName());
		HttpHeaders headers= new HttpHeaders();
		headers.setLocation(uriComponentBuilder.path("/v1/socialMedias/{id}")
				.buildAndExpand(newSocialMedia.getIdSocialMedia())
				.toUri());
		return new ResponseEntity<String>(headers,HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/socialMedias/{id}",method=RequestMethod.PATCH,headers="Accept=application/json")
	public ResponseEntity<SocialMedia> updateSocialMedia(@PathVariable("id") Long idSocialMedia, @RequestBody SocialMedia socialMedia){
		if(idSocialMedia==null || idSocialMedia<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		
		SocialMedia currentSocialMedia= _socialMediaService.findById(idSocialMedia);
		if(currentSocialMedia==null) {
			return new ResponseEntity(new CustomErrorType("La red social debe existir"),HttpStatus.NOT_FOUND);
		}
		
		currentSocialMedia.setIcon(socialMedia.getIcon());
		currentSocialMedia.setName(socialMedia.getName());
		
		_socialMediaService.update(currentSocialMedia);
		
		return new ResponseEntity<SocialMedia>(currentSocialMedia,HttpStatus.OK);
	}
	
	@RequestMapping(value="/socialMedias/{id}",method=RequestMethod.DELETE,headers="Accept=application/json")
	public ResponseEntity<SocialMedia> deleteSocialMedia(@PathVariable("id") Long idSocialMedia){
		if(idSocialMedia==null || idSocialMedia<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		
		SocialMedia currentSocialMedia= _socialMediaService.findById(idSocialMedia);
		if(currentSocialMedia==null) {
			return new ResponseEntity(new CustomErrorType("La red social debe existir"),HttpStatus.NOT_FOUND);
		}
		_socialMediaService.delete(idSocialMedia);
		return new ResponseEntity<SocialMedia>(currentSocialMedia,HttpStatus.OK);
	}
	
	@RequestMapping(value="/socialMedias/picture",method=RequestMethod.POST,headers="content-type=multipart/form-data")
	public ResponseEntity<byte[]> uploadFile(@RequestParam("id_social_media") Long idSocialMedia,
			@RequestParam("file") MultipartFile file,UriComponentsBuilder uriComponentBuilder){
		if(idSocialMedia==null || idSocialMedia<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		if(file.isEmpty() || file==null) {
			return new ResponseEntity(new CustomErrorType("Es necesario proveer un archivo"),HttpStatus.BAD_REQUEST);
		}
		SocialMedia socialMedia= _socialMediaService.findById(idSocialMedia);
		if(socialMedia==null) {
			return new ResponseEntity(new CustomErrorType("La red social debe existir"),HttpStatus.NOT_FOUND);
		}
		try {
			if(socialMedia.getIcon()!=null && !socialMedia.getIcon().isEmpty()) {
				String fileName= socialMedia.getIcon();
				Path path= Paths.get(fileName);
				File oldFile = path.toFile();
				if(oldFile.exists()) {
					oldFile.delete();
				}	
			}
			SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date date= new Date();
			String timestamp=format.format(date);
			String fileName= SOCIAL_MEDIA_IMG_DIR+String.valueOf(idSocialMedia)+"-"+timestamp
					+"."+file.getContentType().split("/")[1];
			byte[] bytes=file.getBytes();
			Path newPath=Paths.get(fileName);
			Files.write(newPath, bytes);
			socialMedia.setIcon(fileName);
			_socialMediaService.update(socialMedia);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Un error ocurrio al intentar subir el archivo: "+e.getMessage())
					,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@RequestMapping(value="/socialMedias/{id}/picture", method=RequestMethod.GET)
	public ResponseEntity<byte[]> getFile(@PathVariable("id") Long idSocialMedia){
		if(idSocialMedia==null || idSocialMedia<0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia invalido"),HttpStatus.BAD_REQUEST);
		}
		
		SocialMedia socialMedia= _socialMediaService.findById(idSocialMedia);
		if(socialMedia==null) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia no encontrado"),HttpStatus.NOT_FOUND);
		}
		try {
			String rutaArchivo= socialMedia.getIcon();
			Path path=Paths.get(rutaArchivo);
		    byte[] bytes= Files.readAllBytes(path);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Un error ocurrió al intentar consultar la imagen: "+e.getMessage()),HttpStatus.BAD_REQUEST);
		}
	}
}


