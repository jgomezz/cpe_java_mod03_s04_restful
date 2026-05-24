package pe.edu.tecsup.restful.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.tecsup.restful.entities.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
