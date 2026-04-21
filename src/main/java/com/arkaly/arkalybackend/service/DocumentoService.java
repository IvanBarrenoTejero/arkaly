package com.arkaly.arkalybackend.service;

import com.arkaly.arkalybackend.dto.DocumentoResponseDTO;
import com.arkaly.arkalybackend.models.Carpeta;
import com.arkaly.arkalybackend.models.Documento;
import com.arkaly.arkalybackend.models.Usuario;
import com.arkaly.arkalybackend.repositories.CarpetaRepository;
import com.arkaly.arkalybackend.repositories.DocumentoRepository;
import com.arkaly.arkalybackend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final CarpetaRepository carpetaRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${arkaly.storage.path}")
    private String storagePath;

    public DocumentoService(DocumentoRepository documentoRepository,
                            CarpetaRepository carpetaRepository,
                            UsuarioRepository usuarioRepository) {
        this.documentoRepository = documentoRepository;
        this.carpetaRepository = carpetaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener documentos de una carpeta
    public List<Documento> getDocumentosPorCarpeta(Integer idCarpeta) {
        return documentoRepository.findByIdCarpetaId(idCarpeta);
    }

    // Obtener documentos raíz del usuario
    public List<Documento> getDocumentosRaiz(Integer idUsuario) {
        return documentoRepository.findByIdUsuarioIdAndIdCarpetaIsNull(idUsuario);
    }

    // Buscar documentos por nombre
    public List<Documento> buscarDocumentos(Integer idUsuario, String nombre) {
        return documentoRepository
                .findByIdUsuarioIdAndNombreOriginalContainingIgnoreCase(idUsuario, nombre);
    }

    // Subir documento
    public Documento subirDocumento(Integer idUsuario, Integer idCarpeta,
                                    String categoria, MultipartFile file) throws IOException {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear carpeta física si no existe
        Path directorioUsuario = Paths.get(storagePath, String.valueOf(idUsuario));
        Files.createDirectories(directorioUsuario);

        // Generar nombre único para evitar colisiones
        String nombreOriginal = file.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        String nombreArchivo = UUID.randomUUID().toString() + extension;

        // Guardar el archivo en disco
        Path rutaArchivo = directorioUsuario.resolve(nombreArchivo);
        Files.copy(file.getInputStream(), rutaArchivo);

        // Guardar metadatos en BD
        Documento documento = new Documento();
        documento.setIdUsuario(usuario);
        documento.setNombreOriginal(nombreOriginal);
        documento.setNombreArchivo(nombreArchivo);
        documento.setRutaRelativa(idUsuario + "/" + nombreArchivo);
        documento.setTipoMime(file.getContentType());
        documento.setTamanioBytes(file.getSize());
        documento.setCategoria(categoria);

        if (idCarpeta != null) {
            Carpeta carpeta = carpetaRepository.findById(idCarpeta)
                    .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));
            documento.setIdCarpeta(carpeta);
        }

        return documentoRepository.save(documento);
    }

    // Mover documento a otra carpeta
    public Documento moverDocumento(Integer idDocumento, Integer idCarpetaDestino) {
        Documento documento = documentoRepository.findById(idDocumento)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        if (idCarpetaDestino == null) {
            documento.setIdCarpeta(null);
        } else {
            Carpeta carpeta = carpetaRepository.findById(idCarpetaDestino)
                    .orElseThrow(() -> new RuntimeException("Carpeta no encontrada"));
            documento.setIdCarpeta(carpeta);
        }

        return documentoRepository.save(documento);
    }

    // Eliminar documento
    public void eliminarDocumento(Integer idDocumento) throws IOException {
        Documento documento = documentoRepository.findById(idDocumento)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        // Eliminar archivo físico del disco
        Path rutaArchivo = Paths.get(storagePath,documento.getRutaRelativa());
        Files.deleteIfExists(rutaArchivo);

        // Eliminar registro de la BD
        documentoRepository.delete(documento);
    }

    public DocumentoResponseDTO toDTO(Documento documento) {
        DocumentoResponseDTO dto = new DocumentoResponseDTO();
        dto.setId(documento.getId());
        dto.setNombreOriginal(documento.getNombreOriginal());
        dto.setRutaRelativa(documento.getRutaRelativa());
        dto.setTipoMime(documento.getTipoMime());
        dto.setTamanioBytes(documento.getTamanioBytes());
        dto.setCategoria(documento.getCategoria());
        dto.setFechaSubida(documento.getFechaSubida());
        dto.setIdUsuario(documento.getIdUsuario().getId());
        dto.setIdCarpeta(documento.getIdCarpeta() != null ?
                documento.getIdCarpeta().getId() : null);
        return dto;
    }
}