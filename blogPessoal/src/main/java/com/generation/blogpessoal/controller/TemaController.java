package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

	@Autowired
	private TemaRepository temaRepository;
	
	@Autowired // da acesso ao meu controller a responsabilidade de criar e instanciar objetos
	private PostagemRepository postagemRepository;

	@GetMapping
	public ResponseEntity<List<Tema>> getAll() {
		return ResponseEntity.ok(temaRepository.findAll());
	}
	
	@PostMapping
	public ResponseEntity<Tema>postTema(@Valid @RequestBody Tema tema){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(temaRepository.save(tema));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Tema> getById(@PathVariable Long id){
		return temaRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	 @GetMapping("/descricao/{descricao}") 
	    public ResponseEntity<List<Tema>> getByDescricao(@PathVariable String descricao) { //o List nÃ£o gera erro igual findById pois ele n gera null, gera uma lista vazia                     
	        return ResponseEntity.ok(temaRepository.findAllByDescricaoContainingIgnoreCase(descricao));
	    } //select * from tb_postagens where titulo like "%titulo%";
	 
	 @PutMapping
	    public ResponseEntity<Tema> putTema (@Valid @RequestBody Tema tema) {
	        //return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
	        return temaRepository.findById(tema.getId()) //procura pelo id 
	                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(temaRepository.save(tema))) //realiza se resposta n for nulla
	                .orElse(ResponseEntity.notFound().build());
	 }
	 
	 @DeleteMapping("/{id}")
	    	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	    	public void deleteTema(@PathVariable Long id) { 
	    		Optional<Tema> tema = temaRepository.findById(id);
	    		if (tema.isEmpty())
	    				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	    		
	    		temaRepository.deleteById(id);
	 }
}
