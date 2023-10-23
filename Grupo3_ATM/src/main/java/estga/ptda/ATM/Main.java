package estga.ptda.ATM;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
class sadasd{
    void setUp() {
        /*
        public Main() {
        super("JogoDoGalo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criar botões
        JLabel labelGalo = new JLabel("Jogo do Galo");
        JButton buttonJogar = new JButton("Novo Jogo");
        JButton buttonContinuar = new JButton("Continuar Jogo");
        JButton buttonJogosRealizados = new JButton("Ver jogos realizados");
        JButton buttonJogosGanhos = new JButton("Ver jogos ganhos");
        JButton buttonJogosEmpatados = new JButton("Ver jogos empatados");
        JButton buttonDefinicoes = new JButton("Definições");
        JButton buttonSair = new JButton("Sair");


        // Personalizar botões
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Font title = new Font("Arial", Font.BOLD, 40);
        Color buttonColor = new Color(60, 179, 113);
        Color sairColor = new Color(255, 69, 0);
        Color buttonTextColor = Color.WHITE;
        labelGalo.setFont(title);
        labelGalo.setForeground(buttonColor);
        labelGalo.setHorizontalAlignment(JLabel.CENTER);
        buttonJogar.setFont(buttonFont);
        buttonJogar.setBackground(buttonColor);
        buttonJogar.setForeground(buttonTextColor);
        buttonContinuar.setFont(buttonFont);
        buttonContinuar.setBackground(buttonColor);
        buttonContinuar.setForeground(buttonTextColor);
        buttonDefinicoes.setFont(buttonFont);
        buttonDefinicoes.setBackground(buttonColor);
        buttonDefinicoes.setForeground(buttonTextColor);
        buttonJogosRealizados.setFont(buttonFont);
        buttonJogosRealizados.setBackground(buttonColor);
        buttonJogosRealizados.setForeground(buttonTextColor);
        buttonJogosGanhos.setFont(buttonFont);
        buttonJogosGanhos.setBackground(buttonColor);
        buttonJogosGanhos.setForeground(buttonTextColor);
        buttonJogosEmpatados.setFont(buttonFont);
        buttonJogosEmpatados.setBackground(buttonColor);
        buttonJogosEmpatados.setForeground(buttonTextColor);
        buttonSair.setFont(buttonFont);
        buttonSair.setBackground(sairColor);
        buttonSair.setForeground(buttonTextColor);




        // Adicionar ouvintes de ação
        buttonJogar.addActionListener(this);
        buttonContinuar.addActionListener(this);
        buttonJogosRealizados.addActionListener(this);
        buttonJogosGanhos.addActionListener(this);
        buttonJogosEmpatados.addActionListener(this);
        buttonSair.addActionListener(this);
        buttonDefinicoes.addActionListener(this);

        // Adicionar botões ao painel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(labelGalo);
        panel.add(buttonJogar);
        panel.add(buttonContinuar);
        panel.add(buttonJogosRealizados);
        panel.add(buttonJogosGanhos);
        panel.add(buttonJogosEmpatados);
        panel.add(buttonDefinicoes);
        panel.add(buttonSair);


        // Adicionar painel à janela
        add(panel);

        // Mostrar janela
        pack();
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
         */
    }

    void Temas() {
        /*
            class Definicoes extends JFrame implements ActionListener {
        public Definicoes() {
            super("Definições");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton buttonMetal = new JButton("Metal");
            JButton buttonNimbusDark = new JButton("Nimbus Dark");
            JButton buttonNimbus = new JButton("Nimbus");
            JButton sair = new JButton("Sair");
            JLabel temas = new JLabel("Temas :");


            Font buttonFont = new Font("Arial", Font.BOLD, 16);
            Font title = new Font("Arial", Font.BOLD, 40);
            Color buttonColor = new Color(60, 179, 113);
            Color sairColor = new Color(255, 69, 0);
            Color buttonTextColor = Color.WHITE;


            temas.setFont(title);
            temas.setForeground(buttonColor);
            temas.setHorizontalAlignment(JLabel.CENTER);
            buttonMetal.setFont(buttonFont);
            buttonMetal.setBackground(buttonColor);
            buttonMetal.setForeground(buttonTextColor);
            buttonNimbusDark.setFont(buttonFont);
            buttonNimbusDark.setBackground(buttonColor);
            buttonNimbusDark.setForeground(buttonTextColor);
            buttonNimbus.setFont(buttonFont);
            buttonNimbus.setBackground(buttonColor);
            buttonNimbus.setForeground(buttonTextColor);
            sair.setFont(buttonFont);
            sair.setBackground(sairColor);
            sair.setForeground(buttonTextColor);


            buttonMetal.addActionListener(this);
            buttonNimbusDark.addActionListener(this);
            buttonNimbus.addActionListener(this);
            sair.addActionListener(this);


            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(6, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.add(temas);
            panel.add(buttonMetal);
            panel.add(buttonNimbusDark);
            panel.add(buttonNimbus);
            panel.add(sair);

            add(panel);

            // Mostrar janela
            pack();
            setSize(500, 500);
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String button = e.getActionCommand();
            switch (button) {
                case "Metal":
                    try {
                        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                        SwingUtilities.updateComponentTreeUI(this);
                    } catch (Exception d) {
                        // Caso o tema Nimbus não esteja disponível, o tema padrão será utilizado
                        System.err.println("Erro ao definir Look and Feel: " + d.getMessage());
                    }
                    break;
                case "Nimbus Dark":
                    try {
                        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                        UIManager.put("nimbusBase", Color.BLACK);
                        UIManager.put("nimbusBlueGrey", Color.DARK_GRAY);
                        UIManager.put("control", Color.DARK_GRAY);
                        UIManager.put("textForeground", Color.WHITE);
                        UIManager.put("nimbusLightBackground", Color.DARK_GRAY);
                        UIManager.put("info", new Color(128, 128, 128));
                        UIManager.put("nimbusFocus", new Color(30, 30, 30));
                        UIManager.put("nimbusSelectionBackground", new Color(50, 50, 50));
                        UIManager.put("nimbusSelectedText", Color.WHITE);
                        SwingUtilities.updateComponentTreeUI(this);
                    } catch (Exception d) {
                        // Caso o tema Nimbus não esteja disponível, o tema padrão será utilizado
                        System.err.println("Erro ao definir Look and Feel: " + d.getMessage());
                    }
                    break;
                case "Nimbus":
                    try {
                        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                        UIManager.put("control", new Color(214, 217, 223));
                        UIManager.put("textForeground", Color.BLACK);
                        UIManager.put("nimbusLightBackground", Color.WHITE);
                        UIManager.put("info", new Color(128, 128, 128));
                        UIManager.put("nimbusFocus", new Color(30, 30, 30));
                        UIManager.put("nimbusSelectionBackground", new Color(50, 50, 50));
                        UIManager.put("nimbusSelectedText", Color.WHITE);

                        SwingUtilities.updateComponentTreeUI(this);
                    } catch (Exception d) {
                        // Caso o tema Nimbus não esteja disponível, o tema padrão será utilizado
                        System.err.println("Erro ao definir Look and Feel: " + d.getMessage());
                    }
                    break;
                case "Sair":
                    dispose();
                    new Main();
                    break;
            }
        }
    }
         */
    }
}