package appAluno.demo.model;

// importa as anotações de persistência e do jackson
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

// define a classe como uma entidade jpa, mapeada para a tabela "evento"
@Entity
@Table(name = "evento")
public class Evento implements Serializable {

    // controle de versão padrão para serialização
    private static final long serialVersionUID = 1L;

    // define o campo 'id' como chave primária com autoincremento pelo banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private LocalDate dataEvento;

    // define a relação: muitos eventos pertencem a um usuário
    // 'fetchtype.lazy' otimiza a performance, carregando o usuário só quando necessário
    @ManyToOne(fetch = FetchType.LAZY)
    // especifica que a coluna é a chave estrangeira aq
    @JoinColumn(name = "usuario_id")
    // ignora este campo ao converter, evitando loops infinitos
    @JsonIgnore
    private Usuario usuario;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public LocalDate getDataEvento() { return dataEvento; }
    public void setDataEvento(LocalDate dataEvento) { this.dataEvento = dataEvento; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}