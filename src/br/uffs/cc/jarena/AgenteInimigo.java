/**
 * Um exemplo de agente que anda aleatoriamente na arena. Esse agente pode ser usado como base
 * para a criação de um agente mais esperto. Para mais informações sobre métodos que podem
 * ser utilizados, veja a classe Agente.java.
 * 
 * Fernando Bevilacqua <fernando.bevilacqua@uffs.edu.br>
 */

package br.uffs.cc.jarena;

public class AgenteInimigo extends Agente
{
	private boolean estaAbsorvendo = false;
	private boolean estaEmCombate = false;
	private boolean alvosAqui = false;
	private int alvoY;
	private int alvoX;
	private boolean pediuAjuda = false;
	private boolean buscandoEnergia = true;
	private boolean estaNaZonaSegura = false;
	private final int ZONA_CENTRO_X = 350;
	private final int ZONA_CENTRO_Y = 400;
	private final int ZONA_RAIO	 = 150;
	private final int ENERGIA_MINIMA_PARA_DIVIDIR = 1050;
	private final int ENERGIA_MINIMA_PARA_ATACAR = 500;
	private final int ENERGIA_MINIMA_PARA_FUGIR = 300;
	private final double CHANCE_MUDAR_DIRECAO = 0.05;

	public AgenteInimigo(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		setDirecao(geraDirecaoAleatoria());
		System.out.println("[Agente "+ getId() + "] Criado! posicão: (" + getX() + ") Energia: " + getEnergia()+"Câmbio");
	}

	private void checaZonaSegura(){
		if (getX() > (ZONA_CENTRO_X - ZONA_RAIO) && getX()< (ZONA_CENTRO_X+ZONA_RAIO) && getY() > (ZONA_CENTRO_Y - ZONA_RAIO) && getY() < (ZONA_CENTRO_Y + ZONA_RAIO)){
			estaNaZonaSegura = true;
		}
	}
	@Override
	public void pensa() {
		checaZonaSegura();
		if (pediuAjuda){
			if (getEnergia() > ENERGIA_MINIMA_PARA_ATACAR){
				System.out.println("[Agente "+ getId() + "] Recuperei Energia! Estou voltando! Câmbio");
				pediuAjuda = false;
			} else {
				para();
				return;
			}
		}
		if (estaAbsorvendo){
			para();
			estaAbsorvendo = false;
			if (getX() == alvoX && getY() == alvoY){
				this.alvosAqui = false;
			}
			return;
		}
		if (estaEmCombate){
			this.estaEmCombate = false;
			this.alvosAqui = false;
			System.out.println("[Agente "+ getId() + "] Acabou o combate, traçando nova rota... Câmbio");
		} 

		if (!estaNaZonaSegura){
			
			boolean linhaX = Math.random() < 0.5;
			int direcao = getDirecao();
			
			if (linhaX){
				if (getX() < ZONA_CENTRO_X) direcao = DIREITA;
				else if (getX() > ZONA_CENTRO_X) direcao = ESQUERDA;
				else if (getY() < ZONA_CENTRO_Y) direcao = BAIXO;
				else if (getY() > ZONA_CENTRO_Y) direcao = CIMA;
			} else {
				if (getY() < ZONA_CENTRO_Y) direcao = BAIXO;
				else if (getY() > ZONA_CENTRO_Y) direcao = CIMA;
				else if (getX() < ZONA_CENTRO_X) direcao = DIREITA;
				else if (getX() > ZONA_CENTRO_X) direcao = ESQUERDA;
			}
			setDirecao(direcao);
			if (!podeMoverPara(getDirecao())){
				setDirecao(geraDirecaoAleatoria());
			}
			return;
		}
		if (getEnergia() < ENERGIA_MINIMA_PARA_FUGIR && !pediuAjuda){
			enviaMensagem("Preciso de ajuda_urgente!!" + getX() + " "+getY());
			pediuAjuda = true;
			System.out.println("[Agente "+ getId() + "] SoCoRrOoO!! Energia acabando ("+ getEnergia() + ")! Posição: ("+ getX() + ","+getY() + ") Câmbio");
			para();
			return;
		}
		if (podeDividir() && getEnergia() >= ENERGIA_MINIMA_PARA_DIVIDIR){
			System.out.println("[Agente " + getId() + "] Dividindo! Energia: "+ getEnergia()+"Câmbio");
			divide();
			para();
			return;
		}
		if (alvosAqui) {
			System.out.println("[Agente "+ getId()+ "] Indo para o alvo em (" + alvoX + ","+alvoY+") Câmbio");
			if (getX() < this.alvoX){
				setDirecao(DIREITA);
			} else if (getX() > this.alvoX) {
				setDirecao(ESQUERDA);
			} else if (getY()< this.alvoY){
				setDirecao(CIMA);
			}else if (getY() > this.alvoY){
				setDirecao(BAIXO);
			}
			if (getX() == alvoX && getY() == alvoY){
				System.out.println("[Agente "+ getId() + "] Cheguei no alvo! Câmbio.");
				alvosAqui = false;
			}
			if (!podeMoverPara(getDirecao())){
				System.out.println("[Agente "+getId() + "] Obstáculo a caminho do alvo, desistindo! Câmbio. ");
				setDirecao(geraDirecaoAleatoria());
				alvosAqui = false;
			}
			return;
		}
		if (buscandoEnergia){
			if (getEnergia() > ENERGIA_MINIMA_PARA_ATACAR){
				buscandoEnergia = false;
			} else {
				if (Math.random() < 0.1){
					enviaMensagem("Buscando_mushroowwns" + getX() + " "+ getY());
				}
			}
		}
		if (!podeMoverPara(getDirecao())){
			System.out.println("[Agente "+ getId() + "] Bati na parede, mudando direção! Câmbio");
			setDirecao(geraDirecaoAleatoria());
		}
		if (Math.random() < CHANCE_MUDAR_DIRECAO){
			setDirecao(geraDirecaoAleatoria());
		}
		if (!buscandoEnergia && getEnergia()> ENERGIA_MINIMA_PARA_ATACAR && Math.random()< 0.2){
			setDirecao(geraDirecaoAleatoria());
		}
		// Se não conseguimos nos mover para a direção atual, quer dizer
		// que chegamos no final do mapa ou existe algo bloqueando nosso
		// caminho.
		// Se o agente conseguie se dividir (tem energia) e se o total de energia
		// do agente é maior que 400, nos dividimos. O agente filho terá a metade
		// da nossa energia atual.
	}
	@Override
	public void recebeuEnergia() {
		// Invocado sempre que o agente recebe energia.
		System.out.println("[Agente "+ getId()+ "] ACHEI MUSHROOWNS! Recarregando... Energia"+ getEnergia()+" Câmbio");
		this.estaAbsorvendo = true;
		para();
		enviaMensagem("Achei_Mushroownss " + getX() + " "+ getY());
		if (getEnergia() > ENERGIA_MINIMA_PARA_ATACAR){
			setDirecao(geraDirecaoAleatoria());
			this.buscandoEnergia = false;
			this.pediuAjuda = false;
		}
		if (podeDividir() && getEnergia() > ENERGIA_MINIMA_PARA_DIVIDIR){
			divide();
		}
	}
	
	@Override
	public void tomouDano(int energiaRestanteInimigo) {
		System.out.println("[Agente "+ getId() + "] Ai! EM combate! Minha energia.."+ getEnergia()+ "| Inimigo: " + energiaRestanteInimigo +"Câmbio");
		this.estaEmCombate = true;
		if(energiaRestanteInimigo > getEnergia() && getEnergia() > ENERGIA_MINIMA_PARA_FUGIR){
			System.out.println("[Agente "+ getId()+ "] Fugindo! INimigo muito forte! Câmbio.");
			enviaMensagem("inimigo_forte" + getX() + ""+getY());
			setDirecao(geraDirecaoAleatoria());
		}
		// Invocado quando o agente está na mesma posição que um agente inimigo
		// e eles estão batalhando (ambos tomam dano).
	}
	
	@Override
	public void ganhouCombate() {
		System.out.println("[Agente " + getId()+ "]VITÓRIA! Ganhei no combate! Energia atual: "+ getEnergia()+"Câmbio");
		enviaMensagem("inimigo_morto"+getX() + ""+getY());
		setDirecao(geraDirecaoAleatoria());
		// Invocado se estamos batalhando e nosso inimigo morreu.
	}
	
	@Override
	public void recebeuMensagem(String msg) {
		// Invocado sempre que um agente aliado próximo envia uma mensagem.
		if (msg == null) return;
		

		String[] partes = msg.split(" ");
		if (partes.length < 3){
			
			return;
		} 
		String tipo = partes[0];
		int x,y;
		try {
			x = Integer.parseInt(partes[1]);
			y = Integer.parseInt(partes[2]);
		} catch (NumberFormatException e){
			
			return;
		}
		switch (tipo){
			case "Achei_Mushroownss" -> {
                            if (buscandoEnergia && !alvosAqui && estaAbsorvendo){
                               
								this.alvosAqui = true;
                                this.alvoX = x;
                                this.alvoY = y;
                            }
                }
				
			case "Buscando_mushroowwns" -> {
                            if (buscandoEnergia && alvosAqui && !estaAbsorvendo){
                               
								setDirecao(geraDirecaoAleatoria());
                            }
                }
			case "ajuda_urgente" -> {
                            if (getEnergia() > ENERGIA_MINIMA_PARA_ATACAR && !alvosAqui){
                                
								this.alvosAqui = true;
                                this.alvoX = x;
                                this.alvoY = y;
                            }
                }
			case "inimigo_forte" -> {
                            if (getX() == x && getY() == y){
								
                                setDirecao(geraDirecaoAleatoria());
                            } else if (getEnergia() < ENERGIA_MINIMA_PARA_ATACAR && Math.random() < 0.3) {
								
                                setDirecao(geraDirecaoAleatoria());
                            }
                }
			case "inimigo_morto" -> {
                            if(Math.random() < 0.5){
								
                                setDirecao(geraDirecaoAleatoria());
                            }
                }

		}
	}

	@Override
	public String getEquipe() {
		// Definimos que o nome da equipe do agente é "Mirror".
		return "Mirror";
	}
}