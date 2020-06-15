package io.horrorshow.soulswap.soap;

import io.horrorshow.soulswap.dao.SOULSwapRepository;
import io.horrorshow.soulswap.xml.SOULPatchXMLType;
import io.horrorshow.soulswap.xml.SoulswapRequest;
import io.horrorshow.soulswap.xml.SoulswapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SOULPatchEndpoint {
    private static final String NAMESPACE_URI = "http://soulswap.horrorshow.io/soulswap";

    private final SOULSwapRepository soulSwapRepository;

    @Autowired
    public SOULPatchEndpoint(SOULSwapRepository soulSwapRepository) {

        this.soulSwapRepository = soulSwapRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "soulswapRequest")
    @ResponsePayload
    public SoulswapResponse getSoulswapResponse(@RequestPayload SoulswapRequest request) {

        var soulPatchEntity =
                soulSwapRepository.findById(Long.parseLong(request.getSoulpatchId()))
                        .orElseThrow();


        SOULPatchXMLType soulPatchXMLType = new SOULPatchXMLType();
        soulPatchXMLType.setId(soulPatchEntity.getId().toString());
        soulPatchXMLType.setId("SUCCESS: found soulpatch with id: " + request.getSoulpatchId() + " in repository");
        SoulswapResponse response = new SoulswapResponse();

        response.getSoulpatch().add(soulPatchXMLType);

        return response;
    }
}
