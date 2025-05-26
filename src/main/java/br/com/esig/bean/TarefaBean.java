package br.com.esig.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import br.com.esig.model.Tarefa;
import br.com.esig.DAO.TarefaDAO;
@ManagedBean
public class TarefaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Tarefa tarefa = new Tarefa();
	private TarefaDAO tarefaDAO;
    private List<Tarefa> tarefas;
    private Tarefa tarefaSelecionada;
    
    
    private String filtroSituacao = "TODAS"; // Valor padrão

    public void aplicarFiltro(AjaxBehaviorEvent event) {
        // Este método é chamado quando o valor do filtro no h:selectOneMenu muda.
        // A principal tarefa dele é ser o "gatilho" para o AJAX.
        // A lógica de filtragem real acontecerá no método getTarefas().
        // O atributo 'render' do f:ajax fará com que a tabela seja redesenhada,
        // e ao ser redesenhada, ela chamará o getTarefas() que aplicará o filtro.
        System.out.println("Filtro de situação alterado para: " + this.filtroSituacao);
    }


    // ... construtor, init, outros métodos ...

    // GETTER PARA tarefaSelecionada
    public Tarefa getTarefaSelecionada() {
        return tarefaSelecionada;
    }

    // SETTER PARA tarefaSelecionada
    public void setTarefaSelecionada(Tarefa tarefaSelecionada) {
        this.tarefaSelecionada = tarefaSelecionada;
    }
    public TarefaBean() {
        this.tarefa = new Tarefa();
        this.tarefaDAO = new TarefaDAO(); // Instanciar o DAO uma vez por bean
        carregarTarefas(); // Carregar a lista de tarefas ao criar o bean
    }
    
    
    private void carregarTarefas() {
        this.tarefas = this.tarefaDAO.listar();
        if (this.tarefas == null) {
          this.tarefas = new ArrayList<>();
      }
    }
    
    
    public void editar(Tarefa tarefaSelecionada) {
        this.tarefa = tarefaSelecionada;
    }
    
    public String atualizarTarefa() {
        String tituloNoFormulario = this.tarefa.getTitulo(); // Pega o título que veio do formulário

        if (tituloNoFormulario == null || tituloNoFormulario.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção!", "O título é obrigatório para tentar uma atualização."));
            return null;
        }

        try {
           
            Tarefa tarefaExistenteNoBanco = this.tarefaDAO.buscarPorTitulo(tituloNoFormulario);

            if (tarefaExistenteNoBanco != null) {
               
                tarefaExistenteNoBanco.setDescricao(this.tarefa.getDescricao());
                tarefaExistenteNoBanco.setResponsavel(this.tarefa.getResponsavel());
                tarefaExistenteNoBanco.setPrioridade(this.tarefa.getPrioridade());
                tarefaExistenteNoBanco.setDeadline(this.tarefa.getDeadline());
                tarefaExistenteNoBanco.setSituacao(this.tarefa.getSituacao()); // Assumindo que situação pode ser editada
                

                this.tarefaDAO.salvar(tarefaExistenteNoBanco); // O salvar do DAO fará merge/update

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Tarefa '" + tarefaExistenteNoBanco.getTitulo() + "' atualizada."));
                this.tarefa = new Tarefa(); // Limpa o formulário no bean
                carregarTarefas();          // Recarrega a lista da tabela
            } else {
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção!", "Nenhuma tarefa encontrada com o título '" + tituloNoFormulario + "' para atualizar. Para criar uma nova tarefa com este título, use o botão 'Salvar'."));
                
            }
        } catch (Exception e) {
            // ... tratamento de erro ...
        }
        return null;
    }
    
    
    
    public String salvar() {
        try {
            
            boolean isNew = (this.tarefa.getId() == null);

            this.tarefaDAO.salvar(this.tarefa); // O DAO já lida com persist (novo) ou merge (atualizar)

            if (isNew) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Nova tarefa salva."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Tarefa atualizada com sucesso."));
            }

           
            this.tarefa = new Tarefa();

            carregarTarefas(); // Recarrega a lista da tabela
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível salvar a tarefa. Detalhes: " + e.getMessage()));
            e.printStackTrace(); // Importante para depuração no console do servidor
        }
        return null; 
    }

 // Método original com parâmetro 
    private void executarConclusaoTarefa(Tarefa tarefaParaConcluir) {
        if (tarefaParaConcluir != null) {
            tarefaParaConcluir.setSituacao("Concluída");
            this.tarefaDAO.atualizar(tarefaParaConcluir);
            carregarTarefas();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Tarefa '" + tarefaParaConcluir.getTitulo() + "' concluída."));
        } else {
             FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção!", "Nenhuma tarefa selecionada para concluir."));
        }
    }

    // NOVO MÉTODO DE AÇÃO (sem parâmetros) para concluir
    public String concluirTarefaAction() {
        try {
            executarConclusaoTarefa(this.tarefaSelecionada);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível concluir a tarefa selecionada."));
            e.printStackTrace();
        }
        this.tarefaSelecionada = null; // Limpar seleção
        return null;
    }

    // Método original com parâmetro (pode ser privado ou protected)
    private void executarExclusaoTarefa(Tarefa tarefaParaExcluir) {
         if (tarefaParaExcluir != null) {
            this.tarefaDAO.excluir(tarefaParaExcluir);
            carregarTarefas();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Tarefa '" + tarefaParaExcluir.getTitulo() + "' excluída."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção!", "Nenhuma tarefa selecionada para excluir."));
        }
    }

    // NOVO MÉTODO DE AÇÃO (sem parâmetros) para excluir
    public String excluirTarefaAction() {
        try {
            executarExclusaoTarefa(this.tarefaSelecionada);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível excluir a tarefa selecionada."));
            e.printStackTrace();
        }
        this.tarefaSelecionada = null; // Limpar seleção
        return null;
    }
    
    public String limparFormulario() {
        this.tarefa = new Tarefa();
        return null;
    }

    // Getters e Setters
    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public List<Tarefa> getTarefas() {
        // Primeiro, garanta que a lista 'master' (this.tarefas) está carregada
        if (this.tarefas == null) {
             // System.out.println("getTarefas(): this.tarefas é null, tentando carregar...");
             // carregarTarefas(); // Chama o método que busca TODAS do DAO
             // Se ainda for null após carregar (erro no DAO, por exemplo), retorna lista vazia
             if (this.tarefas == null) return new ArrayList<>();
        }

        // Se o filtro for "TODAS" ou nulo, retorna a lista completa como está em this.tarefas
        if ("TODAS".equalsIgnoreCase(this.filtroSituacao) || this.filtroSituacao == null) {
            // System.out.println("getTarefas(): Retornando todas as " + (this.tarefas != null ? this.tarefas.size() : 0) + " tarefas.");
            return this.tarefas;
        }

        // Caso contrário, filtra a lista 'this.tarefas' e retorna uma NOVA lista filtrada
        List<Tarefa> tarefasFiltradas = new ArrayList<>();
        for (Tarefa t : this.tarefas) {
            // Certifique-se que a string em 'this.filtroSituacao' (ex: "Em Andamento")
            // corresponde exatamente à string retornada por t.getSituacao(),
            // ignorando maiúsculas/minúsculas para robustez.
            if (this.filtroSituacao.equalsIgnoreCase(t.getSituacao())) {
                tarefasFiltradas.add(t);
            }
        }
        // System.out.println("getTarefas(): Retornando " + tarefasFiltradas.size() + " tarefas filtradas por '" + this.filtroSituacao + "'.");
        return tarefasFiltradas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

	public String getFiltroSituacao() {
		return filtroSituacao;
	}

	public void setFiltroSituacao(String filtroSituacao) {
		this.filtroSituacao = filtroSituacao;
	}
}