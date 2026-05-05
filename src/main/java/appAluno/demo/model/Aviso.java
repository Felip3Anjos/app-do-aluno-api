package appAluno.demo.model;

import jakarta.persistence.*; // Pacote padrão do Spring 3+
import java.time.LocalDate;

@Entity
@Table(name = "avisos") // Spring olhe para a tabela correta
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;

    // Renomeado de 'texto' para 'mensagem' para bater com o C#
    private String mensagem;


    // --- MUDANÇA AQUI ---
    // Renomeado de 'dataPostagem' para 'dataPublicacao'
    // E mapeado para a coluna 'data_publicacao' que o C# usa
    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    // (O Spring precisa deles para funcionar)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
}