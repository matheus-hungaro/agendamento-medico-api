package com.matheus.agendamento_api.repository;

import com.matheus.agendamento_api.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    // Método customizado do Spring Data: busca apenas os médicos onde ativo = true
    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    @Query("select m.ativo from Medico m where m.id = :id")
Boolean findAtivoById(Long id);
}
