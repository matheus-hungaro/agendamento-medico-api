package com.matheus.agendamento_api.dto;

import com.matheus.agendamento_api.model.Especialidade;
import com.matheus.agendamento_api.model.Medico;

public record DadosDetalhamentoMedico(
    Long id,
    String nome,
    String email,
    String crm,
    Especialidade especialidade,
    Boolean ativo
) {
    public DadosDetalhamentoMedico(Medico medico) {
        this(
            medico.getId(),
            medico.getNome(),
            medico.getEmail(),
            medico.getCrm(),
            medico.getEspecialidade(),
            medico.getAtivo()
        );
    }
}