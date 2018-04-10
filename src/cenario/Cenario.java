package cenario;

import model.Jogada;
import model.TabuleiroFoto;

public class Cenario extends TabuleiroFoto {

	private long pontuacao;
	private Jogada jogada;
	
	public Cenario(Jogada jogada, TabuleiroFoto foto, long pontuacao) {
		super(foto);
		this.setJogada(jogada);
		this.setPontuacao(pontuacao);
	}
	
	public long getPontuacao() {
		return this.pontuacao;
	}
	
	public void setPontuacao(long pontuacao) {
		this.pontuacao = pontuacao;
	}

	public Jogada getJogada() {
		return jogada;
	}

	public void setJogada(Jogada jogada) {
		this.jogada = jogada;
	}

}
