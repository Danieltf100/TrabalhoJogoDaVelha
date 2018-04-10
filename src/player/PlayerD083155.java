package player;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import cenario.Cenario;
import model.Jogada;
import model.Jogo;
import model.Player;
import model.TabuleiroFoto;
import playerFactory.PlayerFactory;

public class PlayerD083155 extends Player {
	
	private boolean showLog;

	public PlayerD083155(int aN, TabuleiroFoto aTabuleiroFoto) {
		super(aN, aTabuleiroFoto);
		this.showLog = false;
	}
	
	@Override
	public Jogada joga(TabuleiroFoto tf) {
		return Heuristica.doSearch(tf, this, this.showLog);
	}
	
	public void setShowLog(boolean showLog) {
		this.showLog = showLog;
		if (this.showLog) Logger.getLogger(PlayerD083155.class.getName()).log(Level.INFO, "O log está habilitado.\n");
	}
	
	private static class Heuristica {
		
		private static char VAZIO = '_';
		
		public static Jogada doSearch(TabuleiroFoto tf, Player player, boolean showLog) {
			
			Jogada jogada = new Jogada(0, 0);
			
			ArrayList<Cenario> cenarios = new ArrayList<>();
			
			if (showLog) Logger.getLogger(Heuristica.class.getName()).log(Level.INFO, "Criando cenários possíveis...\n");
			
			// cria todos os casos possíveis de jogada e os pontua
			for (int i =0 ; i < tf.n; i++) {
				for (int j = 0; j < tf.n; j++) {
					if (tf.t[i][j] == VAZIO) {
						TabuleiroFoto temptf = new TabuleiroFoto(tf);
						temptf.t[i][j] = player.getSide();
																	
						int mySide=0, otherSide=0, empty=0; //contadores
						long points = 0L;
						// linha fixa
						for (int c = 0; c < temptf.n; c++) {
							if (temptf.t[i][c] == player.getSide()) mySide++;
							else if (temptf.t[i][c] == player.getOtherSide()) otherSide++;
							else empty++;
						}
						points += calcPoints(mySide, otherSide, empty, temptf.n);
						
						mySide=0; otherSide=0; empty=0;
						// coluna fixa
						for (int l=0; l < temptf.n; l++) {
							if (temptf.t[l][j] == player.getSide()) mySide++;
							else if (temptf.t[l][j] == player.getOtherSide()) otherSide++;
							else empty++;
						}
						points += calcPoints(mySide, otherSide, empty, temptf.n);
						
						// diagonal principal
						if (i==j) {
							mySide=0; otherSide=0; empty=0;
							int p = 0;
							while (p < temptf.n) {
								if (temptf.t[p][p] == player.getSide()) mySide++;
								else if (temptf.t[p][p] == player.getOtherSide()) otherSide++;
								else empty++;
								p++;
							}
							points += calcPoints(mySide, otherSide, empty, temptf.n);
						}
						
						// diagonal secundaria
						if (i == temptf.n-j-1) {
							mySide=0; otherSide=0; empty=0;
							int l = 0, c = temptf.n-1;
							while (l < temptf.n && c >= 0) {
								if (temptf.t[l][c] == player.getSide()) mySide++;
								else if (temptf.t[l][c] == player.getOtherSide()) otherSide++;
								else empty++;
								l++; c--;
							}
							points += calcPoints(mySide, otherSide, empty, temptf.n);
						}
						
						cenarios.add(new Cenario(new Jogada(i, j), temptf, points));
					}
				}
			}
			
			if (showLog) Logger.getLogger(Heuristica.class.getName()).log(Level.INFO, "Existem "+cenarios.size()+" cenários possíveis");
			
			// escolhe o cenario com a maior pontuação, pois a maior significa maior chance de vencer.
			if (!cenarios.isEmpty()) {
				long melhor=cenarios.get(0).getPontuacao();
				jogada = cenarios.get(0).getJogada();
				for (Cenario cenario : cenarios) {
					if (showLog){ 
						Logger.getLogger(Heuristica.class.getName())
								.log(Level.INFO, "Tentativa de "+cenario.getJogada().toString()+" - Pontuação recebida:  "+cenario.getPontuacao()+"\n");
					}
					if (cenario.getPontuacao() > melhor) {
						melhor = cenario.getPontuacao();
						jogada = cenario.getJogada();
					}
				}
			}
			
			return jogada;
			
		}
		
		public static long calcPoints(int mySide, int otherSide, int empty, int n) {
			long GANHANDO = (long) Math.pow(2, n);
			long PERDENDO = (long) Math.pow(2, n);
			if (mySide == n) mySide = mySide*10;
			if (otherSide+1 == n) return (mySide*GANHANDO) + (10*otherSide*PERDENDO) + empty;
			return (mySide*GANHANDO) - (otherSide*PERDENDO) + empty;
		}		
		
	}
	
	public static void main(String[] args) {
		
		int n=5;
		TabuleiroFoto tf = new TabuleiroFoto(n);
		Player p1 = PlayerFactory.getPlayer('R', n, tf);
		PlayerD083155 p2 = new PlayerD083155(n, tf);
		p2.setShowLog(false);
	    new Jogo(tf, p1, p2);
	}

}
