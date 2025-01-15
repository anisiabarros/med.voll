package med.voll.api.domain.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(
        long idMedico,
        @NotNull
        long idPaciente,
        @NotNull @Future
        LocalDateTime data) {
}
