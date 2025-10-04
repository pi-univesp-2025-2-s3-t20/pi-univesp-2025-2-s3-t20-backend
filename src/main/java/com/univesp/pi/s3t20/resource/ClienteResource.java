package com.univesp.pi.s3t20.resource;

import com.univesp.pi.s3t20.dto.ClienteDTO;
import com.univesp.pi.s3t20.model.Cliente;
import com.univesp.pi.s3t20.service.ClienteService;
import com.univesp.pi.s3t20.service.MapperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteResource {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MapperService mapperService;

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
            content = @Content(schema = @Schema(implementation = ClienteDTO.class)))
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarTodos().stream()
                .map(mapperService::toClienteDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteDTO> buscarCliente(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(mapperService.toClienteDTO(cliente.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar cliente por código", description = "Retorna um cliente específico pelo seu código único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteDTO> buscarClientePorCodigo(
            @Parameter(description = "Código único do cliente", required = true, example = "CLI001")
            @PathVariable String codigo) {
        Optional<Cliente> cliente = clienteService.buscarPorCodigo(codigo);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(mapperService.toClienteDTO(cliente.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<ClienteDTO> criarCliente(
            @Parameter(description = "Dados do cliente a ser criado", required = true)
            @RequestBody ClienteDTO clienteDTO) {
        try {
            Cliente cliente = mapperService.toCliente(clienteDTO);
            Optional<Cliente> clienteCriado = clienteService.criar(cliente);
            if (clienteCriado.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toClienteDTO(clienteCriado.get()));
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<ClienteDTO> atualizarCliente(
            @Parameter(description = "ID do cliente a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados do cliente", required = true)
            @RequestBody ClienteDTO clienteDTO) {
        try {
            Cliente cliente = mapperService.toCliente(clienteDTO);
            Optional<Cliente> clienteAtualizado = clienteService.atualizar(id, cliente);
            if (clienteAtualizado.isPresent()) {
                return ResponseEntity.ok(mapperService.toClienteDTO(clienteAtualizado.get()));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente", description = "Remove um cliente do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Void> deletarCliente(
            @Parameter(description = "ID do cliente a ser deletado", required = true, example = "1")
            @PathVariable Long id) {
        boolean deletado = clienteService.deletar(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Contar clientes", description = "Retorna o número total de clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Contagem de clientes retornada com sucesso")
    public Long contar() {
        return clienteService.contar();
    }
}