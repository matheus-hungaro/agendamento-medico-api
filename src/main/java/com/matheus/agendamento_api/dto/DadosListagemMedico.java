package com.matheus.agendamento_api.dto;

import com.matheus.agendamento_api.model.Especialidade;
import com.matheus.agendamento_api.model.Medico;

// DTO seguro responsável apenas por transportar os dados públicos da listagem.
public record DadosListagemMedico(
    Long id,
    String nome,
    String crm,
    Especialidade especialidade
) {

    // Construtor utilitário para converter uma Entidade Medico direto para este DTO.
    public DadosListagemMedico(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getCrm(), medico.getEspecialidade());
    }
}