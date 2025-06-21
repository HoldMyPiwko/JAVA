package kolos_2_22.kolokowium2_22;

public class WordEntry {
    private final String word;
    private final String time;


    public WordEntry(String word, String timestamp) {
        this.word = word;
        this.time = timestamp;
    }

    public String getTimestamp() {
        return time;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString(){
        return time + " " + word;
    }
}
