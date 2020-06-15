package io.horrorshow.soulswap.dao;

import io.horrorshow.soulswap.xml.SOULFileXMLType;
import io.horrorshow.soulswap.xml.SOULPatchFileXMLType;
import io.horrorshow.soulswap.xml.SOULPatchXMLType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class SOULSwapStaticRepository {
    private static final Map<String, SOULPatchXMLType> soulPatches = new HashMap<>();

    @PostConstruct
    public void initData() {
        SOULPatchXMLType classicRingtone = new SOULPatchXMLType();
        classicRingtone.setId("ClassicRingtone");
        SOULPatchFileXMLType classicRingtoneSoulPatchFile = new SOULPatchFileXMLType();
        classicRingtoneSoulPatchFile.setId("ClassicRingtoneSoulPatch");
        classicRingtoneSoulPatchFile.setFilename("ClassicRingtone.soulpatch");
        classicRingtoneSoulPatchFile.setFilecontent("{\n" +
                "    \"soulPatchV1\": \n" +
                "    {\n" +
                "        \"ID\":               \"dev.soul.examples.classicringtone\",\n" +
                "        \"version\":          \"1.0\",\n" +
                "        \"name\":             \"Classic Ringtone\",\n" +
                "        \"description\":      \"SOUL Classic Ringtone\",\n" +
                "        \"category\":         \"generator\",\n" +
                "        \"manufacturer\":     \"soul.dev\",\n" +
                "        \"isInstrument\":     false,\n" +
                "\n" +
                "        \"source\":           \"ClassicRingtone.soul\"\n" +
                "    }\n" +
                "}");
        classicRingtone.getSoulpatchfile().add(classicRingtoneSoulPatchFile);
        SOULFileXMLType classicRingtoneSoulFile = new SOULFileXMLType();
        classicRingtoneSoulFile.setId("ClassicRingtonSoulFile");
        classicRingtoneSoulFile.setFilename("ClassicRingtone.soul");
        classicRingtoneSoulFile.setFilecontent("/*\n" +
                "    == SOUL example code ==\n" +
                "\n" +
                "    Plays the first few notes of Gran Vals by Francisco Tarrega, using a\n" +
                "    sequence of sine-wave tones.\n" +
                "*/\n" +
                "\n" +
                "processor ClassicRingtone\n" +
                "{\n" +
                "    output stream float out;\n" +
                "\n" +
                "    void run()\n" +
                "    {\n" +
                "        int[] pitches    = (76, 74, 66, 68, 73, 71, 62, 64, 71, 69, 61, 64, 69);\n" +
                "        int[] durations  = ( 1,  1,  2,  2,  1,  1,  2,  2,  1,  1,  2,  2,  4);\n" +
                "\n" +
                "        for (int i = 0; i < pitches.size; ++i)\n" +
                "            playNote (pitches.at (i), durations.at (i));\n" +
                "    }\n" +
                "\n" +
                "    float sinewavePhase;\n" +
                "\n" +
                "    void playNote (int pitch, int lengthInQuarterNotes)\n" +
                "    {\n" +
                "        let samplesPerQuarterNote = int (processor.frequency / 7);\n" +
                "\n" +
                "        let noteFrequency  = soul::noteNumberToFrequency (pitch);\n" +
                "        let noteLength     = samplesPerQuarterNote * lengthInQuarterNotes;\n" +
                "        let phaseIncrement = float (noteFrequency * twoPi * processor.period);\n" +
                "\n" +
                "        loop (noteLength)\n" +
                "        {\n" +
                "            out << 0.1f * sin (sinewavePhase);\n" +
                "            sinewavePhase = addModulo2Pi (sinewavePhase, phaseIncrement);\n" +
                "            advance();\n" +
                "        }\n" +
                "    }\n" +
                "}\n");
        classicRingtone.getSoulfile().add(classicRingtoneSoulFile);
        soulPatches.put(classicRingtone.getId(), classicRingtone);

        SOULPatchXMLType delay = new SOULPatchXMLType();
        delay.setId("DelaySoulPatch");
        SOULPatchFileXMLType delaySoulPatchFile = new SOULPatchFileXMLType();
        delaySoulPatchFile.setId("DelaySoulPatch");
        delaySoulPatchFile.setFilename("Delay.soulpatch");
        delaySoulPatchFile.setFilecontent("{\n" +
                "  \"soulPatchV1\": {\n" +
                "    \"ID\": \"dev.soul.examples.delay\",\n" +
                "    \"version\": \"1.0\",\n" +
                "    \"name\": \"Delay\",\n" +
                "    \"description\": \"SOUL Delay\",\n" +
                "    \"category\": \"fx\",\n" +
                "    \"manufacturer\": \"soul.dev\",\n" +
                "    \"isInstrument\": false,\n" +
                "    \"source\": \"Delay.soul\"\n" +
                "  }\n" +
                "}");
        delay.getSoulpatchfile().add(delaySoulPatchFile);
        SOULFileXMLType delaySoulFile = new SOULFileXMLType();
        delaySoulFile.setId("DelaySoulFile");
        delaySoulFile.setFilename("Delay.soul");
        delaySoulFile.setFilecontent("/*\n" +
                "    == SOUL example code ==\n" +
                "\n" +
                "    A simple implementation of a delay, where the length and feedback level can\n" +
                "    be dynamically set using event parameters.\n" +
                "*/\n" +
                "\n" +
                "processor Delay  [[ main ]]\n" +
                "{\n" +
                "    input  stream float audioIn;\n" +
                "    output stream float audioOut;\n" +
                "\n" +
                "    input event\n" +
                "    {\n" +
                "        float delayLength    [[ min: 0,  max: 1000, init:  50,  name: \"Length\",   unit: \"ms\", step: 1 ]];\n" +
                "        float delayFeedback  [[ min: 0,  max: 100,  init:  25,  name: \"Feedback\", unit: \"%\",  step: 1 ]];\n" +
                "    }\n" +
                "\n" +
                "    event delayLength (float delayMs)\n" +
                "    {\n" +
                "        let delaySamples = max (1, int (processor.frequency * (delayMs / 1000.0f)));\n" +
                "\n" +
                "        readPos = writePos - delaySamples;\n" +
                "    }\n" +
                "\n" +
                "    event delayFeedback (float f)\n" +
                "    {\n" +
                "        feedback = f * 0.01f;\n" +
                "    }\n" +
                "\n" +
                "    let maxDelayLength = 100000;\n" +
                "\n" +
                "    float feedback;\n" +
                "    float[maxDelayLength] buffer;\n" +
                "    wrap<maxDelayLength> readPos, writePos;\n" +
                "\n" +
                "    void run()\n" +
                "    {\n" +
                "        loop\n" +
                "        {\n" +
                "            buffer[writePos] = audioIn + feedback * buffer[readPos];\n" +
                "            audioOut << buffer[readPos];\n" +
                "\n" +
                "            ++readPos;\n" +
                "            ++writePos;\n" +
                "\n" +
                "            advance();\n" +
                "        }\n" +
                "    }\n" +
                "}\n");
        delay.getSoulfile().add(delaySoulFile);
        soulPatches.put(delay.getId(), delay);
    }

    public SOULPatchXMLType findSOULPatch(String id) {
        Assert.notNull(id, "missing mandatory soul patch id");
        return soulPatches.get(id);
    }
}
