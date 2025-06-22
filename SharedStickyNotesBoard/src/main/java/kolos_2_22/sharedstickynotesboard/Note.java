package kolos_2_22.sharedstickynotesboard;

public record Note(double x, double y, String text, String color) {
    public static String toMessage(double x, double y, String text, String color){
        return x + ";" + y + ";" + text + ";" + color;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public String color() {
        return color;
    }

    public static Note fromMessage(String msg){
        String[] parts = msg.split("; ", 4);
        return new Note(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                parts[2],
                parts[3]
        );
    }
}
