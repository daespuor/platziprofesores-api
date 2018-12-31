package com.platzi.platziprofesores.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platzi.platziprofesores.dao.TeacherDAO;
import com.platzi.platziprofesores.model.Teacher;

@Service("teacherService")
@Transactional
public class TeacherServiceImpl implements TeacherService{
	
	@Autowired
	private TeacherDAO _teacherDAO;
	@Override
	public void create(Teacher teacher) {
		// TODO Auto-generated method stub
		_teacherDAO.create(teacher);
	}

	@Override
	public List<Teacher> findAll() {
		// TODO Auto-generated method stub
		return _teacherDAO.findAll();
	}

	@Override
	public void update(Teacher teacher) {
		// TODO Auto-generated method stub
		_teacherDAO.update(teacher);
	}

	@Override
	public void delete(Long idTeacher) {
		// TODO Auto-generated method stub
		_teacherDAO.delete(idTeacher);
	}

	@Override
	public Teacher findById(Long idTeacher) {
		// TODO Auto-generated method stub
		return _teacherDAO.findById(idTeacher);
	}

	@Override
	public Teacher findByName(String name) {
		// TODO Auto-generated method stub
		return _teacherDAO.findByName(name);
	}
	
	public void deleteTeacherSocialMediaByIdTeacher(Long idTeacher) {
		_teacherDAO.deleteTeacherSocialMediaByIdTeacher(idTeacher);
	}

}
