import java.util.Random;

public class PlanszaGry {

    private int liczbaKolumn;
    private int liczbaRzedow;
    private int liczbaMin;
    private int[][] wartoscPola;

    public PlanszaGry(int r, int k, int m, int sr, int sk) {
        liczbaKolumn = k;
        liczbaRzedow = r;
        liczbaMin = m;
        wartoscPola = new int[r][k];
        przydzielMiny(sr, sk);
        ustalWartosciPol();
    }

    public int getWartoscPola(int r, int k) {
        return wartoscPola[r][k];
    }

    public void przydzielMiny(int r, int k) {
        int tmp = 0;
        Random rand = new Random();
        while (tmp < liczbaMin) {
            int rzad = rand.nextInt(liczbaRzedow);
            int kolumna = rand.nextInt(liczbaKolumn);
            if (wartoscPola[rzad][kolumna] != 0 || rzad == r && kolumna == k) continue;
            wartoscPola[rzad][kolumna] = 9;
            tmp++;
        }
    }

    public int sprawdzPrzyleglePola(int r, int k) {
        int gornaGranica;
        if (r - 1 < 0) gornaGranica = 0;
        else gornaGranica = r - 1;
        int dolnaGranica;
        if (r + 1 < liczbaRzedow) dolnaGranica = r + 1;
        else dolnaGranica = liczbaRzedow - 1;
        int lewaGranica;
        if (k - 1 < 0) lewaGranica = 0;
        else lewaGranica = k - 1;
        int prawaGranica;
        if (k + 1 < liczbaKolumn) prawaGranica = k + 1;
        else prawaGranica = liczbaKolumn - 1;
        int tmp = 0;
        for (int i = gornaGranica; i <= dolnaGranica; i++) {
            for (int j = lewaGranica; j <= prawaGranica; j++) {
                if (wartoscPola[i][j] == 9) tmp++;
            }
        }
        return tmp;
    }

    public void ustalWartosciPol() {
        for (int i = 0; i < liczbaRzedow; i++) {
            for (int j = 0; j < liczbaKolumn; j++) {
                wartoscPola[i][j] = wartoscPola[i][j]==9 ? 9 : sprawdzPrzyleglePola(i, j);
            }
        }
    }

   /* public static void main(String[] args) {
        PlanszaGry mapaMin = new PlanszaGry(5, 5, 6, 0, 0);
        for (int r = 0; r < mapaMin.liczbaRzedow; r++) {
            for (int k = 0; k < mapaMin.liczbaKolumn; k++) {
                System.out.print(mapaMin.getWartoscPola(r, k) + " ");
            }
            System.out.println();
        }
    }
    */
}
