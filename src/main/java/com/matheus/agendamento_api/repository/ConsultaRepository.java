package com.matheus.agendamento_api.repository;

import com.matheus.agendamento_api.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}