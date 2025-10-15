

package br.uffs.cc.jarena;

public class AgenteDummy extends Agente
{
	public AgenteDummy(Integer x, Integer y, Integer energia) {
		super(x, y, energia);
		setDirecao(geraDirecaoAleatoria());
	}
	
	public void pensa() {
		if(!podeMoverPara(getDirecao())) {
			setDirecao(geraDirecaoAleatoria());
		}
		
		if(podeDividir() && getEnergia() >= 800) {
			divide();
		}
	}

    private void moverParaCoordenada(int destinoX, int destinoY) {
        int dx = destinoX - getX();
        int dy = destinoY - getY();

        int direcao = getDirecao();

        if (Math.abs(dx) > Math.abs(dy)) {
            direcao = dx > 0 ? DIREITA : ESQUERDA;
        } else {
            direcao = dy > 0 ? BAIXO : CIMA;
        }

        if (podeMoverPara(direcao)) {
            setDirecao(direcao);
        } else {
            setDirecao(geraDirecaoAleatoria());
        }
    }
	
	public void recebeuEnergia() {
		enviaMensagem("COGUMELO:" + getX() + ":" + getY());
	}
	
	public void tomouDano(int energiaRestanteInimigo) {
	}
	
	public void ganhouCombate() {
	}
	
	public void recebeuMensagem(String msg) {
        if (msg.startsWith("COGUMELO:")) {
            String[] partes = msg.split(":");
            if (partes.length == 3) {
                try {
                    alvoX = Integer.parseInt(partes[1]);
                    alvoY = Integer.parseInt(partes[2]);
                } catch (NumberFormatException e) {
                }
            }
        }
    }
	
	public String getEquipe() {
		return "AxenteFaiz";
	}
}
