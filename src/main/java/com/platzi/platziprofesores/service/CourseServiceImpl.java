package com.platzi.platziprofesores.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platzi.platziprofesores.dao.CourseDAO;
import com.platzi.platziprofesores.model.Course;

@Service("courseService")
@Transactional
public class CourseServiceImpl implements CourseService{

	@Autowired
	private CourseDAO _courseDAO;
	@Override
	public void create(Course course) {
		// TODO Auto-generated method stub
		_courseDAO.create(course);
	}

	@Override
	public List<Course> findAll() {
		// TODO Auto-generated method stub
		return _courseDAO.findAll();
	}

	@Override
	public void update(Course course) {
		// TODO Auto-generated method stub
		_courseDAO.update(course);
	}

	@Override
	public void delete(Long idCourse) {
		// TODO Auto-generated method stub
		_courseDAO.delete(idCourse);
	}

	@Override
	public Course findById(Long idCourse) {
		// TODO Auto-generated method stub
		return _courseDAO.findById(idCourse);
	}

	@Override
	public Course findByName(String name) {
		// TODO Auto-generated method stub
		return _courseDAO.findByName(name);
	}

	@Override
	public List<Course> findByIdTeacher(Long idTeacher) {
		// TODO Auto-generated method stub
		return _courseDAO.findByIdTeacher(idTeacher);
	}

}
