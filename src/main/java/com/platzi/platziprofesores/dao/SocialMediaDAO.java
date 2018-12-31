package com.platzi.platziprofesores.dao;

import java.util.List;

import com.platzi.platziprofesores.model.SocialMedia;
import com.platzi.platziprofesores.model.TeacherSocialMedia;

public interface SocialMediaDAO {
	public void create(SocialMedia socialMedia);
	public List<SocialMedia> findAll();
	public void update(SocialMedia socialMedia);
	public void delete(Long idSocialMedia);
	public SocialMedia findByName(String name);
	public SocialMedia findById(Long idSocialMedia);
	public TeacherSocialMedia findTeacherSocialMediaByIdAndName(Long idSocialMedia, String nickname);
}
