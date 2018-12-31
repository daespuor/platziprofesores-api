package com.platzi.platziprofesores.service;

import java.util.List;

import com.platzi.platziprofesores.model.Teacher;

public interface TeacherService {
	public void create(Teacher teacher);
	public List<Teacher> findAll();
	public void update(Teacher teacher);
	public void delete(Long idTeacher);
	public Teacher findById(Long idTeacher);
	public Teacher findByName(String name);
	public void deleteTeacherSocialMediaByIdTeacher(Long idTeacher);
}
