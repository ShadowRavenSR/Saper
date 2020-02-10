import javax.swing.*;

public class IconyPrzyciskow {
    private ImageIcon[] ikonaTwarzy = {
            new ImageIcon(getClass().getResource("smile.gif")),
            new ImageIcon(getClass().getResource("Ooo.gif"))
    };
    private ImageIcon[] statusBomby = {
            new ImageIcon(getClass().getResource("mine.gif")),
            new ImageIcon(getClass().getResource("wrongmine.gif")),
            new ImageIcon(getClass().getResource("bomb.gif"))
    };
    private ImageIcon[] statusFlagi = {
            new ImageIcon(getClass().getResource("blank.gif")),
            new ImageIcon(getClass().getResource("flag.gif")),
    };
    private ImageIcon[] liczbaMin = {
            new ImageIcon(getClass().getResource("0.gif")),
            new ImageIcon(getClass().getResource("1.gif")),
            new ImageIcon(getClass().getResource("2.gif")),
            new ImageIcon(getClass().getResource("3.gif")),
            new ImageIcon(getClass().getResource("4.gif")),
            new ImageIcon(getClass().getResource("5.gif")),
            new ImageIcon(getClass().getResource("6.gif")),
            new ImageIcon(getClass().getResource("7.gif")),
            new ImageIcon(getClass().getResource("8.gif"))
    };


    public ImageIcon zwrocIkoneTwarzy(int i){
        return ikonaTwarzy[i];
    }

    public ImageIcon zwrocStatusBomby(int i){
        return statusBomby[i];
    }

    public ImageIcon zwrocLiczbeMiny(int i){
        return liczbaMin[i];
    }

    public ImageIcon zwrocStatusFlagi(int i){
        return statusFlagi[i];
    }
}
