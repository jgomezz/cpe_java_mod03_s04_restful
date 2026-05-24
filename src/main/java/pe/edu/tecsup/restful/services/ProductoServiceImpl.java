package pe.edu.tecsup.restful.services;


import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.restful.entities.Producto;
import pe.edu.tecsup.restful.repositories.ProductoRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Producto findById(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new
                EntityNotFoundException("No existe registro"));
    }
    @Override
    public void save(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }
}