

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class FreeTTSHelloWorld {

    public FreeTTSHelloWorld() {
    }

    public static void listAllVoices() {
        System.out.println();
        System.out.println("All voices available:");
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voices[] = voiceManager.getVoices();
        for (int i = 0; i < voices.length; i++)
            System.out.println("    " + voices[i].getName() + " (" + voices[i].getDomain() + " domain)");

    }

    public static void main(String args[]) {
        listAllVoices();
        System.out.println();
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice helloVoice = voiceManager.getVoice("kevin16");
        if (helloVoice == null) {
            System.exit(1);
        }
        helloVoice.allocate();
//        helloVoice.speak("Thank you for giving me a voice. I'm so glad to say hello to this world.");
        helloVoice.speak("A.K.B.C.");
        helloVoice.deallocate();
        System.exit(0);
    }
}
