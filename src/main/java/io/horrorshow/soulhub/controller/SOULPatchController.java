package io.horrorshow.soulhub.controller;

import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.records.RecordsConverter;
import io.horrorshow.soulhub.data.records.SOULPatchRecord;
import io.horrorshow.soulhub.service.SOULPatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SOULPatchController {

    private final SOULPatchService service;

    @Autowired
    public SOULPatchController(SOULPatchService service) {
        Assert.notNull(service, SOULPatchService.class.getName() + " must not be null.");
        this.service = service;
    }

    @RequestMapping("/")
    public String index() {
        return "This is SOULHub!";
    }

    @GetMapping(value = "/soulpatches")
    public List<SOULPatchRecord> getSOULPatches() {
        return service.findAll().stream()
                .map(RecordsConverter::newSoulPatchRecord)
                .collect(Collectors.toList());
    }

    @GetMapping("/soulpatches/{soulpatchId}")
    public SOULPatchRecord getSOULPatch(@PathVariable Long soulpatchId) {
        return RecordsConverter.newSoulPatchRecord(service.findById(soulpatchId));
    }

    @PostMapping("/soulpatches")
    public SOULPatch createSOULPatch(@Valid @RequestBody SOULPatch soulPatch) {
        return service.save(soulPatch);
    }

    @PutMapping("/soulpatches/{soulpatchId}")
    public SOULPatch updateSOULPatch(@PathVariable Long soulpatchId,
                                     @Valid @RequestBody SOULPatch soulPatch) {
        return service.update(soulpatchId, soulPatch);
    }

    @DeleteMapping("/soulpatches/{soulpatchId}")
    public ResponseEntity<?> deleteSOULPatch(@PathVariable Long soulpatchId) {
        service.deleteById(soulpatchId);
        return ResponseEntity.ok().build();
    }
}
