package io.horrorshow.soulswap.controller;

import io.horrorshow.soulswap.data.SOULPatch;
import io.horrorshow.soulswap.service.SOULPatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SOULPatchController {

    private final SOULPatchService service;

    @Autowired
    public SOULPatchController(SOULPatchService service) {
        Assert.notNull(service, SOULPatchService.class.getName() + " must not be null.");
        this.service = service;
    }

    @RequestMapping("/")
    public String index() {
        return "This is SOULSwap!";
    }

    @GetMapping("/rest/soulpatches")
    public List<SOULPatch> getSOULPatches() {
        return service.findAll();
    }

    @GetMapping("/rest/soulpatches/{soulpatchId}")
    public SOULPatch getSOULPatch(@PathVariable Long soulpatchId) {
        return service.findById(soulpatchId);
    }

    @PostMapping("/rest/soulpatches")
    public SOULPatch createSOULPatch(@Valid @RequestBody SOULPatch soulPatch) {
        return service.save(soulPatch);
    }

    @PutMapping("/rest/soulpatches/{soulpatchId}")
    public SOULPatch updateSOULPatch(@PathVariable Long soulpatchId,
                                     @Valid @RequestBody SOULPatch soulPatch) {
        return service.update(soulpatchId, soulPatch);
    }

    @DeleteMapping("/rest/soulpatches/{soulpatchId}")
    public ResponseEntity<?> deleteSOULPatch(@PathVariable Long soulpatchId) {
        service.deleteById(soulpatchId);
        return ResponseEntity.ok().build();
    }
}
