package com.platzi.platziprofesores.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platzi.platziprofesores.dao.SocialMediaDAO;
import com.platzi.platziprofesores.model.SocialMedia;
import com.platzi.platziprofesores.model.TeacherSocialMedia;

@Service("socialMediaService")
@Transactional
public class SocialMediaServiceImpl implements SocialMediaService{
	
	@Autowired
	private SocialMediaDAO _socialMediaDAO;
	@Override
	public void create(SocialMedia socialMedia) {
		// TODO Auto-generated method stub
		_socialMediaDAO.create(socialMedia);
	}

	@Override
	public List<SocialMedia> findAll() {
		// TODO Auto-generated method stub
		return _socialMediaDAO.findAll();
	}

	@Override
	public void update(SocialMedia socialMedia) {
		// TODO Auto-generated method stub
		_socialMediaDAO.update(socialMedia);
	}

	@Override
	public void delete(Long idSocialMedia) {
		// TODO Auto-generated method stub
		_socialMediaDAO.delete(idSocialMedia);
	}

	@Override
	public SocialMedia findByName(String name) {
		// TODO Auto-generated method stub
		return _socialMediaDAO.findByName(name);
	}

	@Override
	public SocialMedia findById(Long idSocialMedia) {
		// TODO Auto-generated method stub
		return _socialMediaDAO.findById(idSocialMedia);
	}

	@Override
	public TeacherSocialMedia findTeacherSocialMediaByIdAndName(Long idSocialMedia, String nickname) {
		// TODO Auto-generated method stub
		return _socialMediaDAO.findTeacherSocialMediaByIdAndName(idSocialMedia, nickname);
	}

}
