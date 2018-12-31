package com.platzi.platziprofesores.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;


@Entity
@Table(name="teacher")
public class Teacher implements Serializable {
	@Id
	@Column(name="id_teacher")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idTeacher;
	@Column(name="name")
	private String name;
	@Column(name="avatar")
	private String avatar;
	
	@OneToMany
	@JoinColumn(name="id_teacher")
	private Set<Course> courses;
	@OneToMany(orphanRemoval=true,cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="id_teacher")
	private Set<TeacherSocialMedia> teacherSocialMedias;
	
	public Teacher() {
		super();
	}
	
	public Teacher(String name,String avatar) {
		this.name=name;
		this.avatar=avatar;
	}
	
	public Long getIdTeacher() {
		return idTeacher;
	}
	public void setIdTeacher(Long idTeacher) {
		this.idTeacher = idTeacher;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	public Set<TeacherSocialMedia> getTeacherSocialMedias() {
		return teacherSocialMedias;
	}

	public void setTeacherSocialMedias(Set<TeacherSocialMedia> teacherSocialMedia) {
		this.teacherSocialMedias = teacherSocialMedia;
	}
	
	
}
