package io.horrorshow.soulswap;


import io.horrorshow.soulswap.exception.ResourceNotFoundException;
import io.horrorshow.soulswap.model.SOULPatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SOULSwapController {

    private final SOULSwapRepository repository;

    @Autowired
    public SOULSwapController(SOULSwapRepository repository) {
        Assert.notNull(repository, "SOULSwapRepository must not be null.");
        this.repository = repository;
    }

    @RequestMapping("/")
    public String index() {
        return "This is SOULSwap!";
    }

    @GetMapping("/soulspatches")
    public Page<SOULPatchEntity> getSOULPatches(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PostMapping("soulpatches")
    public SOULPatchEntity createSOULPatch(@Valid @RequestBody SOULPatchEntity soulPatchEntity) {
        return repository.save(soulPatchEntity);
    }

    @PutMapping("soulpatches/{soulpatchId}")
    public SOULPatchEntity updateSOULPatch(@PathVariable Long soulpatchId,
                                           @Valid @RequestBody SOULPatchEntity soulPatchEntity) {
        return repository.findById(soulpatchId)
                .map(soulPatch -> {
                    soulPatch.setAuthor(soulPatchEntity.getAuthor());
                    soulPatch.setDescription(soulPatchEntity.getDescription());
                    soulPatch.setSoulFileName(soulPatchEntity.getSoulFileName());
                    soulPatch.setSoulFileContent(soulPatchEntity.getSoulFileContent());
                    soulPatch.setSoulpatchFileName(soulPatchEntity.getSoulpatchFileName());
                    soulPatch.setSoulpatchFileContent(soulPatchEntity.getSoulpatchFileContent());
                    soulPatch.setOffsetDateTime(soulPatchEntity.getOffsetDateTime());
                    soulPatch.setAuthor(soulPatchEntity.getAuthor());
                    soulPatch.setNoServings(soulPatchEntity.getNoServings());
                    return repository.save(soulPatch);
                }).orElseThrow(() -> new ResourceNotFoundException("SOULPatch not found in repository, soulpatchId " + soulpatchId));
    }

    @DeleteMapping("soulpatches/{soulpatchId}")
    public ResponseEntity<?> deleteSOULPatch(@PathVariable Long soulpatchId) {
        return repository.findById(soulpatchId)
                .map(soulPatch -> {
                    repository.delete(soulPatch);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("SOULPatch not found in repository, soulpatchId " + soulpatchId));
    }
}
