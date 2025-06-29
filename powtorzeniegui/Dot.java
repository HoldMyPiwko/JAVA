package pl.umcs.oop.powtorzeniegui;

import javafx.scene.paint.Color;

public record Dot(double x, double y, double r, Color circleColor) {

        public static Color fromHex(String hex) {
            if (hex.startsWith("0x")) {
                hex = hex.substring(2);
            }

            long value = Long.parseLong(hex, 16);

            int a = (int)((value >> 24) & 0xFF);
            int r = (int)((value >> 16) & 0xFF);
            int g = (int)((value >> 8) & 0xFF);
            int b = (int)(value & 0xFF);

            double opacity = a / 255.0;

            return Color.rgb(r, g, b, opacity);
        }


    public String toMessage(){
        return this.x + ", "+ this.y + ", " + this.r +  ", " + this.circleColor;
    }

    public static Dot fromMessage(String message){
        String[] parts = message.split(",");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double r = Double.parseDouble(parts[2]);
        Color color = fromHex(parts[3]);

        return new Dot(x, y, r, color);
    }

}
