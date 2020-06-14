package io.horrorshow.soulswap;

import io.horrorshow.soulswap.soulswap.SOULPatch;
import io.horrorshow.soulswap.soulswap.SoulswapRequest;
import io.horrorshow.soulswap.soulswap.SoulswapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SOULSwapEndpoint {
    private static final String NAMESPACE_URI = "http://soulswap.horrorshow.io/soulswap";

    private final SOULSwapRepository soulSwapRepository;

    @Autowired
    public SOULSwapEndpoint(SOULSwapRepository soulSwapRepository) {

        this.soulSwapRepository = soulSwapRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "soulswapRequest")
    @ResponsePayload
    public SoulswapResponse getSoulswapResponse(@RequestPayload SoulswapRequest request) {

        var soulPatchEntity =
                soulSwapRepository.findById(Long.parseLong(request.getSoulpatchId()))
                        .orElseThrow();


        SOULPatch soulPatch = new SOULPatch();
        soulPatch.setId(soulPatchEntity.getId().toString());
        soulPatch.setId("SUCCESS: found soulpatch with id: " + request.getSoulpatchId() + " in repository");
        SoulswapResponse response = new SoulswapResponse();

        response.getSoulpatch().add(soulPatch);

        return response;
    }
}
