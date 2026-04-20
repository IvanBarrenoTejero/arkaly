package com.arkaly.arkalybackend.service;

import com.arkaly.arkalybackend.models.Carpeta;
import com.arkaly.arkalybackend.models.Documento;
import com.arkaly.arkalybackend.models.Usuario;
import com.arkaly.arkalybackend.repositories.CarpetaRepository;
import com.arkaly.arkalybackend.repositories.DocumentoRepository;
import com.arkaly.arkalybackend.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentosService {

    private DocumentoRepository documentoRepository;
    private UsuarioRepository usuarioRepository;
    private CarpetaRepository carpetaRepository;

    // Carpeta base donde se guardarán los archivos en el sistema
    private final String carpetaBase = System.getProperty("user.home") + "/Arkaly/documentos/";

    public DocumentosService(DocumentoRepository documentoRepository,  UsuarioRepository usuarioRepository, CarpetaRepository carpetaRepository) {
        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.carpetaRepository = carpetaRepository;
    }

    public List<Documento> getDocumentosUsuario(Integer idUsuario) {
        return documentoRepository.findByIdUsuario(idUsuario);
    }

    public List<Documento> getDocumentosPorCarpeta(Integer idCarpeta) {
        return documentoRepository.findByIdCarpeta(idCarpeta);
    }

    public Documento subirDocumento(MultipartFile archivo, Integer idUsuario, Integer idCarpeta, String categoria) throws IOException {

        //Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Id de usuario no encontrado"));
        //Carpeta carpeta = carpetaRepository.findById(idCarpeta).orElseThrow(() -> new RuntimeException("Id de carpeta no encontrado"));

        // Crea objetos fantasma que solo tiene id, para no tener que hacer una consulta select en la bd que consume más.
        Usuario usuario = usuarioRepository.getReferenceById(idUsuario);
        Carpeta carpeta = carpetaRepository.getReferenceById(idCarpeta);

        // Crear la carpeta base si no existe
        Path rutaBase = Paths.get(carpetaBase);
        Files.createDirectories(rutaBase);

        // Generar nombre único para evitar colisiones
        String nombreUnico = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        Path rutaDestino = rutaBase.resolve(nombreUnico);

        // Copiar el archivo al sistema
        Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

        // Guardar los metadatos en la base de datos
        Documento documento = new Documento();
        documento.setIdUsuario(usuario);
        documento.setIdCarpeta(carpeta);
        documento.setNombreOriginal(archivo.getOriginalFilename());
        documento.setNombreArchivo(nombreUnico);
        documento.setRutaRelativa(carpetaBase + nombreUnico);
        documento.setTipoMime(archivo.getContentType());
        documento.setTamanioBytes(archivo.getSize());
        documento.setCategoria(categoria);

        return documentoRepository.save(documento);
    }

    public void eliminarDocumento (Integer id) throws IOException {
        Documento documento = documentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        // Eliminar el archivo físico del sistema
        Path ruta = Paths.get(documento.getRutaRelativa());
        Files.deleteIfExists(ruta);

        // Eliminar el registro de la base de datos
        documentoRepository.deleteById(id);
    }
}

