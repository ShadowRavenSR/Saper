import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PipedOutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Saper implements ActionListener, MouseListener {
    //Dane
    private IconyPrzyciskow icons = new IconyPrzyciskow();

    private int poczRzedy = 8;
    private int poczKolumny = 8;
    private int poczMiny = 10;

    private int zaawRzedy = 16;
    private int zaawKolumny = 16;
    private int zaawMiny = 40;

    private int expRzedy = 16;
    private int expKolumny = 30;
    private int expMiny = 99;


    private int maxRzedow;
    private int maxKolumn;
    private int liczbaMin;
    private int liczbaFlag;
    private boolean rozpoczete;
    private Timer timer;
    private PlanszaGry mapaMin;

    //Graficzne
    private PojedynczyPrzycisk[][] przyciski;
    private JFrame oknoGry = new JFrame("Saper");
    private JMenuBar menuGorne = new JMenuBar();
    private JMenu menuGry = new JMenu("Gra");
    private JMenuItem nowaGra = new JMenuItem("Nowa Gra");
    private JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Początkujący");
    private JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Zaawansowany");
    private JRadioButtonMenuItem expert = new JRadioButtonMenuItem("Ekspert");
    private ButtonGroup trybyRozgrywki = new ButtonGroup();
    private JMenuItem exit = new JMenuItem("Wyjście");

    private JLabel tekst = new JLabel("Status: trwa");

    private JButton twarz = new JButton(icons.zwrocIkoneTwarzy(0));

    private JPanel panelKontrolny = new JPanel();
    private JPanel panelMin = new JPanel();
    private JPanel panelKomponentow = new JPanel();
    private JPanel panelStatusu=new JPanel();

    private Licznik licznikFlag = new Licznik("Flagi:",10);
    private Licznik licznikCzasu = new Licznik("Czas:",0);

    public Saper() {
        inicjalizacja();

        easy.setSelected(true);
        setupGry(poczRzedy, poczKolumny, poczMiny);
    }

    public void inicjalizacja() {
        nowaGra.addActionListener(this);
        easy.addActionListener(this);
        medium.addActionListener(this);
        expert.addActionListener(this);
        exit.addActionListener(this);

        trybyRozgrywki.add(easy);
        trybyRozgrywki.add(medium);
        trybyRozgrywki.add(expert);

        menuGry.add(nowaGra);
        menuGry.addSeparator();
        menuGry.add(easy);
        menuGry.add(medium);
        menuGry.add(expert);
        menuGry.addSeparator();
        menuGry.add(exit);

        menuGorne.add(menuGry);

        oknoGry.setJMenuBar(menuGorne);

        twarz.setMaximumSize(new Dimension(26, 27));
        twarz.setPreferredSize(new Dimension(26, 27));
        twarz.addMouseListener(this);
        twarz.setPressedIcon(icons.zwrocIkoneTwarzy(1));

        panelKontrolny.add(licznikFlag.zwrocPanel());
        panelKontrolny.add(twarz);
        panelKontrolny.add(licznikCzasu.zwrocPanel());

        panelMin.setLayout(new GridBagLayout());

        panelStatusu.setLayout(new BorderLayout());
        panelStatusu.add(tekst,BorderLayout.PAGE_START);

        panelKomponentow.setLayout(new BoxLayout(panelKomponentow, BoxLayout.Y_AXIS));

        oknoGry.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        oknoGry.setResizable(false);
        oknoGry.setVisible(true);
    }

    public void setupGry(int r, int k, int m) {
        maxRzedow = r;
        maxKolumn = k;
        liczbaMin = m;
        liczbaFlag = m;
        rozpoczete = false;
        twarz.setIcon(icons.zwrocIkoneTwarzy(0));
        if (timer != null) timer.cancel();

        panelKomponentow.removeAll();
        licznikFlag.resetLicznika(liczbaFlag);
        licznikCzasu.resetLicznika(0);

        panelKomponentow.add(panelKontrolny);

        panelMin.removeAll();
        przyciski = new PojedynczyPrzycisk[maxRzedow][maxKolumn];
        for (int i = 0; i < maxRzedow; i++) {
            for (int j = 0; j < maxKolumn; j++) {
                przyciski[i][j] = new PojedynczyPrzycisk(i, j, icons.zwrocStatusFlagi(0));
                przyciski[i][j].addMouseListener(this);
                przyciski[i][j].setMaximumSize(new Dimension(20, 20));
                przyciski[i][j].setPreferredSize(new Dimension(20, 20));

                GridBagConstraints GBC = new GridBagConstraints();
                GBC.gridx = j;
                GBC.gridy = i;
                GBC.gridwidth = 1;
                GBC.gridheight = 1;
                panelMin.add(przyciski[i][j], GBC);
            }
        }
        panelKomponentow.add(panelMin);
        panelKomponentow.add(panelStatusu);
        oknoGry.setContentPane(panelKomponentow);
        oknoGry.pack();
        oknoGry.repaint();
    }

    private void startGry(int pr, int pc) {
        mapaMin = new PlanszaGry(maxRzedow, maxKolumn, liczbaMin, pr, pc);
        rozpoczete = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                licznikCzasu.dodaj();
            }
        }, 1000, 1000);
    }

    public void resetGry() {
        liczbaFlag = liczbaMin;
        licznikFlag.resetLicznika(liczbaFlag);
        licznikCzasu.resetLicznika(0);
        rozpoczete = false;
        twarz.setIcon(icons.zwrocIkoneTwarzy(0));
        if (timer != null) timer.cancel();
        licznikFlag.resetLicznika(liczbaFlag);
        licznikCzasu.resetLicznika(0);

        for (int r = 0; r < maxRzedow; r++) {
            for (int k = 0; k < maxKolumn; k++) {
                przyciski[r][k].setKlikniety(false);
                przyciski[r][k].setFlaga(false);
                przyciski[r][k].setIcon(icons.zwrocStatusFlagi(0));
            }
        }
    }

    public void odkryjPole(int r, int k) {
        if (przyciski[r][k].getKlikniety() || przyciski[r][k].getFlaga()) return;
        if (mapaMin.getWartoscPola(r, k) != 9) {
            przyciski[r][k].setIcon(icons.zwrocLiczbeMiny(mapaMin.getWartoscPola(r, k)));
        }
        przyciski[r][k].setKlikniety(true);
        if (mapaMin.getWartoscPola(r, k) == 0) {
            int gornaGranica = r - 1 < 0 ? 0 : r - 1;
            int dolnaGranica = r + 1 < maxRzedow ? r + 1 : maxRzedow - 1;
            int lewaGranica = k - 1 < 0 ? 0 : k - 1;
            int prawaGranica = k + 1 < maxKolumn ? k + 1 : maxKolumn - 1;
            int tmp = 0;
            for (int i = gornaGranica; i <= dolnaGranica; i++) {
                for (int j = lewaGranica; j <= prawaGranica; j++) {
                    odkryjPole(i, j);
                }
            }
        }
    }

    public void odkryjDookola(int r, int k) {
        if (!przyciski[r][k].getKlikniety()) return;
        int gornaGranica = r - 1 < 0 ? 0 : r - 1;
        int dolnaGranica = r + 1 < maxRzedow ? r + 1 : maxRzedow - 1;
        int lewaGranica = k - 1 < 0 ? 0 : k - 1;
        int prawaGranica = k + 1 < maxKolumn ? k + 1 : maxKolumn - 1;
        int tmp = 0;
        for (int i = gornaGranica; i <= dolnaGranica; i++) {
            for (int j = lewaGranica; j <= prawaGranica; j++) {
                if (przyciski[i][j].getFlaga()) tmp++;
            }
        }
        if (tmp == mapaMin.getWartoscPola(r, k)) {
            for (int i = gornaGranica; i <= dolnaGranica; i++) {
                for (int j = lewaGranica; j <= prawaGranica; j++) {
                    if(!przyciski[i][j].getKlikniety()){
                        odkryjPole(i,j);
                    }
                }
            }
        }
    }

    public void oflaguj(int r, int k) {
        if (przyciski[r][k].getKlikniety()) return;
        if (!przyciski[r][k].getFlaga()) {
            przyciski[r][k].setFlaga(true);
            licznikFlag.odejmij();
            przyciski[r][k].setIcon(icons.zwrocStatusFlagi(1));
        } else if (przyciski[r][k].getFlaga()) {
            przyciski[r][k].setFlaga(false);
            licznikFlag.dodaj();
            przyciski[r][k].setIcon(icons.zwrocStatusFlagi(0));
        }
    }


    public boolean warunkiWygranej() {
        for (int r = 0; r < maxRzedow; r++) {
            for (int k = 0; k < maxKolumn; k++) {
                if (mapaMin.getWartoscPola(r, k) != 9 && !przyciski[r][k].getKlikniety()) return false;
            }
        }
        return true;
    }

    public boolean warunkiPrzegranej() {
        for (int r = 0; r < maxRzedow; r++) {
            for (int k = 0; k < maxKolumn; k++) {
                if (mapaMin.getWartoscPola(r, k) == 9 && przyciski[r][k].getKlikniety()) return true;
            }
        }
        return false;
    }

    private void wygrana() {
        timer.cancel();
        tekst.setText("Status:Wygrana");
    }

    private void przegrana() {
        for (int r = 0; r < maxRzedow; r++) {
            for (int k = 0; k < maxKolumn; k++) {
                if (mapaMin.getWartoscPola(r, k) == 9 && przyciski[r][k].getKlikniety()) {
                    przyciski[r][k].setIcon(icons.zwrocStatusBomby(2));
                } else if (mapaMin.getWartoscPola(r, k) == 9 && !przyciski[r][k].getKlikniety()) {
                    przyciski[r][k].setIcon(icons.zwrocStatusBomby(0));
                } else if (mapaMin.getWartoscPola(r, k) != 9 && przyciski[r][k].getFlaga()) {
                    przyciski[r][k].setIcon(icons.zwrocStatusBomby(1));
                }
            }
        }
        timer.cancel();
        tekst.setText("Status:Przegrana");
        twarz.setIcon(icons.zwrocIkoneTwarzy(1));
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == nowaGra) {
            setupGry(maxRzedow, maxKolumn, liczbaMin);
        } else if (src == easy) {
            setupGry(poczRzedy, poczKolumny, poczMiny);
        } else if (src == medium) {
            setupGry(zaawRzedy, zaawKolumny, zaawMiny);
        } else if (src == expert) {
            setupGry(expRzedy, expKolumny, expMiny);
        } else if (src == exit) {
            System.exit(0);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == twarz) {
            resetGry();
            return;
        }

        int rzad = ((PojedynczyPrzycisk) e.getSource()).getRzad();
        int kolumna = ((PojedynczyPrzycisk) e.getSource()).getKolumna();
        if(!rozpoczete&&e.getButton()== MouseEvent.BUTTON1) startGry(rzad,kolumna);

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    odkryjPole(rzad, kolumna);
                    if(warunkiWygranej()) wygrana();
                    if(warunkiPrzegranej()) przegrana();
                }else if(e.getButton() == MouseEvent.BUTTON3) {
                    oflaguj(rzad, kolumna);
                }else if(e.getButton()==MouseEvent.BUTTON2){
                    odkryjDookola(rzad,kolumna);
                }
                this.cancel();
            }
        },new Date(),10);
    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public static void main(String[] args) {
        new Saper();
    }
}
