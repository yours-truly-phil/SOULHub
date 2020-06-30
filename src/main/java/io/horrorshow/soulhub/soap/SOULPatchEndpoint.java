package io.horrorshow.soulhub.soap;

import io.horrorshow.soulhub.service.SOULPatchService;
import io.horrorshow.soulhub.xml.SOULPatchXMLType;
import io.horrorshow.soulhub.xml.SoulhubRequest;
import io.horrorshow.soulhub.xml.SoulhubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class SOULPatchEndpoint {

    private static final String NAMESPACE_URI = "http://soulhub.horrorshow.io/soulhub";

    private final SOULPatchService soulPatchService;

    @Autowired
    public SOULPatchEndpoint(SOULPatchService soulPatchService) {

        this.soulPatchService = soulPatchService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "soulhubRequest")
    @ResponsePayload
    public SoulhubResponse getSoulhubResponse(@RequestPayload SoulhubRequest request) {

        SoulhubResponse response = new SoulhubResponse();
        List<SOULPatchXMLType> xmlSoulPatches = soulPatchService.findAllXML();

        xmlSoulPatches.forEach(sp -> response.getSoulpatch().add(sp));

        return response;
    }
}
