package io.horrorshow.soulswap;

import io.horrorshow.soulswap.soulswap.SoulswapRequest;
import io.horrorshow.soulswap.soulswap.SoulswapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SOULPatchEndpoint {
    private static final String NAMESPACE_URI = "http://soulswap.horrorshow.io/soulswap";

    private SOULSwapRepository soulSwapRepository;

    @Autowired
    public SOULPatchEndpoint(SOULSwapRepository soulSwapRepository) {
        this.soulSwapRepository = soulSwapRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "soulswapRequest")
    @ResponsePayload
    public SoulswapResponse getSoulswapResponse(@RequestPayload SoulswapRequest request) {
        SoulswapResponse response = new SoulswapResponse();
        response.getSoulpatch().add(
                soulSwapRepository.findSOULPatch(request.getSoulpatchId()));
        return response;
    }
}
