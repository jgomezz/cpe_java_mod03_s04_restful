package pe.edu.tecsup.restful.services;


import pe.edu.tecsup.restful.entities.Producto;

import java.util.List;

public interface ProductoService {

    List<Producto> findAll();

    Producto findById(Long id);

    void save(Producto producto);

    void deleteById(Long id);
}
