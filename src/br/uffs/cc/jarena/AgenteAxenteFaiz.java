/**
 * Agente da equipe AxenteFaiz
 * 
 * Integrantes:
 *  - Deborah Ely
 *  - Kauã Perosso
 *  
 * Estratégia:
 *  O agente se movimenta até encontrar um cogumelo, quando encontrado o agente se multiplica
 * uma única vez e manda a coordenada do cogumelo para os outros agentes.
 */

package br.uffs.cc.jarena;

public class AgenteAxenteFaiz extends Agente {
    
    private boolean jaDividiu = false; // controla se já se dividiu após ganhar energia
    private Integer alvoX = null;
    private Integer alvoY = null;

    public AgenteAxenteFaiz(Integer x, Integer y, Integer energia) {
        super(x, y, energia);
        setDirecao(geraDirecaoAleatoria());
    }

    @Override
    public void pensa() {
        // Se temos um alvo (um cogumelo informado por outro agente)
        if (alvoX != null && alvoY != null) {
            moverParaCoordenada(alvoX, alvoY);
        } else {
            // Movimento aleatório normal
            if (!podeMoverPara(getDirecao())) {
                setDirecao(geraDirecaoAleatoria());
            }
        }
    }

    private void moverParaCoordenada(int destinoX, int destinoY) {
        int dx = destinoX - getX();
        int dy = destinoY - getY();

        if (dx == 0 && dy == 0) return; 
        int direcao;

        if (Math.abs(dx) > Math.abs(dy)) {
            direcao = dx > 0 ? DIREITA : ESQUERDA;
        } else {
            direcao = dy > 0 ? BAIXO : CIMA;
        }

        if (podeMoverPara(direcao)) {
            setDirecao(direcao);
        }
    }

    @Override
    public void recebeuEnergia() {
        if (podeDividir() && !jaDividiu && totalAgentes < MAX_AGENTES && getEnergia() > 60) {
            divide();
            jaDividiu = true;
        }
        enviaMensagem("COGUMELO:" + getX() + ":" + getY());
    }

    @Override
    public void recebeuMensagem(String msg) {
        if (msg.startsWith("COGUMELO:")) {
        String[] partes = msg.split(":");
        if (partes.length == 3) {
            try {
                alvoX = Integer.parseInt(partes[1]);
                alvoY = Integer.parseInt(partes[2]);
            } catch (NumberFormatException e) { }
        }
    } else if (msg.equals("TODOS_INIMIGOS_MORTOS")) {
        inimigosDerrotados = true; 
    }
}

    @Override
    public void tomouDano(int energiaRestanteInimigo) {
        // comportamento padrão: ignora
    }

    @Override
    public void ganhouCombate() {
        // comportamento padrão: ignora
    }

    @Override
    public String getEquipe() {
        return "AxenteFaiz";
    }

}