package com.example.lojinha.controller;

import com.example.lojinha.model.Checkout;
import com.example.lojinha.model.Carrinho;
import com.example.lojinha.repository.CheckoutRepository;
import com.example.lojinha.repository.CarrinhoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutRepository checkoutRepository;
    private final CarrinhoRepository carrinhoRepository;

    public CheckoutController(CheckoutRepository checkoutRepository, CarrinhoRepository carrinhoRepository) {
        this.checkoutRepository = checkoutRepository;
        this.carrinhoRepository = carrinhoRepository;
    }

    // POST - Finalizar a compra
    @PostMapping
    public ResponseEntity<Checkout> criarCheckout(@RequestParam Long carrinhoId, @RequestParam String metodoPagamento) {
        return carrinhoRepository.findById(carrinhoId).map(carrinho -> {
            Checkout checkout = new Checkout();
            checkout.setCarrinho(carrinho);
            checkout.setMetodoPagamento(metodoPagamento);
            checkout.setStatus("PENDENTE");
            checkoutRepository.save(checkout);
            return ResponseEntity.ok(checkout);
        }).orElse(ResponseEntity.notFound().build());
    }

    // GET - Obter detalhes do checkout
    @GetMapping("/{id}")
    public ResponseEntity<Checkout> obterCheckoutPorId(@PathVariable Long id) {
        return checkoutRepository.findById(id)
                .map(checkout -> ResponseEntity.ok(checkout))
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT - Atualizar status do checkout
    @PutMapping("/{id}/status")
    public ResponseEntity<Checkout> atualizarStatusDoCheckout(@PathVariable Long id, @RequestParam String status) {
        return checkoutRepository.findById(id)
                .map(checkout -> {
                    checkout.setStatus(status);
                    checkoutRepository.save(checkout);
                    return ResponseEntity.ok(checkout);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
