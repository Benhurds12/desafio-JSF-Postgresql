package br.com.esig.DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import javax.persistence.*;

import br.com.esig.model.Tarefa;

public class TarefaDAO {

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("teste_mvp");

	

	public void salvar(Tarefa tarefa) {
	    EntityManager em = emf.createEntityManager();
	    em.getTransaction().begin();
	    if (tarefa.getId() == null) { // Se é nova, persiste
	        em.persist(tarefa);
	    } else { // Se já existe (tem ID), atualiza (merge)
	        em.merge(tarefa);
	    }
	    em.getTransaction().commit();
	    em.close();
	}
	public List<Tarefa> listar() {
	    return listar("TODAS"); // Chama a nova versão com o filtro padrão
	}
	
	public List<Tarefa> listar(String filtroSituacao) { // filtroSituacao pode ser "TODAS", ou um status específico
	    EntityManager em = emf.createEntityManager();
	    try {
	        String qlString = "SELECT t FROM Tarefa t";
	        boolean temFiltro = false;

	        if (filtroSituacao != null && !filtroSituacao.equalsIgnoreCase("TODAS") && !filtroSituacao.trim().isEmpty()) {
	            qlString += " WHERE t.situacao = :situacaoParam";
	            temFiltro = true;
	        }
	        qlString += " ORDER BY t.id ASC"; // Ou a ordenação que preferir

	        TypedQuery<Tarefa> query = em.createQuery(qlString, Tarefa.class);

	        if (temFiltro) {
	            query.setParameter("situacaoParam", filtroSituacao);
	        }
	        return query.getResultList();
	    } finally {
	        em.close();
	    }
	}

	public Tarefa buscarPorId(Long id) {
		EntityManager em = emf.createEntityManager();
		try {
			return em.find(Tarefa.class, id);
		} finally {
			em.close();
		}
	}
	
	public Tarefa buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return null;
        }
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Tarefa> query = em.createQuery("SELECT t FROM Tarefa t WHERE t.titulo = :tituloParam", Tarefa.class);
            query.setParameter("tituloParam", titulo);
            List<Tarefa> resultados = query.getResultList();
            if (resultados.isEmpty()) {
                return null;
            } else {
                // ATENÇÃO: Se houver múltiplos resultados, retorna o primeiro.
                // Isso pode não ser o comportamento ideal se títulos não são únicos.
                if (resultados.size() > 1) {
                    System.err.println("AVISO DAO: Múltiplas tarefas encontradas com o título '" + titulo + "'. Retornando a primeira.");
                }
                return resultados.get(0);
            }
        } finally {
            em.close();
        }
    }

	public void excluir(Long id) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			Tarefa tarefa = em.find(Tarefa.class, id);
			if (tarefa != null) {
				em.remove(tarefa);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	// Método ATUALIZAR (NOVO)
    public void atualizar(Tarefa tarefa) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(tarefa); // Usa merge para atualizar a entidade existente
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // Idealmente, logar o erro
        } finally {
            em.close();
        }
    }

	// Método para excluir passando o objeto, se preferir
	public void excluir(Tarefa tarefa) {
		if (tarefa == null || tarefa.getId() == null) {
			System.err.println("Tentativa de excluir tarefa nula ou sem ID.");
			return;
		}
		excluir(tarefa.getId());
	}

}