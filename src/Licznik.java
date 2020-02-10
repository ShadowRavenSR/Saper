import javax.swing.*;

public class Licznik {
    private JLabel label=new JLabel();
    private int licz;
    private String nazwa;

    public Licznik(String s, int l){
        nazwa=s;
        licz=l;
        label.setText(nazwa+licz);
        label.setVisible(true);
    }

    public void resetLicznika(int l){
        licz=l;
    }

    public void dodaj() {
        licz ++;
        label.setText(nazwa+licz);
    }

    public void odejmij() {
        licz --;
        label.setText(nazwa+licz);
    }

    public JLabel zwrocPanel(){
        return label;
    }
}
