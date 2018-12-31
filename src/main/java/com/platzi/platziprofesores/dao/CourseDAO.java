package com.platzi.platziprofesores.dao;

import java.util.List;

import com.platzi.platziprofesores.model.Course;

public interface CourseDAO {
	public void create(Course course);
	public List<Course> findAll();
	public void update(Course course);
	public void delete(Long idCourse);
	public Course findById(Long idCourse);
	public Course findByName(String name);
	public List<Course> findByIdTeacher(Long idTeacher);
}
