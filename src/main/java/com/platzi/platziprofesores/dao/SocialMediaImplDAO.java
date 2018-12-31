package com.platzi.platziprofesores.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.platzi.platziprofesores.model.SocialMedia;
import com.platzi.platziprofesores.model.TeacherSocialMedia;

@Repository
@Transactional
public class SocialMediaImplDAO extends AbstractSession implements SocialMediaDAO {
	@Override
	public void create(SocialMedia socialMedia) {
		// TODO Auto-generated method stub
		getSession().persist(socialMedia);
	}

	@Override
	public List<SocialMedia> findAll() {
		// TODO Auto-generated method stub
		return getSession().createQuery("from SocialMedia").list();
	}

	@Override
	public void update(SocialMedia socialMedia) {
		// TODO Auto-generated method stub
		getSession().update(socialMedia);
	}

	@Override
	public void delete(Long idSocialMedia) {
		// TODO Auto-generated method stub
		SocialMedia socialMedia= findById(idSocialMedia);
		if(socialMedia != null) {
			getSession().delete(socialMedia);
		}
	}

	@Override
	public SocialMedia findById(Long idSocialMedia) {
		// TODO Auto-generated method stub
		return getSession().get(SocialMedia.class, idSocialMedia);
	}

	@Override
	public TeacherSocialMedia findTeacherSocialMediaByIdAndName(Long idSocialMedia, String nickname) {
		// TODO Auto-generated method stub
		return (TeacherSocialMedia)getSession().createQuery(
				"select tsm from TeacherSocialMedia tsm join tsm.socialMedia sm"
				+ "where sm.idSocialMedia= :idSocialMedia and tsm.nickname= :nickname ")
				.setParameter("idSocialMedia", idSocialMedia)
				.setParameter("nickname", nickname)
				.uniqueResult();
	}

	@Override
	public SocialMedia findByName(String name) {
		// TODO Auto-generated method stub
		return (SocialMedia)getSession().createQuery("from SocialMedia where name= :name")
				.setParameter("name", name)
				.uniqueResult();
	}

}
