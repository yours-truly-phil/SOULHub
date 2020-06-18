package io.horrorshow.soulswap.soap;

import io.horrorshow.soulswap.service.SOULPatchService;
import io.horrorshow.soulswap.xml.SOULPatchXMLType;
import io.horrorshow.soulswap.xml.SoulswapRequest;
import io.horrorshow.soulswap.xml.SoulswapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class SOULPatchEndpoint {

    private static final String NAMESPACE_URI = "http://soulswap.horrorshow.io/soulswap";

    private final SOULPatchService soulPatchService;

    @Autowired
    public SOULPatchEndpoint(SOULPatchService soulPatchService) {

        this.soulPatchService = soulPatchService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "soulswapRequest")
    @ResponsePayload
    public SoulswapResponse getSoulswapResponse(@RequestPayload SoulswapRequest request) {

        SoulswapResponse response = new SoulswapResponse();
        List<SOULPatchXMLType> xmlSoulPatches = soulPatchService.findAllXML();

        xmlSoulPatches.forEach(sp -> response.getSoulpatch().add(sp));

        return response;
    }
}
