package com.example.transfert.controller;

import antlr.StringUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class uploadfilecontroler {


    /* @GetMapping("/file")
   public ResponseEntity<?> uploard(@RequestParam("file") MultipartFile file)
    {
       String filename= file.getOriginalFilename();
        Long size= file.getSize();
        String content= file.getContentType();
        String name= file.getName();
        Map<String,Object> m=new LinkedHashMap<>();
        m.put("images filename",filename);
        m.put("images size",size);
        m.put("images content",content);
        m.put("images name",name);
       return new ResponseEntity<>(m, HttpStatus.OK);
    } */

    @PostMapping("/file")
    public ResponseEntity<?> uploard(@RequestParam("file") MultipartFile file) throws IOException {
        int id = 1;
        String filename = file.getOriginalFilename();
        // Path path= Paths.get("src/main/resources/images/"); //les chemair de recources/images
        String pathAdd = filename; //les fichier qu'on va ajouter dans les path. on peut utiliser id ou pas
        try (InputStream inputStream = file.getInputStream()) {
            Path path = Files.createDirectories(Paths.get("src/main/resources/images/" + id));//pour creer le nouveau dossier avec l'id de utilisateur
            Path pathComplet = path.resolve(pathAdd);//completer les nom du fichier ou image
            //   File fil=new File(id);
            //   fil.createNewFile();
            Files.copy(inputStream, pathComplet, StandardCopyOption.REPLACE_EXISTING);
            return new ResponseEntity<>(pathComplet, HttpStatus.OK);
        } catch (IOException e) {
            // throw new IOException("erreur "+filename,e);
            return new ResponseEntity<>(e, HttpStatus.OK);
        }

    }

    @DeleteMapping("/file/delete")
    public ResponseEntity<?> deleteFile() {
        int userId = 1;
        String filename = "IMG_1188.png";
        Path path = Paths.get("src/main/resources/images/" + userId + "/" + filename);
        try {
            Files.deleteIfExists(path);
            return new ResponseEntity<>("ficher supprimer", HttpStatus.OK);
        } catch (IOException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("file/view")
    public ResponseEntity<?> viewFile() {
        int userId = 1;
        String filename = "IMG_1308.png";
        String path = "src/main/resources/images/" + userId + "/" + filename;
        Map<String, Object> message = new HashMap<>();
        try {
            message.put("Message", path);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }


    //enregistrer plusieur fichers en meme temps
    @PostMapping("/file/multfile")
    public ResponseEntity<?> uploardMulti(@RequestParam("file") MultipartFile[] file) throws IOException {
        int id = 1;
        Path path = Files.createDirectories(Paths.get("src/main/resources/images/" + id));//pour creer le nouveau dossier avec l'id de utilisateur
        List<Path> list = new ArrayList<>();
        try {

            for (MultipartFile fil : file) {
                InputStream inputStream = fil.getInputStream();
                String filename = fil.getOriginalFilename();
                // Path path= Paths.get("src/main/resources/images/"); //les chemair de recources/images
                String pathAdd = filename; //les fichier qu'on va ajouter dans les path. on peut utiliser id ou pas

                Path pathComplet = path.resolve(pathAdd);//completer les nom du fichier ou image
                //   File fil=new File(id);
                //   fil.createNewFile();
                Files.copy(inputStream, pathComplet, StandardCopyOption.REPLACE_EXISTING);
                list.add(pathComplet); //pour retourner les resultats
            }
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (IOException e) {
            // throw new IOException("erreur "+filename,e);
            return new ResponseEntity<>(e, HttpStatus.OK);
        }
    }
}
