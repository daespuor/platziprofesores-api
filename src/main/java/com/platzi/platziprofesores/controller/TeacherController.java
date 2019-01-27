package com.platzi.platziprofesores.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.platzi.platziprofesores.model.SocialMedia;
import com.platzi.platziprofesores.model.Teacher;
import com.platzi.platziprofesores.model.TeacherSocialMedia;
import com.platzi.platziprofesores.service.SocialMediaService;
import com.platzi.platziprofesores.service.TeacherService;
import com.platzi.platziprofesores.util.CustomErrorType;


@Controller
@RequestMapping("/v1")
public class TeacherController {
	
	@Autowired
	private TeacherService _teacherService;
	@Autowired
	private SocialMediaService _socialMediaService;
	private static final String TEACHER_IMG_DIR="images/teachers/";
	
	@RequestMapping(value="/teachers",method=RequestMethod.POST,headers="Accept=application/json")
	public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher,
			UriComponentsBuilder uriComponentBuilder){
		if(teacher.getName()==null || teacher.getName().equals("")) {
			return new ResponseEntity(new CustomErrorType("Nombre invalido"),HttpStatus.BAD_REQUEST);
		}
		
		if(_teacherService.findByName(teacher.getName())!=null) {
			return new ResponseEntity(new CustomErrorType("El profesor ya existe"),HttpStatus.BAD_REQUEST);
		}
		_teacherService.create(teacher);
		Teacher currentTeacher= _teacherService.findByName(teacher.getName());
		HttpHeaders headers= new HttpHeaders();
		headers.setLocation(uriComponentBuilder.path("/v1/teachers/{id}").buildAndExpand(currentTeacher.getIdTeacher()).toUri());
		return new ResponseEntity<String>(headers,HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/teachers",method=RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value="name",required=false) String name){
		List teachers= new ArrayList<Teacher>();
		if(name!=null && !name.equals("")){
			Teacher teacher= _teacherService.findByName(name);
			if(teacher==null) {
				return new ResponseEntity(new CustomErrorType("El profesor con ese nombre no existe"),HttpStatus.NOT_FOUND);
			}
			teachers.add(teacher);
			return new ResponseEntity<List<Teacher>>(teachers,HttpStatus.OK);
		}
		teachers=_teacherService.findAll();
		return new ResponseEntity<List<Teacher>>(teachers,HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/{id}",method=RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<Teacher> getTeacher(@PathVariable("id") Long idTeacher){
		if(idTeacher==null || idTeacher<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		Teacher teacher=_teacherService.findById(idTeacher);
		if(teacher==null) {
			return new ResponseEntity(new CustomErrorType("Profesor no encontrado"),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Teacher>(teacher,HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/{id}",method=RequestMethod.PATCH,headers="Accept=application/json")
	public ResponseEntity<Teacher> updateTeacher(@PathVariable("id") Long idTeacher, @RequestBody Teacher teacher){
		if(idTeacher==null || idTeacher<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		
		Teacher currentTeacher= _teacherService.findById(idTeacher);
		if(currentTeacher==null) {
			return new ResponseEntity(new CustomErrorType("El profesor debe existir"),HttpStatus.NOT_FOUND);
		}
		
		//currentTeacher.setAvatar(teacher.getAvatar());
		currentTeacher.setName(teacher.getName());
		
		_teacherService.update(currentTeacher);
		
		return new ResponseEntity<Teacher>(currentTeacher,HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/{id}",method=RequestMethod.DELETE,headers="Accept=application/json")
	public ResponseEntity<Teacher> deleteTeacher(@PathVariable("id") Long idTeacher){
		if(idTeacher==null || idTeacher<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		
		Teacher currentTeacher= _teacherService.findById(idTeacher);
		if(currentTeacher==null) {
			return new ResponseEntity(new CustomErrorType("El profesor debe existir"),HttpStatus.NOT_FOUND);
		}
		_teacherService.delete(idTeacher);
		return new ResponseEntity<Teacher>(currentTeacher,HttpStatus.OK);
	}
	
	@RequestMapping(value="/teachers/picture",method=RequestMethod.POST,headers="content-type=multipart/form-data")
	public ResponseEntity<byte[]> uploadFile(@RequestParam("id_teacher") Long idTeacher,
			@RequestParam("file") MultipartFile file){
		if(idTeacher==null || idTeacher<0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		if(file.isEmpty() || file==null) {
			return new ResponseEntity(new CustomErrorType("Es necesario proveer un archivo"),HttpStatus.BAD_REQUEST);
		}
		Teacher teacher= _teacherService.findById(idTeacher);
		if(teacher==null) {
			return new ResponseEntity(new CustomErrorType("Profesor no encontrado"),HttpStatus.NOT_FOUND);
		}
		try {
			if(teacher.getAvatar()!=null && !teacher.getAvatar().equals("")) {
				String filePath= teacher.getAvatar();
				Path path=Paths.get(filePath);
				File oldFile= path.toFile();
				if(oldFile.exists()) {
					oldFile.delete();
				}
			}
			SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date date= new Date();
			String newFileName=TEACHER_IMG_DIR+format.format(date)+"."+file.getContentType().split("/")[1];
			Path path= Paths.get(newFileName);
			Files.write(path, file.getBytes());
			teacher.setAvatar(newFileName);
			_teacherService.update(teacher);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file.getBytes());
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Ocurrio un error: "+e.getMessage()),HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@RequestMapping(value="/teachers/{id_teacher}/picture",method=RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable("id_teacher") Long idTeacher) throws Exception{
		if(idTeacher==null || idTeacher<=0) {
			return new ResponseEntity(new CustomErrorType("idTeacher invalido"),HttpStatus.BAD_REQUEST);
		}
		Teacher teacher= _teacherService.findById(idTeacher);
		if(teacher==null) {
			return new ResponseEntity(new CustomErrorType("El profesor no existe"),HttpStatus.NOT_FOUND);
		}
		try {
			String filePath= teacher.getAvatar();
			Path path= Paths.get(filePath);
			byte[] bytes=Files.readAllBytes(path);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		}catch(Exception e) {
			e.printStackTrace();
			String filePath="images/teachers/not_found.png";
			Path path= Paths.get(filePath);
			byte[] bytes= Files.readAllBytes(path);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		}
	}
	
	@RequestMapping(value="/teachers/socialMedias",method=RequestMethod.PATCH,headers="Accept=application/json")
	public ResponseEntity<Teacher> addSocialMedia(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentBuilder){
		List<TeacherSocialMedia> savedTeacherSocialMedia= new ArrayList<TeacherSocialMedia>();
		if(teacher.getIdTeacher()==null || teacher.getIdTeacher()<=0 ) {
			return new ResponseEntity(new CustomErrorType("idTeacher invalido"),HttpStatus.BAD_REQUEST);
		}
		if(teacher.getTeacherSocialMedias()==null || teacher.getTeacherSocialMedias().size()<=0) {
			return new ResponseEntity(new CustomErrorType("No hay SocialMedias que agregar"),HttpStatus.BAD_REQUEST);
		}
		Teacher updatedTeacher= _teacherService.findById(teacher.getIdTeacher());
		if(updatedTeacher==null) {
			return new ResponseEntity(new CustomErrorType("El profesor no existe"),HttpStatus.NOT_FOUND);
		}
		Iterator iterator= teacher.getTeacherSocialMedias().iterator();
		
		while(iterator.hasNext()) {
			TeacherSocialMedia teacherSocialMedia= (TeacherSocialMedia)iterator.next();
			if(teacherSocialMedia.getNickname()==null || teacherSocialMedia.getNickname().equals("")) {
				return new ResponseEntity(new CustomErrorType("Registro sin nickname"),HttpStatus.BAD_REQUEST);
			}
			if(teacherSocialMedia.getSocialMedia()==null || teacherSocialMedia.getSocialMedia().getIdSocialMedia()<=0
					|| teacherSocialMedia.getSocialMedia().getIdSocialMedia()==null) {
				return new ResponseEntity(new CustomErrorType("SocialMedia invalido"),HttpStatus.BAD_REQUEST);
			}
			SocialMedia currentSocialMedia= _socialMediaService.findById(teacherSocialMedia.getSocialMedia().getIdSocialMedia());
			if(currentSocialMedia==null) {
				return new ResponseEntity(new CustomErrorType("SocialMedia no existe"),HttpStatus.NOT_FOUND);
			}
			
			teacherSocialMedia.setSocialMedia(currentSocialMedia);
			teacherSocialMedia.setTeacher(updatedTeacher);
			savedTeacherSocialMedia.add(teacherSocialMedia);
		}
		updatedTeacher.getTeacherSocialMedias().clear();
		if(updatedTeacher.getTeacherSocialMedias().isEmpty()) {
			updatedTeacher.getTeacherSocialMedias().addAll(savedTeacherSocialMedia);
			_teacherService.update(updatedTeacher);
		}
		
		return new ResponseEntity<Teacher>(updatedTeacher,HttpStatus.OK);
	}
}
