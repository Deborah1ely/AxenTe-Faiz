

package br.uffs.cc.jarena;

/**
 * Agente da equipe AxenteFaiz
 * 
 * Integrantes:
 *  - Deborah Ely
 *  - Kauã Perosso
 *  
 * Estratégia:
 * '- No início, apenas 5% dos agentes (escolhidos aleatoriamente) ficam parados como mini-bases.
 *  - O restante sai explorando o mapa em busca de cogumelos.
 *  - Quando algum agente encontrar um cogumelo, ele envia uma mensagem com sua posição e energia.
 *  - O agente com maior energia se torna a "base principal" (apenas 1 pode estar parado assim).
 *  - Quando um novo agente tiver mais energia, ele assume o posto de base e o anterior volta a explorar.
 * 
 */


public class AgenteAxenteFaiz extends Agente {

    private boolean parado = false;       
    private boolean explorador = true;    
    private boolean souBasePrincipal = false; 
    private Integer alvoX = null;         
    private Integer alvoY = null;

    private int idBasePrincipal = -1;     

    public AgenteAxenteFaiz(Integer x, Integer y, Integer energia) {
        super(x, y, energia);

    
        if (Math.random() < 0.05) {
            parado = true;
            explorador = false;
            System.out.println("[AxenteFaiz " + getId() + "] Sou uma mini-base inicial.");
        } else {
            setDirecao(geraDirecaoAleatoria());
            System.out.println("[AxenteFaiz " + getId() + "] Criado como explorador.");
        }
    }

    @Override
    public void pensa() {
        if (parado) {
            para();
            return;
        }

      
        if (alvoX != null && alvoY != null) {
            moverParaCoordenada(alvoX, alvoY);
            if (distanciaPara(alvoX, alvoY) <= 3) {
                para();
                alvoX = null;
                alvoY = null;
            }
            return;
        }

   
        if (!podeMoverPara(getDirecao()) || Math.random() < 0.1) {
            setDirecao(geraDirecaoAleatoria());
        }
    }

    @Override
    public void recebeuEnergia() {
        if (!explorador) return;

   
        String msg = "COGUMELO:" + getId() + ":" + getX() + ":" + getY() + ":" + getEnergia();
        enviaMensagem(msg);
        System.out.println("[AxenteFaiz " + getId() + "] Achei cogumelo! " + msg);
    }

    @Override
    public void recebeuMensagem(String msg) {
        if (msg == null) return;

      
        if (msg.startsWith("NOVA_BASE:")) {
            try {
                int idNovaBase = Integer.parseInt(msg.split(":")[1]);
                idBasePrincipal = idNovaBase;

              
                if (getId() != idNovaBase && souBasePrincipal) {
                    souBasePrincipal = false;
                    explorador = true;
                    parado = false;
                    System.out.println("[AxenteFaiz " + getId() + "] Base transferida. Voltando a explorar.");
                }

            } catch (Exception e) {
                // ignora formato incorreto
            }
            return;
        }

        // Mensagem de cogumelo
        if (msg.startsWith("COGUMELO:")) {
            String[] partes = msg.split(":");
            if (partes.length != 5) return;

            try {
                int remetenteId = Integer.parseInt(partes[1]);
                int cx = Integer.parseInt(partes[2]);
                int cy = Integer.parseInt(partes[3]);
                int energiaRemetente = Integer.parseInt(partes[4]);

              
                if (getEnergia() > energiaRemetente && !souBasePrincipal) {
                 
                    souBasePrincipal = true;
                    parado = true;
                    explorador = false;
                    idBasePrincipal = getId();

                   
                    enviaMensagem("NOVA_BASE:" + getId());
                    System.out.println("[AxenteFaiz " + getId() + "] Virei a nova base principal!");

                } else if (!souBasePrincipal) {
                
                    alvoX = cx;
                    alvoY = cy;
                }

            } catch (NumberFormatException e) {
           
            }
        }
    }

    @Override
    public void tomouDano(int energiaRestanteInimigo) {

        if (Math.random() < 0.5) setDirecao(geraDirecaoAleatoria());
    }

    @Override
    public void ganhouCombate() {
        enviaMensagem("INIMIGO_DERROTADO");
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

        if (podeMoverPara(direcao)) setDirecao(direcao);
    }

    private int distanciaPara(int x, int y) {
        return (int) Math.sqrt(Math.pow(getX() - x, 2) + Math.pow(getY() - y, 2));
    }
}
