package com.platzi.platziprofesores.dao;

import java.util.List;


import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.platzi.platziprofesores.model.Course;

@Repository
@Transactional
public class CourseImplDAO extends AbstractSession implements CourseDAO{

	@Override
	public void create(Course course) {
		// TODO Auto-generated method stub
		getSession().persist(course);
	}

	@Override
	public List<Course> findAll() {
		// TODO Auto-generated method stub
		return getSession().createQuery("from Course").list();
	}

	@Override
	public void update(Course course) {
		// TODO Auto-generated method stub
		getSession().update(course);
	}

	@Override
	public void delete(Long idCourse) {
		// TODO Auto-generated method stub
		Course course= findById(idCourse);
		if(course != null) {
			getSession().delete(course);
		}
	}

	@Override
	public Course findById(Long idCourse) {
		// TODO Auto-generated method stub
		return getSession().get(Course.class, idCourse);
	}

	@Override
	public Course findByName(String name) {
		// TODO Auto-generated method stub
		return (Course)getSession().createQuery("from Course where name= :name")
				.setParameter("name", name).uniqueResult();
	}

	@Override
	public List<Course> findByIdTeacher(Long idTeacher) {
		// TODO Auto-generated method stub
		return (List<Course>)getSession().createQuery(
				"select c from Course c join c.teacher t where t.idTeacher= :idTeacher")
				.setParameter("idTeacher", idTeacher).list();
	}

}
