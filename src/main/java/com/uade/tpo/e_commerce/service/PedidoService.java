package com.uade.tpo.e_commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.uade.tpo.e_commerce.dto.PedidoItemResponseDTO;
import com.uade.tpo.e_commerce.dto.PedidoRequestDTO;
import com.uade.tpo.e_commerce.dto.PedidoResponseDTO;
import com.uade.tpo.e_commerce.exception.PedidoNotFoundException;
import com.uade.tpo.e_commerce.exception.ProductoNotFoundException;
import com.uade.tpo.e_commerce.exception.UsuarioNotFoundException;
import com.uade.tpo.e_commerce.model.Pedido;
import com.uade.tpo.e_commerce.model.PedidoEstado;
import com.uade.tpo.e_commerce.model.PedidoItem;
import com.uade.tpo.e_commerce.model.Producto;
import com.uade.tpo.e_commerce.model.Usuario;
import com.uade.tpo.e_commerce.repository.ProductoRepository;
import com.uade.tpo.e_commerce.repository.PedidoRepository;
import com.uade.tpo.e_commerce.repository.UsuarioRepository;

@Service
@Transactional

public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Devuelve la lista completa de pedidos.
     */
    public List<PedidoResponseDTO> getAllPedidos() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Busca un pedido por su id.
     */
    public PedidoResponseDTO getPedidoById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del pedido no puede ser nulo");
        }
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado con id: " + id));
        return toResponse(pedido);
    }

    /**
     * Crea un nuevo pedido.
     */
    public PedidoResponseDTO addPedido(PedidoRequestDTO pedidoDTO) {
        if (pedidoDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuarioId es obligatorio");
        }
        if (pedidoDTO.getItems() == null || pedidoDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un item");
        }

        Usuario usuario = usuarioRepository.findById(pedidoDTO.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con id: " + pedidoDTO.getUsuarioId()));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEstado(PedidoEstado.CONFIRMADO);

        double total = 0.0;
        List<PedidoItem> items = pedidoDTO.getItems().stream().map(itemDTO -> {
            if (itemDTO.getCantidad() == null || itemDTO.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }

            Producto producto = productoRepository.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + itemDTO.getProductoId()));

            PedidoItem item = new PedidoItem();
            item.setPedido(pedido);
            item.setProducto(producto);
            item.setCantidad(itemDTO.getCantidad());
            item.setPrecioUnitario(producto.getPrecio());
            return item;
        }).collect(Collectors.toList());

        for (PedidoItem item : items) {
            total += item.getPrecioUnitario() * item.getCantidad();
        }

        pedido.setItems(items);
        pedido.setTotal(total);

        Pedido savedPedido = pedidoRepository.save(pedido);
        return toResponse(savedPedido);
    }

    public PedidoResponseDTO updatePedido(Long id, PedidoRequestDTO pedidoDTO) {
        Pedido existingPedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado con id: " + id));

        if (pedidoDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(pedidoDTO.getUsuarioId())
                    .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con id: " + pedidoDTO.getUsuarioId()));
            existingPedido.setUsuario(usuario);
        }

        if (pedidoDTO.getItems() != null) {
            existingPedido.getItems().clear();

            double total = 0.0;
            List<PedidoItem> items = pedidoDTO.getItems().stream().map(itemDTO -> {
                if (itemDTO.getCantidad() == null || itemDTO.getCantidad() <= 0) {
                    throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
                }

                Producto producto = productoRepository.findById(itemDTO.getProductoId())
                        .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + itemDTO.getProductoId()));

                PedidoItem item = new PedidoItem();
                item.setPedido(existingPedido);
                item.setProducto(producto);
                item.setCantidad(itemDTO.getCantidad());
                item.setPrecioUnitario(producto.getPrecio());
                return item;
            }).collect(Collectors.toList());

            for (PedidoItem item : items) {
                total += item.getPrecioUnitario() * item.getCantidad();
            }

            existingPedido.getItems().addAll(items);
            existingPedido.setTotal(total);
        }

        Pedido updatedPedido = pedidoRepository.save(existingPedido);
        return toResponse(updatedPedido);
    }

    /**
     * Elimina un pedido por su id.
     */
    public void deletePedido(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id del pedido no puede ser nulo");
        }
        if (!pedidoRepository.existsById(id)) {
            throw new PedidoNotFoundException("Pedido no encontrado con id: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    private PedidoResponseDTO toResponse(Pedido pedido) {
        List<PedidoItemResponseDTO> items = pedido.getItems().stream()
                .map(item -> new PedidoItemResponseDTO(
                        item.getProducto().getId(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getProducto().getImagenUrl()))
                .collect(Collectors.toList());

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getUsuario().getId(),
                pedido.getEstado() != null ? pedido.getEstado().name() : PedidoEstado.CONFIRMADO.name(),
                pedido.getTotal(),
                items);
    }
}
