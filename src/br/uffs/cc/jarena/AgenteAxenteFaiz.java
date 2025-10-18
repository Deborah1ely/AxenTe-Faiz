package br.uffs.cc.jarena;

/**
 * Agente da equipe AxenteFaiz
 * 
 * Integrantes:
 *  - Deborah Ely
 *  - Kauã Perosso
 *  
 * Estratégia:
 *  O agente se movimenta até encontrar um cogumelo, quando encontrado o agente se multiplica
 *  uma única vez, respeitando o limite máximo de agentes na equipe.
 *  Quando ele encontra o cogumelo, o agente manda uma mensagem para todos irem em direção a ele.
 *
 *  Sua movimentação principal é voltada ao centro, cujo a chance de encontrar cogumelo é maior.
 * 
 *  Quando a vida do agente chega em 30 ele para de se mover com o intuito de economizar energia.
 *  Essa mesma estrategia é aplicada quando não há mais inimigos na arena.
 */

public class AgenteAxenteFaiz extends Agente {

    private static boolean inimigosDerrotados = false;

    private boolean jaDividiu = false;
    private static int totalAgentes = 0;
    private static final int MAX_AGENTES = 30;


    private Integer alvoX = null;
    private Integer alvoY = null;

    private final int centroX = 25;
    private final int centroY = 25;
    private boolean indoParaCentro = true;

    public AgenteAxenteFaiz(Integer x, Integer y, Integer energia) {
        super(x, y, energia);
        setDirecao(geraDirecaoAleatoria());
        totalAgentes++;
    }

    @Override
    public void pensa() {
        if (getEnergia() < 30) {
            para();
            return;
        }

        if (inimigosDerrotados) {
        para();
        return;
    }

    
        if (indoParaCentro) {
            moverParaCoordenada(centroX, centroY);

            if (Math.abs(getX() - centroX) <= 2 && Math.abs(getY() - centroY) <= 2) {
                indoParaCentro = false; 
            }
            return;
        }
        if (alvoX != null && alvoY != null) {
            moverParaCoordenada(alvoX, alvoY);

            if (Math.abs(getX() - alvoX) <= 1 && Math.abs(getY() - alvoY) <= 1) {
                alvoX = null;
                alvoY = null;
            }
        } else {
            if (!podeMoverPara(getDirecao())) {
                setDirecao(geraDirecaoAleatoria());
            }

            if (Math.random() < 0.3) {
                setDirecao(geraDirecaoAleatoria());
            }
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
    }

    @Override
    public void ganhouCombate() {
        enviaMensagem("INIMIGO_MORTO");

        inimigosDerrotados = false;
    }

    @Override
    public String getEquipe() {
        return "AxenteFaiz";
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
}
