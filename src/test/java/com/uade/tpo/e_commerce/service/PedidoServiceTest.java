package com.uade.tpo.e_commerce.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uade.tpo.e_commerce.dto.PedidoItemRequestDTO;
import com.uade.tpo.e_commerce.dto.PedidoRequestDTO;
import com.uade.tpo.e_commerce.exception.ProductoNotFoundException;
import com.uade.tpo.e_commerce.exception.UsuarioNotFoundException;
import com.uade.tpo.e_commerce.model.Pedido;
import com.uade.tpo.e_commerce.model.Producto;
import com.uade.tpo.e_commerce.model.Usuario;
import com.uade.tpo.e_commerce.repository.PedidoRepository;
import com.uade.tpo.e_commerce.repository.ProductoRepository;
import com.uade.tpo.e_commerce.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void addPedido_lanzaSiUsuarioNoExiste() {
        PedidoRequestDTO request = new PedidoRequestDTO(1L, List.of(new PedidoItemRequestDTO(1L, 1)));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.addPedido(request))
                .isInstanceOf(UsuarioNotFoundException.class);
    }

    @Test
    void addPedido_lanzaSiProductoNoExiste() {
        PedidoRequestDTO request = new PedidoRequestDTO(1L, List.of(new PedidoItemRequestDTO(1L, 1)));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(productoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pedidoService.addPedido(request))
                .isInstanceOf(ProductoNotFoundException.class);
    }

    @Test
    void addPedido_calculaTotal() {
        PedidoRequestDTO request = new PedidoRequestDTO(1L, List.of(new PedidoItemRequestDTO(1L, 2)));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(new Usuario()));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(Producto.builder().id(1L).nombre("Producto").precio(10.0).stock(10).build()));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = pedidoService.addPedido(request);

        assertThat(response.getTotal()).isEqualTo(20.0);
        assertThat(response.getItems()).hasSize(1);
    }
}
