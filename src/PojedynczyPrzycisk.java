import javax.swing.*;

public class PojedynczyPrzycisk extends JButton {

    private int kolumna;
    private int rzad;
    private boolean flaga;
    private boolean klikniety;

    public PojedynczyPrzycisk (int rz,int ko,ImageIcon ic){
        setIcon(ic);
        kolumna=ko;
        rzad=rz;
        flaga=false;
        klikniety=false;
    }

    public boolean getFlaga(){
        return flaga;
    }

    public boolean getKlikniety(){
        return klikniety;
    }


    public int getRzad(){
        return rzad;
    }

    public int getKolumna(){
        return kolumna;
    }

    public void setFlaga(boolean f){
        flaga=f;
    }

    public void setKlikniety(boolean k){
        klikniety=k;
    }

}
