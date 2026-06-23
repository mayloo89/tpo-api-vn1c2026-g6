package com.uade.tpo.e_commerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce.dto.PedidoItemRequestDTO;
import com.uade.tpo.e_commerce.dto.PedidoItemResponseDTO;
import com.uade.tpo.e_commerce.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce.exception.ProductoNotFoundException;
import com.uade.tpo.e_commerce.exception.UsuarioNotFoundException;
import com.uade.tpo.e_commerce.model.Pedido;
import com.uade.tpo.e_commerce.model.PedidoEstado;
import com.uade.tpo.e_commerce.model.PedidoItem;
import com.uade.tpo.e_commerce.model.Producto;
import com.uade.tpo.e_commerce.model.Usuario;
import com.uade.tpo.e_commerce.repository.PedidoRepository;
import com.uade.tpo.e_commerce.repository.ProductoRepository;
import com.uade.tpo.e_commerce.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarritoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public CarritoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    public PedidoResponseDTO getCarrito(Authentication authentication) {
        Pedido carrito = getOrCreateCarrito(authentication);
        return toResponse(carrito);
    }

    public PedidoResponseDTO addItem(Authentication authentication, PedidoItemRequestDTO request) {
        if (request.getProductoId() == null) {
            throw new IllegalArgumentException("El productoId es obligatorio");
        }
        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Pedido carrito = getOrCreateCarrito(authentication);
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + request.getProductoId()));

        PedidoItem item = carrito.getItems().stream()
                .filter(current -> current.getProducto().getId().equals(producto.getId()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = new PedidoItem();
            item.setPedido(carrito);
            item.setProducto(producto);
            item.setCantidad(request.getCantidad());
            item.setPrecioUnitario(producto.getPrecio());
            carrito.getItems().add(item);
        } else {
            item.setCantidad(item.getCantidad() + request.getCantidad());
        }

        recomputeTotal(carrito);
        return toResponse(pedidoRepository.save(carrito));
    }

    public PedidoResponseDTO updateItem(Authentication authentication, Long productoId, Integer cantidad) {
        if (cantidad == null) {
            throw new IllegalArgumentException("La cantidad es obligatoria");
        }

        Pedido carrito = getOrCreateCarrito(authentication);
        if (cantidad <= 0) {
            carrito.getItems().removeIf(item -> item.getProducto().getId().equals(productoId));
        } else {
            PedidoItem item = carrito.getItems().stream()
                    .filter(current -> current.getProducto().getId().equals(productoId))
                    .findFirst()
                    .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado en el carrito con id: " + productoId));
            item.setCantidad(cantidad);
        }

        recomputeTotal(carrito);
        return toResponse(pedidoRepository.save(carrito));
    }

    public PedidoResponseDTO removeItem(Authentication authentication, Long productoId) {
        Pedido carrito = getOrCreateCarrito(authentication);
        carrito.getItems().removeIf(item -> item.getProducto().getId().equals(productoId));
        recomputeTotal(carrito);
        return toResponse(pedidoRepository.save(carrito));
    }

    public void clear(Authentication authentication) {
        Pedido carrito = getOrCreateCarrito(authentication);
        carrito.getItems().clear();
        carrito.setTotal(0.0);
        pedidoRepository.save(carrito);
    }

    private Pedido getOrCreateCarrito(Authentication authentication) {
        Usuario usuario = getUsuarioActual(authentication);

        return pedidoRepository.findByUsuarioIdAndEstado(usuario.getId(), PedidoEstado.CARRITO)
                .orElseGet(() -> {
                    Pedido carrito = new Pedido();
                    carrito.setUsuario(usuario);
                    carrito.setEstado(PedidoEstado.CARRITO);
                    carrito.setTotal(0.0);
                    carrito.setItems(new ArrayList<>());
                    return pedidoRepository.save(carrito);
                });
    }

    private Usuario getUsuarioActual(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new UsuarioNotFoundException("Usuario no autenticado");
        }

        return usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "Usuario no encontrado con email: " + authentication.getName()));
    }

    private void recomputeTotal(Pedido carrito) {
        double total = carrito.getItems().stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
        carrito.setTotal(total);
    }

    private PedidoResponseDTO toResponse(Pedido pedido) {
        List<PedidoItemResponseDTO> items = pedido.getItems().stream()
                .map(item -> new PedidoItemResponseDTO(
                        item.getProducto().getId(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getProducto().getImagenUrl()))
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getUsuario().getId(),
                pedido.getEstado() != null ? pedido.getEstado().name() : PedidoEstado.CARRITO.name(),
                pedido.getTotal(),
                items);
    }
}