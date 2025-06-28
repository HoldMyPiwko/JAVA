GraphicsContext – najważniejsze metody

🟥 Prostokąty i elipsy:
strokeRect(x, y, w, h) – rysuje obramowanie prostokąta

fillRect(x, y, w, h) – rysuje wypełniony prostokąt

strokeOval(x, y, w, h) – rysuje kontur elipsy

fillOval(x, y, w, h) – rysuje wypełnioną elipsę

🟨 Linie i kształty:

strokeLine(x1, y1, x2, y2) – rysuje linię między punktami

strokePolygon(xPoints[], yPoints[], nPoints) – rysuje wielokąt z ramką

fillPolygon(xPoints[], yPoints[], nPoints) – rysuje wypełniony wielokąt

strokePolyline(xPoints[], yPoints[], nPoints) – rysuje linię łamaną

🟦 Styl:
setFill(Color) – ustawia kolor wypełnienia

setStroke(Color) – ustawia kolor konturu

setLineWidth(double) – ustawia grubość linii

setLineDashes(...) – styl przerywany (np. kreski)

🟩 Tekst:
strokeText(String text, x, y) – rysuje obramowanie tekstu

fillText(String text, x, y) – rysuje wypełniony tekst

setFont(Font) – ustawia czcionkę

🖼️ Obrazy:
drawImage(Image, x, y) – rysuje obraz

drawImage(Image, x, y, w, h) – rysuje obraz z rozciągnięciem

drawImage(Image, sx, sy, sw, sh, dx, dy, dw, dh) – kopiowanie fragmentu obrazu

🧽 Czyszczenie:
clearRect(x, y, w, h) – czyści dany obszar canvasu

🔧 Transformacje (zaawansowane):
translate(x, y) – przesunięcie układu współrzędnych

rotate(angle) – obrót (w stopniach)

scale(sx, sy) – skalowanie

👉 Przy transformacjach często używa się save() i restore() do zachowania stanu rysowania:

Do transformacji:
gc.save(); 
gc.rotate(45);
gc.fillRect(...);
gc.restore();
