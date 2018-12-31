package com.platzi.platziprofesores.dao;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.platzi.platziprofesores.model.Teacher;
import com.platzi.platziprofesores.model.TeacherSocialMedia;


@Repository
@Transactional
public class TeacherImplDAO extends AbstractSession  implements TeacherDAO{
	
	public TeacherImplDAO() {
		
	}
	public void create(Teacher teacher) {
		getSession().persist(teacher);
	};
	public List<Teacher> findAll(){
		List<Teacher> teachers= getSession().createQuery("from Teacher").list();
		for(Teacher t:teachers) {
			//Hibernate.initialize(t.getTeacherSocialMedias());
			Hibernate.initialize(t.getCourses());
		}
		return teachers;
	};
	public void update(Teacher teacher) {
		getSession().update(teacher);
	};
	public void delete(Long idTeacher) {
		Teacher teacher= findById(idTeacher);
		if(teacher != null) {
			Iterator<TeacherSocialMedia> iterator=teacher.getTeacherSocialMedias().iterator();
			while(iterator.hasNext()) {
				TeacherSocialMedia teacherSocialMedia= (TeacherSocialMedia)iterator.next();
				iterator.remove();
				getSession().delete(teacherSocialMedia);
			}
			teacher.getTeacherSocialMedias().clear();
			getSession().delete(teacher);
		}
	};
	public Teacher findById(Long idTeacher) {
		Teacher teacher= getSession().get(Teacher.class, idTeacher);
		//Hibernate.initialize(teacher.getTeacherSocialMedias());
		Hibernate.initialize(teacher.getCourses());
		return teacher;
	}
	
	public void deleteTeacherSocialMediaByIdTeacher(Long idTeacher) {
		Teacher teacher= findById(idTeacher);
		Iterator iterator= teacher.getTeacherSocialMedias().iterator();
		while(iterator.hasNext()) {
			TeacherSocialMedia teacherSocialMedia= (TeacherSocialMedia)iterator.next();
			getSession().delete(teacherSocialMedia);
		}
	}

	public Teacher findByName(String name) {
		// TODO Auto-generated method stub
		return (Teacher)getSession().createQuery("from Teacher where name= :name")
				.setParameter("name", name).uniqueResult();
	};
}
