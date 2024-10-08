package com.example.febackendproject.Controller;

import com.example.febackendproject.Entity.Provider;
import com.example.febackendproject.Service.ProviderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RestControllerAdvice
@Validated
@RequestMapping("/provider")
public class ProviderController {
    
    private final ProviderService providerService;
    
    public ResponseEntity<?> notFound(String dataType, String data) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provider with " + dataType + " " + data + " not found");
    }
    
    public ResponseEntity<?> existingAttribute(String dataType, String data) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provider with " + dataType + " " + data + " already exists");
    }
    
    @GetMapping()
    public ResponseEntity<?> list() {
        return ResponseEntity.status(HttpStatus.OK).body(providerService.list());
    }
    
    @GetMapping("/{identifier}")
    public ResponseEntity<?> get(@PathVariable String identifier) {
        try {
            Long id = Long.parseLong(identifier);
            Optional<Provider> provider = providerService.findById(id);
            return provider.isEmpty()
                    ? notFound("ID", identifier)
                    : ResponseEntity.ok(provider.get());
        } catch (NumberFormatException e) {
            Optional<Provider> provider = providerService.findByName(identifier);
            return provider.isEmpty()
                    ? notFound("Name", identifier)
                    : ResponseEntity.ok(provider.get());
        }
    }
    
    @PostMapping("/{name}")
    public ResponseEntity<?> save(@PathVariable String name) {
        Optional<Provider> repeatedProvider = providerService.findByName(name);
        if (repeatedProvider.isPresent()) {
            return existingAttribute("Name", name);
        }
        Provider newProvider = providerService.save(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProvider);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Provider> provider = providerService.findById(id);
        if (provider.isPresent()) {
            providerService.deleteById(id);
            return ResponseEntity.ok("Provider with ID " + id + " deleted successfully");
        }
        return notFound("ID", Long.toString(id));
    }
    
}