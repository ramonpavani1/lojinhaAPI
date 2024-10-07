package com.example.lojinha.controller;

import com.example.lojinha.model.Carrinho;
import com.example.lojinha.model.ItemCarrinho;
import com.example.lojinha.model.Produto;
import com.example.lojinha.repository.CarrinhoRepository;
import com.example.lojinha.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;

    public CarrinhoController(CarrinhoRepository carrinhoRepository, ProdutoRepository produtoRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.produtoRepository = produtoRepository;
    }

    // GET - Obter o carrinho
    @GetMapping("/{id}")
    public ResponseEntity<Carrinho> obterCarrinhoPorId(@PathVariable Long id) {
        return carrinhoRepository.findById(id)
                .map(carrinho -> ResponseEntity.ok(carrinho))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Adicionar um item ao carrinho
    @PostMapping("/{carrinhoId}/itens")
    public ResponseEntity<Carrinho> adicionarItemAoCarrinho(@PathVariable Long carrinhoId, @RequestParam Long produtoId, @RequestParam Integer quantidade) {
        Optional<Produto> produto = produtoRepository.findById(produtoId);
        return (ResponseEntity<Carrinho>) carrinhoRepository.findById(carrinhoId).map(carrinho -> {
            if (produto.isPresent()) {
                ItemCarrinho item = new ItemCarrinho();
                item.setProduto(produto.get());
                item.setQuantidade(quantidade);
                carrinho.getItens().add(item);
                carrinho.setPrecoTotal(carrinho.getPrecoTotal() + produto.get().getPreco() * quantidade);
                carrinhoRepository.save(carrinho);
                return ResponseEntity.ok(carrinho);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Remover um item do carrinho
    @DeleteMapping("/{carrinhoId}/itens/{itemId}")
    public ResponseEntity<Carrinho> removerItemDoCarrinho(@PathVariable Long carrinhoId, @PathVariable Long itemId) {
        return carrinhoRepository.findById(carrinhoId).map(carrinho -> {
            carrinho.getItens().removeIf(item -> item.getId().equals(itemId));
            carrinhoRepository.save(carrinho);
            return ResponseEntity.ok(carrinho);
        }).orElse(ResponseEntity.notFound().build());
    }
}
