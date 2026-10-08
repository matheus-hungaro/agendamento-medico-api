package com.matheus.agendamento_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosEndereco(
        @NotBlank(message = "Logradouro é obrigatório")
        @Schema(description = "Rua, Avenida ou Alameda", example = "Rua das Flores")
        String logradouro,

        @NotBlank(message = "Bairro é obrigatório")
        @Schema(description = "Bairro", example = "Centro")
        String bairro,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{8}", message = "Formato do CEP é inválido. Digite apenas 8 números")
        @Schema(description = "CEP com 8 dígitos (apenas números)", example = "01001000")
        String cep,

        @NotBlank(message = "Cidade é obrigatória")
        @Schema(description = "Cidade", example = "São Paulo")
        String cidade,

        @NotBlank(message = "UF é obrigatória")
        @Schema(description = "Estado (UF em 2 letras)", example = "SP")
        String uf,

        @Schema(description = "Complemento (opcional)", example = "Apto 101")
        String complemento,

        @Schema(description = "Número da residência (opcional)", example = "123")
        String numero
) {}
