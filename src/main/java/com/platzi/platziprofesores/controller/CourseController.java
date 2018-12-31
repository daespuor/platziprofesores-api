package com.platzi.platziprofesores.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.platzi.platziprofesores.model.Course;
import com.platzi.platziprofesores.service.CourseService;
import com.platzi.platziprofesores.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class CourseController {

	@Autowired
	private CourseService _courseService;
	
	@RequestMapping(value="/courses",method=RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value="id_teacher",required=false) Long idTeacher,
			@RequestParam(value="name",required=false) String name){
		
		List<Course> courses= new ArrayList<Course>();
		
		if(idTeacher!=null && idTeacher>0) {
			courses= _courseService.findByIdTeacher(idTeacher);
			if(courses==null) {
				return new ResponseEntity(new CustomErrorType("No existen cursos para ese profesor"),HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Course>>(courses,HttpStatus.OK);
		}

		if(name!=null && !"".equals(name)) {
			Course course=_courseService.findByName(name);
			if(course==null) {
				return new ResponseEntity(new CustomErrorType("No existe un curso con ese nombre"),HttpStatus.NOT_FOUND);
			}
			courses.add(course);
			return new ResponseEntity<List<Course>>(courses,HttpStatus.OK);
		}
		
		courses= _courseService.findAll();
		if(courses==null) {
			return new ResponseEntity(new CustomErrorType("No existen cursos"),HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Course>>(courses,HttpStatus.OK);
	}
	
	@RequestMapping(value="/courses/{id}", method=RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<Course> getCourse(@PathVariable("id") Long idCourse){
		if(idCourse==null || idCourse<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		Course course= _courseService.findById(idCourse);
		if(course==null) {
			return new ResponseEntity(new CustomErrorType("El curso no existe"),HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Course>(course,HttpStatus.OK);
	}
	
	@RequestMapping(value="/courses",method=RequestMethod.POST,headers="Accept=application/json")
	public ResponseEntity<?> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentsBuilder){
		if(course.getName().equals("") || course.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Nombre de curso invalido"),HttpStatus.BAD_REQUEST);
		}
		if(_courseService.findByName(course.getName())!=null) {
			return new ResponseEntity(new CustomErrorType("El curso ya existe"),HttpStatus.BAD_REQUEST);
		}
		_courseService.create(course);
		Course currentCourse=_courseService.findByName(course.getName());
		HttpHeaders headers= new HttpHeaders();
		headers.setLocation(uriComponentsBuilder.path("/courses/{id}").buildAndExpand(currentCourse.getId()).toUri());
		return new ResponseEntity(headers,HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/courses/{id}",method=RequestMethod.PATCH,headers="Accept=application/json")
	public ResponseEntity<Course> updateCourse(@RequestBody Course course,@PathVariable("id") Long idCourse){
		if(idCourse==null || idCourse<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		Course currentCourse= _courseService.findById(idCourse);
		if(currentCourse==null) {
			return new ResponseEntity(new CustomErrorType("El curso no existe"),HttpStatus.NOT_FOUND);
		}
		currentCourse.setName(course.getName());
		currentCourse.setProject(course.getProject());
		currentCourse.setThemes(course.getThemes());
		
		return new ResponseEntity<Course>(currentCourse,HttpStatus.OK);
	}
	
	@RequestMapping(value="/courses/{id}",method=RequestMethod.DELETE,headers="Accept=application/json")
	public ResponseEntity<Course> deleteCourse(@PathVariable("id") Long idCourse){
		if(idCourse==null || idCourse<=0) {
			return new ResponseEntity(new CustomErrorType("identificador invalido"),HttpStatus.BAD_REQUEST);
		}
		Course currentCourse= _courseService.findById(idCourse);
		if(currentCourse==null) {
			return new ResponseEntity(new CustomErrorType("El curso no existe"),HttpStatus.NOT_FOUND);
		}
		_courseService.delete(idCourse);
		return new ResponseEntity<Course>(currentCourse,HttpStatus.OK);
	}
}
