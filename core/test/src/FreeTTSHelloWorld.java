

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FreeTTSHelloWorld {

    private static Log logger = LogFactory.getLog(FreeTTSHelloWorld.class);

    public static void listAllVoices() {
        logger.debug("All voices available:");
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voices[] = voiceManager.getVoices();
        for (Voice voice : voices) logger.debug("    " + voice.getName() + " (" + voice.getDomain() + " domain)");
    }

    public void speak() {
        listAllVoices();
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice helloVoice = voiceManager.getVoice("kevin16");
        if (helloVoice == null) {
            System.exit(1);
        }
        helloVoice.allocate();
        helloVoice.speak("Thank you for giving me a voice. I'm so glad to say hello to this world.");
        helloVoice.speak("A.K.B.C.");
        helloVoice.deallocate();
    }
}
