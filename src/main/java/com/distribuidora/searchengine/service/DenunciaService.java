package com.distribuidora.searchengine.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.distribuidora.searchengine.dto.Denunciagrilla;
import com.distribuidora.searchengine.dto.Resugrilla;
import com.distribuidora.searchengine.repository.DenunciaRepository;

@Service
public class DenunciaService {
	@Autowired
	DenunciaRepository denunciaRepository;
	
	public List<Denunciagrilla> lista(){
		return denunciaRepository.llamaproce();
	}
	
	public List<Resugrilla> listanew(){
		return denunciaRepository.llamaprocedos();
	}
}
