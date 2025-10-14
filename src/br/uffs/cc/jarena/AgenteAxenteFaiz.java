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

    @Override
    public void recebeuEnergia() {
        // Ao receber energia, divide-se uma vez e envia posição para aliados
        if (podeDividir() && !jaDividiu) {
            divide();
            jaDividiu = true;
        }

        // Envia localização atual — interpretada como o local do cogumelo
        enviaMensagem("COGUMELO:" + getX() + ":" + getY());
    }

    @Override
    public void recebeuMensagem(String msg) {
        // Espera receber mensagem no formato "COGUMELO:x:y"
        if (msg.startsWith("COGUMELO:")) {
            String[] partes = msg.split(":");
            if (partes.length == 3) {
                try {
                    alvoX = Integer.parseInt(partes[1]);
                    alvoY = Integer.parseInt(partes[2]);
                } catch (NumberFormatException e) {
                    // ignora mensagem inválida
                }
            }
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

    // Move-se passo a passo até uma coordenada
    private void moverParaCoordenada(int destinoX, int destinoY) {
        int dx = destinoX - getX();
        int dy = destinoY - getY();

        int direcao = getDirecao();

        if (Math.abs(dx) > Math.abs(dy)) {
            // Move horizontalmente
            direcao = dx > 0 ? DIREITA : ESQUERDA;
        } else {
            // Move verticalmente
            direcao = dy > 0 ? BAIXO : CIMA;
        }

        if (podeMoverPara(direcao)) {
            setDirecao(direcao);
        } else {
            setDirecao(geraDirecaoAleatoria());
        }
    }
}
