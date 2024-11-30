package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("paciente")
public class pacienteController {
    @Autowired
    private PacienteRepository repository;
    @Transactional
    @PostMapping
    public void cadastrar (@RequestBody @Valid DadosCadastroPaciente dados){
        repository.save(new Paciente(dados));
    }
    @GetMapping
    public Page<DadosListagemPaciente> listar(@PageableDefault (size = 10, sort = {"nome"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
    }
    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados){
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);
    }

    @DeleteMapping
    @Transactional
    public void excluir(@PathVariable Long id){
        var paciente = repository.getReferenceById(id);
        paciente.excluir();
    }

}