package io.horrorshow.soulhub.controller;

import io.horrorshow.soulhub.data.SOULPatch;
import io.horrorshow.soulhub.data.records.SOULPatchRecord;
import io.horrorshow.soulhub.data.records.UserRecord;
import io.horrorshow.soulhub.service.SOULPatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
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

    @GetMapping("/soulpatches")
    public List<SOULPatchRecord> getSOULPatches() {
        return service.findAll().stream().map((SOULPatch sp) ->
                new SOULPatchRecord(
                        sp.getId(),
                        sp.getName(),
                        sp.getDescription(),
                        Collections.emptySet(),
                        new UserRecord(sp.getAuthor()),
                        sp.getCreatedAt(),
                        sp.getUpdatedAt()
                )).collect(Collectors.toList());
    }

    @GetMapping("/soulpatches/{soulpatchId}")
    public SOULPatch getSOULPatch(@PathVariable Long soulpatchId) {
        return service.findById(soulpatchId);
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
