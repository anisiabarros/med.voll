package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class medicoController {
    @Autowired // injeção de dependencia
    private MedicoRepository repository;

    @PostMapping
    @Transactional// quando esta cadastrando deve ser o codigo 201 devolvido e esse codigo tem algumas regras:
    // devolver no corpo da resposta os dados do novo recurso criado e precisa devolver o cabeçalho location do protocolo http
    //UriComponentsBuilder é uma classe do spring que consegue localizar o endereço da api automaticamente
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) { // anotação valid para que o spring valid os dados do dto
        var medico = new Medico(dados);
        repository.save(medico);
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        //return ResponseEntity.created(uri).body(dto); esse é o padrao, a uri é o endereco da api
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

    }
    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
       var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
       return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
    @DeleteMapping("/{id}")//codigo 204 na exclusao
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.excluir();
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")//codigo 204 na exclusao
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

}