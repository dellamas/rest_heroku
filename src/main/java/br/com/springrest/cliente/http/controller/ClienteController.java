package br.com.springrest.cliente.http.controller;

import br.com.springrest.cliente.entity.Cliente;
import br.com.springrest.cliente.service.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/cliente")

public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvar(Cliente cliente){
        return clienteService.salvar(cliente);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> ListaCliente(){
        return clienteService.listaCliente();
    }
    @GetMapping("/id")
    public Cliente buscarClientePorId(@PathVariable("id")long id){
        return clienteService.buscarPorId(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado"));
    }

    @DeleteMapping("/{ID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerCliente(@PathVariable("id") Long id){
        clienteService.buscarPorId(id)
            .map(cliente -> {
                clienteService.removerPorId(cliente.getId());
                return Void.TYPE;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado"));
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarCliente(@PathVariable("id") long id, @RequestBody Cliente cliente){
        clienteService.buscarPorId(id)
                .map(clienteBase ->{
                    modelMapper.map(cliente, clienteBase);
                    clienteService.salvar(clienteBase);
                    return Void.TYPE;

                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado"));
    }
}
