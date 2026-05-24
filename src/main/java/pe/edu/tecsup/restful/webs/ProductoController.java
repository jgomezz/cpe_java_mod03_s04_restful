package pe.edu.tecsup.restful.webs;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.tecsup.restful.entities.Producto;
import pe.edu.tecsup.restful.services.ProductoService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
@RestController
public class ProductoController {


    @Value("${app.storage.path}")
    private String STORAGEPATH;

    private final ProductoService  productoService;


    @GetMapping("/productos")
    public List<Producto> findAll() {
        return productoService.findAll();
    }

    /**
     * Método para obtener la imagen del producto
     * @param filename  Nombre del archivo de la imagen
     * @return          Recurso de la imagen
     * @throws Exception
     */
    @GetMapping("/productos/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws
            Exception {
        log.info("call images: {}", filename);
        Path path = Paths.get(STORAGEPATH).resolve(filename);
        log.info("Path: {}", path);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build(); //
        }

        Resource resource = new UrlResource(path.toUri());

        log.info("Resource: {}", resource);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename='" + resource.getFilename() + "'")
                .header(HttpHeaders.CONTENT_TYPE,
                        Files.probeContentType(Paths.get(STORAGEPATH).resolve(filename)))
                .header(HttpHeaders.CONTENT_LENGTH,
                        String.valueOf(resource.contentLength())).body(resource);
    }


    /**
     * CREATE --> POST
     * @param imagen
     * @param nombre
     * @param precio
     * @param detalles
     * @return
     * @throws Exception
     */
    @PostMapping("/productos")
    public Producto crear(@RequestParam(name = "imagen", required = false) MultipartFile imagen,
                          @RequestParam("nombre") String nombre,
                          @RequestParam("precio") Double precio,
                          @RequestParam("detalles") String detalles) throws Exception {

        log.info("crear producto: {}, precio: {}", nombre, precio);

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setDetalles(detalles);
        producto.setEstado("1"); // Activo el producto

        if (imagen != null && !imagen.isEmpty()) {
            String filename = imagen.getOriginalFilename();
            producto.setImagen(filename);
            if (Files.notExists(Paths.get(STORAGEPATH))) {
                Files.createDirectories(Paths.get(STORAGEPATH));
            }
            Files.copy(imagen.getInputStream(),
                    Paths.get(STORAGEPATH).resolve(filename));
        }
        productoService.save(producto);
        return producto;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/productos/id/{id}")
    public Producto obtener(@PathVariable Long id) throws Exception{
        log.info("call obtener: {}", id);
        return productoService.findById(id);
    }

    /**
     * Remove --> DELETE
     * Delete product
     * @param id
     * @return
     */
    @DeleteMapping("/productos/id/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("call eliminar: {}", id);
        productoService.deleteById(id);
        return ResponseEntity.ok().body("Registro eliminado");
    }


}
