package com.distribuidora.searchengine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.distribuidora.searchengine.dto.Denunciagrilla;
import com.distribuidora.searchengine.dto.Resugrilla;

@Repository
public interface DenunciaRepository extends JpaRepository<Denunciagrilla, Integer> {

	@Query(value="{call procedureprueba()}", nativeQuery = true)
	List<Denunciagrilla> llamaproce();

	@Query(value="{call procedureprueba()}", nativeQuery = true)
	List<Resugrilla> llamaprocedos();

	
}
